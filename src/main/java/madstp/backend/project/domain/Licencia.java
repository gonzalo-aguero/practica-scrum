package madstp.backend.project.domain;

import jakarta.persistence.*;
import lombok.*;
import madstp.backend.project.enums.ClaseLicenciaEnum;
import madstp.backend.project.enums.GrupoSanguineoEnum;


import java.time.LocalDate;
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

//    @Column(nullable = false)
//    String nroLicencia;
//
//    @Column(nullable = false)
//    String cuil;
//
//    @Column(nullable = false)
//    String apellido;
//
//    @Column(nullable = false)
//    String nombre;
//
//    @Column(nullable = false)
//    String direccion;
//
//    @Column(nullable = false)
//    String fechaNacimiento;
//
//    @Column(nullable = false)
//    Boolean esDonante;
//
//    @Column(nullable = false)
//    GrupoSanguineoEnum grupoSanguineo;

    @Column(nullable = false)
    private String numero;

    @OneToMany(mappedBy = "Licencia")
    private List<ClaseLicencia> clasesLicencia;

    @Column(nullable = false)
    private String observaciones;

    @Column(nullable = false)
    private LocalDate fechaEmision;

    @Column(nullable = false)
    private LocalDate fechaExpiracion;

    @ManyToOne
    @JoinColumn(name = "documento_titular", referencedColumnName = "documento", nullable = false)
    private Titular titular;

}
