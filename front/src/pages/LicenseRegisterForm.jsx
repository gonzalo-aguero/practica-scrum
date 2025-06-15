import { useState, useEffect } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const CLASES = [
  { value: 'A', label: 'Ciclomotores, motocicletas y triciclos motorizados.' },
  { value: 'B', label: 'Automóviles y camionetas con acoplado.' },
  { value: 'C', label: 'Camiones sin acoplado y los comprendidos en la clase B.' },
  { value: 'D', label: 'Servicio de transporte de pasajeros, emergencia, seguridad y los comprendidos en la clase B o C, según el caso.' },
  { value: 'E', label: 'Camiones articulados o con acoplado, maquinaria especial no agrícola y los comprendidos en la clase B y C.' },
  { value: 'F', label: 'Automotores especialmente adaptados para discapacitados.' },
  { value: 'G', label: 'Tractores agrícolas y maquinaria especial agrícola.' },
];

const TIPO_DOCS = [
  { value: 'DNI', label: 'DNI' },
  { value: 'CEDULA_IDENTIDAD', label: 'Cédula de Identidad' },
  { value: 'PASAPORTE', label: 'Pasaporte' },
  { value: 'LIBRETA_CIVICA', label: 'Libreta Cívica' },
  { value: 'LIBRETA_ENROLAMIENTO', label: 'Libreta de Enrolamiento' },
];

const validationSchema = Yup.object({
  clase: Yup.string().oneOf(CLASES.map(c => c.value), 'Clase inválida'),
  observaciones: Yup.string().max(255, 'Máximo 255 caracteres'),
});

const calcularEdad = (fechaNacimiento) => {
  const hoy = new Date();
  const nacimiento = new Date(fechaNacimiento);
  let edad = hoy.getFullYear() - nacimiento.getFullYear();
  const m = hoy.getMonth() - nacimiento.getMonth();
  if (m < 0 || (m === 0 && hoy.getDate() < nacimiento.getDate())) {
    edad--;
  }
  return edad;
};

const LicenseRegisterForm = () => {
  const navigate = useNavigate();
  const usuario = {
    documento: '12345678', // ! Remplazar por contexto real si existe
    nombre: 'Usuario Test', // ! Remplazar por contexto real si existe
  };

  const [titular, setTitular] = useState(null);
  const [buscando, setBuscando] = useState(false);
  const [busquedaError, setBusquedaError] = useState('');
  const [costo, setCosto] = useState('');
  const [profesionalOk] = useState(true);
  const [profesionalError] = useState('');
  const [message, setMessage] = useState({ type: '', text: '' });
  const [clasesSeleccionadas, setClasesSeleccionadas] = useState([]);
  const [clasesDisponibles, setClasesDisponibles] = useState(CLASES);

  // Actualizar clases disponibles cuando se carga el titular
  useEffect(() => {
    if (titular?.licencias) {
      const clasesAsignadas = titular.licencias.flatMap(l => l.clases);
      const nuevasClasesDisponibles = CLASES.filter(c => !clasesAsignadas.includes(c.value));
      setClasesDisponibles(nuevasClasesDisponibles);
    } else {
      setClasesDisponibles(CLASES);
    }
  }, [titular]);

  const calcularCosto = async () => {
    console.log('Calculando costo. Clases seleccionadas:', clasesSeleccionadas);
    if (!titular || clasesSeleccionadas.length === 0) {
      setCosto('');
      return;
    }

    try {
      const params = {
        clasesSeleccionadas: clasesSeleccionadas.map(c => c.value),
        idTitular: titular.ID
      };
      console.log('Request URL:', 'http://localhost:8080/api/licencias/costo');
      console.log('Request params:', params);
      
      const res = await axios.get('http://localhost:8080/api/licencias/costo', { params });
      console.log('Response:', res.data);
      setCosto(res.data.costo);
    } catch (error) {
      console.error('Error al calcular el costo:', error);
      setCosto('');
    }
  };

  // Efecto para calcular el costo cuando cambian las clases seleccionadas
  useEffect(() => {
    calcularCosto();
  }, [clasesSeleccionadas, titular]);

  const buscarTitular = async (tipo, numero) => {
    setBuscando(true);
    setBusquedaError('');
    setTitular(null);
    setClasesSeleccionadas([]);
    try {
      const res = await axios.get(`http://localhost:8080/api/titulares`, {
        params: {
          tipoDocumento: tipo,
          documento: numero
        }
      });
      
      if (res.status === 204) {
        setBusquedaError('No se encontró ningún titular');
      } else if (res.status === 200 && res.data) {
        console.log(res.data);
        // TODO: Luego de implementar el backend, eliminar esta parte
        // Agregar datos de prueba para la licencia

        // const titularConLicencia = res.data  TENDRIA QUE IR ESTE PARA QUE ANDE
        const titularConLicencia = {
          ...res.data,
          licencia: {
            nroLicencia: "30400568",
            observaciones: "Sin restricciones",
            clasesLicencia: [
              {
                claseLicenciaEnum: "B",
                fechaEmision: "2023-01-15",
                fechaVencimiento: "2028-01-15",
                usuarioEmisor: {
                  nombre: "Juan Pérez",
                  documento: "12345678"
                }
              },
              {
                claseLicenciaEnum: "A",
                fechaEmision: "2023-01-15",
                fechaVencimiento: "2028-01-15",
                usuarioEmisor: {
                  nombre: "Juan Pérez",
                  documento: "12345678"
                }
              }
            ]
          }
        };
        setTitular(titularConLicencia);
      }
    } catch (error) {
      console.log(error);
      if (error.response?.status === 400) {
        setBusquedaError('El documento es obligatorio');
      } else if (error.response?.status === 404) {
        setBusquedaError('No se encontró ningún titular');
      } else {
        setBusquedaError('Error al buscar titular');
      }
    } finally {
      setBuscando(false);
    }
  };

  const agregarClase = (clase) => {
    if (!clase) return;
    if (!clasesSeleccionadas.find(c => c.value === clase)) {
      const claseInfo = CLASES.find(c => c.value === clase);
      setClasesSeleccionadas([...clasesSeleccionadas, claseInfo]);
      setClasesDisponibles(clasesDisponibles.filter(c => c.value !== clase));
    }
  };

  const eliminarClase = (claseValue) => {
    const claseInfo = CLASES.find(c => c.value === claseValue);
    setClasesSeleccionadas(clasesSeleccionadas.filter(c => c.value !== claseValue));
    setClasesDisponibles([...clasesDisponibles, claseInfo]);
  };

  const edadMinimaPorClase = (clase) => ['C', 'D', 'E'].includes(clase) ? 21 : 17;

  const handleSubmit = async (values, { setSubmitting, resetForm }) => {
    setMessage({ type: '', text: '' });
    if (!titular) {
      setMessage({ type: 'error', text: 'Debe buscar y seleccionar un titular.' });
      setSubmitting(false);
      return;
    }
    if (clasesSeleccionadas.length === 0) {
      setMessage({ type: 'error', text: 'Debe seleccionar al menos una clase.' });
      setSubmitting(false);
      return;
    }

    // Registrar licencia
    try {
      const requestData = {
        clases: clasesSeleccionadas.map(c => { 
          return { 
            letraClase: c.value, 
            usuarioEmisor: usuario.documento
          };
        }),
        observaciones: values.observaciones,
        titularId: titular.id
      };
      console.log("Request url:", 'http://localhost:8080/api/licencias');
      console.log('Request data:', requestData);

      const response = await axios.post('http://localhost:8080/api/licencias', requestData);
      console.log('Response:', response.data);
      
      setMessage({ type: 'success', text: '¡Licencia registrada exitosamente!' });
      resetForm();
      setTitular(null);
      setClasesSeleccionadas([]);
      setCosto('');

      
    } catch (err) {
      setMessage({ type: 'error', text: err.response?.data?.message || 'Error al registrar licencia.' });
    } finally {
      // Redireccinar a la página de comprobante
      navigate('/comprobante', { 
        state: { 
          usuarioEmisor: usuario,
          titular: titular,
          clases: clasesSeleccionadas,
          observaciones: values.observaciones,
          costo: costo,
          // TODO: Los siguientes datos deben ser obtenidos a través del backend:
          nuevaLicencia: {
            nroLicencia: "30400568",
          },
          fechaVencimiento: new Date(new Date().setFullYear(new Date().getFullYear() + 5)).toISOString().split('T')[0],
          fechaEmision: new Date().toISOString().split('T')[0]
        }
      });
      setSubmitting(false);
    }
  };

  return (
    <div className="min-h-screen w-full flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-md w-full space-y-8 bg-white p-8 rounded-xl shadow-lg border-solid border-2 border-gray-200">
        <div>
          <h2 className="mt-2 text-center text-3xl font-extrabold text-gray-900">Registrar Licencia</h2>
          <p className="mt-4 text-center text-sm text-gray-600">Complete los datos para emitir una nueva licencia</p>
        </div>

        {/* Buscador de titular */}
        <Formik
          initialValues={{ tipodocumento: 'DNI', documento: '' }}
          validationSchema={Yup.object({
            tipodocumento: Yup.string().oneOf(TIPO_DOCS.map(d => d.value), 'Tipo inválido').required(),
            documento: Yup.string().matches(/^\d{7,8}$/, 'Debe tener 7 u 8 dígitos').required('Requerido'),
          })}
          onSubmit={async (values) => {
            await buscarTitular(values.tipodocumento, values.documento);
          }}
        >
          {({ isSubmitting }) => (
            <Form className="space-y-2 mb-4">
              <div className="flex gap-2">
                <Field as="select" name="tipodocumento" className="w-1/3 px-2 py-1 border rounded">
                  {TIPO_DOCS.map(opt => <option key={opt.value} value={opt.value}>{opt.label}</option>)}
                </Field>
                <Field name="documento" type="text" placeholder="Documento Titular" className="w-1/2 px-2 py-1 border rounded" />
                <button type="submit" disabled={isSubmitting || buscando} className="px-3 py-1 bg-indigo-600 text-white rounded disabled:opacity-50">{buscando ? 'Buscando...' : 'Buscar'}</button>
              </div>
              <ErrorMessage name="documento" component="div" className="text-red-500 text-xs" />
              {busquedaError && <div className="text-red-500 text-xs">{busquedaError}</div>}
            </Form>
          )}
        </Formik>

        {/* Datos del titular */}
        {titular && (
          <div className="bg-gray-50 p-3 rounded mb-4 border border-gray-200">
            <div className="space-y-4">
              {/* Información Personal */}
              <div>
                <h3 className="font-semibold text-gray-900 mb-2">Información Personal</h3>
                <div className="grid grid-cols-2 gap-2 text-sm">
                  <div><span className="font-medium">Nombre:</span> {titular.nombre}</div>
                  <div><span className="font-medium">Fecha Nac.:</span> {titular.fechaNacimiento}</div>
                  <div><span className="font-medium">Dirección:</span> {titular.domicilio}</div>
                  <div><span className="font-medium">Grupo Sanguíneo:</span> {titular.grupoSanguineo}</div>
                  <div><span className="font-medium">Donante de órganos:</span> {titular.esDonanteOrganos ? 'Sí' : 'No'}</div>
                </div>
              </div>

              {/* Licencias Actuales */}
              {titular.licencia && titular.licencia !== null && (
                <div>
                  <h3 className="font-semibold text-gray-900 mb-2">Licencias Actuales</h3>
                  <div className="space-y-3">
                    <div className="bg-white p-3 rounded-md border border-gray-200">
                      <div className="grid grid-cols-2 gap-2 text-sm mb-2">
                        <div><span className="font-medium">Nro. Licencia:</span> {titular.licencia.nroLicencia}</div>
                        <div><span className="font-medium">Observaciones:</span> {titular.licencia.observaciones || 'Sin observaciones'}</div>
                      </div>
                      
                      {/* Clases de la Licencia */}
                      {titular.licencia.clasesLicencia && titular.licencia.clasesLicencia.length > 0 && (
                        <div className="mt-2">
                          <h4 className="text-sm font-medium text-gray-700 mb-1">Clases Habilitadas:</h4>
                          <div className="space-y-1">
                            {titular.licencia.clasesLicencia.map((clase, idx) => (
                              <div key={idx} className="ml-2 text-sm">
                                <div className="flex items-center justify-between">
                                  <span>• {clase.claseLicenciaEnum} - {CLASES.find(c => c.value === clase.claseLicenciaEnum)?.label}</span>
                                  <span className="text-gray-500 text-xs">
                                    Emitida: {new Date(clase.fechaEmision).toLocaleDateString()}
                                  </span>
                                </div>
                                <div className="ml-4 text-xs text-gray-500">
                                  Vence: {new Date(clase.fechaVencimiento).toLocaleDateString()}
                                  {clase.usuarioEmisor && (
                                    <span className="ml-2">
                                      • Emitida por: {clase.usuarioEmisor.nombre}
                                    </span>
                                  )}
                                </div>
                              </div>
                            ))}
                          </div>
                        </div>
                      )}
                    </div>
                  </div>
                </div>
              )}
            </div>
          </div>
        )}

        {/* Formulario de licencia */}
        <Formik
          enableReinitialize
          initialValues={{
            clase: '',
            observaciones: '',
          }}
          validationSchema={validationSchema}
          onSubmit={handleSubmit}
        >
          {({ values, isSubmitting, setFieldValue }) => (
            <Form className="space-y-4">
              <div>
                <label htmlFor="clase" className="block text-sm font-medium text-gray-700 mb-1">Clase solicitada</label>
                <div className="flex gap-2">
                  <Field as="select" name="clase" id="clase" className="block w-full px-3 py-2 border rounded">
                    <option value="">Seleccione clase</option>
                    {clasesDisponibles.map(opt => (
                      <option key={opt.value} value={opt.value}>{opt.value} - {opt.label}</option>
                    ))}
                  </Field>
                  <button
                    type="button"
                    onClick={() => {
                      agregarClase(values.clase);
                      setFieldValue('clase', '');
                    }}
                    disabled={!values.clase}
                    className="px-4 pispoy-2 bg-green-600 text-white rounded hover:bg-green-700 disabled:opacity-50"
                  >
                    Agregar
                  </button>
                </div>
                <ErrorMessage name="clase" component="div" className="text-red-500 text-xs mt-1" />
              </div>

              {/* Lista de clases seleccionadas */}
              {clasesSeleccionadas.length > 0 && (
                <div className="mt-2">
                  <label className="block text-sm font-medium text-gray-700 mb-1">Clases seleccionadas:</label>
                  <div className="space-y-2">
                    {clasesSeleccionadas.map((clase) => (
                      <div key={clase.value} className="flex items-center justify-between bg-gray-50 p-2 rounded border">
                        <span>{clase.value} - {clase.label}</span>
                        <button
                          type="button"
                          onClick={() => eliminarClase(clase.value)}
                          className="text-red-600 hover:text-red-800"
                        >
                          Eliminar
                        </button>
                      </div>
                    ))}
                  </div>
                </div>
              )}

              <div>
                <label htmlFor="observaciones" className="block text-sm font-medium text-gray-700 mb-1">Observaciones / Limitaciones</label>
                <Field as="textarea" name="observaciones" id="observaciones" rows={2} className="block w-full px-3 py-2 border rounded" placeholder="Ingrese observaciones si corresponde..." />
                <ErrorMessage name="observaciones" component="div" className="text-red-500 text-xs mt-1" />
              </div>

              {/* Costo de emitir Licencia */}
              <div className="flex gap-4">
                <div className="flex-1">
                  <span className="block text-xs text-gray-500">Costo</span>
                  <span className="font-semibold">{costo ? `$${costo}` : '-'}</span>
                </div>
              </div>

              {/* Mensaje de error profesional */}
              {profesionalError && clasesSeleccionadas.some(c => ['C', 'D', 'E'].includes(c.value)) && (
                <div className="text-red-500 text-xs">{profesionalError}</div>
              )}

              <button
                type="submit"
                disabled={isSubmitting || !titular || clasesSeleccionadas.length === 0}
                className="w-full py-2.5 px-4 bg-indigo-600 text-white rounded font-medium hover:bg-indigo-700 disabled:opacity-50"
              >
                {isSubmitting ? 'Registrando...' : 'Registrar Licencia'}
              </button>
            </Form>
          )}
        </Formik>

        {/* Mensaje de éxito o error */}
        {message.text && (
          <div className={`rounded-lg p-4 mt-4 ${
            message.type === 'success' ? 'bg-green-50 text-green-700 border border-green-200' : 'bg-red-50 text-red-700 border border-red-200'
          }`}>
            {message.text}
          </div>
        )}
      </div>
    </div>
  );
};

export default LicenseRegisterForm; 