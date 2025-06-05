package madstp.backend.project.service;

import java.util.List;

import jakarta.validation.Valid;
import madstp.backend.project.domain.Licencia;
import madstp.backend.project.dto.LicenciaDTO;
import madstp.backend.project.model.LicenciaDTO;
import madstp.backend.project.repos.LicenciaRepository;
import madstp.backend.project.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class LicenciaService {

    private final LicenciaRepository licenciaRepository;

    public LicenciaService(final LicenciaRepository licenciaRepository) {
        this.licenciaRepository = licenciaRepository;
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
        licenciaDTO.setClases(licencia.getClases());
        licenciaDTO.setObservaciones(licencia.getObservaciones());
        licenciaDTO.setFechaEmision(licencia.getFechaEmision());
        licenciaDTO.setFechaExpiracion(licencia.getFechaExpiracion());
        licenciaDTO.setNumero(licencia.getNumero());
        return licenciaDTO;
    }

    private Licencia mapToEntity(final LicenciaDTO licenciaDTO, final Licencia licencia) {
        licencia.setClases(licenciaDTO.getClases());
        licencia.setObservaciones(licenciaDTO.getObservaciones());
        licencia.setFechaEmision(licenciaDTO.getFechaEmision());
        licencia.setFechaExpiracion(licenciaDTO.getFechaExpiracion());
        licencia.setNumero(licenciaDTO.getNumero());
        return licencia;
    }

}
