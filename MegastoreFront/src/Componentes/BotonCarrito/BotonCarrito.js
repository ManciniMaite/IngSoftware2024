import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import './BotonCarrito.css';

const BotonCarrito = ({ carrito, pedidoId }) => {
  const navigate = useNavigate();
  const location = useLocation();

  const manejarClickCarrito = () => {
    if (location.pathname === '/pedido/carrito') {
      // Si ya estás en el carrito, volver a la página anterior
      navigate('/catalogo');
    } else {
      // Si no estás en el carrito, navegar al carrito o detalles del pedido
      navigate('/pedido/carrito', { state: { productos: carrito, pedidoId, editable: true } });
    }
  };

  return (
    <div className="button">
      <button className="carrito-button" onClick={manejarClickCarrito}>
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" className="bi bi-cart-fill" viewBox="0 0 16 16">
          <path d="M1 1.5A.5.5 0 0 1 1.5 1h.682l1.1 8.36a1 1 0 0 0 1.002.93h6.54a1 1 0 0 0 .994-.883L14.32 3H4.6l-.1-.5H1.5a.5.5 0 0 1-.5-.5zM3 15a2 2 0 1 1 0-4 2 2 0 0 1 0 4zm10 0a2 2 0 1 1 0-4 2 2 0 0 1 0 4z" />
        </svg>
      </button>
    </div>
  );
};

export default BotonCarrito;
