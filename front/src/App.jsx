import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Header from './components/Header';
import RegisterForm from './pages/RegisterForm';
import './App.css';

function App() {
  return (
    <Router>
      <div className="min-h-screen bg-gray-50">
        <Header />
        <main className="container mx-auto px-4 py-8">
          <Routes>
            <Route path="/" element={<div className="text-center">
              <h1 className="text-3xl font-bold text-gray-900">Bienvenido al Sistema de Licencias</h1>
              <p className="mt-4 text-gray-600">Sistema de gestión de licencias de conducir de la Municipalidad de Santa Fe</p>
            </div>} />
            <Route path="/alta-usuario" element={<RegisterForm />} />
            {/* Agregar más rutas según sea necesario */}
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;
