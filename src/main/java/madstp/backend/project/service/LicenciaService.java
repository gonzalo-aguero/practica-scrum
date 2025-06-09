package madstp.backend.project.service;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import madstp.backend.project.domain.ClaseLicencia;
import madstp.backend.project.domain.Licencia;
import madstp.backend.project.dto.LicenciaDTO;
//import madstp.backend.project.model.LicenciaDTO;
import madstp.backend.project.model.ClaseLicenciaDTO;
import madstp.backend.project.repos.LicenciaRepository;
import madstp.backend.project.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class LicenciaService {

    private final LicenciaRepository licenciaRepository;
    private final ClaseLicenciaService claseLicenciaService;

    public LicenciaService(final LicenciaRepository licenciaRepository, ClaseLicenciaService claseLicenciaService) {
        this.licenciaRepository = licenciaRepository;
        this.claseLicenciaService = claseLicenciaService;
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

    public Long create(final @Valid LicenciaDTO licenciaDTO) {
        final Licencia licencia = new Licencia();
        mapToEntity(licenciaDTO, licencia);
        return licenciaRepository.save(licencia).getId();
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
