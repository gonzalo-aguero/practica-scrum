package madstp.backend.project.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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
    private Long titularId;
}
