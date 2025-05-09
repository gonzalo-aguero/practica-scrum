package madstp.backend.project.repos;

import madstp.backend.project.domain.Titular;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TitularRepository extends JpaRepository<Titular, Long> {

    boolean existsByDocumentoIgnoreCase(String documento);

}
