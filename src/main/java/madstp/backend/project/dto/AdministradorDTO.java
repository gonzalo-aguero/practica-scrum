package madstp.backend.project.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import madstp.backend.project.enums.TipoDocumentoEnum;


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
    private TipoDocumentoEnum tipodocumento;

    @NotNull
    @Size(max = 255)
    private String domicilio;

    @NotNull
    private LocalDate fechaNacimiento;

    @NotNull
    private String contrasena;

}
