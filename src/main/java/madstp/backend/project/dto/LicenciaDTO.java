package madstp.backend.project.dto;

import madstp.backend.project.model.ClaseLicenciaDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LicenciaDTO {

    private Long id;

    @NotNull
    private List<ClaseLicenciaDTO> clases;

    @NotNull
    @Size(max = 255)
    private String observaciones;

    @NotNull
    @Size(max = 255)
    private String numero;

}
