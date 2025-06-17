package madstp.backend.project.service;

import madstp.backend.project.domain.Titular;
import madstp.backend.project.dto.TitularDTO;
import madstp.backend.project.enums.TipoDocumentoEnum;
import madstp.backend.project.repos.TitularRepository;
import madstp.backend.project.util.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TitularServiceTest {

    @InjectMocks
    private TitularService titularService;

    @Mock
    private TitularRepository titularRepository;

    private Titular titular;
    private TitularDTO titularDTO;

    @BeforeEach
    public void init() {
        // Inicializo los atributos de instancia
        titular = new Titular();
        titular.setId(1L);
        titular.setNombre("John Doe");
        titular.setDocumento("123456");
        titular.setTipoDocumento(TipoDocumentoEnum.DNI);
        titular.setDomicilio("123 Main Street");
        titular.setFechaNacimiento(LocalDate.of(1985, 5, 20));
        titular.setContrasena("jd1985");
        titular.setEsDonanteOrganos(true);

        titularDTO = new TitularDTO();
        titularDTO.setId(1L);
        titularDTO.setNombre("John Doe");
        titularDTO.setDocumento("123456");
        titularDTO.setTipoDocumento(TipoDocumentoEnum.DNI);
        titularDTO.setDomicilio("123 Main Street");
        titularDTO.setFechaNacimiento(LocalDate.of(1985, 5, 20));
        titularDTO.setContrasena("jd1985");
        titularDTO.setEsDonanteOrganos(true);
    }

    @Test
    public void testGet_WithValidData_ReturnTitularDTO() {
        Long titularId = 1L;
        when(titularRepository.findById(titularId)).thenReturn(Optional.of(titular));

        TitularDTO titularReturn = titularService.get(titularId);

        assertThat(titularReturn).isNotNull();
        assertEquals(titularDTO.getId(), titularReturn.getId());
        assertEquals(titularDTO.getNombre(), titularReturn.getNombre());
        assertEquals(titularDTO.getDocumento(), titularReturn.getDocumento());

    }

    @Test
    public void testCreate_WithValidData_ReturnGeneratedId() {
        // Simula guardar y devolver el titular con ID
        when(titularRepository.save(any(Titular.class))).thenReturn(titular);

        Long titularIdReturn = titularService.create(titularDTO);

        assertThat(titularIdReturn).isNotNull();
        assertEquals(titular.getId(), titularIdReturn);
    }

    @Test
    public void testUpdate_WithValidData_ReturnGeneratedId() {
        Long titularId = titular.getId();
        when(titularRepository.findById(titularId)).thenReturn(Optional.of(titular));
        when(titularRepository.save(any(Titular.class))).thenReturn(titular);

        titularService.update(titularId, titularDTO);

        TitularDTO updatedDto = titularService.get(titularId);
        assertThat(updatedDto).isNotNull();
        assertEquals(titularDTO.getNombre(), updatedDto.getNombre());
        // Más asserts si consideras necesario
    }

    @Test
    public void testGetByTipoDocumentoAndDocumento_WithValidData_ReturnTitularDTO() {
        TipoDocumentoEnum tipoDocumento = TipoDocumentoEnum.DNI;
        String documento = "123456";

        when(titularRepository.findByTipoDocumentoAndDocumento(tipoDocumento, documento))
                .thenReturn(Optional.of(titular));

        TitularDTO titularReturn = titularService.getByTipoDocumentoAndDocumento(tipoDocumento, documento);

        assertThat(titularReturn).isNotNull();
        assertEquals(tipoDocumento, titularReturn.getTipoDocumento());
        assertEquals(documento, titularReturn.getDocumento());
    }

    @Test
    public void testGetByTipoDocumentoAndDocumento_WithInvalidData_ThrowsNotFoundException() {
        TipoDocumentoEnum tipoDocumento = TipoDocumentoEnum.DNI;
        String documento = "999999";

        when(titularRepository.findByTipoDocumentoAndDocumento(tipoDocumento, documento)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> 
            titularService.getByTipoDocumentoAndDocumento(tipoDocumento, documento)
        );
    }
}