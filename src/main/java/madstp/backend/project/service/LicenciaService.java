package madstp.backend.project.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import madstp.backend.project.domain.ClaseLicencia;
import madstp.backend.project.domain.Licencia;
import madstp.backend.project.dto.LicenciaDTO;
import madstp.backend.project.dto.ClaseLicenciaDTO;
import madstp.backend.project.repos.LicenciaRepository;
import madstp.backend.project.service.TitularService;
import madstp.backend.project.service.ClaseLicenciaService;
import madstp.backend.project.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import madstp.backend.project.dto.TitularDTO;
import madstp.backend.project.enums.ClaseLicenciaEnum;
import java.time.LocalDate;
import java.time.Period;


@Service
public class LicenciaService {

    private final LicenciaRepository licenciaRepository;
    private final ClaseLicenciaService claseLicenciaService;
    private final TitularService titularService;

    public LicenciaService(final LicenciaRepository licenciaRepository, ClaseLicenciaService claseLicenciaService, TitularService titularService) {
        this.licenciaRepository = licenciaRepository;
        this.claseLicenciaService = claseLicenciaService;
        this.titularService = titularService;
    }

    public List<LicenciaDTO> findAll() {
        final List<Licencia> licencias = licenciaRepository.findAll(Sort.by("id"));
        return licencias.stream()
                .map(licencia -> mapToDTO(licencia, new LicenciaDTO()))
                .toList();
    }

    public LicenciaDTO get(final Long id) {
        return licenciaRepository.findById(id)
                .map(licencia -> mapToDTO(licencia, new LicenciaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    /**
     * Crea una nueva licencia.
     * Valida los datos de la licencia, calcula su vigencia y costo, y guarda la licencia en la base de datos.
     * @param licenciaDTO
     * @return 
     */
    public Long create(final @Valid LicenciaDTO licenciaDTO) {
        // Recuperar titular por id
        TitularDTO titular = titularService.get(licenciaDTO.getTitularId());
        LocalDate fechaNacimiento = titular.getFechaNacimiento();
        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();

        // Validar datos de la licencia
        create_validate(licenciaDTO, edad, fechaNacimiento);

        // Calcular vigencia de la Licencia
        LocalDate fechaInicioVigencia = LocalDate.now();
        int vigenciaAnios = calcularVigenciaLicencia(edad, fechaInicioVigencia, fechaNacimiento, licenciaDTO);
        LocalDate fechaVencimiento = fechaInicioVigencia
            .plusYears(vigenciaAnios)
            .withMonth(fechaNacimiento.getMonthValue())
            .withDayOfMonth(fechaNacimiento.getDayOfMonth());
        if (!fechaVencimiento.isAfter(fechaInicioVigencia)) {
            fechaVencimiento = fechaVencimiento.plusYears(1);
        }

        // Setear fechas en cada clase a incorporar
        List<ClaseLicenciaDTO> clasesAIncorporar = licenciaDTO.getClases();
        for (ClaseLicenciaDTO claseDTO : clasesAIncorporar) {
            claseDTO.setFechaEmision(fechaInicioVigencia);
            claseDTO.setFechaVencimiento(fechaVencimiento);
        }
        
        // Calcular costo de la Licencia
        Integer costo = calcularCostoLicencia(licenciaDTO, 1);

        final Licencia licencia = new Licencia();
        mapToEntity(licenciaDTO, licencia);
        
        // Hay que de alguna forma guardar o devolver el costo de la licencia para mostrarle al usuario...
        // idem con la vigencia
        return licenciaRepository.save(licencia).getId();
    }

    /**
     * Valida los datos de la licencia a crear.
     * - Verifica que las clases solicitadas sean válidas según la edad del titular.
     * - Si se solicita una licencia profesional (C, D o E), verifica que el titular tenga al menos 21 años.
     * - Si se solicita una licencia profesional (C, D o E), verifica que haya tenido una clase B al menos un año antes.
     * - Si es la primera licencia profesional, verifica que el titular no tenga más de 65 años.
     * @param licenciaDTO
     * @param edad
     * @param fechaNacimiento
     */
    private void create_validate(LicenciaDTO licenciaDTO, Integer edad, LocalDate fechaNacimiento){
        Boolean solicitaProfesional = false;
        for (ClaseLicenciaDTO clase : licenciaDTO.getClases()) {
            ClaseLicenciaEnum claseEnum = clase.getClaseLicenciaEnum();
            if (claseEnum == ClaseLicenciaEnum.C || claseEnum == ClaseLicenciaEnum.D || claseEnum == ClaseLicenciaEnum.E) {
                solicitaProfesional = true;
                if (edad < 21) {
                    throw new IllegalArgumentException("La edad mínima para las clases C, D y E es 21 años");
                }
            } else {
                if (edad < 17) {
                    throw new IllegalArgumentException("La edad mínima para esta clase es 17 años");
                }
            }
        }

        if (solicitaProfesional) {
            // Buscar licencia previa del titular
            Optional<Licencia> licenciaPrevia = licenciaRepository.findByTitular_Id(licenciaDTO.getTitularId());
            Boolean tuvoB = false;
            LocalDate fechaB = null;
            if (licenciaPrevia.isPresent()) {
                for (ClaseLicencia clase : licenciaPrevia.get().getClasesLicencia()) {
                    if (clase.getClaseLicenciaEnum() == ClaseLicenciaEnum.B) {
                        tuvoB = true;
                        fechaB = clase.getFechaEmision();
                        break;
                    }
                }
            }
            if (!tuvoB || fechaB == null || Period.between(fechaB, LocalDate.now()).getYears() < 1) {
                throw new IllegalArgumentException("Para obtener una licencia profesional (C, D o E) debe haber tenido una licencia clase B al menos un año antes.");
            }

            // No puede ser primera profesional si tiene más de 65 años
            Boolean yaTuvoProfesional = licenciaPrevia.isPresent() && licenciaPrevia.get().getClasesLicencia().stream()
                .anyMatch(c -> c.getClaseLicenciaEnum() == ClaseLicenciaEnum.C || c.getClaseLicenciaEnum() == ClaseLicenciaEnum.D || c.getClaseLicenciaEnum() == ClaseLicenciaEnum.E);
            if (!yaTuvoProfesional && edad > 65) {
                throw new IllegalArgumentException("No se puede otorgar licencia profesional por primera vez a mayores de 65 años.");
            }
        }
    }

    private Integer calcularVigenciaLicencia(Integer edad, LocalDate fechaInicio, LocalDate fechaNacimiento, LicenciaDTO licenciaDTO) {
        // TODO: Implementar lógica real acá o en la clase que corresponda. Modificar firma del  método en caso de ser necesario.
        return 1;
    }

    private Integer calcularCostoLicencia(LicenciaDTO licenciaDTO, Integer vigenciaAnios) {
        // TODO: Implementar lógica real acá o en la clase que corresponda. Modificar firma del  método en caso de ser necesario.
        return 0;
    }

    public void update(final Long id, final  @Valid  LicenciaDTO licenciaDTO) {
        final Licencia licencia = licenciaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(licenciaDTO, licencia);
        licenciaRepository.save(licencia);
    }

    public void delete(final Long id) {
        licenciaRepository.deleteById(id);
    }

    private LicenciaDTO mapToDTO(final Licencia licencia, final LicenciaDTO licenciaDTO) {

        licenciaDTO.setId(licencia.getId());

        List<ClaseLicenciaDTO> clasesDTO = licencia.getClasesLicencia().stream()
                        .map(clase -> claseLicenciaService.mapToDTO(clase, new ClaseLicenciaDTO()))
                                .collect(Collectors.toList());

        licenciaDTO.setClases(clasesDTO);

        licenciaDTO.setObservaciones(licencia.getObservaciones());
        licenciaDTO.setNumero(licencia.getNroLicencia());
        return licenciaDTO;
    }

    private Licencia mapToEntity(final LicenciaDTO licenciaDTO, final Licencia licencia) {

        List<ClaseLicencia> clases = licenciaDTO.getClases().stream()
                .map(dto -> claseLicenciaService.mapToEntity(dto, new ClaseLicencia()))
                .peek(clase -> clase.setLicencia(licencia)) // muy importante para mantener la relación bidireccional
                .toList();

        licencia.setClasesLicencia(clases);

        licencia.setObservaciones(licenciaDTO.getObservaciones());
        licencia.setNroLicencia(licenciaDTO.getNumero());
        return licencia;
    }

}

