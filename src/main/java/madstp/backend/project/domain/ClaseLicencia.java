package madstp.backend.project.domain;

import jakarta.persistence.*;
import lombok.*;
import madstp.backend.project.model.ClaseLicenciaEnum;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "ClaseLicencia", schema = "public")
public class ClaseLicencia {
    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "licencia_id")
    Licencia licencia;

    @Column(nullable = false)
    ClaseLicenciaEnum claseLicenciaEnum;

    @Column(nullable = false)
    LocalDate fechaEmision;

    @Column(nullable = false)
    LocalDate fechaVencimiento;

    @ManyToOne
    @JoinColumn(name = "usuario_responsable_id")
    Usuario usuarioResponsable;
}
