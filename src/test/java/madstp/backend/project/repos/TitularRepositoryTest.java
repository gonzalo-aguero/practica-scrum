package madstp.backend.project.repos;

import madstp.backend.project.domain.Titular;
import madstp.backend.project.model.TipoDocumento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TitularRepositoryTest {

    @Autowired
    private TitularRepository titularRepository;

//    @BeforeEach
//    void setUp() {
//    }

    @Test
    public void testSave_ReturnSavedTitular() {
        // Arrange
        Titular titular = new Titular();
        titular.setNombre("John");
        titular.setDocumento("123456");
        titular.setTipoDocumento(TipoDocumento.DNI);
        titular.setDomicilio("123 Main Street");
        titular.setFechaNacimiento(null);
        titular.setContrasena("jd1985");
        titular.setEsDonanteOrganos(true);

        // Act
        Titular savedTitular = titularRepository.save(titular);

        // Assert
        assertNotNull(savedTitular);
        assertEquals(titular.getId(), savedTitular.getId());

    }
}