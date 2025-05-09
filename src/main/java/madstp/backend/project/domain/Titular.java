package madstp.backend.project.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Titular extends Persona {

    @Column
    private Boolean esDonanteOrganos;

}
