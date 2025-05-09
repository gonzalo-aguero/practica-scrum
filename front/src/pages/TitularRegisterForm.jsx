import { Formik, Form, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import axios from 'axios';
import { useState } from 'react';

const validationSchema = Yup.object({
    nombre: Yup.string().required('El nombre es obligatorio'),
    documento: Yup.string().required('El numero de documento es obligatorio'),
    tipoDocumento: Yup.string().oneOf(['DNI', 'PASAPORTE', 'CEDULA_IDENTIDAD'], 'Tipo de documento invalido').required('Tipo de documento es obligatorio'),
    domicilio: Yup.string().required('El domicilio es obligatorio'),
    fechaNacimiento: Yup.date().required('La fecha de nacimiento es obligatoria'),
    contrasena: Yup.string().min(8, 'La contraseña debe tener al menos 8 caracteres').required('La contraseña es obligatoria'),
    confirmContrasena: Yup.string()
        .oneOf([Yup.ref('contrasena'), null], 'Las contraseñas deben coincidir')
        .required('Debes confirmar la contraseña'),
    esDonanteOrganos: Yup.boolean()
});

const RegisterTitularForm = () => {
    const [message, setMessage] = useState({ type: '', text: '' });

    const handleSubmit = async (values, { setSubmitting, resetForm }) => {
        const { confirmContrasena, ...dataToSend } = values;

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
                    <div className={`p-4 rounded ${
                        message.type === 'success' ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'
                    }`}>
                        {message.text}
                    </div>
                )}

                <Formik
                    initialValues={{
                        nombre: '',
                        documento: '',
                        tipoDocumento: '',
                        domicilio: '',
                        fechaNacimiento: '',
                        contrasena: '',
                        confirmContrasena: '',
                        esDonanteOrganos: false
                    }}
                    validationSchema={validationSchema}
                    onSubmit={handleSubmit}
                >
                    {({ isSubmitting }) => (
                        <Form className="space-y-4">
                            <div>
                                <label className="block text-sm">Nombre</label>
                                <Field name="nombre" type="text" className="form-input" />
                                <ErrorMessage name="nombre" component="div" className="text-red-500 text-sm" />
                            </div>

                            <div>
                                <label className="block text-sm">Documento</label>
                                <Field name="documento" type="text" className="form-input" />
                                <ErrorMessage name="documento" component="div" className="text-red-500 text-sm" />
                            </div>

                            <div>
                                <label className="block text-sm">Tipo de Documento</label>
                                <Field as="select" name="tipoDocumento" className="form-input">
                                    <option value="">Selecciona uno</option>
                                    <option value="DNI">DNI</option>
                                    <option value="PASAPORTE">Pasaporte</option>
                                    <option value="CEDULA_IDENTIDAD">Cedula de Identidad</option>
                                </Field>
                                <ErrorMessage name="tipoDocumento" component="div" className="text-red-500 text-sm" />
                            </div>

                            <div>
                                <label className="block text-sm">Domicilio</label>
                                <Field name="domicilio" type="text" className="form-input" />
                                <ErrorMessage name="domicilio" component="div" className="text-red-500 text-sm" />
                            </div>

                            <div>
                                <label className="block text-sm">Fecha de Nacimiento</label>
                                <Field name="fechaNacimiento" type="date" className="form-input" />
                                <ErrorMessage name="fechaNacimiento" component="div" className="text-red-500 text-sm" />
                            </div>

                            <div>
                                <label className="block text-sm">Contraseña</label>
                                <Field name="contrasena" type="password" className="form-input" />
                                <ErrorMessage name="contrasena" component="div" className="text-red-500 text-sm" />
                            </div>

                            <div>
                                <label className="block text-sm">Confirmar Contraseña</label>
                                <Field name="confirmContrasena" type="password" className="form-input" />
                                <ErrorMessage name="confirmContrasena" component="div" className="text-red-500 text-sm" />
                            </div>

                            <div className="flex items-center gap-2">
                                <Field type="checkbox" name="esDonanteOrganos" className="form-checkbox" />
                                <label className="text-sm">Es donante de organos</label>
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
