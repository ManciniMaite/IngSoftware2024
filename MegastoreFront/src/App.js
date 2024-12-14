import React, { useState, useEffect } from 'react';
import { AuthProvider } from './Componentes/Autenticacion/AuthContext'; // Contexto de autenticación
import { SidebarProvider } from './Componentes/Autenticacion/SidebarContext'; // Contexto de la sidebar
import { BrowserRouter as Router, Route, Routes, useLocation } from 'react-router-dom';
import { useSidebar } from './Componentes/Autenticacion/SidebarContext'; // Importa el hook useSidebar
import Menu from './Componentes/Menu/Menu';
import Home from './Componentes/Home/Home';
import Login from './Componentes/Login/Login';
import Registro from './Componentes/Registro/Registro';
import Catalogo from './Componentes/Catalogo/Catalogo';
import Cabecera from './Componentes/Cabecera/Cabecera';
import Pedidos from './Componentes/Pedidos/Pedidos';
import DetallePedido from './Componentes/DetallePedido/DetallePedido';
import Carrito from './Componentes/BotonCarrito/BotonCarrito';
import './App.css';
import PedidosAdmin from './Componentes/PedidosAdmin/PedidosAdmin';
import GestionarCatalogo from './Componentes/Catalogo/GestionarCatalogo';
import Admin from './Componentes/Admin/Admin';
import LoginAdmin from './Componentes/Admin/LoginAdmin';
import Edicion from './Componentes/Edicion/Edicion';
import ReporteVentas from './Componentes/Estadisticas/ventas';
import EstadisticasHome from './Componentes/Estadisticas/EstadisticasHome';

function App() {
  const [carrito, setCarrito] = useState([]);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userRole, setUserRole] = useState(localStorage.getItem('userRole') || '');

  useEffect(() => {
    const savedRole = localStorage.getItem('userRole');
    if (savedRole) {
      setUserRole(savedRole);
    }
  }, []);

  return (
    <AuthProvider>  {/* Contexto de autenticación */}
      <SidebarProvider>  {/* Contexto de la sidebar */}
        <Router>
          <AppContent
            carrito={carrito}
            isLoggedIn={isLoggedIn}
            setIsLoggedIn={setIsLoggedIn}
            userRole={userRole}
          />
        </Router>
      </SidebarProvider>
    </AuthProvider>
  );
}

function AppContent({ carrito, isLoggedIn, setIsLoggedIn, userRole }) {
  const location = useLocation();
  const { isSidebarVisible, toggleSidebar } = useSidebar(); // Usamos el hook de la sidebar
  
  const shouldShowCabecera = () => {
    return ['/login', '/registro', '/catalogo'].includes(location.pathname);
  };

  return (
    <div className="App">
      {shouldShowCabecera() && <Cabecera isLoggedIn={isLoggedIn} carrito={carrito} />}
      
      <Menu isVisible={isSidebarVisible} toggleSidebar={toggleSidebar} />
      
      <div className={`content ${isSidebarVisible ? 'with-sidebar' : 'full-width'}`}>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/catalogo" element={<Catalogo />} />
          <Route path="/login" element={<Login setIsLoggedIn={setIsLoggedIn} />} />
          <Route path="/registro" element={<Registro />} />
          <Route path="/pedidosAdmin" element={<PedidosAdmin />} />
          <Route path="/admin" element={<Admin />} />
          <Route path="/loginAdmin" element={<LoginAdmin />} />
          <Route path="/admin/pedidos" element={<Pedidos />} />
          <Route path="/pedidos" element={<Pedidos />} />
          <Route path="/pedido/:id" element={<DetallePedido />} />
          <Route path="/edicion" element={<Edicion />} />
          <Route path="/carrito" element={<Carrito productos={carrito} />} />
          <Route path="/estadisticasHome" element={<EstadisticasHome />} />
        </Routes>
      </div>
    </div>
  );
}

export default App;
