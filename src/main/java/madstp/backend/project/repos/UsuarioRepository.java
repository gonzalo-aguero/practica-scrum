package madstp.backend.project.repos;

import io.micrometer.observation.ObservationFilter;
import io.swagger.v3.oas.annotations.Parameter;
import madstp.backend.project.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByDocumentoIgnoreCase(String documento);

    Optional<Usuario> findByDocumento(String documento);
}
