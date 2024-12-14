import React, { useRef, useState } from "react";
import html2pdf from "html2pdf.js"; // Importamos la librería para exportar a PDF
import Ventas from "./ventas";
import TasaCancelacion from "./TasaCancelacion";
import CategoriasMasVendidas from "./CategoriasMasVendidas";
import Cabecera from "../Cabecera/Cabecera";

const EstadisticasHome = () => {
  const chartsRef = useRef(null); // Referencia para el contenedor que envuelve los gráficos
  const exportButtonRef = useRef(null); // Referencia para el botón de exportación
  const [exporting, setExporting] = useState(false); // Estado para controlar si estamos exportando

  // Función para exportar todos los gráficos a PDF
  const exportToPDF = () => {
    if (exportButtonRef.current) {
      exportButtonRef.current.style.display = "none"; // Ocultar el botón durante la exportación
    }
  
    const element = chartsRef.current;
  
    if (!element) {
      console.error("El contenedor de gráficos no existe.");
      return;
    }
  
    // Guardar estilos originales
    const originalStyles = Array.from(element.children).map((child) => ({
      pageBreakBefore: child.style.pageBreakBefore,
      pageBreakAfter: child.style.pageBreakAfter,
    }));
  
    // Eliminar saltos de página innecesarios
    Array.from(element.children).forEach((child, index) => {
      // Solo agregar salto de página antes del segundo gráfico y después del primero
      child.style.pageBreakBefore = index === 0 ? "auto" : "always";
      child.style.pageBreakAfter = "auto"; // Quitar el salto de página innecesario después de cada gráfico
    });
  
    // Opciones para la exportación
    const opt = {
      margin: [0.5, 0.10, 0.5, 0.5], // Márgenes ajustados para evitar márgenes grandes
      filename: "estadisticas_completas.pdf",
      image: { type: "jpeg", quality: 0.98 },
      html2canvas: { 
        scale: 2, 
        useCORS: true, 
        letterRendering: true,
      },
      jsPDF: { 
        unit: "in", 
        format: "letter", 
        orientation: "portrait",
        margin: [0.25, 0.25, 0.25, 0.25], // Márgenes más pequeños para evitar espacios vacíos innecesarios
      },
    };
  
    // Generar PDF
    html2pdf()
      .from(element)
      .set(opt)
      .toPdf()
      .get("pdf")
      .then((pdf) => {
        console.log("El PDF se generó correctamente.");
      })
      .save()
      .catch((err) => {
        console.error("Error durante la generación del PDF:", err);
      })
      .finally(() => {
        // Restaurar el botón
        if (exportButtonRef.current) {
          exportButtonRef.current.style.display = "block";
        }
  
        // Restaurar estilos originales
        Array.from(element.children).forEach((child, index) => {
          child.style.pageBreakBefore = originalStyles[index].pageBreakBefore;
          child.style.pageBreakAfter = originalStyles[index].pageBreakAfter;
        });
      });
  };
  
  
  

  
  
  

  return (
    <div style={{ paddingTop: "120px" }}> {/* Espaciado para la cabecera */}
      <Cabecera />

      {/* Botón para exportar todos los gráficos */}
      <button
        onClick={exportToPDF}
        ref={exportButtonRef} // Referencia al botón
        style={{
          padding: "10px 20px",
          cursor: "pointer",
          marginBottom: "20px",
          display: "block",
          marginLeft: "680px",
          marginRight: "auto",
          backgroundColor: "#4CAF50", // Color verde
          color: "white",
          fontSize: "16px",
          border: "none",
          borderRadius: "5px",
          zIndex: 10, // Asegura que el botón quede visible
          width: "200px", // Tamaño fijo para el botón
        }}
      >
        Exportar Todos los Gráficos a PDF
      </button>

      {/* Contenedor de todos los gráficos */}
      <div
        ref={chartsRef} // Asignamos la referencia al contenedor
        style={{
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          width: "100%",
          padding: "20px",
          paddingTop: exporting ? "0px" : "120px", // Ajuste del padding durante la exportación
          marginTop: exporting ? "-20px" : "0", // Ajuste del margen superior durante la exportación
        }}
      >
        <Ventas />
        <TasaCancelacion />
        <CategoriasMasVendidas />
      </div>
    </div>
  );
};

export default EstadisticasHome;
