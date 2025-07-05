// src/components/LicenciaConducir.jsx
import React from 'react';

const descripcionClases = {
    'A': 'Ciclomotores, motocicletas y triciclos motorizados.',
    'B': 'Automóviles y camionetas con acoplado.',
    'C': 'Camiones sin acoplado y los comprendidos en la clase B.',
    'D': 'Servicio de transporte de pasajeros, emergencia, seguridad y los comprendidos en la clase B o C, según el caso.',
    'E': 'Camiones articulados o con acoplado, maquinaria especial no agrícola y los comprendidos en la clase B y C.',
    'F': 'Automotores especialmente adaptados para discapacitados.',
    'G': 'Tractores agrícolas y maquinaria especial agrícola.',
};

const LicenciaConducir = React.forwardRef(({ licencia, datos, clasesLicencia, usuariosEmisores }, ref) => {
    const clases = Array.isArray(clasesLicencia) ? clasesLicencia : [];

    // Encuentra la clase con la fecha de emisión más reciente
    const claseMasReciente = Array.isArray(clases) && clases.length > 0
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
                <img src={"data:image/jpeg;base64,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"} alt="Foto titular" className="w-32 h-40 mt-2 object-cover border" />

                <div className="mt-4 space-y-1">
                    <p><strong>Nombre:</strong> {datos.nombre}</p>
                    <p><strong>Apellido:</strong> {datos.apellido}</p>
                    <p><strong>F. Nacimiento:</strong> {datos.fechaNacimiento}</p>
                    <p><strong>Domicilio:</strong> {datos.domicilio}</p>
                    <p><strong>Localidad:</strong> Santa Fe</p>
                    <p><strong>Provincia:</strong> Santa Fe</p>
                    <p><strong>N° de Licencia:</strong> {licencia.nroLicencia}</p>
                    <p><strong>Clases:</strong> {clases.map(c => c.claseLicenciaEnum).join(', ')}</p>
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
                        <p className="font-semibold">{clase.claseLicenciaEnum} - {descripcionClases[clase.claseLicenciaEnum] || 'Clase no especificada.'}</p>
                    </div>
                ))}
                <h2 className="font-bold text-gray-800 mb-2">Observaciones: {licencia.observaciones}</h2>
                <div className="mt-4 border-t border-gray-300 pt-2">
                    <p className="text-xs text-gray-500">Organismo: Dirección de tránsito Santa Fe</p>
                    <p className="text-xs text-gray-500">Responsable: {usuariosEmisores[0].apellido}, {usuariosEmisores[0].nombre}</p>
                </div>
            </div>
        </div>
    );
});

export default LicenciaConducir;