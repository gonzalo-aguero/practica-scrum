import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import RegisterForm from './pages/RegisterForm';
import './App.css'

function App() {
  return (
    <Router>
      <div>
        <Routes>
          <Route path="/alta-usuario" element={<RegisterForm />} />
          <Route path="/" element={<div className="p-4 text-center">Welcome to the application</div>} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
