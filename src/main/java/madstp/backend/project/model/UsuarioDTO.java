package madstp.backend.project.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UsuarioDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String nombre;

    @NotNull
    @Size(max = 255)
    @UsuarioDocumentoUnique
    private String documento;

    @NotNull
    private TipoDocumento tipodocumento;

    @NotNull
    @Size(max = 255)
    private String domicilio;

    @NotNull
    private LocalDate fechaNacimiento;

    @NotNull
    private String contrasena;

    @NotNull
    private String confirmarContrasena;
}
