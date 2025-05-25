package madstp.backend.project.service;

import madstp.backend.project.domain.Titular;
import madstp.backend.project.model.TipoDocumento;
import madstp.backend.project.model.TitularDTO;
import madstp.backend.project.repos.TitularRepository;
import madstp.backend.project.util.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TitularServiceTest {

    @InjectMocks
    private TitularService titularService;

    @Mock
    private TitularRepository titularRepository;

    private Titular titular;
    private TitularDTO titularDTO;

    @BeforeEach
    public void init(){
        titular.setId(1L);
        titular.setNombre("John Doe");
        titular.setDocumento("123456");
        titular.setTipoDocumento(TipoDocumento.DNI);
        titular.setDomicilio("123 Main Street");
        titular.setFechaNacimiento(LocalDate.of(1985, 5, 20));
        titular.setContrasena("jd1985");
        titular.setEsDonanteOrganos(true);

        titularDTO.setId(1L);
        titularDTO.setNombre("John Doe");
        titularDTO.setDocumento("123456");
        titularDTO.setTipoDocumento(TipoDocumento.DNI);
        titularDTO.setDomicilio("123 Main Street");
        titularDTO.setFechaNacimiento(LocalDate.of(1985, 5, 20));
        titularDTO.setContrasena("jd1985");
        titularDTO.setEsDonanteOrganos(true);
    }


    @Test
    public void testGet_WithValidData_ReturnTitularDTO() {
        // Arrange
        Long titularId = 1L;

        when(titularRepository.findById(titularId)).thenReturn(Optional.of(titular));

        // Act
        TitularDTO titularReturn = titularService.get(titularId);

        // Assert
        assertThat(titularReturn).isNotNull();
    }

    @Test
    public void testCreate_WithValidData_ReturnGeneratedId() {
        // Arrange
        when(titularRepository.findById(titular.getId())).thenReturn(Optional.of(titular));
        when(titularRepository.save(any(Titular.class))).thenReturn(titular);

        // Act
        Long titularIdReturn = titularService.create(titularDTO);

        // Assert
        assertThat(titularIdReturn).isNotNull();
        assertEquals(titularIdReturn, titular.getId());
    }

    @Test
    public void testUpdate_WithValidData_ReturnGeneratedId() {
        // Arrange
        Long titularId = 1L;
        when(titularRepository.findById(titularId)).thenReturn(Optional.of(titular));
        when(titularRepository.save(any(Titular.class))).thenReturn(titular);

        // Act
        titularService.update(titularId, titularDTO);

        // Assert
        assertThat(titularService.get(titularId)).isNotNull();
        assertEquals(titularDTO, titularService.get(titularId));
    }

    @Test
    public void testGetByTipoDocumentoAndDocumento_WithValidData_ReturnTitularDTO() {
        // Arrange
        TipoDocumento tipoDocumento = TipoDocumento.DNI;
        String documento = "123456";

        when(titularRepository.findByTipoDocumentoAndDocumento(tipoDocumento, documento)).thenReturn(Optional.of(titular));

        // Act
        TitularDTO titularReturn = titularService.getByTipoDocumentoAndDocumento(tipoDocumento, documento);

        // Assert
        assertThat(titularReturn).isNotNull();
        assertEquals(tipoDocumento, titularReturn.getTipoDocumento());
        assertEquals(documento, titularReturn.getDocumento());
    }

    @Test
    public void testGetByTipoDocumentoAndDocumento_WithInvalidData_ThrowsNotFoundException() {
        // Arrange
        TipoDocumento tipoDocumento = TipoDocumento.DNI;
        String documento = "999999";

        when(titularRepository.findByTipoDocumentoAndDocumento(tipoDocumento, documento)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> titularService.getByTipoDocumentoAndDocumento(tipoDocumento, documento));
    }


}