package madstp.backend.project.repos;

import madstp.backend.project.domain.Titular;
import madstp.backend.project.enums.TipoDocumentoEnum;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface TitularRepository extends JpaRepository<Titular, Long> {

    boolean existsByDocumentoIgnoreCase(String documento);

    Optional<Titular> findByTipoDocumentoAndDocumento(TipoDocumentoEnum tipoDocumento, String documento);

    @Query("SELECT t FROM Titular t WHERE " +
            "(:nombre IS NULL OR LOWER(t.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
            "(:apellido IS NULL OR LOWER(t.apellido) LIKE LOWER(CONCAT('%', :apellido, '%')))")
    List<Titular> findByNombreLikeAndApellidoLike(
            @Param("nombre") String nombre,
            @Param("apellido") String apellido,
            Sort sort
    );
    List<Titular> findByNombreIgnoreCaseStartingWithAndApellidoIgnoreCaseStartingWith(String nombre, String apellido);
}