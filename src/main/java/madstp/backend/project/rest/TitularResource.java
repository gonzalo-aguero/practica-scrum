package madstp.backend.project.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import madstp.backend.project.model.TitularDTO;
import madstp.backend.project.service.TitularService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/titulares", produces = MediaType.APPLICATION_JSON_VALUE)
public class TitularResource {

    private final TitularService titularService;

    public TitularResource(final TitularService titularService) {
        this.titularService = titularService;
    }

    @GetMapping
    public ResponseEntity<List<TitularDTO>> getAllTitulares() {
        return ResponseEntity.ok(titularService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TitularDTO> getTitular(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(titularService.get(id));
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
