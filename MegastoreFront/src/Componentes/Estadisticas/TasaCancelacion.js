import React, { useState, useEffect } from "react";
import { Pie } from "react-chartjs-2";
import { Chart as ChartJS, ArcElement, Tooltip, Legend, Title, CategoryScale, LinearScale } from "chart.js";
import ChartDataLabels from "chartjs-plugin-datalabels"; // Asegúrate de importar correctamente el plugin
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";

// Registrar Chart.js y el plugin de datalabels
ChartJS.register(ArcElement, Tooltip, Legend, Title, CategoryScale, LinearScale, ChartDataLabels);

const TasaCancelacion = () => {
  const [tasaData, setTasaData] = useState(null);
  const [fechaDesde, setFechaDesde] = useState(new Date());
  const [fechaHasta, setFechaHasta] = useState(new Date());
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const formatDate = (date) => {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, "0");
    const day = date.getDate().toString().padStart(2, "0");
    return `${year}-${month}-${day}`;
  };

  const obtenerDatosTasaCancelacion = async () => {
    setLoading(true);
    setError(null);
    try {
      const fechaDesdeFormatted = formatDate(fechaDesde);
      const fechaHastaFormatted = formatDate(fechaHasta);

      const response = await fetch(
        `http://localhost:8080/pedido/tasaCancelacion?fechaDesde=${fechaDesdeFormatted}&fechaHasta=${fechaHastaFormatted}`
      );
      const data = await response.json();
      setTasaData(data);
    } catch (err) {
      setError("Error al obtener los datos");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    obtenerDatosTasaCancelacion();
  }, [fechaDesde, fechaHasta]);

  const hayDatos = tasaData && tasaData.entregados !== undefined && tasaData.cancelados !== undefined;

  const chartData = {
    labels: ["Pedidos Cancelados", "Pedidos Entregados"],
    datasets: [
      {
        data: tasaData ? [tasaData.cancelados || 0, tasaData.entregados - tasaData.cancelados || 0] : [0, 0],
        backgroundColor: ["rgba(255, 99, 132, 0.2)", "rgba(75, 192, 192, 0.2)"],
        borderColor: ["rgba(255, 99, 132, 1)", "rgba(75, 192, 192, 1)"],
        borderWidth: 1,
      },
    ],
  };

  const options = {
    responsive: true,
    plugins: {
      legend: {
        position: "top",
      },
      title: {
        display: true,
        text: "Tasa de Cancelación de Pedidos",
      },
      tooltip: {
        callbacks: {
          label: (context) => {
            const total = tasaData.entregados + tasaData.cancelados;
            const percentage = ((context.raw / total) * 100).toFixed(2);
            return `${context.label}: ${percentage}% (${context.raw} pedidos)`;
          },
        },
      },
      datalabels: {
        display: true,
        color: "#000",
        font: {
          weight: "bold",
        },
        formatter: (value, context) => {
            const total = tasaData.entregados + tasaData.cancelados;
            const percentage = ((value / total) * 100).toFixed(2);
            
            // Asegúrate de que el índice sea correcto
            if (context.dataIndex === 0) {
              // Si es el primer valor (Pedidos Cancelados)
              return `${percentage}%\n(${value} Pedidos Cancelados)`;
            } else {
              // Si es el segundo valor (Pedidos Entregados)
              return `${percentage}%\n(${value} Pedidos Entregados)`;
               }   }
      },
    },
    maintainAspectRatio: false,
  };

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }

  return (
    <div style={{ display: "flex", flexDirection: "column", alignItems: "center", marginBottom: "150px" }}>
      <h2>Tasa de Cancelación de Pedidos</h2>

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

      {hayDatos ? (
        <div style={{ width: "600px", height: "400px" }}>
          <Pie data={chartData} options={options} />
        </div>
      ) : (
        <div>No hay datos disponibles para el rango de fechas seleccionado.</div>
      )}
    </div>
  );
};

export default TasaCancelacion;
