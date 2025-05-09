package madstp.backend.project.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AdministradorDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String nombre;

    @NotNull
    @Size(max = 255)
    @AdministradorDocumentoUnique
    private String documento;

    @NotNull
    private TipoDocumento tipodocumento;

    @NotNull
    @Size(max = 255)
    private String domicilio;

    @NotNull
    private LocalDate fehcaNacimiento;

    @NotNull
    private String contrasena;

}
