import React, { useState, useEffect } from "react";
import { Bar } from "react-chartjs-2";
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend } from "chart.js";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

// Función para formatear la fecha en "YYYY-MM-DD"
const formatDate = (date) => {
  const year = date.getFullYear();
  const month = (date.getMonth() + 1).toString().padStart(2, "0");
  const day = date.getDate().toString().padStart(2, "0");
  return `${year}-${month}-${day}`;
};

const CategoriasMasVendidas = () => {
  const [fechaDesde, setFechaDesde] = useState(new Date());
  const [fechaHasta, setFechaHasta] = useState(new Date());
  const [categorias, setCategorias] = useState([]);
  const [ventas, setVentas] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const fetchData = async () => {
    if (fechaDesde > fechaHasta) {
      setError("La fecha desde no puede ser mayor que la fecha hasta.");
      return;
    }
    setLoading(true);
    setError("");
    try {
        console.log("Fecha Desde:", formatDate(fechaDesde));
        console.log("Fecha Hasta:", formatDate(fechaHasta));
        const fechaDesdeFormateada = formatDate(fechaDesde);
        const fechaHastaFormateada = formatDate(fechaHasta);
  
        const response = await fetch(
          `http://localhost:8080/pedido/categoriasMasVendidas?fechaDesde=${fechaDesdeFormateada}&fechaHasta=${fechaHastaFormateada}`, 
          { method: "GET", headers: { "Content-Type": "application/json" } }
        );
    
        if (!response.ok) {
          console.error("Error en la solicitud:", response.statusText);
          return;
        }
    
      const data = await response.json();
      const nombresCategorias = data.map((item) => item.categoria);
      const cantidades = data.map((item) => item.cantidad);
      setCategorias(nombresCategorias);
      setVentas(cantidades);
    } catch (err) {
      setError("Error al cargar los datos. Intenta nuevamente.");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, [fechaDesde, fechaHasta]);

  const chartData = {
    labels: categorias,
    datasets: [
      {
        label: "Ventas por Categoría",
        data: ventas,
        backgroundColor: "rgba(75, 192, 192, 0.2)",
        borderColor: "rgba(75, 192, 192, 1)",
        borderWidth: 1,
      },
    ],
  };

  const options = {
    responsive: true,
    plugins: {
      legend: { position: "top" },
      title: { display: true, text: "Categorías Más Vendidas" },
    },
    maintainAspectRatio: false,
  };

  return (
    <div style={{ marginBottom: "30px", width: "100%", display: "flex", flexDirection: "column", alignItems: "center" }}>
      <h2>Categorías Más Vendidas</h2>

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

      {/* Mensajes de Error */}
      {error && <p style={{ color: "red" }}>{error}</p>}

      {/* Gráfico de Barras */}
      {loading ? (
        <p>Cargando datos...</p>
      ) : (
        <div
          id="chartContainer"
          style={{ width: "700px", height: "500px", display: "flex", flexDirection: "column", alignItems: "center" }}
        >
          <Bar data={chartData} options={options} />
        </div>
      )}
    </div>
  );
};

export default CategoriasMasVendidas;
