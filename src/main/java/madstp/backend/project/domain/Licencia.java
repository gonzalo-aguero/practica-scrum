package madstp.backend.project.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "Licencia", schema = "public")
public class Licencia {
    @Id
    @Column
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "titular_id", referencedColumnName = "id")
    private Titular titular;


    @Column(nullable = false)
    String nroLicencia;

    @Column(nullable = false)
    String observaciones;

    @OneToMany(mappedBy = "licencia", fetch = FetchType.EAGER)
    List<ClaseLicencia> clasesLicencia;

}
