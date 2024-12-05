// src/Componentes/PedidosAdmin/PedidosAdmin.js
import React, { useState,useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Cabecera from '../Cabecera/Cabecera';
import '../Pedidos/Pedidos.css';
import './PedidosAmin.css'; // Fixed typo in the import statement



const PedidosAdmin = () => {
  const navigate = useNavigate();
  const [confirmacionVisible, setConfirmacionVisible] = useState(false);
  const [codigoPedidoSeleccionado, setCodigoPedidoSeleccionado] = useState(null);
  const [pedidoSeleccionado, setPedidoSeleccionado] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState();
  const [pedidos, setPedidos] = useState([]); // State for orders
  
 
  

  // Navigate to details
  const verDetalles = (id) => {
    navigate(`/pedido/${id}`);
  };
   // Fetch para obtener todos los pedidos desde la base de datos
   useEffect(() => {
    const fetchPedidos = async () => {
      try {
        const response = await fetch('http://localhost:8080/pedido/getAll'); 
        if (!response.ok) {
          throw new Error('Error al obtener los pedidos');
        }
        const data = await response.json();
        console.log("Pedidos obtenidos del backend:", data);
        setPedidos(data); // Guarda los pedidos en el estado
        setLoading(false); // Cambia el estado de carga a falso
      } catch (error) {
        setError(error.message); // Maneja el error si ocurre
        setLoading(false); // Cambia el estado de carga a falso en caso de error
      }
    };
    

    fetchPedidos();
  }, []); // El array vacío asegura que solo se ejecute una vez al montar el componente
  // Ahora puedes hacer un segundo useEffect para actualizar los estados cuando se cargan los pedidos
  useEffect(() => {
    if (pedidos.length > 0) {
      // Aquí puedes realizar cualquier acción que dependa de los pedidos
      console.log("Pedidos cargados correctamente");
    }
  }, [pedidos]); // Este useEffect se ejecutará cada vez que `pedidos` se actualice

  

  const confirmarCambioEstado = async () => {
    try {
      // Paso 1: Obtener los estados disponibles (si es necesario)
      const responseEstados = await fetch("http://localhost:8080/estado/getAll");
      if (!responseEstados.ok) {
        throw new Error('Error al obtener los estados');
      }
      const estados = await responseEstados.json();
      console.log("Estados recibidos:", estados);
  
      // Paso 2: Filtrar el estado "Cancelar" del array de estados
      const estadosSinCancelar = estados.filter(estado => estado.codigo !== "CN");
      console.log("Estados después de filtrar 'Cancelar':", estadosSinCancelar);
  
      // Paso 3: Calcular el siguiente estado basado en el estado actual
      const estadoActual = codigoPedidoSeleccionado; // El estado actual del pedido
      const siguienteEstado = obtenerSiguienteEstado(estadoActual, estadosSinCancelar);
      if (!siguienteEstado) {
        throw new Error("No se puede calcular el siguiente estado");
      }
  
      
  
      // Paso 5: Enviar el pedido al backend para actualizar su estado
      const responseActualizar = await fetch(`http://localhost:8080/pedido/actualizarEstado/${siguienteEstado}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(pedidoSeleccionado), // Enviamos el pedido con el estado actualizado
      });
  
      if (!responseActualizar.ok) {
        throw new Error('Error al actualizar el estado del pedido');
      }
  
      const updatedPedido = await responseActualizar.json(); // Pedido actualizado
  
      // Actualizamos la lista de pedidos con el pedido actualizado
      setPedidos((prevPedidos) =>
        prevPedidos.map((pedido) =>
          pedido.id === updatedPedido.id ? updatedPedido : pedido
        )
      );
  
      setConfirmacionVisible(false);
    } catch (error) {
      console.error('Error al cambiar el estado del pedido:', error);
      window.location.reload();
    }
  };
  

  const obtenerSiguienteEstado = (estadoActual, estadosSinCancelar) => {
    console.log("Estado actual recibido:", estadoActual);
    console.log("Estados recibidos:", estadosSinCancelar);
    
    if (!estadoActual || !Array.isArray(estadosSinCancelar) || estadosSinCancelar.length === 0) {
      console.error("Datos insuficientes para calcular el siguiente estado.");
      
      return null;
    }
  
    const indiceActual = estadosSinCancelar.findIndex((estado) => estado.codigo === estadoActual);
  
    if (indiceActual === -1) {
      console.error(`Estado actual (${estadoActual}) no encontrado en la lista de estados.`);
      return null;
    }
  
    const siguienteIndice = indiceActual + 1;
    if (siguienteIndice < estadosSinCancelar.length) {
      return siguienteIndice < estadosSinCancelar.length
      ? estadosSinCancelar[siguienteIndice].codigo 
      : null ;
      
    }
  
    console.warn("El estado actual es el último en la lista, no hay un siguiente estado.");
    return null;
  };
  


  // Handle order update with confirmation
  const manejarActualizarPedido = (pedido) => {
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
  return (
    <div className="pedidos-container">
      <Cabecera />

      <div className="titulo-logo-container">
        <div className="titulo-logo">
          <svg xmlns="http://www.w3.org/2000/svg" width="50" height="50" fill="currentColor" className="logo-pedido" viewBox="0 0 16 16">
            <path fillRule="evenodd" d="M5 11.5a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5m0-4a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5m0-4a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5M3.854 2.146a.5.5 0 0 1 0 .708l-1.5 1.5a.5.5 0 0 1-.708 0l-.5-.5a.5.5 0 1 1 .708-.708L2 3.293l1.146-1.147a.5.5 0 0 1 .708 0m0 4a.5.5 0 0 1 0 .708l-1.5 1.5a.5.5 0 0 1-.708 0l-.5-.5a.5.5 0 1 1 .708-.708L2 7.293l1.146-1.147a.5.5 0 0 1 .708 0m0 4a.5.5 0 0 1 0 .708l-1.5 1.5a.5.5 0 0 1-.708 0l-.5-.5a.5.5 0 1 1 .708-.708l.146.147 1.146-1.147a.5.5 0 0 1 .708 0"/>
          </svg>
          <h2>PEDIDOS </h2>
        </div>
      </div>

      <table className="pedidos-tabla">
        <thead>
          <tr>
            <th>Número de Pedido</th>
            <th>Fecha de Solicitud</th>
            <th>Fecha de Entrega</th>
            <th>Usuario</th>
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
                <td>
                  {pedido.estado?.nombre?.toLowerCase() === "cancelado"
                    ? "-"
                    : pedido.fechaEntrega || "Pendiente"}
                </td>
                <td>{pedido.usuario.nombre}</td>
                <td>{pedido.estado ? pedido.estado.nombre : 'No disponible'}</td>
                <td>
                  <button className="detalles-btn-pedidosAdmin" onClick={() => verDetalles(pedido.id)}>
                    Detalle
                  </button>
                  {pedido.estado.codigo !== "ET" && pedido.estado.codigo !== "CN" && (
                    <button className="actualizar-btn-pedidosAdmin" onClick={() => manejarActualizarPedido(pedido)}>
                      Actualizar Estado
                    </button>
                  )}
                </td>
              </tr>
            ))
          ) : (
            <tr><td colSpan="6">No hay pedidos disponibles</td></tr>
          )}
        </tbody>
      </table>

      {confirmacionVisible && pedidoSeleccionado && pedidoSeleccionado.estado && (
        <div className="confirmacion-cambio">
          
          <p>
              ¿Está seguro que desea cambiar el estado del pedido con Numero de Pedido =  {pedidoSeleccionado.id}?
            

          </p>
          <div>
            <button className='confirmar-btn' onClick={confirmarCambioEstado}>Confirmar</button>
            <button className='cancelar-btn' onClick={() => setConfirmacionVisible(false)}>Cancelar</button>
          </div>
        </div>
      )}
    </div>
  );
};

export default PedidosAdmin;
