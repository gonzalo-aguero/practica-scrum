// Header.jsx
import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useHeader } from "./HeaderContext.jsx";
import logo from '../assets/Santa_Fe_Capital.png';

function Header() {
    const normalButtonStyle = "text-gray-600 hover:text-[#12bc8e] font-medium px-3 py-2 rounded-md text-medium";
    const specialButtonStyle = "bg-[#12bc8e] text-white hover:opacity-85 px-3 py-2 rounded-md text-medium font-medium";
    const location = useLocation();
    const { headerState, setHeaderState } = useHeader();

    //Por cada pagina que agreguen y quieran mantener o cambiar el header deberan agregarlo en el useEffect del react.
    React.useEffect(() => {
        switch (location.pathname) {
            case '/alta-usuario':
            case '/homeAdministrador':
                setHeaderState({ tipo: 'admin' });
                break;
            case '/buscar-titular':
            case '/emitir-licencia':
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
                            className={normalButtonStyle}
                        >
                            Inicio
                        </Link>
                        <Link
                            to="/alta-usuario"
                            className={normalButtonStyle}
                        >
                            Registrar Usuario
                        </Link>
                        <Link
                            to="/"
                            className={specialButtonStyle}
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
                            className={normalButtonStyle}
                        >
                            Inicio
                        </Link>
                        <Link to="/buscar-titular"
                              className={normalButtonStyle}>
                            Buscar Titular
                        </Link>
                        <Link
                            to="/alta-titular"
                            className={normalButtonStyle}
                        >
                            Registrar Titular
                        </Link>
                        <Link
                            to="/emitir-licencia"
                            className={normalButtonStyle}
                        >
                            Registrar Licencia
                        </Link>
                        <Link
                            to="/"
                            className={specialButtonStyle}
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
                            className={normalButtonStyle}
                        >
                            Inicio
                        </Link>
                        <Link
                            to="/login"
                            className={specialButtonStyle}
                        >
                            Iniciar Sesión
                        </Link>
                    </nav>
                );
        }
    };

    return (
        <header className="bg-white shadow-md py-4">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="flex justify-between items-center h-16">
                    <div className="flex items-center">
                        <Link to="/" className="flex items-center">
                            <img
                                className="h-20 w-auto"
                                src={logo}
                                alt="Logo Municipalidad de Santa Fe"
                            />
                                {/* <span className="ml-3 text-medium font-semibold text-[#212121]">
                    Municipalidad de Santa Fe
                </span> */}
                        </Link>
                    </div>
                    {renderNavigationLinks()}
                </div>
            </div>
        </header>
    );
}

export default Header;