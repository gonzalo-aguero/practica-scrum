package madstp.backend.project.repos;

import madstp.backend.project.enums.ClaseLicenciaEnum;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import madstp.backend.project.domain.ClaseLicencia;
import madstp.backend.project.domain.Licencia;
import madstp.backend.project.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;


public interface ClaseLicenciaRepository extends JpaRepository<ClaseLicencia, Long> {

    ClaseLicencia findFirstByUsuarioEmisor(Usuario usuario);
    Optional<ClaseLicencia> findByLicencia(Licencia licencia);

    Optional<ClaseLicencia> findByLicenciaAndClaseLicenciaEnumAndActivoIsTrue(Licencia licencia, ClaseLicenciaEnum claseLicenciaEnum);

    Boolean existsClaseLicenciaByLicenciaAndClaseLicenciaEnumAndActivoIsTrue(Licencia licencia, ClaseLicenciaEnum claseLicenciaEnum);

    @Modifying
    @Query("UPDATE ClaseLicencia c SET c.activo = false WHERE c.id = :id")
    void DeleteById(@Param("id") long id);

    List<ClaseLicencia> findByLicenciaId(Long licenciaId);
}