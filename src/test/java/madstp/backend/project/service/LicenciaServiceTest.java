package madstp.backend.project.service;

import madstp.backend.project.domain.Licencia;
import madstp.backend.project.dto.ClaseLicenciaDTO;
import madstp.backend.project.dto.LicenciaDTO;
import madstp.backend.project.dto.TitularDTO;
import madstp.backend.project.enums.ClaseLicenciaEnum;
import madstp.backend.project.repos.LicenciaRepository;
import madstp.backend.project.util.NotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.ArrayList;
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

    private Licencia licencia;
    private LicenciaDTO licenciaDTO;
    private TitularDTO titularDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        licencia = new Licencia();
        licencia.setId(1L);
        licencia.setNroLicencia("12345678");
        licencia.setObservaciones("Observaciones de prueba");
        licencia.setClasesLicencia(new ArrayList<>());

        licenciaDTO = new LicenciaDTO();
        licenciaDTO.setId(1L);
        licenciaDTO.setTitularId(1L);
        licenciaDTO.setNroLicencia("12345678");
        licenciaDTO.setObservaciones("Observaciones de prueba");
        licenciaDTO.setClases(new ArrayList<>());

        titularDTO = new TitularDTO();
        titularDTO.setId(1L);
        titularDTO.setDocumento("12345678");
        titularDTO.setFechaNacimiento(LocalDate.of(1990, 1, 1));
    }

    // ----------------------- FIND ALL Y GET -----------------------

    @Test
    void cuandoBuscarTodasLasLicencias_devuelveLista() {
        when(licenciaRepository.findAll(any(Sort.class))).thenReturn(List.of(licencia));
        List<LicenciaDTO> result = licenciaService.findAll();
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(licenciaRepository).findAll(any(Sort.class));
    }

    @Test
    void cuandoBuscarLicenciaPorId_existente_devuelveDto() {
        when(licenciaRepository.findById(1L)).thenReturn(Optional.of(licencia));
        LicenciaDTO result = licenciaService.get(1L);
        assertThat(result).isNotNull();
        verify(licenciaRepository).findById(1L);
    }

    @Test
    void cuandoBuscarLicenciaPorId_inexistente_lanzaNotFound() {
        when(licenciaRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> licenciaService.get(2L))
                .isInstanceOf(NotFoundException.class);
    }

    // ----------------------- CREATE (ALTAS Y VALIDACIONES) -----------------------

    @Test
    void cuandoCrearLicenciaConDatosValidos_retornaIdCreado() {
        when(titularService.get(1L)).thenReturn(titularDTO);

        ClaseLicenciaDTO claseDTO = new ClaseLicenciaDTO();
        claseDTO.setClaseLicenciaEnum(ClaseLicenciaEnum.B);
        licenciaDTO.setClases(List.of(claseDTO));

        when(licenciaRepository.save(any(Licencia.class))).thenAnswer(invocation -> {
            Licencia lic = invocation.getArgument(0);
            lic.setId(10L);
            return lic;
        });

        Long id = licenciaService.create(licenciaDTO);

        assertThat(id).isEqualTo(10L);
        ArgumentCaptor<Licencia> licenciaCaptor = ArgumentCaptor.forClass(Licencia.class);
        verify(licenciaRepository).save(licenciaCaptor.capture());
        Licencia savedLicencia = licenciaCaptor.getValue();
        assertThat(savedLicencia.getNroLicencia()).isEqualTo(titularDTO.getDocumento());
    }

    @Test
    void cuandoCrearLicenciaProfesionalMenorDe21_lanzaExcepcion() {
        titularDTO.setFechaNacimiento(LocalDate.now().minusYears(20));
        when(titularService.get(1L)).thenReturn(titularDTO);

        ClaseLicenciaDTO claseDTO = new ClaseLicenciaDTO();
        claseDTO.setClaseLicenciaEnum(ClaseLicenciaEnum.C); // Profesional
        licenciaDTO.setClases(List.of(claseDTO));

        assertThatThrownBy(() -> licenciaService.create(licenciaDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La edad mínima para las clases C, D y E es 21 años");
    }

    @Test
    void cuandoCrearLicenciaMenorDe17_lanzaExcepcion() {
        titularDTO.setFechaNacimiento(LocalDate.now().minusYears(16));
        when(titularService.get(1L)).thenReturn(titularDTO);

        ClaseLicenciaDTO claseDTO = new ClaseLicenciaDTO();
        claseDTO.setClaseLicenciaEnum(ClaseLicenciaEnum.A);
        licenciaDTO.setClases(List.of(claseDTO));

        assertThatThrownBy(() -> licenciaService.create(licenciaDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La edad mínima para esta clase es 17 años");
    }

    @Test
    void cuandoCrearLicenciaProfesionalSinLicenciaBPrevia_lanzaExcepcion() {
        when(titularService.get(1L)).thenReturn(titularDTO);

        ClaseLicenciaDTO claseDTO = new ClaseLicenciaDTO();
        claseDTO.setClaseLicenciaEnum(ClaseLicenciaEnum.C);
        licenciaDTO.setClases(List.of(claseDTO));

        when(licenciaRepository.findByTitular_Id(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> licenciaService.create(licenciaDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Para obtener una licencia profesional (C, D o E) debe haber tenido una licencia clase B al menos un año antes");
    }

    // ----------------ார்கள் OTROS MÉTODOS -----------------------

    @Test
    void cuandoCalcularVigenciaLicencia_jovenDevuelveVigenciaCorrecta() {
        titularDTO.setFechaNacimiento(LocalDate.now().minusYears(18));
        when(titularService.get(1L)).thenReturn(titularDTO);
        when(licenciaRepository.findByTitular_Id(1L)).thenReturn(Optional.empty());
        LocalDate fechaVencimiento = licenciaService.calcularVigenciaLicencia(1L, titularService, claseLicenciaService);
        assertThat(fechaVencimiento).isEqualTo(LocalDate.now().plusYears(3));
    }

    // ----------------------- DELETE -----------------------

    @Test
    void cuandoEliminarLicencia_llamaADeleteById() {
        licenciaService.delete(1L);
        verify(licenciaRepository, times(1)).deleteById(1L);
    }
}