package madstp.backend.project.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;


@Getter
@Setter
@Entity
@Table(name = "Titular", schema = "public")
public class Titular extends Persona {


    @Column
    private Boolean esDonanteOrganos;

}
