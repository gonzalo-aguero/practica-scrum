import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const LoginForm = () => {
    const [documento, setDocumento] = useState('');
    const [contrasena, setContrasena] = useState('');
    const [error, setError] = useState('')
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();

        if (documento.length === 0 || contrasena.length === 0) {
            setError('Todos los campos son obligatorios');
            return;
        }

        console.log('Documento: ', documento);
        console.log('Contrasena: ', contrasena);
        setError('');

        if(documento === 'admin' && contrasena === 'admin12345_'){
            navigate('/homeAdministrador');
            alert('Login exitoso');
        }else if(documento === 'user' && contrasena === 'user12345_'){
            navigate('/homeUsuario');
            alert('Login exitoso');
        }else{
            alert('Para navegar para el homeAdministrador por favor ingrese admin y para el homeUsuario por favor ingrese user');
        }

    };
    return (
        <form onSubmit={handleSubmit} className="p-4 max-w-md mx-auto">
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
    )
};

export default LoginForm;