package madstp.backend.project.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import madstp.backend.project.dto.ClaseLicenciaDTO;
import madstp.backend.project.service.ClaseLicenciaService;
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
@RequestMapping(value = "/api/claseLicencias", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClaseLicenciaResource {

    private final ClaseLicenciaService claseLicenciaService;

    public ClaseLicenciaResource(final ClaseLicenciaService claseLicenciaService) {
        this.claseLicenciaService = claseLicenciaService;
    }

    @GetMapping
    public ResponseEntity<List<ClaseLicenciaDTO>> getAllClaseLicencias() {
        return ResponseEntity.ok(claseLicenciaService.findAll());
    }

    @GetMapping("/porLicencia/{id}")
    public ResponseEntity<List<ClaseLicenciaDTO>> getClaseLicencia(@PathVariable(name = "id") final Long id) {
        List<ClaseLicenciaDTO> clases = claseLicenciaService.findByLicenciaId(id);
        return ResponseEntity.ok(clases);
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createClaseLicencia(
            @RequestBody @Valid final ClaseLicenciaDTO claseLicenciaDTO) {
        final Long createdId = claseLicenciaService.create(claseLicenciaDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateClaseLicencia(@PathVariable(name = "id") final Long id,
                                                    @RequestBody @Valid final ClaseLicenciaDTO claseLicenciaDTO) {
        claseLicenciaService.update(id, claseLicenciaDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteClaseLicencia(@PathVariable(name = "id") final Long id) {
        claseLicenciaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}