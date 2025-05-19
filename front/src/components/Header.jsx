// Header.jsx
import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useHeader } from "./HeaderContext.jsx";
import logo from '../assets/municipalidad-logo.svg';

function Header() {
    const location = useLocation();
    const { headerState, setHeaderState } = useHeader();

    //Por cada pagina que agreguen y quieran mantener o cambiar el header deberan agregarlo en el useEffect del react.
    React.useEffect(() => {
        switch (location.pathname) {
            case '/alta-usuario':
            case '/homeAdministrador':
                setHeaderState({ tipo: 'admin' });
                break;
            case '/alta-titular':
            case '/homeUsuario':
                setHeaderState({ tipo: 'usuario' });
                break;
            default:
                setHeaderState({ tipo: 'default' });
        }
    }, [location.pathname, setHeaderState]);

    //Si necesitan agregar alguna funcionalidad en algun header o agregar alguno lo escriben en los cases.
    const renderNavigationLinks = () => {
        switch (headerState?.tipo) {
            case 'admin':
                return (
                    <div className="flex items-center space-x-4">
                        <Link
                            to="/homeAdministrador"
                            className="text-gray-600 hover:text-gray-900 px-3 py-2 rounded-md text-sm font-medium"
                        >
                            Inicio
                        </Link>
                        <Link
                            to="/alta-usuario"
                            className="text-gray-600 hover:text-gray-900 px-3 py-2 rounded-md text-sm font-medium"
                        >
                            Registrar Usuario
                        </Link>
                        <Link
                            to="/"
                            className="text-gray-600 hover:text-gray-900 px-3 py-2 rounded-md text-sm font-medium"
                        >
                            Cerrar Sesión
                        </Link>
                    </div>
                );

            case 'usuario':
                return (
                    <div className="flex items-center space-x-4">
                        <Link
                            to="/homeUsuario"
                            className="text-gray-600 hover:text-gray-900 px-3 py-2 rounded-md text-sm font-medium"
                        >
                            Inicio
                        </Link>
                        <Link
                            to="/alta-titular"
                            className="text-gray-600 hover:text-gray-900 px-3 py-2 rounded-md text-sm font-medium"
                        >
                            Registrar Titular
                        </Link>
                        <Link
                            to="/"
                            className="text-gray-600 hover:text-gray-900 px-3 py-2 rounded-md text-sm font-medium"
                        >
                            Cerrar Sesión
                        </Link>
                    </div>
                );

            default:
                return (
                    <nav className="flex items-center space-x-4">
                        <Link
                            to="/"
                            className="px-3 py-2 rounded-md text-sm font-medium text-gray-700 hover:text-indigo-600 hover:bg-gray-50 transition duration-150 ease-in-out"
                        >
                            Inicio
                        </Link>
                        <Link
                            to="/login"
                            className="px-3 py-2 rounded-md text-sm font-medium text-gray-700 hover:text-indigo-600 hover:bg-gray-50 transition duration-150 ease-in-out"
                        >
                            Iniciar Sesión
                        </Link>
                    </nav>
                );
        }
    };

    return (
        <header className="bg-white shadow-md">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="flex justify-between items-center h-16">
                    <div className="flex items-center">
                        <Link to="/" className="flex items-center">
                            <img
                                className="h-8 w-auto"
                                src={logo}
                                alt="Logo Municipalidad"
                            />
                            <span className="ml-3 text-xl font-semibold text-gray-900">
                Municipalidad de Santa Fe
              </span>
                        </Link>
                    </div>
                    {renderNavigationLinks()}
                </div>
            </div>
        </header>
    );
}

export default Header;