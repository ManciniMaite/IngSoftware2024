// src/Componentes/DetallePedido/DetallePedido.js
import React, { useEffect, useState } from 'react';
import { useNavigate, useParams, useLocation } from 'react-router-dom';
import './DetallePedido.css';
import Cabecera from '../Cabecera/Cabecera';
import { useAuth } from '../Autenticacion/AuthContext';

const DetallePedido = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const location = useLocation();
  const { user, carrito, updateProductQuantity, setCarrito, isLoggedIn,hasRole } = useAuth(); // Obtiene datos del usuario y su carrito
  const [detallesPedido, setDetallesPedido] = useState([]);
 
  const isEditable = location.state?.editable ?? false; // Determina si el detalle es editable
  const isFromPedidoAdmin = 
  (location.pathname.includes('/pedido') || location.pathname.includes('/pedidosAdmin')) &&
  !location.pathname.includes('/pedido/carrito');

  const [direccionSeleccionada, setDireccionSeleccionada] = useState(null);
  const [direcciones, setDirecciones] = useState([]);
  const [mostrarDirecciones, setMostrarDirecciones] = useState(false);
  const [loading, setLoading] = useState(false);

  

  
  useEffect(() => {
    if (isLoggedIn && id) {
      fetchDetalles();
    }
  }, [isLoggedIn, id]); // Solo depende de `isLoggedIn` y `id`

  
  
  const fetchDetalles = async () => {
    if (!id) {
      console.warn("Detalle no disponible.");
      return;
    }

    // Convertir el ID a número y verificar su validez
    const parsedId = parseInt(id.trim(), 10); // Usar .trim() para eliminar espacios y saltos de línea

    console.log("Tipo de parsedId:", typeof parsedId); // Verifica que es 'number'
    if (isNaN(parsedId) || parsedId <= 0) {
      console.error("ID de usuario inválido:", parsedId);
      return;
    }

    console.log('User ID:', parsedId); // Verifica que user.id no sea undefined o null
    console.log("Haciendo solicitud a:", `http://localhost:8080/pedido/${parsedId}/detalles`);

    // Realizar la solicitud al backend
    try {
      const response = await fetch(`http://localhost:8080/pedido/${parsedId}/detalles`);
      console.log('direccion enviada', `http://localhost:8080/pedido/${parsedId}/detalles`);
      if (response.ok) {
        
        const data = await response.json();
        console.log('detalles front:', data);
        if (Array.isArray(data)) {
          setDetallesPedido(data);
        } else {
          console.error("La respuesta no es un arreglo válido");
        }
        console.log('Detalles del pedido:', detallesPedido);  // Asegúrate de que se imprimen correctamente

      } else {
        console.error("Error al cargar los pedidos");
      }
    } catch (error) {
      console.error("Error al obtener los pedidos:", error);
    }
};
   

  
  

  const totalDetalle = detallesPedido.reduce(
    (acc, detalle) => acc + detalle.precio * detalle.cantidad,
    0
  );
  const totalCarrito = carrito.reduce(
    (acc, carrito) => acc + carrito.precio * carrito.cantidad,
    0
  );

  const cargarDirecciones = async () => {
    if (!user || !user.id) {
      console.warn("Usuario o ID no disponible, no se cargarán direcciones.");
      return;
    }
    try {
      const response = await fetch(`http://localhost:8080/usuario/${user.id}/direcciones`);
      if (response.ok) {
        const data = await response.json();
        setDirecciones(data);
        setMostrarDirecciones(true); // Muestra el modal de direcciones
      } else {
        console.error("Error al cargar las direcciones");
      }
    } catch (error) {
      console.error("Error al cargar las direcciones:", error);
    }
  };

  const enviarPedido = async () => {
    if (!direccionSeleccionada) {
      console.log("Debe seleccionar una dirección antes de finalizar la compra");
      return;
    }

    const detallesPedido = carrito.map((producto) => ({
      cantidad: producto.cantidad,
      producto: { id: producto.id },
    }));

    const pedidoData = {
      usuario: { id: user.id },
      direccion: { id: direccionSeleccionada },
      detallesPedido,
    };

    try {
      setLoading(true);
      const response = await fetch('http://localhost:8080/pedido/insert', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(pedidoData),
      });

      if (response.ok) {
        console.log('Pedido finalizado');
        setCarrito([]); // Limpiar el carrito
        navigate('/pedidos');
      } else {
        console.error('Hubo un error al finalizar el pedido');
      }
    } catch (error) {
      console.error('Error al realizar la solicitud:', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="detalle-pedido-container">
      <Cabecera />
      <div className="titulo-logo-container">
        <h2>DETALLE DE LA COMPRA #{id}</h2>
      </div>

      <table className="detalle-tabla">
        <thead>
          <tr>
            <th>PRODUCTO</th>
            <th>PRECIO POR UNIDAD</th>
            <th>CANTIDAD</th>
            <th>SUBTOTAL</th>
          </tr>
        </thead>
        <tbody>
          {isFromPedidoAdmin
            ? detallesPedido.length > 0 
              ? detallesPedido.map((detalle, index) => (
                  <tr key={index}>
                    <td>{detalle.producto?.nombre || "Producto no disponible"}</td>
                    <td>${detalle.precio?.toFixed(2) || "0.00"}</td>
                    <td>{detalle.cantidad || "0"}</td>
                    <td>${(detalle.precio * detalle.cantidad).toFixed(2) || "0.00"}</td>
                  </tr>
                ))
              : carrito.map((producto, index) => (
                  <tr key={index}>
                    <td>{producto.nombre}</td>
                    <td>${producto.precio.toFixed(2)}</td>
                    <td>
                      {isEditable ? (
                        <>
                          <button onClick={() => updateProductQuantity(producto.id, producto.cantidad - 1)}>-</button>
                          {producto.cantidad}
                          <button onClick={() => updateProductQuantity(producto.id, producto.cantidad + 1)}>+</button>
                        </>
                      ) : (
                        producto.cantidad
                      )}
                    </td>
                    <td>${(producto.precio * producto.cantidad).toFixed(2)}</td>
                  </tr>
                ))
            : carrito.map((producto, index) => (
                <tr key={index}>
                  <td>{producto.nombre}</td>
                  <td>${producto.precio.toFixed(2)}</td>
                  <td>
                    {isEditable ? (
                      <>
                        <button onClick={() => updateProductQuantity(producto.id, producto.cantidad - 1)}>-</button>
                        {producto.cantidad}
                        <button onClick={() => updateProductQuantity(producto.id, producto.cantidad + 1)}>+</button>
                      </>
                    ) : (
                      producto.cantidad
                    )}
                  </td>
                  <td>${(producto.precio * producto.cantidad).toFixed(2)}</td>
                </tr>
              ))}
        </tbody>


      </table>

      <div className="total-container">
      {isFromPedidoAdmin ? (
        <h3>TOTAL DEL DETALLE: ${totalDetalle.toFixed(2)}</h3> // Muestra el total del detalle
      ) : (
        <h3>TOTAL DEL CARRITO: ${totalCarrito.toFixed(2)}</h3> // Muestra el total del carrito
      )}
      </div>

      {isEditable ? (
        <button className="finalizar-btn" onClick={cargarDirecciones}>
          Finalizar
        </button>
      ) : (
        <button className="volver-btn-detalle" onClick={() => navigate(-1)}>
          Volver
        </button>
      )}

      {mostrarDirecciones && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h3>Selecciona una dirección</h3>
            {direcciones.length > 0 ? (
              <select onChange={(e) => setDireccionSeleccionada(e.target.value)} value={direccionSeleccionada || ''}>
                <option value="">Seleccione una dirección</option>
                {direcciones.map((direccion) => (
                  <option key={direccion.id} value={direccion.id}>
                    {direccion.calle}, {direccion.numero}
                  </option>
                ))}
              </select>
            ) : (
              <p>No hay direcciones disponibles.</p>
            )}
            <div className='modal-buttons'>
              <button onClick={enviarPedido} disabled={!direccionSeleccionada}>
                Aceptar
              </button>
              <button onClick={() => setMostrarDirecciones(false)}>Cancelar</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default DetallePedido;
