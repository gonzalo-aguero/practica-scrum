package madstp.backend.project.service;

import java.util.List;
import java.util.stream.Collectors;

import madstp.backend.project.domain.Titular;
import madstp.backend.project.dto.ClaseLicenciaDTO;
import madstp.backend.project.dto.TitularDTO;
import madstp.backend.project.dto.LicenciaDTO;
import madstp.backend.project.enums.TipoDocumentoEnum;
import madstp.backend.project.repos.TitularRepository;
import madstp.backend.project.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TitularService {

    private final TitularRepository titularRepository;

    public TitularService(final TitularRepository titularRepository) {
        this.titularRepository = titularRepository;
    }

    public List<TitularDTO> findAll() {
        final List<Titular> titulares = titularRepository.findAll(Sort.by("id"));
        return titulares.stream()
                .map(titular -> mapToDTO(titular, new TitularDTO()))
                .toList();
    }

    public TitularDTO get(final Long id) {
        return titularRepository.findById(id)
                .map(titular -> mapToDTO(titular, new TitularDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public TitularDTO getByTipoDocumentoAndDocumento(final TipoDocumentoEnum tipoDocumento, final String documento) {
        return titularRepository.findByTipoDocumentoAndDocumento(tipoDocumento, documento)
                .map(titular -> mapToDTO(titular, new TitularDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final TitularDTO titularDTO) {
        final Titular titular = new Titular();
        mapToEntity(titularDTO, titular);
        return titularRepository.save(titular).getId();
    }

    public void update(final Long id, final TitularDTO titularDTO) {
        final Titular titular = titularRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(titularDTO, titular);
        titularRepository.save(titular);
    }

    public void delete(final Long id) {
        titularRepository.deleteById(id);
    }

    private TitularDTO mapToDTO(final Titular titular, final TitularDTO titularDTO) {
        titularDTO.setId(titular.getId());
        titularDTO.setNombre(titular.getNombre());
        titularDTO.setApellido(titular.getApellido());
        titularDTO.setTipoDocumento(titular.getTipoDocumento());
        titularDTO.setDocumento(titular.getDocumento());
        titularDTO.setDomicilio(titular.getDomicilio());
        titularDTO.setFechaNacimiento(titular.getFechaNacimiento());
        titularDTO.setEsDonanteOrganos(titular.getEsDonanteOrganos());
        titularDTO.setGrupoSanguineo(titular.getGrupoSanguineo());
        
        if (titular.getLicencia() != null) {
            LicenciaDTO licenciaDTO = new LicenciaDTO();
            licenciaDTO.setId(titular.getLicencia().getId());
            licenciaDTO.setNroLicencia(titular.getLicencia().getNroLicencia());
            licenciaDTO.setObservaciones(titular.getLicencia().getObservaciones());
            licenciaDTO.setTitularId(titular.getLicencia().getTitular().getId());
        
            // Mapear las clases de licencia
            if (titular.getLicencia().getClasesLicencia() != null) {
                List<ClaseLicenciaDTO> clasesDTO = titular.getLicencia().getClasesLicencia()
                    .stream()
                    .map(clase -> {
                        ClaseLicenciaDTO claseDTO = new ClaseLicenciaDTO();
                        claseDTO.setLicenciaId(clase.getId());
                        claseDTO.setClaseLicenciaEnum(clase.getClaseLicenciaEnum());
                        claseDTO.setFechaEmision(clase.getFechaEmision());
                        claseDTO.setFechaVencimiento(clase.getFechaVencimiento());
                        claseDTO.setUsuarioEmisor(clase.getUsuarioEmisor() == null ? null : clase.getUsuarioEmisor().getId());
                        return claseDTO;
                    })
                    .collect(Collectors.toList());
                
                licenciaDTO.setClases(clasesDTO);
            }
        
            titularDTO.setLicencia(licenciaDTO);
        }
    
        return titularDTO;
    }

    private Titular mapToEntity(final TitularDTO titularDTO, final Titular titular) {
        titular.setNombre(titularDTO.getNombre());
        titular.setApellido(titularDTO.getApellido());
        titular.setTipoDocumento(titularDTO.getTipoDocumento());
        titular.setDocumento(titularDTO.getDocumento());
        titular.setDomicilio(titularDTO.getDomicilio());
        titular.setFechaNacimiento(titularDTO.getFechaNacimiento());
        titular.setGrupoSanguineo(titularDTO.getGrupoSanguineo());
        titular.setEsDonanteOrganos(titularDTO.getEsDonanteOrganos());
        return titular;
    }

    public boolean documentoExists(final String documento) {
        return titularRepository.existsByDocumentoIgnoreCase(documento);
    }

}