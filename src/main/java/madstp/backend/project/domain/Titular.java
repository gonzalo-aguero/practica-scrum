package madstp.backend.project.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;
import madstp.backend.project.enums.GrupoSanguineoEnum;


@Getter
@Setter
@Entity
@Table(name = "Titular", schema = "public")
public class Titular extends Persona {

    @Column
    private Boolean esDonanteOrganos;

    @Column(nullable = false)
    private GrupoSanguineoEnum grupoSanguineo;
    
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "licencia_id", nullable = true, referencedColumnName = "id")
    private Licencia licencia;
}
