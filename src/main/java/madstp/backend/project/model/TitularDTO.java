package madstp.backend.project.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TitularDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String nombre;

    @NotNull
    @Size(max = 255)
    @TitularDocumentoUnique
    private String documento;

    @NotNull
    private TipoDocumento tipoDocumento;

    @NotNull
    @Size(max = 255)
    private String domicilio;

    @NotNull
    private LocalDate fechaNacimiento;

    @NotNull
    private String contrasena;

    private Boolean esDonanteOrganos;

}
