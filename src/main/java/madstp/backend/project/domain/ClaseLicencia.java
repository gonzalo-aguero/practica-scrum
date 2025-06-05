package madstp.backend.project.domain;

import jakarta.persistence.*;
import lombok.*;
import madstp.backend.project.enums.ClaseLicenciaEnum;

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

/*
LocalDate calcularFechaVencimiento(string fechaNacimiento){
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDate fechaNac = LocalDate.parse(fechaNacimiento, formatter);
    LocalDate fechaVencimiento;

    Long edad = ChronoUnit.YEARS.between(fechaNac, LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires")));

    if(edad<21){
        if(si es la primer licencia){
            fechaVencimiento = LocalDate.of(LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires")).getYear()+1, fechaNac.getMonth(), fechaNac.getDayOfMonth());
        }
        else{
            fechaVencimiento = LocalDate.of(LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires")).getYear()+3, fechaNac.getMonth(), fechaNac.getDayOfMonth());
        }
    }
    else {
        if(edad >=21 && edad <= 46){
            fechaVencimiento = LocalDate.of(LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires")).getYear()+5, fechaNac.getMonth(), fechaNac.getDayOfMonth());
        }
        else {
            if(edad > 46 && edad <= 60){
                fechaVencimiento = LocalDate.of(LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires")).getYear()+4, fechaNac.getMonth(), fechaNac.getDayOfMonth());
            }
            else{
                if(edad > 60 && edad <= 70){
                    fechaVencimiento = LocalDate.of(LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires")).getYear()+3, fechaNac.getMonth(), fechaNac.getDayOfMonth());
                }
                else{
                    fechaVencimiento = LocalDate.of(LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires")).getYear()+1, fechaNac.getMonth(), fechaNac.getDayOfMonth());
                }
            }
        }
    }

    return fechaVencimiento;
}
 */