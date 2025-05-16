import { useState } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import axios from 'axios';

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
  clase: Yup.string().oneOf(CLASES.map(c => c.value), 'Clase inválida').required('La clase es requerida'),
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
  // Simulación de usuario administrativo logueado
  const usuarioAdmin = 'admin@municipalidad.com'; // Reemplazar por contexto real si existe
  const [titular, setTitular] = useState(null);
  const [buscando, setBuscando] = useState(false);
  const [busquedaError, setBusquedaError] = useState('');
  const [vigencia, setVigencia] = useState('');
  const [costo, setCosto] = useState('');
  const [profesionalOk] = useState(true);
  const [profesionalError] = useState('');
  const [message, setMessage] = useState({ type: '', text: '' });

  // Buscar titular por tipo y número de documento
  const buscarTitular = async (tipo, numero) => {
    setBuscando(true);
    setBusquedaError('');
    setTitular(null);
    try {
      const res = await axios.get(`http://localhost:8080/api/titulares?tipodocumento=${tipo}&documento=${numero}`);
      if (res.data) {
        setTitular(res.data);
      } else {
        setBusquedaError('No se encontró el titular.');
      }
    } catch {
      setBusquedaError('Error al buscar titular.');
    } finally {
      setBuscando(false);
    }
  };

  // Validación de edad mínima
  const edadMinimaPorClase = (clase) => ['C', 'D', 'E'].includes(clase) ? 21 : 17;

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
            <div className="font-semibold">Titular:</div>
            <div><span className="font-medium">Nombre:</span> {titular.nombre}</div>
            <div><span className="font-medium">Fecha Nac.:</span> {titular.fechaNacimiento}</div>
            <div><span className="font-medium">Dirección:</span> {titular.domicilio}</div>
            <div><span className="font-medium">Grupo Sanguíneo:</span> {titular.grupoSanguineo} {titular.factorRH}</div>
            <div><span className="font-medium">Donante de órganos:</span> {titular.donanteOrganos ? 'Sí' : 'No'}</div>
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
          onSubmit={async (values, { setSubmitting, resetForm }) => {
            setMessage({ type: '', text: '' });
            if (!titular) {
              setMessage({ type: 'error', text: 'Debe buscar y seleccionar un titular.' });
              setSubmitting(false);
              return;
            }
            // Validación de edad mínima
            const edad = calcularEdad(titular.fechaNacimiento);
            const edadMin = edadMinimaPorClase(values.clase);
            if (edad < edadMin) {
              setMessage({ type: 'error', text: `El titular debe tener al menos ${edadMin} años para la clase seleccionada.` });
              setSubmitting(false);
              return;
            }
            // Validación profesional
            if (['C', 'D', 'E'].includes(values.clase)) {
              if (!profesionalOk) {
                setMessage({ type: 'error', text: profesionalError || 'No cumple requisitos para licencia profesional.' });
                setSubmitting(false);
                return;
              }
              if (edad > 65) {
                setMessage({ type: 'error', text: 'No puede otorgarse licencia profesional por primera vez a mayores de 65 años.' });
                setSubmitting(false);
                return;
              }
            }
            // Registrar licencia
            try {
              await axios.post('http://localhost:8080/api/licencias', {
                ...values,
                titular: {
                  tipodocumento: titular.tipodocumento,
                  documento: titular.documento,
                },
                usuarioAdmin,
                fechaTramite: new Date().toISOString().split('T')[0],
              });
              setMessage({ type: 'success', text: '¡Licencia registrada exitosamente!' });
              resetForm();
              setTitular(null);
              setVigencia('');
              setCosto('');
            } catch (err) {
              setMessage({ type: 'error', text: err.response?.data?.message || 'Error al registrar licencia.' });
            } finally {
              setSubmitting(false);
            }
          }}
        >
          {({ values, isSubmitting }) => (
            <Form className="space-y-4">
              <div>
                <label htmlFor="clase" className="block text-sm font-medium text-gray-700 mb-1">Clase solicitada</label>
                <Field as="select" name="clase" id="clase" className="block w-full px-3 py-2 border rounded">
                  <option value="">Seleccione clase</option>
                  {CLASES.map(opt => <option key={opt.value} value={opt.value}>{opt.value} - {opt.label}</option>)}
                </Field>
                <ErrorMessage name="clase" component="div" className="text-red-500 text-xs mt-1" />
              </div>
              <div>
                <label htmlFor="observaciones" className="block text-sm font-medium text-gray-700 mb-1">Observaciones / Limitaciones</label>
                <Field as="textarea" name="observaciones" id="observaciones" rows={2} className="block w-full px-3 py-2 border rounded" placeholder="Ingrese observaciones si corresponde..." />
                <ErrorMessage name="observaciones" component="div" className="text-red-500 text-xs mt-1" />
              </div>
              {/* Vigencia y costo */}
              <div className="flex gap-4">
                <div className="flex-1">
                  <span className="block text-xs text-gray-500">Vigencia</span>
                  <span className="font-semibold">{vigencia ? `${vigencia} años` : '-'}</span>
                </div>
                <div className="flex-1">
                  <span className="block text-xs text-gray-500">Costo</span>
                  <span className="font-semibold">{costo ? `$${costo}` : '-'}</span>
                </div>
              </div>
              {/* Mensaje de error profesional */}
              {profesionalError && ['C', 'D', 'E'].includes(values.clase) && (
                <div className="text-red-500 text-xs">{profesionalError}</div>
              )}
              <button
                type="submit"
                disabled={isSubmitting || !titular}
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