import { BrowserRouter as Router, Routes, Route, useNavigate } from 'react-router-dom';
import Header from './components/Header';
import RegisterForm from './pages/RegisterForm';
import RegisterTitularForm from './pages/TitularRegisterForm.jsx';
import LoginForm from './pages/LoginForm';
import homeUsuario from "./pages/homeUsuario.jsx";
import homeAdministrador from "./pages/homeAdministrador.jsx";

import './App.css';
import LicenseRegisterForm from './pages/LicenseRegisterForm.jsx';


function HomePage() {
  const navigate = useNavigate();

  return (
    <div className="text-center">
      <h1 className="text-3xl font-bold text-gray-900">Bienvenido al Sistema de Licencias</h1>
      <p className="mt-4 text-gray-600">Sistema de gestión de licencias de conducir de la Municipalidad de Santa Fe</p>
      <button
        onClick={() => navigate('/login')}
        className="mt-6 bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded"
      >
        Iniciar Sesión
      </button>
    </div>
  );
}

function App() {
  return (
    <Router>
      <div className="min-h-screen bg-gray-50">
        <Header />
        <main className="container mx-auto px-4 py-8">
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/alta-usuario" element={<RegisterForm />} />
            <Route path="/alta-titular" element={<RegisterTitularForm />} />
            <Route path="/login" element={<LoginForm />} />
            <Route path="/homeUsuario" element={<homeUsuario />} />
            <Route path="/homeAdministrador" element={<homeAdministrador />} />
            <Route path="/emitir-licencia" element={<LicenseRegisterForm/>} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;