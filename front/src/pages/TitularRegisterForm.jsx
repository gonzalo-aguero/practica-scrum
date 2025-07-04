import { Formik, Form, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import axios from 'axios';
import { useState } from 'react';

// Opciones del Enum del backend:
const GRUPOS_SANGUINEOS = [
    "A_POSITIVO",
    "A_NEGATIVO",
    "B_POSITIVO",
    "B_NEGATIVO",
    "AB_POSITIVO",
    "AB_NEGATIVO",
    "O_POSITIVO",
    "O_NEGATIVO",
];

const GRUPOS_SANGUINEOS_FRIENDLY = {
    "A_POSITIVO": "A+",
    "A_NEGATIVO": "A–",
    "B_POSITIVO": "B+",
    "B_NEGATIVO": "B–",
    "AB_POSITIVO": "AB+",
    "AB_NEGATIVO": "AB–",
    "O_POSITIVO": "O+",
    "O_NEGATIVO": "O–",
};

const validationSchema = Yup.object({
    nombre: Yup.string().required('El nombre es obligatorio'),
    apellido: Yup.string().required('El apellido es obligatorio'),
    documento: Yup.string().required('El número de documento es obligatorio'),
    tipoDocumento: Yup.string()
        .oneOf(['DNI', 'PASAPORTE', 'CEDULA_IDENTIDAD'], 'Tipo de documento inválido')
        .required('Tipo de documento es obligatorio'),
    domicilio: Yup.string().required('El domicilio es obligatorio'),
    fechaNacimiento: Yup.date().required('La fecha de nacimiento es obligatoria'),
    esDonanteOrganos: Yup.boolean(),
    grupoSanguineo: Yup.string()
        .oneOf(GRUPOS_SANGUINEOS, "Grupo sanguíneo inválido")
        .required('El grupo sanguíneo es obligatorio'),
});

const RegisterTitularForm = () => {
    const [message, setMessage] = useState({ type: '', text: '' });

    const handleSubmit = async (values, { setSubmitting, resetForm }) => {
        const { confirmContrasena, ...dataToSend } = values;

        console.log(dataToSend);

        try {
            await axios.post('http://localhost:8080/api/titulares', dataToSend);
            setMessage({ type: 'success', text: 'Titular registrado correctamente' });
            resetForm();
        } catch (error) {
            setMessage({
                type: 'error',
                text: error.response?.data?.message || 'Error al registrar titular',
            });
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <div className="min-h-screen w-full flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
            <div className="max-w-md w-full space-y-8 bg-white p-8 rounded-xl shadow-lg border border-gray-200">
                <h2 className="text-center text-2xl font-bold text-gray-800">Registro de Titular</h2>
                {message.text && (
                    <div className={`p-4 rounded ${message.type === 'success' ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}`}>
                        {message.text}
                    </div>
                )}

                <Formik
                    initialValues={{
                        nombre: '',
                        apellido: '',
                        documento: '',
                        tipoDocumento: '',
                        domicilio: '',
                        fechaNacimiento: '',
                        esDonanteOrganos: false,
                        grupoSanguineo: '', // campo nuevo
                    }}
                    validationSchema={validationSchema}
                    onSubmit={handleSubmit}
                >
                    {({ isSubmitting }) => (
                        <Form className="space-y-4">
                            <div>
                                <label className="block text-sm">Nombre</label>
                                <Field name="nombre" type="text" className="block w-full border-2 border-gray-300 rounded-lg px-3 py-2 text-lg focus:outline-none focus:border-indigo-600" />
                                <ErrorMessage name="nombre" component="div" className="text-red-500 text-sm" />
                            </div>

                            <div>
                                <label className="block text-sm">Apellido</label>
                                <Field name="apellido" type="text" className="block w-full border-2 border-gray-300 rounded-lg px-3 py-2 text-lg focus:outline-none focus:border-indigo-600" />
                                <ErrorMessage name="apellido" component="div" className="text-red-500 text-sm" />
                            </div>

                            <div>
                                <label className="block text-sm">Documento</label>
                                <Field name="documento" type="text" className="block w-full border-2 border-gray-300 rounded-lg px-3 py-2 text-lg focus:outline-none focus:border-indigo-600" />
                                <ErrorMessage name="documento" component="div" className="text-red-500 text-sm" />
                            </div>

                            <div>
                                <label className="block text-sm">Tipo de Documento</label>
                                <Field as="select" name="tipoDocumento" className="block w-full border-2 border-gray-300 rounded-lg px-3 py-2 text-lg focus:outline-none focus:border-indigo-600">
                                    <option value="">Selecciona uno</option>
                                    <option value="DNI">DNI</option>
                                    <option value="PASAPORTE">Pasaporte</option>
                                    <option value="CEDULA_IDENTIDAD">Cédula de Identidad</option>
                                </Field>
                                <ErrorMessage name="tipoDocumento" component="div" className="text-red-500 text-sm" />
                            </div>

                            <div>
                                <label className="block text-sm">Domicilio</label>
                                <Field name="domicilio" type="text" className="block w-full border-2 border-gray-300 rounded-lg px-3 py-2 text-lg focus:outline-none focus:border-indigo-600" />
                                <ErrorMessage name="domicilio" component="div" className="text-red-500 text-sm" />
                            </div>

                            <div>
                                <label className="block text-sm">Fecha de Nacimiento</label>
                                <Field name="fechaNacimiento" type="date" className="block w-full border-2 border-gray-300 rounded-lg px-3 py-2 text-lg focus:outline-none focus:border-indigo-600" />
                                <ErrorMessage name="fechaNacimiento" component="div" className="text-red-500 text-sm" />
                            </div>

                            <div>
                                <label className="block text-sm">Grupo sanguíneo</label>
                                <Field as="select" name="grupoSanguineo" className="block w-full border-2 border-gray-300 rounded-lg px-3 py-2 text-lg focus:outline-none focus:border-indigo-600">
                                    <option value="">Selecciona uno</option>
                                    {GRUPOS_SANGUINEOS.map(gs => (
                                        <option key={gs} value={gs}>{GRUPOS_SANGUINEOS_FRIENDLY[gs]}</option>
                                    ))}
                                </Field>
                                <ErrorMessage name="grupoSanguineo" component="div" className="text-red-500 text-sm" />
                            </div>

                            <div className="flex items-center gap-2">
                                <Field type="checkbox" name="esDonanteOrganos" className="form-checkbox" />
                                <label className="text-sm">Es donante de órganos</label>
                            </div>

                            <button
                                type="submit"
                                disabled={isSubmitting}
                                className="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-medium py-2 px-4 rounded transition"
                            >
                                {isSubmitting ? 'Registrando...' : 'Registrar Titular'}
                            </button>
                        </Form>
                    )}
                </Formik>
            </div>
        </div>
    );
};

export default RegisterTitularForm;