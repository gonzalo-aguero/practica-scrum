package madstp.backend.project.service;

import java.util.List;
import java.util.Optional;

import jakarta.validation.constraints.NotNull;
import madstp.backend.project.domain.ClaseLicencia;
import madstp.backend.project.domain.Licencia;
import madstp.backend.project.dto.ClaseLicenciaDTO;
import madstp.backend.project.enums.ClaseLicenciaEnum;
import madstp.backend.project.repos.ClaseLicenciaRepository;
import madstp.backend.project.repos.LicenciaRepository;
import madstp.backend.project.repos.UsuarioRepository;
import madstp.backend.project.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ClaseLicenciaService {

    private final ClaseLicenciaRepository claseLicenciaRepository;
    private final UsuarioRepository usuarioRepository;
    private final LicenciaRepository licenciaRepository;

    public ClaseLicenciaService(final ClaseLicenciaRepository claseLicenciaRepository,
                                final UsuarioRepository usuarioRepository, LicenciaRepository licenciaRepository) {
        this.claseLicenciaRepository = claseLicenciaRepository;
        this.usuarioRepository = usuarioRepository;
        this.licenciaRepository = licenciaRepository;
    }

    public List<ClaseLicenciaDTO> findAll() {
        final List<ClaseLicencia> claseLicencias = claseLicenciaRepository.findAll(Sort.by("id"));
        return claseLicencias.stream()
                .map(claseLicencia -> mapToDTO(claseLicencia, new ClaseLicenciaDTO()))
                .toList();
    }

    public List<ClaseLicenciaDTO> findByLicenciaId(long id){
        final List<ClaseLicencia> claseLicencias = claseLicenciaRepository.findByLicenciaId(id);
        return claseLicencias.stream()
                .map(claseLicencia -> mapToDTO(claseLicencia, new ClaseLicenciaDTO()))
                .toList();
    }

    public Optional<ClaseLicenciaDTO> findAllByLicencia(final Licencia licencia) {
        final Optional<ClaseLicencia> claseLicencia = claseLicenciaRepository.findByLicencia(licencia);

        return claseLicencia.map(cl -> mapToDTO(cl, new ClaseLicenciaDTO()));
    }


    public ClaseLicenciaDTO get(final Long id) {
        return claseLicenciaRepository.findById(id)
                .map(claseLicencia -> mapToDTO(claseLicencia, new ClaseLicenciaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public ClaseLicencia getByLicenciaIdAndClaseLicenciaEnumAndActivoIsTrue(final Long licenciaId, final ClaseLicenciaEnum claseLicenciaEnum){
        Licencia licencia = licenciaRepository.findById(licenciaId).orElseThrow(NotFoundException::new);
        return claseLicenciaRepository.findByLicenciaAndClaseLicenciaEnumAndActivoIsTrue(licencia, claseLicenciaEnum).orElseThrow(NotFoundException::new);
    }

    public Boolean existsLicenciaIdAndClaseLicenciaEnumAndActivoIsTrue(final Long licenciaId, final ClaseLicenciaEnum claseLicenciaEnum){
        Licencia licencia = licenciaRepository.findById(licenciaId).orElseThrow(NotFoundException::new);
        return claseLicenciaRepository.existsClaseLicenciaByLicenciaAndClaseLicenciaEnumAndActivoIsTrue(licencia, claseLicenciaEnum);
    }

    public Boolean moreRestrictive(ClaseLicencia clase1, ClaseLicenciaEnum clase2) {
        if (clase1 == null || clase2 == null) {
            throw new IllegalArgumentException("Las clases de licencia no pueden ser nulas");
        }
        return (clase1.getClaseLicenciaEnum().ordinal() < clase2.ordinal());
    }


    public Optional<ClaseLicencia> obtenerLicenciaNoA(List<ClaseLicencia> licencias) {

        return licencias.stream()
                .filter(licencia ->
                        licencia.getActivo() &&
                                !ClaseLicenciaEnum.A.equals(licencia.getClaseLicenciaEnum()))
                .findFirst();
    }



    public Long create(final ClaseLicenciaDTO claseLicenciaDTO) {
        final ClaseLicencia claseLicencia = new ClaseLicencia();
        mapToEntity(claseLicenciaDTO, claseLicencia);
        return claseLicenciaRepository.save(claseLicencia).getId();
    }

    public void update(final Long id, final ClaseLicenciaDTO claseLicenciaDTO) {
        final ClaseLicencia claseLicencia = claseLicenciaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(claseLicenciaDTO, claseLicencia);
        claseLicenciaRepository.save(claseLicencia);
    }

    public void delete(final Long id) {
        claseLicenciaRepository.deleteById(id);
    }

    public ClaseLicenciaDTO mapToDTO(final ClaseLicencia claseLicencia,
                                      final ClaseLicenciaDTO claseLicenciaDTO) {
        claseLicenciaDTO.setClaseLicenciaEnum(claseLicencia.getClaseLicenciaEnum());
        claseLicenciaDTO.setFechaEmision(claseLicencia.getFechaEmision());
        claseLicenciaDTO.setFechaVencimiento(claseLicencia.getFechaVencimiento());
        claseLicenciaDTO.setUsuarioEmisor(claseLicencia.getUsuarioEmisor() == null ? null : claseLicencia.getUsuarioEmisor().getId());
        return claseLicenciaDTO;
    }

    public ClaseLicencia mapToEntity(final ClaseLicenciaDTO claseLicenciaDTO,
                                      final ClaseLicencia claseLicencia) {
        claseLicencia.setClaseLicenciaEnum(claseLicenciaDTO.getClaseLicenciaEnum());
        claseLicencia.setFechaEmision(claseLicenciaDTO.getFechaEmision());
        claseLicencia.setFechaVencimiento(claseLicenciaDTO.getFechaVencimiento());
        if (usuarioRepository.findById(claseLicenciaDTO.getUsuarioEmisor()).isPresent()) {
            usuarioRepository.findById(claseLicenciaDTO.getUsuarioEmisor()).get();
        }
        //final Usuario usuarioEmisor = claseLicenciaDTO.getUsuarioEmisor() == null ? null : usuarioRepository.findById(claseLicenciaDTO.getUsuarioEmisor())
                //.orElseThrow(() -> new NotFoundException("usuarioEmisor not found"));
        //claseLicencia.setUsuarioEmisor(usuarioEmisor);
        return claseLicencia;
    }

}
