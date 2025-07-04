package madstp.backend.project.service;

import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import madstp.backend.project.domain.ClaseLicencia;
import madstp.backend.project.domain.Licencia;
import madstp.backend.project.dto.LicenciaDTO;
import madstp.backend.project.dto.ClaseLicenciaDTO;
import madstp.backend.project.repos.LicenciaRepository;
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
        final List<Licencia> licencias = licenciaRepository.findAll();
        System.out.println("P1 " + licencias);

        List<LicenciaDTO> licenciaDTOs = licencias.stream()
                .map(licencia -> mapToDTO(licencia, new LicenciaDTO()))
                .toList();

        System.out.println(licenciaDTOs);

        return licenciaDTOs;
    }

    public Optional<LicenciaDTO> findByTitularId(Long id) {
        return licenciaRepository.findByTitular_Id(id)
                .map(licencia -> mapToDTO(licencia, new LicenciaDTO()));
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
        LocalDate fechaInicioVigencia = LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires"));
        LocalDate fechaVencimiento = calcularVigenciaLicencia(titular.getId(), titularService, claseLicenciaService);

        // Setear fechas en cada clase a incorporar
        List<ClaseLicenciaDTO> clasesAIncorporar = licenciaDTO.getClases();
        for (ClaseLicenciaDTO claseDTO : clasesAIncorporar) {
            claseDTO.setFechaEmision(fechaInicioVigencia);
            claseDTO.setFechaVencimiento(fechaVencimiento);
        }

        // Calcular costo de la Licencia
        //Integer costo = obtenerCostoLicencia(licenciaDTO);

        final Licencia licencia = new Licencia();
        mapToEntity(licenciaDTO, licencia);

        licencia.setNroLicencia(titular.getDocumento());

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

    LocalDate calcularVigenciaLicencia(long titularId, TitularService titularService, ClaseLicenciaService claseLicenciaService) {

        TitularDTO titular = titularService.get(titularId);

//          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaNac = titular.getFechaNacimiento();
        LocalDate fechaVencimiento;

        long edad = ChronoUnit.YEARS.between(fechaNac, LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires")));

        if(edad<21){
            if(licenciaRepository.findByTitular_Id(titularId).isPresent()){
                fechaVencimiento = LocalDate.of(LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires")).getYear()+3, fechaNac.getMonth(), fechaNac.getDayOfMonth());
            }
        else{
                fechaVencimiento = LocalDate.of(LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires")).getYear()+1, fechaNac.getMonth(), fechaNac.getDayOfMonth());
            }
        }
        else {
            if(edad <= 46){
                fechaVencimiento = LocalDate.of(LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires")).getYear()+5, fechaNac.getMonth(), fechaNac.getDayOfMonth());
            }
            else {
                if(edad <= 60){
                    fechaVencimiento = LocalDate.of(LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires")).getYear()+4, fechaNac.getMonth(), fechaNac.getDayOfMonth());
                }
                else{
                    if(edad <= 70){
                        fechaVencimiento = LocalDate.of(LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires")).getYear()+3, fechaNac.getMonth(), fechaNac.getDayOfMonth());
                    }
                    else{
                        fechaVencimiento = LocalDate.of(LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires")).getYear()+1, fechaNac.getMonth(), fechaNac.getDayOfMonth());
                    }
                }
            }
        }

        return fechaVencimiento;

    }

    private Integer calcularCostoLicencia(int vigenciaAnios, List<ClaseLicenciaEnum> listaClaseLicenciaEnum){
        int costo = 8;//8 por gastos administrativos.
        if(vigenciaAnios == 5){
            if(listaClaseLicenciaEnum.contains(ClaseLicenciaEnum.A)){
                costo += 40;
            }
            if(listaClaseLicenciaEnum.contains(ClaseLicenciaEnum.B)){
                costo += 40;
            }
            if(listaClaseLicenciaEnum.contains(ClaseLicenciaEnum.C)){
                costo += 47;
            }
            if(listaClaseLicenciaEnum.contains(ClaseLicenciaEnum.D)){
                costo += 59;
            }
            if(listaClaseLicenciaEnum.contains(ClaseLicenciaEnum.G)){
                costo += 40;
            }
        }else if(vigenciaAnios == 4){
            if(listaClaseLicenciaEnum.contains(ClaseLicenciaEnum.A)){
                costo += 30;
            }
            if(listaClaseLicenciaEnum.contains(ClaseLicenciaEnum.B)){
                costo += 30;
            }
            if(listaClaseLicenciaEnum.contains(ClaseLicenciaEnum.C)){
                costo += 35;
            }
            if(listaClaseLicenciaEnum.contains(ClaseLicenciaEnum.D)){
                costo += 44;
            }
            if(listaClaseLicenciaEnum.contains(ClaseLicenciaEnum.G)){
                costo += 30;
            }
        }else if(vigenciaAnios == 3){
            if(listaClaseLicenciaEnum.contains(ClaseLicenciaEnum.A)){
                costo += 25;
            }
            if(listaClaseLicenciaEnum.contains(ClaseLicenciaEnum.B)){
                costo += 25;
            }
            if(listaClaseLicenciaEnum.contains(ClaseLicenciaEnum.C)){
                costo += 30;
            }
            if(listaClaseLicenciaEnum.contains(ClaseLicenciaEnum.D)){
                costo += 39;
            }
            if(listaClaseLicenciaEnum.contains(ClaseLicenciaEnum.G)){
                costo += 25;
            }
        }else if(vigenciaAnios == 1){
            if(listaClaseLicenciaEnum.contains(ClaseLicenciaEnum.A)){
                costo += 20;
            }
            if(listaClaseLicenciaEnum.contains(ClaseLicenciaEnum.B)){
                costo += 20;
            }
            if(listaClaseLicenciaEnum.contains(ClaseLicenciaEnum.C)){
                costo += 23;
            }
            if(listaClaseLicenciaEnum.contains(ClaseLicenciaEnum.D)){
                costo += 29;
            }
            if(listaClaseLicenciaEnum.contains(ClaseLicenciaEnum.G)){
                costo += 20;
            }
        }
        return costo;
    }

    public Integer obtenerCostoLicencia(List<String> clasesSeleccionadasStr, Long idTitular) {
        // Convertir strings a enums
        List<ClaseLicenciaEnum> listaClaseLicenciaEnum = clasesSeleccionadasStr.stream()
                .map(String::toUpperCase)
                .map(ClaseLicenciaEnum::valueOf)
                .toList();

        LocalDate fechaVencimiento = calcularVigenciaLicencia(idTitular, titularService, claseLicenciaService);

        int vigenciaAnios = Period.between(LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires")), fechaVencimiento).getYears();

        return calcularCostoLicencia(vigenciaAnios, listaClaseLicenciaEnum);
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
        licenciaDTO.setNroLicencia(licencia.getNroLicencia());

        List<ClaseLicenciaDTO> clasesDTO = licencia.getClasesLicencia().stream()
                        .map(clase -> claseLicenciaService.mapToDTO(clase, new ClaseLicenciaDTO()))
                                .collect(Collectors.toList());

        licenciaDTO.setClases(clasesDTO);
        licenciaDTO.setObservaciones(licencia.getObservaciones());
        return licenciaDTO;
    }

    private Licencia mapToEntity(final LicenciaDTO licenciaDTO, final Licencia licencia) {

        List<ClaseLicencia> clases = licenciaDTO.getClases().stream()
                .map(dto -> claseLicenciaService.mapToEntity(dto, new ClaseLicencia()))// muy importante para mantener la relación bidireccional
                .toList();

        System.out.println(clases);

        licencia.setClasesLicencia(clases);

        licencia.setObservaciones(licenciaDTO.getObservaciones());

        return licencia;
    }

}

