import React, { useState, useEffect } from "react";
import { Line } from "react-chartjs-2";

import DatePicker from "react-datepicker";  // Importar el componente DatePicker
import "react-datepicker/dist/react-datepicker.css";  // Importar los estilos de DatePicker
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";
import ChartDataLabels from 'chartjs-plugin-datalabels';  // Importar el plugin

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  ChartDataLabels  // Registrar el plugin
);

const Ventas = () => {
  
  const [ventasData, setVentasData] = useState([]);
  const [fechaDesde, setFechaDesde] = useState(new Date());  // Fecha desde
  const [fechaHasta, setFechaHasta] = useState(new Date());  // Fecha hasta
  const [pedidos, setPedido] = useState([]);
  const [fechas, setFechas] = useState([]);  // Nuevo estado para las fechas


  

  // Función para obtener los datos de ventas desde el backend
  const obtenerVentas = async () => {
    try {
      console.log("Fecha Desde:", formatDate(fechaDesde));
      console.log("Fecha Hasta:", formatDate(fechaHasta));
      const fechaDesdeFormateada = formatDate(fechaDesde);
      const fechaHastaFormateada = formatDate(fechaHasta);

      const response = await fetch(
        `http://localhost:8080/pedido/pedidoFiltrado?fechaDesde=${fechaDesdeFormateada}&fechaHasta=${fechaHastaFormateada}`, 
        { method: "GET", headers: { "Content-Type": "application/json" } }
      );
  
      if (!response.ok) {
        console.error("Error en la solicitud:", response.statusText);
        return;
      }
  
      const pedidos = await response.json();
      console.log("Pedidos recibidos:", pedidos); // Verifica los datos aquí
  
      // Procesar los datos
      const ventasFiltradas = pedidos.map((pedido) => pedido.precio);
      const fechas = pedidos.map((pedido) => pedido.fechaEntrega);
      if (ventasFiltradas.length === fechas.length) {
        setVentasData(ventasFiltradas);
        setFechas(fechas);
      } else {
        console.error("Error: las longitudes de ventas y fechas no coinciden.");
      }
      
      
    } catch (error) {
      console.error("Error al obtener las ventas:", error);
    }
  };
  
  useEffect(() => {
    console.log("Fechas para el gráfico:", fechas);
    console.log("Datos de ventas para el gráfico:", ventasData);
  }, [fechas, ventasData]);
  
  // Función para formatear la fecha en "YYYY-MM-DD"
    const formatDate = (date) => {
        const year = date.getFullYear();
        const month = (date.getMonth() + 1).toString().padStart(2, "0");
        const day = date.getDate().toString().padStart(2, "0");
        return `${year}-${month}-${day}`;
    };
  
  
  
  

  // Llamar a la función cuando el periodo o las fechas cambien
  useEffect(() => {
    obtenerVentas();
  }, [ fechaDesde, fechaHasta]);

  const chartData = {
    labels: fechas.length > 0 ? fechas : ["No hay datos"],  // Usamos las fechas dinámicas
    datasets: [
      {
        label: "Ventas Totales",
        data: ventasData.length > 0 ? ventasData : [0],  // Datos predeterminados si no hay ventas
        borderColor: "rgb(75, 192, 192)",
        backgroundColor: "rgba(75, 192, 192, 0.2)",
        fill: true,
      },
    ],
  };
  

  const options = {
    responsive: true,
    plugins: {
      legend: { position: "top" },
      title: { display: true, text: "Ventas por Periodo" },
      tooltip: {
        callbacks: {
          label: (tooltipItem) => {
            return `Ventas: ${tooltipItem.raw}`;
          },
        },
      },
      datalabels: {
        display: true,
        color: "black",
        align: "top",
        font: {
          weight: "bold",
        },
      },
    },
    maintainAspectRatio: false,
  };

  return (
    <div style={{ marginTop: "-90px", display: "flex", flexDirection: "column", alignItems: "center", marginBottom: "30px" }}>
      <h2>Ventas Totales</h2>
      
      {/* Selectores de Fecha */}
      <div style={{ display: "flex", gap: "10px", marginBottom: "20px" }}>
        <div>
          <label>Fecha Desde:</label>
          <DatePicker
            selected={fechaDesde}
            onChange={(date) => setFechaDesde(date)}
            dateFormat="yyyy-MM-dd"
          />
        </div>
        <div>
          <label>Fecha Hasta:</label>
          <DatePicker
            selected={fechaHasta}
            onChange={(date) => setFechaHasta(date)}
            dateFormat="yyyy-MM-dd"
          />
        </div>
      </div>


      

      <div style={{ width: "700px", height: "600px" }}>
        <Line data={chartData} options={options} />
      </div>
    </div>
  );
};

export default Ventas;
