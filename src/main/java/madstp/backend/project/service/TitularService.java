package madstp.backend.project.service;

import java.util.List;
import madstp.backend.project.domain.Titular;
import madstp.backend.project.dto.TitularDTO;
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
        titularDTO.setTipoDocumento(titular.getTipoDocumento());
        titularDTO.setDocumento(titular.getDocumento());
        titularDTO.setDomicilio(titular.getDomicilio());
        titularDTO.setFechaNacimiento(titular.getFechaNacimiento());
        titularDTO.setEsDonanteOrganos(titular.getEsDonanteOrganos());
        return titularDTO;
    }

    private Titular mapToEntity(final TitularDTO titularDTO, final Titular titular) {
        titular.setNombre(titularDTO.getNombre());
        titular.setTipoDocumento(titularDTO.getTipoDocumento());
        titular.setDocumento(titularDTO.getDocumento());
        titular.setDomicilio(titularDTO.getDomicilio());
        titular.setFechaNacimiento(titularDTO.getFechaNacimiento());
        titular.setEsDonanteOrganos(titularDTO.getEsDonanteOrganos());
        return titular;
    }

    public boolean documentoExists(final String documento) {
        return titularRepository.existsByDocumentoIgnoreCase(documento);
    }

}
