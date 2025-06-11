package madstp.backend.project.repos;

import madstp.backend.project.domain.Licencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LicenciaRepository extends JpaRepository<Licencia, Long> {
    Optional<Licencia> findByTitular_Id(Long titularId);
}
