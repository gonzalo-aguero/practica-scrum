// src/pages/VerLicencia.jsx
import React, { useEffect, useRef, useState } from 'react';
import LicenciaConducir from '../components/LicenciaConducir';
import { useReactToPrint } from 'react-to-print';
import axios from 'axios';
import { useParams } from 'react-router-dom';

const VerLicencia = () => {
    const { id } = useParams();
    const licenciaRef = useRef();
    const [datosLicencia, setDatosLicencia] = useState(null);
    const [datosClaseLicencia, setDatosClaseLicencia] = useState([]);
    const [datosTitular, setDatosTitular] = useState(null);
    const [datosUsuarios, setDatosUsuarios] = useState([]);
    const [loading, setLoading] = useState(true);

    const handlePrint = useReactToPrint({
        content: () => licenciaRef.current,
        documentTitle: 'Licencia-Conducir',
    });

    useEffect(() => {
        const fetchDatos = async () => {
            try {
                // 1. Obtener la licencia por titular:
                const response = await axios.get(`http://localhost:8080/api/licencias/LicXTitular/${id}`);
                const licencia = response.data;
                setDatosLicencia(licencia);
                console.log(licencia);

                // 2. Obtener datos del titular:
                if (licencia && licencia.titularId) {
                    const responseTitular = await axios.get(`http://localhost:8080/api/titulares/${licencia.titularId}`);
                    setDatosTitular(responseTitular.data);
                    console.log(responseTitular.data);
                }

                // 3. Obtener las clases de licencia (probablemente como array):
                if (licencia && licencia.id) {
                    const responseClaseLicencia = await axios.get(`http://localhost:8080/api/claseLicencias/porLicencia/${licencia.id}`);
                    setDatosClaseLicencia(responseClaseLicencia.data);
                    console.log(responseClaseLicencia.data);

                    // 4. Obtener todos los usuarios emisores para esas clases:
                    const clasesLicencias = responseClaseLicencia.data;
                    const usuariosPromise = clasesLicencias.map(async (clase) => {
                        if (clase.usuarioEmisor) {
                            const resUsuario = await axios.get(`http://localhost:8080/api/usuarios/${clase.usuarioEmisor}`);
                            return resUsuario.data;
                        }
                        return null;
                    });
                    const usuarios = await Promise.all(usuariosPromise);
                    setDatosUsuarios(usuarios.filter(Boolean));
                }
            } catch (error) {
                console.error("Error al obtener los datos de la licencia", error);
            } finally {
                setLoading(false);
            }
        };

        fetchDatos();
    }, [id]);

    if (loading) return <p className="p-8">Cargando licencia...</p>;
    if (!datosTitular) return <p className="p-8 text-red-600">No se pudo cargar la licencia.</p>;

    return (
        <div className="p-8 bg-gray-100 min-h-screen">
            <h1 className="text-2xl font-bold mb-6">Licencia Generada</h1>
            <div className="mb-4">
                <button onClick={handlePrint} className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition">
                    Imprimir / Descargar
                </button>
            </div>
            <LicenciaConducir
                ref={licenciaRef}
                datos={datosTitular}
                licencia={datosLicencia}
                clasesLicencia={datosClaseLicencia}
                usuariosEmisores={datosUsuarios}
            />
        </div>
    );
};

export default VerLicencia;