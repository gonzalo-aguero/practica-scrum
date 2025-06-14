package madstp.backend.project.repos;

import madstp.backend.project.domain.ClaseLicencia;
import madstp.backend.project.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClaseLicenciaRepository extends JpaRepository<ClaseLicencia, Long> {

    ClaseLicencia findFirstByUsuarioEmisor(Usuario usuario);

}