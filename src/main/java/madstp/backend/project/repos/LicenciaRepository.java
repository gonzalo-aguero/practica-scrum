package madstp.backend.project.repos;

import madstp.backend.project.domain.Licencia;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LicenciaRepository extends JpaRepository<Licencia, Long> {
}
