-- Insertar usuarios del sistema (personal municipal)
INSERT INTO usuario (
    id, nombre, apellido, documento, tipo_documento, domicilio,
    fecha_nacimiento, contrasena, date_created, last_updated
)
VALUES
    (1, 'Juan', 'Rodríguez', '30456789', 0, 'Av. Rivadavia 8000',
     '1980-07-15', 'pass', now(), now()),
    (2, 'María', 'Fernández', '25789123', 0, 'Callao 500',
     '1975-11-30', 'pass', now(), now())
ON CONFLICT (id) DO NOTHING;

-- Insertar titulares vinculados a las licencias
INSERT INTO titular (
    id, nombre, apellido, documento, tipo_documento, domicilio,
    fecha_nacimiento, grupo_sanguineo, es_donante_organos,
    date_created, last_updated, licencia_id
)
VALUES
    (1, 'Juan', 'Pérez', '12345678', 0, 'Calle Falsa 123',
     '1990-05-10', 2, true, now(), now(),null),
    (2, 'María', 'Gómez', '87654321', 0, 'Av. Siempre Viva 742',
     '1985-11-23', 4, false, now(), now(),null),
    (3, 'Carlos', 'Ruiz', '34567890', 0, 'Boulevard Central 1000',
     '2000-01-15', 1, true, now(), now(), null)
ON CONFLICT (id) DO NOTHING;

-- Insertar licencias
INSERT INTO licencia (id, nro_licencia, observaciones, titular_id)
VALUES
(1, 'ABC12345', 'Primera licencia de Juan Pérez',1),
(2, 'XYZ67890', 'Licencia profesional de María Gómez',2),
(3, 'LMN54321', 'Renovación reciente de licencia de Carlos Ruiz',3)
ON CONFLICT (id) DO NOTHING;

UPDATE titular SET licencia_id = 1 WHERE id=1;
UPDATE titular SET licencia_id = 2 WHERE id=2;
UPDATE titular SET licencia_id = 3 WHERE id=3;

-- Insertar clases de licencia para cada titular
INSERT INTO clase_licencia (
  id, clase_licencia_enum, fecha_emision, fecha_vencimiento, licencia_id, documento_usuario_emisor, activo
)
VALUES
(1, 1, '2023-01-01', '2028-01-01', 1, '30456789', true),
(2, 2, '2022-06-15', '2027-06-15', 2, '25789123', true),
(3, 0, '2024-03-20', '2029-03-20', 3, '25789123', true)
ON CONFLICT (id) DO NOTHING;