import React, { useEffect, useState } from 'react';
import axios from 'axios';

const findAllTitulares = async (nombre, apellido) => {
    try {
        const params = {};
        if (nombre && nombre.trim() !== '') params.nombre = nombre;
        if (apellido && apellido.trim() !== '') params.apellido = apellido;

        const response = await axios.get(
            'http://localhost:8080/api/titulares/all-titulares', 
            { params }
        );
        return response.data;
    } catch (error) {
        console.log('Error al buscar titulares: ', error.message);
        throw error;
    }
};

const TablaTitulares = ({ nombre, apellido }) => {
    const [titulares, setTitulares] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchTitulares = async () => {
            try {
                const titularesData = await findAllTitulares(nombre, apellido);
                setTitulares(titularesData);
                setError(null);
            } catch (err) {
                setError('Hubo un error al cargar los titulares.');
            }
        };
        fetchTitulares();
    }, [nombre, apellido]); // Aquí es la clave: corre cada vez que cambian nombre o apellido

    return (
        <div>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            {titulares.length > 0 ? (
                <table className="table-auto border-collapse border border-gray-400 w-full text-left">
                    <thead>
                        <tr>
                            <th className="border border-gray-400 px-4 py-2">Tipo Documento</th>
                            <th className="border border-gray-400 px-4 py-2">Documento</th>
                            <th className="border border-gray-400 px-4 py-2">Nombre</th>
                            <th className="border border-gray-400 px-4 py-2">Apellido</th>
                            <th className="border border-gray-400 px-4 py-2">Domicilio</th>
                        </tr>
                    </thead>
                    <tbody>
                        {titulares.map((titular) => (
                            <tr key={titular.id}>
                                <td className="border border-gray-400 px-4 py-2">{titular.tipoDocumento}</td>
                                <td className="border border-gray-400 px-4 py-2">{titular.documento}</td>
                                <td className="border border-gray-400 px-4 py-2">{titular.nombre}</td>
                                <td className="border border-gray-400 px-4 py-2">{titular.apellido}</td>
                                <td className="border border-gray-400 px-4 py-2">{titular.domicilio}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            ) : (
                !error && <p>No hay titulares registrados.</p>
            )}
        </div>
    );
};

const BuscarTitular = () => {
    const [nombreYApellido, setNombreYApellido] = useState('');
    const [nombre, setNombre] = useState('');
    const [apellido, setApellido] = useState('');

    const handleInputChange = (e) => {
        const input = e.target.value;
        setNombreYApellido(input);
        const partes = input.trim().split(' ');
        if (partes.length > 1) {
            setNombre(partes.slice(0, -1).join(' '));
            setApellido(partes[partes.length - 1]);
        } else if (input.trim() === '') {
            setNombre(null);
            setApellido(null);
        } else {
            setNombre(input);
            setApellido(null);
        }
    };

    return (
        <div>
            <div>
                <label htmlFor="nombreYApellido" className="block font-semibold mb-1">
                    Nombre y Apellido:
                </label>
                <input
                    id="nombreYApellido"
                    type="text"
                    placeholder="nombre y apellido"
                    className="border-2 border-gray-300 rounded-lg px-3 py-2 text-lg focus:outline-none focus:border-indigo-600"
                    value={nombreYApellido}
                    onChange={handleInputChange}
                />
            </div>
            <TablaTitulares nombre={nombre} apellido={apellido} />
        </div>
    );
};

export default BuscarTitular;