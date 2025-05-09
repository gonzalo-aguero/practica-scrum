import { Link } from 'react-router-dom';
import logo from '../assets/municipalidad-logo.svg';

const Header = () => {
  return (
    <header className="bg-white shadow-md">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-16">
          {/* Logo y Título */}
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

          {/* Menú de Navegación */}
          <nav className="flex items-center space-x-4">
            <Link
              to="/"
              className="px-3 py-2 rounded-md text-sm font-medium text-gray-700 hover:text-indigo-600 hover:bg-gray-50 transition duration-150 ease-in-out"
            >
              Inicio
            </Link>
            <Link
              to="/licencias"
              className="px-3 py-2 rounded-md text-sm font-medium text-gray-700 hover:text-indigo-600 hover:bg-gray-50 transition duration-150 ease-in-out"
            >
              Licencias
            </Link>
            <Link
              to="/titulares"
              className="px-3 py-2 rounded-md text-sm font-medium text-gray-700 hover:text-indigo-600 hover:bg-gray-50 transition duration-150 ease-in-out"
            >
              Titulares
            </Link>
            <Link
              to="/reportes"
              className="px-3 py-2 rounded-md text-sm font-medium text-gray-700 hover:text-indigo-600 hover:bg-gray-50 transition duration-150 ease-in-out"
            >
              Reportes
            </Link>
            <Link
              to="/alta-usuario"
              className="px-3 py-2 rounded-md text-sm font-medium text-gray-700 hover:text-indigo-600 hover:bg-gray-50 transition duration-150 ease-in-out"
            >
              Crear Usuario
            </Link>
          </nav>
        </div>
      </div>
    </header>
  );
};

export default Header; 