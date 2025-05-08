package madstp.backend.project.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import madstp.backend.project.model.AdministradorDTO;
import madstp.backend.project.service.AdministradorService;
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
@RequestMapping(value = "/api/administradors", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdministradorResource {

    private final AdministradorService administradorService;

    public AdministradorResource(final AdministradorService administradorService) {
        this.administradorService = administradorService;
    }

    @GetMapping
    public ResponseEntity<List<AdministradorDTO>> getAllAdministradors() {
        return ResponseEntity.ok(administradorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdministradorDTO> getAdministrador(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(administradorService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createAdministrador(
            @RequestBody @Valid final AdministradorDTO administradorDTO) {
        final Long createdId = administradorService.create(administradorDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateAdministrador(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final AdministradorDTO administradorDTO) {
        administradorService.update(id, administradorDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAdministrador(@PathVariable(name = "id") final Long id) {
        administradorService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
