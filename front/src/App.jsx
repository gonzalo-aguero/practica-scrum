import { BrowserRouter as Router, Routes, Route, useNavigate } from 'react-router-dom';
import Header from './components/Header';
import {HeaderProvider} from "./components/HeaderContext.jsx";
import RegisterUsuarioForm from './pages/RegisterUsuarioForm.jsx';
import RegisterTitularForm from './pages/TitularRegisterForm.jsx';
import LoginForm from './pages/LoginForm';
import homeUsuario from "./pages/HomeUsuario.jsx";
import homeAdministrador from "./pages/HomeAdministrador.jsx";
import LicenseRegisterForm from './pages/LicenseRegisterForm.jsx';
import Comprobante from './pages/Comprobante.jsx';
import BuscarTitular from './pages/BuscarTitular.jsx';
import './App.css';



function HomePage() {
    const navigate = useNavigate();

    return (
        <div className="text-center">
            <h1 className="text-3xl font-bold text-gray-900">Bienvenido al Sistema de Licencias</h1>
            <p className="mt-4 text-gray-600">Sistema de gestión de licencias de conducir de la Municipalidad de Santa Fe</p>
        </div>
    );
}

function App() {
    return (
        <Router>
            <HeaderProvider>
                <div className="min-h-screen bg-gray-50">
                    <Header />
                    <main className="container mx-auto px-4 py-8">
                        <Routes>
                            <Route path="/" element={<HomePage />} />
                            <Route path="/alta-usuario" element={<RegisterUsuarioForm />} />
                            <Route path="/alta-titular" element={<RegisterTitularForm />} />
                            <Route path="/login" element={<LoginForm />} />
                            <Route path="/homeUsuario" element={<homeUsuario />} />
                            <Route path="/homeAdministrador" element={<homeAdministrador />} />
                            <Route path="/emitir-licencia" element={<LicenseRegisterForm/>} />
                            <Route path="/comprobante" element={<Comprobante/>} />
                            <Route path="/buscar-titular" element={<BuscarTitular/>} />
                        </Routes>
                    </main>
                </div>
            </HeaderProvider>
        </Router>
    );
}

export default App;