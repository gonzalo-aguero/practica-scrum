import React, {useEffect, useState} from 'react';
import axios from 'axios';

const findAllTitulares = async () =>{
    try{
        const response = await axios.get('http://localhost:8080/api/titulares/all-titulares');
        const titulares = response.data;
        console.log('Titulares encontrados!');
        return titulares;
    }catch(error) {
        console.log('Error al buscar titulares: ', error.message);
        throw error;
    }
}
const TablaTitulares = () => {
    const [titulares, setTitulares] = useState([]);
    const [error , setError] = useState(null);

    useEffect(() => {
        const fetchTitulares = async () => {
            try {
                const titularesData = await findAllTitulares(); // Llama a la función
                setTitulares(titularesData);// Almacena los titulares en el estado
                console.log('Titulares: ', titularesData);
            } catch (err) {
                setError('Hubo un error al cargar los titulares.');
            }
        }
        fetchTitulares();
    }, []);


    const personas = [
        {tipoDocumento: 'DNI', documento: '12345678', nombre: 'Juan', apellido: 'Perez'},
        {tipoDocumento: 'DNI', documento: '98765432', nombre: 'Maria', apellido: 'Lopez'},
        {tipoDocumento: 'DNI', documento: '12345678', nombre: 'Juan', apellido: 'Perez'},
    ];

    return (
        <div>
            <h1 className="text-2xl font-bold mb-4">Listado de Titulares</h1>
            {error && <p style={{ color: 'red' }}>{error}</p>} {/* Muestra el error si ocurre */}

            {titulares.length > 0 ? ( <table
                className="table-auto border-collapse border border-gray-400 w-full text-left">
                <thead>
                <tr>
                    <th className="border border-gray-400 px-4 py-2"
                    >Tipo Documento</th>
                    <th className="border border-gray-400 px-4 py-2"
                    >Documento</th>
                    <th className="border border-gray-400 px-4 py-2"
                    >Nombre</th>
                    <th className="border border-gray-400 px-4 py-2"
                    >Apellido</th>
                    <th className="border border-gray-400 px-4 py-2"
                    >Domicilio</th>
                </tr>
                </thead>
                <tbody>
                {titulares.map((titular, index) => (
                    <tr key={titular.id}>
                        <td className="border border-gray-400 px-4 py-2"
                        >{titular.tipoDocumento}</td>
                        <td className="border border-gray-400 px-4 py-2"
                        >{titular.documento}</td>
                        <td className="border border-gray-400 px-4 py-2"
                        >{titular.nombre}</td>
                        <td className="border border-gray-400 px-4 py-2"
                        >{titular.apellido}</td>
                        <td className="border border-gray-400 px-4 py-2"
                        >{titular.domicilio}</td>
                    </tr>
                ))}
                </tbody>
            </table>) : (
                !error && <p>No hay titulares registrados.</p>
            )}

        </div>
    )
}

const BuscarTitular = () => {
    return (
        <div>
            <TablaTitulares />
        </div>
    );
}

export default BuscarTitular;