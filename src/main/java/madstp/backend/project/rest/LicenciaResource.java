package madstp.backend.project.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import madstp.backend.project.dto.LicenciaDTO;
import madstp.backend.project.service.LicenciaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/licencias", produces = MediaType.APPLICATION_JSON_VALUE)
public class LicenciaResource {

    private final LicenciaService licenciaService;

    public LicenciaResource(final LicenciaService licenciaService) {
        this.licenciaService = licenciaService;
    }

    @GetMapping
    public ResponseEntity<List<LicenciaDTO>> getAllLicencias() {
        return ResponseEntity.ok(licenciaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LicenciaDTO> getLicencia(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(licenciaService.get(id));
    }

    @GetMapping("/costo")
    public ResponseEntity<Map<String, Integer>> calcularCostoLicencia(
            @RequestParam List<String> clasesSeleccionadas,
            @RequestParam Long idTitular) {

        Integer costo = licenciaService.obtenerCostoLicencia(clasesSeleccionadas, idTitular);

        Map<String, Integer> response = new HashMap<>();
        response.put("costo", costo);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createLicencia(@RequestBody @Valid final LicenciaDTO licenciaDTO) {
        final Long createdId = licenciaService.create(licenciaDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateLicencia(@PathVariable(name = "id") final Long id,
                                               @RequestBody @Valid final LicenciaDTO licenciaDTO) {
        licenciaService.update(id, licenciaDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteLicencia(@PathVariable(name = "id") final Long id) {
        licenciaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}