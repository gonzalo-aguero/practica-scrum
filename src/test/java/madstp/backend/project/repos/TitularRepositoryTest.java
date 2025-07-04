package madstp.backend.project.repos;

import madstp.backend.project.domain.Titular;
import madstp.backend.project.enums.TipoDocumentoEnum;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

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
        titular.setTipoDocumento(TipoDocumentoEnum.DNI);
        titular.setDomicilio("123 Main Street");
        titular.setFechaNacimiento(null);
        titular.setEsDonanteOrganos(true);

        // Act
        Titular savedTitular = titularRepository.save(titular);

        // Assert
        assertNotNull(savedTitular);
        assertEquals(titular.getId(), savedTitular.getId());

    }

    @Test
    public void testFindByTipoDocumentoAndDocumento_ReturnsTitularWhenExists() {
        // Arrange
        Titular titular = new Titular();
        titular.setNombre("Jane");
        titular.setDocumento("654321");
        titular.setTipoDocumento(TipoDocumentoEnum.PASAPORTE);
        titular.setDomicilio("456 Another Street");
        titular.setFechaNacimiento(LocalDate.of(2002, 1, 8));
        titular.setEsDonanteOrganos(false);
        titularRepository.save(titular);

        // Act
        Optional<Titular> foundTitular = titularRepository.findByTipoDocumentoAndDocumento(TipoDocumentoEnum.PASAPORTE, "654321");

        // Assert
        assertTrue(foundTitular.isPresent());
        assertEquals(titular.getId(), foundTitular.get().getId());
    }

    @Test
    public void testFindByTipoDocumentoAndDocumento_ReturnsEmptyWhenNotExists() {
        // Act
        Optional<Titular> foundTitular = titularRepository.findByTipoDocumentoAndDocumento(TipoDocumentoEnum.DNI, "000000");

        // Assert
        assertFalse(foundTitular.isPresent());
    }
}