package madstp.backend.project.service;

import madstp.backend.project.repos.LicenciaRepository;
import madstp.backend.project.dto.LicenciaDTO;
import madstp.backend.project.domain.Licencia;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class LicenciaServiceTest {

    @Mock
    private LicenciaRepository licenciaRepository;
    @Mock
    private ClaseLicenciaService claseLicenciaService;
    @Mock
    private TitularService titularService;

    @InjectMocks
    private LicenciaService licenciaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Licencia licencia = mock(Licencia.class);
        // Simula el valor que debería tener la licencia si se va a mapear a DTO
        when(licencia.getId()).thenReturn(1L); // ejemplo si usas el id en tu mapping

        when(licenciaRepository.findAll(any(Sort.class))).thenReturn(List.of(licencia));

        List<LicenciaDTO> result = licenciaService.findAll();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);

        // Si el DTO tiene un campo id, se puede comprobar así (ajusta según tus campos)
        assertThat(result.get(0).getId()).isEqualTo(1L);

        verify(licenciaRepository).findAll(any(Sort.class));
    }

    @Test
    void testGetLicenciaFound() {
        Licencia licencia = mock(Licencia.class);
        when(licenciaRepository.findById(1L)).thenReturn(Optional.of(licencia));

        LicenciaDTO result = licenciaService.get(1L);

        assertThat(result).isNotNull();
        verify(licenciaRepository).findById(1L);
    }

    @Test
    void testGetLicenciaNotFound() {
        when(licenciaRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> licenciaService.get(2L))
                .isInstanceOf(madstp.backend.project.util.NotFoundException.class);
    }

    @Test
    void testCreateLicencia() {
        // Configura los mocks y los datos del DTO
        LicenciaDTO dto = mock(LicenciaDTO.class);
        Licencia licenciaMock = mock(Licencia.class);
        when(licenciaRepository.save(any(Licencia.class))).thenReturn(licenciaMock);
        // Simula el id retornado por licenciaMock.getId()
        when(licenciaMock.getId()).thenReturn(10L);

        Long id = licenciaService.create(dto);

        assertThat(id).isEqualTo(10L);
        verify(licenciaRepository).save(any(Licencia.class));
    }

    @Test
    void testDeleteLicencia() {
        // No importa el resultado, solo verificar repo.deleteById
        licenciaService.delete(1L);
        verify(licenciaRepository, times(1)).deleteById(1L);
    }
}