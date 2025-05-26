package madstp.backend.project.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import madstp.backend.project.enums.TipoDocumentoEnum;


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
    private TipoDocumentoEnum tipodocumento;

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
