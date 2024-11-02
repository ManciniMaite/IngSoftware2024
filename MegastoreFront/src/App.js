import React, { useState } from 'react'; // Asegúrate de importar useState
import { BrowserRouter as Router, Route, Routes, useLocation } from 'react-router-dom';
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

function App() {
  const [isSidebarVisible, setIsSidebarVisible] = useState(false);
  const [carrito, setCarrito] = useState([]);
  const [isLoggedIn, setIsLoggedIn] = useState(false); // Añadido: estado de isLoggedIn

  const toggleSidebar = () => {
    setIsSidebarVisible(!isSidebarVisible);
  };

  const agregarAlCarrito = (producto) => {
    setCarrito((prevCarrito) => {
      const productoExistente = prevCarrito.find((item) => item.id === producto.id);
      if (productoExistente) {
        return prevCarrito.map((item) =>
          item.id === producto.id ? { ...item, cantidad: item.cantidad + 1 } : item
        );
      } else {
        return [...prevCarrito, { ...producto, cantidad: 1 }];
      }
    });
  };

  return (
    <Router>
      <AppContent
        toggleSidebar={toggleSidebar}
        isSidebarVisible={isSidebarVisible}
        carrito={carrito}
        agregarAlCarrito={agregarAlCarrito}
        isLoggedIn={isLoggedIn} // Pasamos el estado de isLoggedIn
        setIsLoggedIn={setIsLoggedIn} // Pasamos la función para cambiar el estado
      />
    </Router>
  );
}

function AppContent({ toggleSidebar, isSidebarVisible, carrito, agregarAlCarrito, isLoggedIn, setIsLoggedIn }) {
  const location = useLocation();

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
          <Route path="/catalogo" element={<Catalogo agregarAlCarrito={agregarAlCarrito} />} />
          <Route path="/sobre-nosotros" element={<h1>Sobre Nosotros</h1>} />
          <Route path="/sucursales" element={<h1>Sucursales</h1>} />
          <Route path="/login" element={<Login setIsLoggedIn={setIsLoggedIn} />} /> {/* Pasa la función a Login */}
          <Route path="/registro" element={<Registro />} />
          <Route path="/pedidos" element={<Pedidos />} />
          <Route path="/pedido/:id" element={<DetallePedido />} />
          <Route path="/carrito" element={<Carrito productos={carrito} />} />
          <Route path="/pedidosAdmin" element={<PedidosAdmin />} />
          <Route path="/admin" element={<Admin />} /> {/*  ruta para admin */}
          <Route path="/loginAdmin" element={<LoginAdmin />} />
          <Route path="/catalogo/GestionarCatalogo" element={<GestionarCatalogo/>} />
          <Route path="/admin/pedidos" element={<Pedidos />} />
          <Route path="/edicion" element={<Edicion/>} />
        </Routes>
      </div>
    </div>
  );
}

export default App;
