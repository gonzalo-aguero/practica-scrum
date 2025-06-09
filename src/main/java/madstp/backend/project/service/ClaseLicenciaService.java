package madstp.backend.project.service;

import java.util.List;
import madstp.backend.project.domain.ClaseLicencia;
import madstp.backend.project.domain.Usuario;
import madstp.backend.project.model.ClaseLicenciaDTO;
import madstp.backend.project.repos.ClaseLicenciaRepository;
import madstp.backend.project.repos.UsuarioRepository;
import madstp.backend.project.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ClaseLicenciaService {

    private final ClaseLicenciaRepository claseLicenciaRepository;
    private final UsuarioRepository usuarioRepository;

    public ClaseLicenciaService(final ClaseLicenciaRepository claseLicenciaRepository,
                                final UsuarioRepository usuarioRepository) {
        this.claseLicenciaRepository = claseLicenciaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<ClaseLicenciaDTO> findAll() {
        final List<ClaseLicencia> claseLicencias = claseLicenciaRepository.findAll(Sort.by("id"));
        return claseLicencias.stream()
                .map(claseLicencia -> mapToDTO(claseLicencia, new ClaseLicenciaDTO()))
                .toList();
    }

    public ClaseLicenciaDTO get(final Long id) {
        return claseLicenciaRepository.findById(id)
                .map(claseLicencia -> mapToDTO(claseLicencia, new ClaseLicenciaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ClaseLicenciaDTO claseLicenciaDTO) {
        final ClaseLicencia claseLicencia = new ClaseLicencia();
        mapToEntity(claseLicenciaDTO, claseLicencia);
        return claseLicenciaRepository.save(claseLicencia).getId();
    }

    public void update(final Long id, final ClaseLicenciaDTO claseLicenciaDTO) {
        final ClaseLicencia claseLicencia = claseLicenciaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(claseLicenciaDTO, claseLicencia);
        claseLicenciaRepository.save(claseLicencia);
    }

    public void delete(final Long id) {
        claseLicenciaRepository.deleteById(id);
    }

    public ClaseLicenciaDTO mapToDTO(final ClaseLicencia claseLicencia,
                                      final ClaseLicenciaDTO claseLicenciaDTO) {
        claseLicenciaDTO.setId(claseLicencia.getId());
        claseLicenciaDTO.setClaseLicenciaEnum(claseLicencia.getClaseLicenciaEnum());
        claseLicenciaDTO.setFechaEmision(claseLicencia.getFechaEmision());
        claseLicenciaDTO.setFechaVencimiento(claseLicencia.getFechaVencimiento());
        claseLicenciaDTO.setUsuarioEmisor(claseLicencia.getUsuarioEmisor() == null ? null : claseLicencia.getUsuarioEmisor().getId());
        return claseLicenciaDTO;
    }

    public ClaseLicencia mapToEntity(final ClaseLicenciaDTO claseLicenciaDTO,
                                      final ClaseLicencia claseLicencia) {
        claseLicencia.setClaseLicenciaEnum(claseLicenciaDTO.getClaseLicenciaEnum());
        claseLicencia.setFechaEmision(claseLicenciaDTO.getFechaEmision());
        claseLicencia.setFechaVencimiento(claseLicenciaDTO.getFechaVencimiento());
        final Usuario usuarioEmisor = claseLicenciaDTO.getUsuarioEmisor() == null ? null : usuarioRepository.findById(claseLicenciaDTO.getUsuarioEmisor())
                .orElseThrow(() -> new NotFoundException("usuarioEmisor not found"));
        claseLicencia.setUsuarioEmisor(usuarioEmisor);
        return claseLicencia;
    }

}
