import { Formik, Form, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import axios from 'axios';
import { useState } from 'react';

const validationSchema = Yup.object({
    email: Yup.string()
        .email('Correo electrónico inválido')
        .required('El correo es requerido'),
    contrasena: Yup.string()
        .min(8, 'La contraseña debe tener al menos 8 caracteres')
        .required('La contraseña es requerida'),
    confirmarContrasena: Yup.string()
        .oneOf([Yup.ref('contrasena'), null], 'Las contraseñas deben coincidir')
        .required('La confirmación de contraseña es requerida'),
    nombre: Yup.string()
        .max(255, 'El nombre no puede tener más de 255 caracteres')
        .required('El nombre es requerido'),
    tipodocumento: Yup.string()
        .oneOf(['DNI', 'CEDULA_IDENTIDAD', 'PASAPORTE', 'LIBRETA_CIVICA', 'LIBRETA_ENROLAMIENTO'], 'Tipo de documento inválido')
        .required('El tipo de documento es requerido'),
    documento: Yup.string()
        .matches(/^\d{7,8}$/, 'El documento debe tener 7 u 8 dígitos')
        .required('El número de documento es requerido'),
    domicilio: Yup.string()
        .max(255, 'El domicilio no puede tener más de 255 caracteres')
        .required('El domicilio es requerido'),
    fechaNacimiento: Yup.date()
        .required('La fecha de nacimiento es requerida')
        .max(new Date(), 'La fecha de nacimiento no puede ser futura'),
    userType: Yup.string()
        .oneOf(['normal', 'admin'], 'Tipo de usuario inválido')
        .required('El tipo de usuario es requerido'),
});

const RegisterForm = () => {
    const [message, setMessage] = useState({ type: '', text: '' });

    const handleSubmit = async (values, { setSubmitting, resetForm }) => {
        console.log(values);
        try {
            await axios.post('http://localhost:8080/api/usuarios', values);
            setMessage({ type: 'success', text: '¡Usuario registrado exitosamente!' });
            resetForm();
        } catch (error) {
            setMessage({
                type: 'error',
                text: error.response?.data?.message || 'Ocurrió un error durante el registro',
            });
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <div className="min-h-screen w-full flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
            <div id="form-container" className="max-w-md w-full space-y-8 bg-white p-8 rounded-xl shadow-lg border-solid border-2 border-gray-200">
                <div>
                    <h2 className="mt-2 text-center text-3xl font-extrabold text-gray-900">
                        Registrar Nuevo Usuario
                    </h2>
                    <p className="mt-4 text-center text-sm text-gray-600">
                        Complete los datos del nuevo usuario del sistema
                    </p>
                </div>

                {message.text && (
                    <div className={`rounded-lg p-4 ${
                        message.type === 'success' ? 'bg-green-50 text-green-700 border border-green-200' : 'bg-red-50 text-red-700 border border-red-200'
                    }`}>
                        {message.text}
                    </div>
                )}

                <Formik
                    initialValues={{
                        email: '',
                        contrasena: '',
                        confirmarContrasena: '',
                        nombre: '',
                        tipodocumento: 'DNI',
                        documento: '',
                        domicilio: '',
                        fechaNacimiento: '',
                        userType: 'normal',
                    }}
                    validationSchema={validationSchema}
                    onSubmit={handleSubmit}
                >
                    {({ isSubmitting }) => (
                        <Form className="mt-8 space-y-6">
                            <div className="space-y-4">
                                <div>
                                    <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-1">
                                        Correo electrónico
                                    </label>
                                    <Field
                                        id="email"
                                        name="email"
                                        type="email"
                                        placeholder="usuario@municipalidad.com"
                                        className="appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-400 text-gray-900 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                                    />
                                    <ErrorMessage name="email" component="div" className="text-red-500 text-sm mt-1" />
                                </div>

                                <div>
                                    <label htmlFor="contrasena" className="block text-sm font-medium text-gray-700 mb-1">
                                        Contraseña
                                    </label>
                                    <Field
                                        id="contrasena"
                                        name="contrasena"
                                        type="password"
                                        placeholder="••••••••"
                                        className="appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-400 text-gray-900 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                                    />
                                    <ErrorMessage name="contrasena" component="div" className="text-red-500 text-sm mt-1" />
                                </div>

                                <div>
                                    <label htmlFor="confirmarContrasena" className="block text-sm font-medium text-gray-700 mb-1">
                                        Confirmar Contraseña
                                    </label>
                                    <Field
                                        id="confirmarContrasena"
                                        name="confirmarContrasena"
                                        type="password"
                                        placeholder="••••••••"
                                        className="appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-400 text-gray-900 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                                    />
                                    <ErrorMessage name="confirmarContrasena" component="div" className="text-red-500 text-sm mt-1" />
                                </div>

                                <div>
                                    <label htmlFor="nombre" className="block text-sm font-medium text-gray-700 mb-1">
                                        Nombre Completo
                                    </label>
                                    <Field
                                        id="nombre"
                                        name="nombre"
                                        type="text"
                                        placeholder="Juan Pérez"
                                        className="appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-400 text-gray-900 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                                    />
                                    <ErrorMessage name="nombre" component="div" className="text-red-500 text-sm mt-1" />
                                </div>

                                <div>
                                    <label htmlFor="tipodocumento" className="block text-sm font-medium text-gray-700 mb-1">
                                        Tipo de Documento
                                    </label>
                                    <Field
                                        as="select"
                                        id="tipodocumento"
                                        name="tipodocumento"
                                        className="appearance-none relative block w-full px-3 py-2 border border-gray-300 text-gray-900 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                                    >
                                        <option value="DNI">DNI</option>
                                        <option value="CEDULA_IDENTIDAD">Cédula de Identidad</option>
                                        <option value="PASAPORTE">Pasaporte</option>
                                        <option value="LIBRETA_CIVICA">Libreta Cívica</option>
                                        <option value="LIBRETA_ENROLAMIENTO">Libreta de Enrolamiento</option>
                                    </Field>
                                    <ErrorMessage name="tipodocumento" component="div" className="text-red-500 text-sm mt-1" />
                                </div>

                                <div>
                                    <label htmlFor="documento" className="block text-sm font-medium text-gray-700 mb-1">
                                        Número de Documento
                                    </label>
                                    <Field
                                        id="documento"
                                        name="documento"
                                        type="text"
                                        placeholder="12345678"
                                        className="appearance-none relative block w-full px-3 py-2 border border-gray-300 text-gray-900 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                                    />
                                    <ErrorMessage name="documento" component="div" className="text-red-500 text-sm mt-1" />
                                </div>

                                <div>
                                    <label htmlFor="domicilio" className="block text-sm font-medium text-gray-700 mb-1">
                                        Domicilio
                                    </label>
                                    <Field
                                        id="domicilio"
                                        name="domicilio"
                                        type="text"
                                        placeholder="Calle Ejemplo 123"
                                        className="appearance-none relative block w-full px-3 py-2 border border-gray-300 text-gray-900 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                                    />
                                    <ErrorMessage name="domicilio" component="div" className="text-red-500 text-sm mt-1" />
                                </div>

                                <div>
                                    <label htmlFor="fechaNacimiento" className="block text-sm font-medium text-gray-700 mb-1">
                                        Fecha de Nacimiento
                                    </label>
                                    <Field
                                        id="fechaNacimiento"
                                        name="fechaNacimiento"
                                        type="date"
                                        className="appearance-none relative block w-full px-3 py-2 border border-gray-300 text-gray-900 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                                    />
                                    <ErrorMessage name="fechaNacimiento" component="div" className="text-red-500 text-sm mt-1" />
                                </div>

                                <div>
                                    <label htmlFor="userType" className="block text-sm font-medium text-gray-700 mb-1">
                                        Tipo de Usuario
                                    </label>
                                    <Field
                                        as="select"
                                        id="userType"
                                        name="userType"
                                        className="appearance-none relative block w-full px-3 py-2 border border-gray-300 text-gray-900 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                                    >
                                        <option value="normal">Usuario</option>
                                        <option value="admin">Administrador</option>
                                    </Field>
                                    <ErrorMessage name="userType" component="div" className="text-red-500 text-sm mt-1" />
                                </div>
                            </div>

                            <div>
                                <button
                                    type="submit"
                                    disabled={isSubmitting}
                                    className="group relative w-full flex justify-center py-2.5 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition duration-150 ease-in-out disabled:opacity-50 disabled:cursor-not-allowed"
                                >
                                    {isSubmitting ? (
                                        <span className="flex items-center">
                      <svg className="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                        <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                        <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                      </svg>
                      Registrando...
                    </span>
                                    ) : (
                                        'Registrar Usuario'
                                    )}
                                </button>
                            </div>
                        </Form>
                    )}
                </Formik>
            </div>
        </div>
    );
};

export default RegisterForm;
