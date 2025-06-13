import { useLocation, useNavigate } from 'react-router-dom';

const Comprobante = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { usuarioEmisor, titular, clases, observaciones, costo, fechaVencimiento, fechaEmision, nuevaLicencia } = location.state || {};

  if (!location.state) {
    navigate('/');
    return null;
  }

  const formatDate = (dateStr) => {
    const meses = [
      'enero', 'febrero', 'marzo', 'abril', 'mayo', 'junio',
      'julio', 'agosto', 'septiembre', 'octubre', 'noviembre', 'diciembre'
    ];
    const date = new Date(dateStr);
    return `${date.getDate()} de ${meses[date.getMonth()]} de ${date.getFullYear()}`;
  };

  return (
    <div className="min-h-screen bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-3xl mx-auto">
        {/* Header */}
        <div className="bg-white rounded-lg shadow-lg overflow-hidden">
          <div className="px-6 py-4 bg-[#12bc8e] text-white">
            <h1 className="text-2xl font-bold text-center">Comprobante de Emisión de Licencia</h1>
					</div>

          {/* Content */}
          <div className="p-6 space-y-6">
            {/* Emisión Info */}
            <div className="grid grid-cols-2 gap-4 border-b pb-4">
							<div>
									<p className="text-sm text-gray-500">Fecha de Emisión</p>
									<p className="font-medium">{formatDate(fechaEmision)}</p>
							</div>
								<div>
										<p className="text-sm text-gray-500">Fecha de Vencimiento</p>
										<p className="font-medium">{formatDate(fechaVencimiento)}</p>
								</div>
            </div>

            {/* Titular Info */}
            <div className="space-y-4">
              <h2 className="text-lg font-semibold text-gray-900">Datos del Titular</h2>
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <p className="text-sm text-gray-500">Nombre Completo</p>
                  <p className="font-medium">{titular.nombre}</p>
                </div>
                <div>
                  <p className="text-sm text-gray-500">Documento</p>
                  <p className="font-medium">{titular.documento}</p>
                </div>
                <div>
                  <p className="text-sm text-gray-500">Fecha de Nacimiento</p>
                  <p className="font-medium">{formatDate(titular.fechaNacimiento)}</p>
                </div>
                <div>
                  <p className="text-sm text-gray-500">Domicilio</p>
                  <p className="font-medium">{titular.domicilio}</p>
                </div>
								<div>
                  <p className="text-sm text-gray-500">Donante</p>
                  <p className="font-medium">{titular.esDonanteOrganos ? "SI" : "NO"}</p>
                </div>
								<div>
                  <p className="text-sm text-gray-500">Grupo Sanguíneo</p>
                  <p className="font-medium">{titular.grupoSanguineo}</p>
                </div>
              </div>
            </div>

            {/* Licencia Info */}
            <div className="space-y-4">
              <h2 className="text-lg font-semibold text-gray-900">Detalles de la Licencia</h2>
              <div className="space-y-2">
                <p className="text-sm text-gray-500">Número de Licencia</p>
								<p className="font-medium">{nuevaLicencia.nroLicencia}</p>
              </div>
							<div className="space-y-2">
                <p className="text-sm text-gray-500">Clases Otorgadas</p>
                <div className="grid grid-cols-1 gap-2">
                  {clases.map((clase) => (
                    <div key={clase.value} className="bg-gray-50 p-3 rounded-md">
                      <p className="font-medium">{clase.value} - {clase.label}</p>
                    </div>
                  ))}
                </div>
              </div>
              {observaciones && (
                <div>
                  <p className="text-sm text-gray-500">Observaciones</p>
                  <p className="font-medium">{observaciones}</p>
                </div>
              )}
            </div>

            {/* Costo */}
            <div className="border-t pt-4">
              <div className="flex justify-between items-center">
                <p className="text-lg font-semibold text-gray-900">Costo Total</p>
                <p className="text-2xl font-bold text-[#12bc8e]">${costo}</p>
              </div>
            </div>

            {/* Footer */}
            <div className="border-t pt-6">
              <div className="text-center space-y-2">
                <p className="text-sm text-gray-500">Emitido por</p>
                <p className="font-medium">{usuarioEmisor.nombre}</p>
                <p className="text-sm text-gray-500">Documento: {usuarioEmisor.documento}</p>
              </div>
            </div>
          </div>
        </div>

        {/* Actions */}
        <div className="mt-6 flex justify-center space-x-4">
          <button
            onClick={() => window.print()}
            className="px-4 py-2 bg-[#12bc8e] text-white rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
          >
            Imprimir Comprobante
          </button>
          <button
            onClick={() => navigate('/')}
            className="px-4 py-2 bg-gray-200 text-gray-700 rounded-md hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
          >
            Volver al Inicio
          </button>
        </div>
      </div>
    </div>
  );
};

export default Comprobante; 