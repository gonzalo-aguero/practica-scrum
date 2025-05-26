package madstp.backend.project.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import madstp.backend.project.model.TipoDocumento;
import madstp.backend.project.model.TitularDTO;
import madstp.backend.project.service.TitularService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/titulares", produces = MediaType.APPLICATION_JSON_VALUE)
public class TitularResource {

    private final TitularService titularService;

    public TitularResource(final TitularService titularService) {
        this.titularService = titularService;
    }

    @GetMapping("/all-titulares")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<List<TitularDTO>> getAllTitulares() {
        return ResponseEntity.ok(titularService.findAll());
    }

    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<TitularDTO> getTitular(@PathVariable(name = "id") @NotBlank(message = "El id es obligatorio") final Long id) {
        return ResponseEntity.ok(titularService.get(id));
    }

    @GetMapping
    @ApiResponse(responseCode = "200")
    public ResponseEntity<?> getTitularByTipoDocumentoAndDocumento(@RequestParam(required = false) final TipoDocumento tipoDocumento, @RequestParam(required = false) @NotBlank(message = "El documento es obligatorio") final String documento) {
        TitularDTO titularDTO = titularService.getByTipoDocumentoAndDocumento(tipoDocumento, documento);
        if (titularDTO == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(Map.of("message", "No se encontró ningún titular"));
        }
        return ResponseEntity.ok(titularDTO);
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createTitular(@RequestBody @Valid final TitularDTO titularDTO) {
        final Long createdId = titularService.create(titularDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateTitular(@PathVariable(name = "id") final Long id,
                                              @RequestBody @Valid final TitularDTO titularDTO) {
        titularService.update(id, titularDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteTitular(@PathVariable(name = "id") final Long id) {
        titularService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
