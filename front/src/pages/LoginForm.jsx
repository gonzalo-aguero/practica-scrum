import React, { useState } from 'react';

const LoginForm = () => {
    const [documento, setDocumento] = useState('');
    const [contrasena, setContrasena] = useState('');
    const [error, setError] = useState('')

    const handleSubmit = () => {
        e.preventDefault();

        if (documento.length === 0 || contrasena.length === 0) {
            setError('Todos los campos son obligatorios');
            return;
        }

        console.log('Documento: ', documento);
        console.log('Contrasena: ', contrasena);
        setError('');
        alert('Login exitoso');
    };
    return (
        <div>
            <h1>Login</h1>
            <form onSubmit={handleSubmit} style={{maxWidth: '300', margin: '0 auto'}}>
                <input type="text" placeholder="Documento" value={documento} onChange={(e) => setDocumento(e.target.value)} />
                <input type="password" placeholder="<PASSWORD>" value={contrasena} onChange={(e) => setContrasena(e.target.value)} />
                <button type="submit">Login</button>
            </form>
            {error && <p>{error}</p>}
        </div>
    )
}