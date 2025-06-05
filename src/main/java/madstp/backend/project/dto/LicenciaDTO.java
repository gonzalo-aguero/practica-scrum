package madstp.backend.project.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import madstp.backend.project.enums.GrupoSanguineoEnum;
import madstp.backend.project.enums.TipoDocumentoEnum;

import java.time.LocalDate;

public class LicenciaDTO {

    private long id;

    @NotNull
    @Size(max = 255)
    private String nombre;

    @NotNull
    @Size(max = 255)
    private String apellido;

    @NotNull
    @Size(max = 255)
    private String cuil;

    @NotNull
    @Size(max = 255)
    private String direccion;

    @NotNull
    @Size(max = 255)
    private String nroLicencia;

    @NotNull
    @Size(max = 255)
    private LocalDate fechaNacimiento;

    @NotNull
    private GrupoSanguineoEnum grupoSanguineo;

    private Boolean esDonanteOrganos;

    private String observaciones;
}
