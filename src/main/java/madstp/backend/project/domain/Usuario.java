package madstp.backend.project.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;


@Entity
@Table(name = "Usuario", schema = "public")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Usuario extends Persona {

    @Column(nullable = false)
    private String contrasena;

    @OneToMany(mappedBy = "usuarioEmisor")
    List<ClaseLicencia> licenciasEmitidas;
}
