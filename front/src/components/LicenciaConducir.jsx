// src/components/LicenciaConducir.jsx
import React from 'react';

const descripcionClases = {
    'A1': 'Motocicletas hasta 50cc y una potencia máxima de 4 kW si es eléctrica.',
    'A1.1': 'Ciclomotores y motocicletas hasta 50cc o 4 kW si es eléctrica.',
    'A2': 'Motocicletas de más de 50cc y hasta 150cc.',
    'A3': 'Motocicletas de más de 150cc y hasta 300cc.',
    'A4': 'Motocicletas de más de 300cc.',
    'B1': 'Automóviles, utilitarios, vans hasta 3.500 kg.',
    'B2': 'Vehículos de clase B1 con acoplado o casa rodante.',
    'C': 'Camiones sin acoplado y casas rodantes motorizadas.',
    'D1': 'Transporte de hasta 8 pasajeros.',
    'D2': 'Transporte de más de 8 pasajeros.',
    'D3': 'Servicio de emergencia, seguridad o transporte escolar.',
    'E1': 'Camiones articulados o con acoplado.',
    'E2': 'Maquinaria especial no agrícola.',
    'F': 'Personas con discapacidad que requieren adaptaciones.',
    'G1': 'Tractores agrícolas.',
    'G2': 'Maquinaria agrícola autopropulsada.',
    'G3': 'Equipos especiales autopropulsados.',
};

const LicenciaConducir = React.forwardRef(({ licencia, datos, clasesLicencia, usuariosEmisores }, ref) => {
    const clases = Array.isArray(datosClaseLicencia.clases) ? datosClaseLicencia.clases : [];

    // Encuentra la clase con la fecha de emisión más reciente
    const claseMasReciente = Array.isArray(clasesLicencia) && clasesLicencia.length > 0
      ? clasesLicencia.reduce((max, current) => {
          // Convierte las fechas a timestamp si llegan como string
          const fechaMax = new Date(max.fechaEmision).getTime();
          const fechaCurrent = new Date(current.fechaEmision).getTime();
          return fechaCurrent > fechaMax ? current : max;
        })
      : null;

    return (
        <div ref={ref} className="flex flex-col md:flex-row border border-gray-300 shadow-lg max-w-4xl p-4 bg-white font-sans text-sm" style={{ fontFamily: 'sans-serif' }}>
            {/* Columna izquierda */}
            <div className="w-full md:w-1/2 p-4 border-r border-gray-300 relative">
                <h1 className="text-sm font-bold uppercase text-blue-800">Licencia Nacional de Conducir</h1>
                <img src={datos.fotoUrl} alt="Foto titular" className="w-32 h-40 mt-2 object-cover border" />

                <div className="mt-4 space-y-1">
                    <p><strong>Nombre:</strong> {datos.nombre}</p>
                    <p><strong>Apellido:</strong> {datos.apellido}</p>
                    <p><strong>F. Nacimiento:</strong> {datos.fechaNacimiento}</p>
                    <p><strong>Domicilio:</strong> {datos.domicilio}</p>
                    <p><strong>Localidad:</strong> Santa Fe</p>
                    <p><strong>Provincia:</strong> Santa Fe</p>
                    <p><strong>N° de Licencia:</strong> {licencia.nroLicencia}</p>
                    <p><strong>Clases:</strong> {clases.join(', ')}</p>
                </div>

                <div>
                  <p>
                    <strong>Fecha de otorgamiento:</strong>{" "}
                    {claseMasReciente ? claseMasReciente.fechaEmision : "-"}
                  </p>
                  <p>
                    <strong>Fecha de vencimiento:</strong>{" "}
                    {claseMasReciente ? claseMasReciente.fechaVencimiento : "-"}
                  </p>
                </div>
            </div>

            {/* Columna derecha */}
            <div className="w-full md:w-1/2 p-4">
                <h2 className="font-bold text-gray-800 mb-2">Clases habilitadas</h2>
                {clases.map((clase) => (
                    <div key={clase} className="mb-4">
                        <p className="font-semibold">{clase} - {descripcionClases[clase] || 'Clase no especificada.'}</p>
                    </div>
                ))}
                <h2 className="font-bold text-gray-800 mb-2">Observaciones: {datosLicencia.observaciones}</h2>
                <div className="mt-4 border-t border-gray-300 pt-2">
                    <p className="text-xs text-gray-500">Organismo: Dirección de tránsito Santa Fe</p>
                    <p className="text-xs text-gray-500">Responsable: {datosEmisor.apellido}, {datosEmisor.nombre}</p>
                </div>
            </div>
        </div>
    );
});

export default LicenciaConducir;