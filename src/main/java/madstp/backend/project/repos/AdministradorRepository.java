package madstp.backend.project.repos;

import madstp.backend.project.domain.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AdministradorRepository extends JpaRepository<Administrador, Long> {

    boolean existsByDocumentoIgnoreCase(String documento);


    Optional<Administrador> findByDocumento(String documento);

    }
