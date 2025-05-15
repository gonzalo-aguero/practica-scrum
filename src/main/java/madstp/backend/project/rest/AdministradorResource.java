package madstp.backend.project.rest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import madstp.backend.project.model.AdministradorDTO;
import madstp.backend.project.service.AdministradorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/administradores", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @GetMapping("/{documento}")
    public ResponseEntity<AdministradorDTO> getByDocumentoAdministrador(
            @PathVariable(name = "documento") final String documento) {
        return ResponseEntity.ok(administradorService.getByDocumento(documento));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Boolean> authenticate(
            @RequestBody final AdministradorDTO administradorDTO){
        return ResponseEntity.ok(administradorService.authenticate(administradorDTO.getDocumento(), administradorDTO.getContrasena()));
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
