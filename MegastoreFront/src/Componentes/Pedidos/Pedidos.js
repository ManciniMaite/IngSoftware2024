// src/Componentes/Pedidos/Pedidos.js
import React, {useEffect, useState} from 'react';
import { useNavigate } from 'react-router-dom';
import Cabecera from '../Cabecera/Cabecera';
import './Pedidos.css';
import { useAuth } from '../Autenticacion/AuthContext';



const Pedidos = () => {
  const navigate = useNavigate(); // Hook para navegación
  const {user, isLoggedIn} =useAuth();
  const [pedidos, setPedidos] = useState([]);
  const [pedidoSeleccionado, setPedidoSeleccionado] = useState(false);
  const [codigoPedidoSeleccionado, setCodigoPedidoSeleccionado] = useState();
  const [confirmacionVisible, setConfirmacionVisible] = useState(false);

  useEffect (() =>{
    if (isLoggedIn && user?.id){
      fetchPedidos(user.id);

    }
  },[]);

  const fetchPedidos = async () => {
    if (!user || !user.id) {
      console.warn("Usuario o ID no disponible, no se cargarán pedidos.");
      return;
    }

    // Convertir el ID a número y verificar su validez
    

    // Realizar la solicitud al backend
    try {
      const response = await fetch(`http://localhost:8080/pedido/${user.id}/pedidos`);
      console.log('direccion enviada', `http://localhost:8080/pedido/${user.id}/pedidos`);
      if (response.ok) {
        
        const data = await response.json();
         // Filtrar pedidos con estado "CN" (cancelado)
        const pedidosFiltrados = data.filter(pedido => pedido.estado?.codigo !== 'CN');
        console.log('pedidos front:', data);
        setPedidos(pedidosFiltrados);
      } else {
        console.error("Error al cargar los pedidos");
      }
    } catch (error) {
      console.error("Error al obtener los pedidos:", error);
      
    }
    
};

const confirmarCancelar = async () => {
  if (!pedidoSeleccionado) {
    console.error('No se seleccionó ningún pedido para cancelar');
    return;
  }
  try {
    const siguienteEstado = 'CN';
    // Paso 3: Enviar el pedido al backend para actualizar su estado
    const responseCancelar = await fetch(`http://localhost:8080/pedido/actualizarEstado/${siguienteEstado}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(pedidoSeleccionado), // Enviamos el pedido con el estado actualizado
    });

    if (!responseCancelar.ok) {
      throw new Error('Error al actualizar el estado del pedido');
    }

    const updatedPedido = await responseCancelar.json(); // Pedido actualizado

    // Actualizamos la lista de pedidos con el pedido actualizado
    setPedidos((prevPedidos) =>
      prevPedidos.map((pedido) =>
        pedido.id === updatedPedido.id ? updatedPedido : pedido
      )
    );

    

  } catch (error) {
    console.error('Error al cambiar el estado del pedido:', error);
    window.location.reload()
    // Aquí puedes agregar un estado para mostrar un mensaje de error en la UI si es necesario
  }
  fetchPedidos();
};

const Cancelar = (pedido) => {
  if (!pedido || !pedido.estado) {
    console.error('El pedido no es válido o no tiene un estado:', pedido);
    return;
  }
  console.log("pedidos enviados a manejarActualizarPedido",pedido);
  const codigo = pedido.estado.codigo;
  if (!codigo) {
    console.error('El código del estado del pedido es undefined:', pedido.estado);
    return;
  }
  setCodigoPedidoSeleccionado(codigo);
  setPedidoSeleccionado(pedido); 
  setConfirmacionVisible(true);
  
  console.log("Código del estado del pedido seleccionado:", codigo);
  console.log("Pedido seleccionado:", pedido);


};
const confirmarCambioEstado = async () => {
  try {
    await confirmarCancelar();
    setConfirmacionVisible(false);
  } catch (error) {
    console.error("Error al confirmar el cambio de estado:", error);
  }
};
  
  

  // Función para redirigir a la página de detalles del pedido
  const verDetalles = (id) => {
    console.log("id del pedido que mando a detalle:", id);
    navigate(`/pedido/${id}`); // Redirigir a la ruta de detalles
  }; 

  

  return (
    <div className="pedidos-container">
      <Cabecera />

      <div className="titulo-logo-container">
        <div className="titulo-logo">
          <svg xmlns="http://www.w3.org/2000/svg" width="50" height="50" fill="currentColor" className="logo-pedido" viewBox="0 0 16 16">
            <path fillRule="evenodd" d="M5 11.5a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5m0-4a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5m0-4a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5M3.854 2.146a.5.5 0 0 1 0 .708l-1.5 1.5a.5.5 0 0 1-.708 0l-.5-.5a.5.5 0 1 1 .708-.708L2 3.293l1.146-1.147a.5.5 0 0 1 .708 0m0 4a.5.5 0 0 1 0 .708l-1.5 1.5a.5.5 0 0 1-.708 0l-.5-.5a.5.5 0 1 1 .708-.708L2 7.293l1.146-1.147a.5.5 0 0 1 .708 0m0 4a.5.5 0 0 1 0 .708l-1.5 1.5a.5.5 0 0 1-.708 0l-.5-.5a.5.5 0 1 1 .708-.708l.146.147 1.146-1.147a.5.5 0 0 1 .708 0" />
          </svg>
          <h2>MIS PEDIDOS</h2>
        </div>
      </div>

      <table className="pedidos-tabla">
        <thead>
          <tr>
            <th>Número de Pedido</th>
            <th>Fecha</th>
            <th>Dirección de Envío</th>
            <th>Estado</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
            {pedidos && pedidos.length > 0 ? (
              pedidos.map((pedido) => (
                <tr key={pedido.id}>
                  <td>{pedido.id}</td>
                  <td>{pedido.fechaPedido}</td>
                  <td>{pedido.direccion?.calle} {pedido.direccion?.numero}</td>
                  
                  <td>{pedido.estado && pedido.estado.nombre ? pedido.estado.nombre : "Estado desconocido"}</td>
                  <td>
                    <button 
                      className="detalles-btn" 
                      onClick={() => verDetalles(pedido.id, pedido.detallesPedido)}
                    >
                      Ver Detalles
                    </button>
                    {(pedido.estado?.codigo === 'PN' || pedido.estado?.codigo === 'EP') && (
                        <button 
                          className="cancelar-btn-pedidos" 
                          onClick={() => Cancelar(pedido)}
                        >
                          Cancelar
                        </button>
                    )}
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="5">No hay pedidos disponibles</td>
              </tr>
            )}
        </tbody>
      </table>
      {confirmacionVisible && pedidoSeleccionado && (
        <div className="confirmacion-cambio">
          <p>
            ¿Está seguro que desea Cancelar el pedido con Número de Pedido = {pedidoSeleccionado.id}?
          </p>
          <div>
            <button className="confirmar-btn" onClick={confirmarCambioEstado}>
              Confirmar
            </button>
            <button className="cancelar-btn" onClick={() => setConfirmacionVisible(false)}>
              Cancelar
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default Pedidos;
