package madstp.backend.project.service;

import java.util.List;
import madstp.backend.project.domain.Usuario;
import madstp.backend.project.model.TipoDocumento;
import madstp.backend.project.model.UsuarioDTO;
import madstp.backend.project.repos.UsuarioRepository;
import madstp.backend.project.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(final UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<UsuarioDTO> findAll() {
        final List<Usuario> usuarios = usuarioRepository.findAll(Sort.by("id"));
        return usuarios.stream()
                .map(usuario -> mapToDTO(usuario, new UsuarioDTO()))
                .toList();
    }

    public UsuarioDTO get(final Long id) {
        return usuarioRepository.findById(id)
                .map(usuario -> mapToDTO(usuario, new UsuarioDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UsuarioDTO getByDocumento(final String documento){
        return usuarioRepository.findByDocumento(documento)
                .map(usuario -> mapToDTO(usuario, new UsuarioDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final UsuarioDTO usuarioDTO) {
        final Usuario usuario = new Usuario();
        mapToEntity(usuarioDTO, usuario);
        return usuarioRepository.save(usuario).getId();
    }

    public void update(final Long id, final UsuarioDTO usuarioDTO) {
        final Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(usuarioDTO, usuario);
        usuarioRepository.save(usuario);
    }

    public void delete(final Long id) {
        usuarioRepository.deleteById(id);
    }

    private UsuarioDTO mapToDTO(final Usuario usuario, final UsuarioDTO usuarioDTO) {
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setNombre(usuario.getNombre());
        usuarioDTO.setDocumento(usuario.getDocumento());
        usuarioDTO.setDomicilio(usuario.getDomicilio());
        usuarioDTO.setFechaNacimiento(usuario.getFechaNacimiento());
        usuarioDTO.setContrasena(usuario.getContrasena());
        return usuarioDTO;
    }

    private Usuario mapToEntity(final UsuarioDTO usuarioDTO, final Usuario usuario) {
        usuario.setId(usuarioDTO.getId());
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setDocumento(usuarioDTO.getDocumento());
        usuario.setTipoDocumento(usuarioDTO.getTipodocumento());
        usuario.setDomicilio(usuarioDTO.getDomicilio());
        usuario.setFechaNacimiento(usuarioDTO.getFechaNacimiento());
        usuario.setContrasena(usuarioDTO.getContrasena());
        return usuario;
    }

    public boolean documentoExists(final String documento) {
        return usuarioRepository.existsByDocumentoIgnoreCase(documento);
    }

    public boolean authenticate(final String documento, final String contrasena){
        return usuarioRepository.findByDocumento(documento)
                .map(usuario -> usuario.getContrasena().equals(contrasena))
                .orElse(false);
    }
}
