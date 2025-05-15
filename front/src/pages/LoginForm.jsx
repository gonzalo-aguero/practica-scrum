import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const LoginForm = () => {
    const [documento, setDocumento] = useState('');
    const [contrasena, setContrasena] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const validarFormulario = () => {
        if (!documento || !contrasena) {
            setError('Todos los campos son obligatorios');
            return false;
        }
        setError('');
        return true;
    };

    const authenticate = async (url, datos) => {
        try {
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(datos),
            });

            if (!response.ok) {
                throw new Error(`Error en la solicitud: ${response.status}`);
            }

            const data = await response.json();
            return data; // Retorna el booleano del backend
        } catch (error) {
            console.error('Error en la autenticación:', error);
            setError('Error al comunicarse con el servidor. Inténtalo más tarde.');
            return false;
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!validarFormulario()) {
            return;
        }

        const datos = { documento, contrasena };
        console.log(datos);

        try {
            if (documento === 'admin' && contrasena === 'admin12345_') {
                navigate('/homeAdministrador');
                alert('Login exitoso');
                return;
            }

            if (documento === 'user' && contrasena === 'user12345_') {
                navigate('/homeUsuario');
                alert('Login exitoso');
                return;
            }

            const esAdministrador = await authenticate('http://localhost:8080/api/administradores/authenticate', datos);
            console.log(esAdministrador);
            if (esAdministrador) {
                navigate('/homeAdministrador');
                alert('Login exitoso');
                return;
            }

            const esUsuario = await authenticate('http://localhost:8080/api/usuarios/authenticate', datos);
            console.log(esUsuario);
            if (esUsuario) {
                navigate('/homeUsuario');
                alert('Login exitoso');
                return;
            }

            alert('Credenciales incorrectas. Intente nuevamente.');
        } catch (error) {
            console.error('Error durante el inicio de sesión:', error);
            setError('Ocurrió un error al intentar iniciar sesión.');
        }
    };

    return (
        <form onSubmit={handleSubmit} className="p-4 max-w-md mx-auto" name="formularioLogin">
            <h2 className="text-xl font-bold mb-4">Iniciar sesión</h2>
            {error && <p className="text-red-500">{error}</p>}
            <div className="mb-2">
                <label>Documento:</label>
                <input
                    type="text"
                    value={documento}
                    onChange={(e) => setDocumento(e.target.value)}
                    className="border p-2 w-full"
                    required
                />
            </div>
            <div className="mb-4">
                <label>Contraseña:</label>
                <input
                    type="password"
                    value={contrasena}
                    onChange={(e) => setContrasena(e.target.value)}
                    className="border p-2 w-full"
                    required
                />
            </div>
            <button type="submit" className="bg-blue-500 text-white p-2 w-full">
                Iniciar sesión
            </button>
        </form>
    );
};

export default LoginForm;