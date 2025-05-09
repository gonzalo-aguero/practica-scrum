package madstp.backend.project.service;

import java.util.List;
import madstp.backend.project.domain.Administrador;
import madstp.backend.project.model.AdministradorDTO;
import madstp.backend.project.repos.AdministradorRepository;
import madstp.backend.project.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AdministradorService {

    private final AdministradorRepository administradorRepository;

    public AdministradorService(final AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
    }

    public List<AdministradorDTO> findAll() {
        final List<Administrador> administradores = administradorRepository.findAll(Sort.by("id"));
        return administradores.stream()
                .map(administrador -> mapToDTO(administrador, new AdministradorDTO()))
                .toList();
    }

    public AdministradorDTO get(final Long id) {
        return administradorRepository.findById(id)
                .map(administrador -> mapToDTO(administrador, new AdministradorDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public AdministradorDTO getByDocumento(final String documento) {
        return administradorRepository.findByDocumento(documento)
                .map(administrador -> mapToDTO(administrador, new AdministradorDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final AdministradorDTO administradorDTO) {
        final Administrador administrador = new Administrador();
        mapToEntity(administradorDTO, administrador);
        return administradorRepository.save(administrador).getId();
    }

    public void update(final Long id, final AdministradorDTO administradorDTO) {
        final Administrador administrador = administradorRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(administradorDTO, administrador);
        administradorRepository.save(administrador);
    }

    public void delete(final Long id) {
        administradorRepository.deleteById(id);
    }

    private AdministradorDTO mapToDTO(final Administrador administrador,
                                      final AdministradorDTO administradorDTO) {
        administradorDTO.setId(administrador.getId());
        administradorDTO.setNombre(administrador.getNombre());
        administradorDTO.setDocumento(administrador.getDocumento());
        administradorDTO.setTipodocumento(administrador.getTipoDocumento());
        administradorDTO.setDomicilio(administrador.getDomicilio());
        administradorDTO.setFechaNacimiento(administrador.getFechaNacimiento());
        administradorDTO.setContrasena(administrador.getContrasena());
        return administradorDTO;
    }

    private Administrador mapToEntity(final AdministradorDTO administradorDTO,
                                      final Administrador administrador) {
        administrador.setNombre(administradorDTO.getNombre());
        administrador.setDocumento(administradorDTO.getDocumento());
        administrador.setTipoDocumento(administradorDTO.getTipodocumento());
        administrador.setDomicilio(administradorDTO.getDomicilio());
        administrador.setFechaNacimiento(administradorDTO.getFechaNacimiento());
        administrador.setContrasena(administradorDTO.getContrasena());
        return administrador;
    }

    public boolean documentoExists(final String documento) {
        return administradorRepository.existsByDocumentoIgnoreCase(documento);
    }

}
