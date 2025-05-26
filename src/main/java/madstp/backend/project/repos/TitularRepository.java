package madstp.backend.project.repos;

import madstp.backend.project.domain.Titular;
import madstp.backend.project.enums.TipoDocumentoEnum;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TitularRepository extends JpaRepository<Titular, Long> {

    boolean existsByDocumentoIgnoreCase(String documento);

    Optional<Titular> findByTipoDocumentoAndDocumento(TipoDocumentoEnum tipoDocumento, String documento);

}
