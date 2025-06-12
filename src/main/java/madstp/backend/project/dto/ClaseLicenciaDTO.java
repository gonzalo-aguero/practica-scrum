package madstp.backend.project.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import madstp.backend.project.enums.ClaseLicenciaEnum;

@Getter
@Setter
public class ClaseLicenciaDTO {

    private Long licenciaId;

    @NotNull
    private ClaseLicenciaEnum claseLicenciaEnum;

    private LocalDate fechaEmision;

    private LocalDate fechaVencimiento;

    private Long usuarioEmisor;
}
