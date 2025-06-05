package madstp.backend.project.model;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import madstp.backend.project.enums.ClaseLicenciaEnum;


@Getter
@Setter
public class ClaseLicenciaDTO {

    private Long id;

    @NotNull
    private ClaseLicenciaEnum claseLicenciaEnum;

    @NotNull
    private LocalDate fechaEmision;

    @NotNull
    private LocalDate fechaVencimiento;

    private Long usuarioEmisor;

}
