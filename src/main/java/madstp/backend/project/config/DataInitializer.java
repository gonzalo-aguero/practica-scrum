//package madstp.backend.project.config;
//
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import madstp.backend.project.domain.Administrador;
//import madstp.backend.project.domain.Persona;
//import madstp.backend.project.domain.Titular;
//import madstp.backend.project.domain.Usuario;
//import madstp.backend.project.model.AdministradorDTO;
//import madstp.backend.project.model.TipoDocumento;
//import madstp.backend.project.model.TitularDTO;
//import madstp.backend.project.model.UsuarioDTO;
//import madstp.backend.project.service.AdministradorService;
//import madstp.backend.project.service.TitularService;
//import madstp.backend.project.service.UsuarioService;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Configuration
//@RequiredArgsConstructor
//@Profile("dev") // Solo se ejecutará en el perfil de desarrollo
//public class DataInitializer {
//
//    private final AdministradorService administradorService;
//    private final TitularService titularService;
//    private final UsuarioService usuarioService;
//
//    @PostConstruct
//    public void init() {
//        System.out.println("Inicializando datos de prueba...");
//
//        // Inicializar administradores
//        List<AdministradorDTO> administradores = List.of(
//            createAdministradorDTO("Juan Pérez", "12345678", TipoDocumento.DNI, "Calle 123", LocalDate.of(1980, 1, 1), "admin123"),
//            createAdministradorDTO("María García", "23456789", TipoDocumento.DNI, "Avenida 456", LocalDate.of(1985, 2, 2), "admin456"),
//            createAdministradorDTO("Carlos López", "34567890", TipoDocumento.DNI, "Plaza 789", LocalDate.of(1990, 3, 3), "admin789"),
//            createAdministradorDTO("Ana Martínez", "45678901", TipoDocumento.DNI, "Ruta 101", LocalDate.of(1995, 4, 4), "admin101"),
//            createAdministradorDTO("Pedro Sánchez", "56789012", TipoDocumento.DNI, "Camino 202", LocalDate.of(2000, 5, 5), "admin202")
//        );
//        administradores.forEach(administradorService::create);
//
//        // Inicializar titulares
//        List<TitularDTO> titulares = List.of(
//            createTitularDTO("Roberto Díaz", "67890123", TipoDocumento.DNI, "Calle 303", LocalDate.of(1975, 6, 6), "titular123", true),
//            createTitularDTO("Laura Torres", "78901234", TipoDocumento.DNI, "Avenida 404", LocalDate.of(1980, 7, 7), "titular456", false),
//            createTitularDTO("Miguel Ruiz", "89012345", TipoDocumento.DNI, "Plaza 505", LocalDate.of(1985, 8, 8), "titular789", true),
//            createTitularDTO("Sofía Vargas", "90123456", TipoDocumento.DNI, "Ruta 606", LocalDate.of(1990, 9, 9), "titular101", false),
//            createTitularDTO("Diego Morales", "01234567", TipoDocumento.DNI, "Camino 707", LocalDate.of(1995, 10, 10), "titular202", true)
//        );
//        titulares.forEach(titularService::create);
//
//        // Inicializar usuarios
//        List<UsuarioDTO> usuarios = List.of(
//            createUsuarioDTO("Elena Castro", "12345098", TipoDocumento.DNI, "Calle 808", LocalDate.of(2000, 11, 11), "user123"),
//            createUsuarioDTO("Fernando Luna", "23450987", TipoDocumento.DNI, "Avenida 909", LocalDate.of(2005, 12, 12), "user456"),
//            createUsuarioDTO("Carmen Vega", "34509876", TipoDocumento.DNI, "Plaza 101", LocalDate.of(2010, 1, 13), "user789"),
//            createUsuarioDTO("Ricardo Soto", "45098765", TipoDocumento.DNI, "Ruta 202", LocalDate.of(2015, 2, 14), "user101"),
//            createUsuarioDTO("Patricia Ríos", "50987654", TipoDocumento.DNI, "Camino 303", LocalDate.of(2020, 3, 15), "user202")
//        );
//        usuarios.forEach(usuarioService::create);
//    }
//
//    private AdministradorDTO createAdministradorDTO(String nombre, String documento, TipoDocumento tipoDocumento,
//                                                  String domicilio, LocalDate fechaNacimiento, String contrasena) {
//        AdministradorDTO dto = new AdministradorDTO();
//        setPersonaDTOFields(dto, nombre, documento, tipoDocumento, domicilio, fechaNacimiento, contrasena);
//        return dto;
//    }
//
//    private TitularDTO createTitularDTO(String nombre, String documento, TipoDocumento tipoDocumento,
//                                      String domicilio, LocalDate fechaNacimiento, String contrasena,
//                                      Boolean esDonanteOrganos) {
//        TitularDTO dto = new TitularDTO();
//        setPersonaDTOFields(dto, nombre, documento, tipoDocumento, domicilio, fechaNacimiento, contrasena);
//        dto.setEsDonanteOrganos(esDonanteOrganos);
//        return dto;
//    }
//
//    private UsuarioDTO createUsuarioDTO(String nombre, String documento, TipoDocumento tipoDocumento,
//                                      String domicilio, LocalDate fechaNacimiento, String contrasena) {
//        UsuarioDTO dto = new UsuarioDTO();
//        setPersonaDTOFields(dto, nombre, documento, tipoDocumento, domicilio, fechaNacimiento, contrasena);
//        return dto;
//    }
//
//    private void setPersonaDTOFields(Object dto, String nombre, String documento, TipoDocumento tipoDocumento,
//                                   String domicilio, LocalDate fechaNacimiento, String contrasena) {
//        if (dto instanceof AdministradorDTO) {
//            AdministradorDTO adminDTO = (AdministradorDTO) dto;
//            adminDTO.setNombre(nombre);
//            adminDTO.setDocumento(documento);
//            adminDTO.setTipodocumento(tipoDocumento);
//            adminDTO.setDomicilio(domicilio);
//            adminDTO.setFechaNacimiento(fechaNacimiento);
//            adminDTO.setContrasena(contrasena);
//        } else if (dto instanceof TitularDTO) {
//            TitularDTO titularDTO = (TitularDTO) dto;
//            titularDTO.setNombre(nombre);
//            titularDTO.setDocumento(documento);
//            titularDTO.setDomicilio(domicilio);
//            titularDTO.setFechaNacimiento(fechaNacimiento);
//        } else if (dto instanceof UsuarioDTO) {
//            UsuarioDTO usuarioDTO = (UsuarioDTO) dto;
//            usuarioDTO.setNombre(nombre);
//            usuarioDTO.setDocumento(documento);
//            usuarioDTO.setTipodocumento(tipoDocumento);
//            usuarioDTO.setDomicilio(domicilio);
//            usuarioDTO.setFechaNacimiento(fechaNacimiento);
//            usuarioDTO.setContrasena(contrasena);
//        }
//    }
//}