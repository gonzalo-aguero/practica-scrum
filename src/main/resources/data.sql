-- Insertar licencias
INSERT INTO licencia (id, nro_licencia, observaciones) VALUES
(1, 'ABC12345', 'Primera licencia de Juan Pérez'),
(2, 'XYZ67890', 'Licencia profesional de María Gómez'),
(3, 'LMN54321', 'Renovación reciente de licencia de Carlos Ruiz');

-- Insertar titulares vinculados a las licencias
INSERT INTO titular (
  id, nombre, documento, cuil, contrasena, domicilio,
  tipo_documento, grupo_sanguineo, fecha_nacimiento, es_donante_organos,
  date_created, last_updated, licencia_id
) VALUES
(1, 'Juan Pérez', '12345678', '20-12345678-3', 'clave1', 'Calle Falsa 123',
 1, 2, '1990-05-10', true, now(), now(), 1),
(2, 'María Gómez', '87654321', '27-87654321-7', 'clave2', 'Av. Siempre Viva 742',
 2, 4, '1985-11-23', false, now(), now(), 2),
(3, 'Carlos Ruiz', '34567890', '23-34567890-0', 'clave3', 'Boulevard Central 1000',
 0, 1, '2000-01-15', true, now(), now(), 3);

-- Insertar clases de licencia para cada titular
INSERT INTO clase_licencia (
  id, clase_licencia_enum, fecha_emision, fecha_vencimiento, licencia_id, documento_usuario_emisor
) VALUES
(1, 1, '2023-01-01', '2028-01-01', 1, NULL),
(2, 2, '2022-06-15', '2027-06-15', 2, NULL),
(3, 0, '2024-03-20', '2029-03-20', 3, NULL); 