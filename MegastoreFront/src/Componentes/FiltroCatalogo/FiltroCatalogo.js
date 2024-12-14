import React, { useState, useEffect } from 'react';
import './FiltroCatalogo.css';


const FiltroCatalogo = ({ onFilterChange }) => {
    
    const [categorias, setCategorias] = useState([]);
    const [variantes, setVariantes] = useState([]);
    const [productos, setProductos] = useState([]);
    const [sizes, setSizes] = useState([]);
    const [filters, setFilters] = useState({
        price: null,
        categories: null,
        size: null,
    });
    
        // Obtener variantes disponibles
    useEffect(() => {
        const fetchVariantes = async () => {
            try {
                const tipoVarianteCodigo = "T"; // Declarar correctamente la variable
                console.log('tipo variante codigo:', tipoVarianteCodigo)
                console.log(typeof tipoVarianteCodigo); // Muestra "string"
                const response = await fetch(`http://localhost:8080/varianteProducto/variantes/tipo-codigo?codigo=${tipoVarianteCodigo}`); // Usar backticks para interpolación
                if (response.ok) {
                    const varianteTalle = await response.json();

                    console.log('variantes:', varianteTalle);
                    setVariantes(varianteTalle); // Guardar las variantes
                } else {
                    alert('Error al obtener las variantes');
                }
            } catch (error) {
                console.error('Error al obtener variantes:', error);
            }
        };

        
        
        
        const fetchCategorias = async () => {
            try {
                const response = await fetch('http://localhost:8080/categoria/getAll');
                if (response.ok) {
                    const data = await response.json();
                    setCategorias(data);
                } else {
                    alert('Error al obtener las categorías');
                }
            } catch (error) {
                console.error('Error al obtener categorías:', error);
            }
        };

        // Llamar a las funciones dentro del useEffect
        fetchVariantes();
        fetchCategorias();
    }, []); 


       
    

     // Manejo de cambios en filtros
    const handleCategoryChange = (e) => {
        console.log('categoria Seleccionada', e);
        const category = e.target.value;
        console.log('categoria Seleccionada asignada', e);
        setFilters((prevFilters) => {
            console.log('prevFilters', prevFilters);

            const updatedFilters = { ...prevFilters, categoria: category };
            console.log('update Filters', updatedFilters);
            onFilterChange(updatedFilters); // Llama a la función de cambio de filtros
            return updatedFilters;
        });
    };

    const handlePriceChange = (e) => {
        const newPrice = e.target.value ? parseFloat(e.target.value) : null;
        setFilters((prevFilters) => {
          const updatedFilters = { ...prevFilters, precio: newPrice };
          onFilterChange(updatedFilters); // Llama a la función de cambio de filtros
          return updatedFilters;
        });
      };
      const handleSizeChange = (e) => {
        const size = e.target.value;
        setFilters((prevFilters) => {
          const updatedFilters = { ...prevFilters, talle: size };
          onFilterChange(updatedFilters); // Llama a la función de cambio de filtros
          return updatedFilters;
        });
      };

    

      const resetFilters = () => {
        const resetState = { precio: null, categoria: null, talle: null };
        setFilters(resetState);
        onFilterChange(resetState); // Restablece los filtros
      };

      const priceOptions = [10000, 20000, 30000, 40000, 50000, 60000, 70000, 80000, 90000, 100000]; // Precios sugeridos

    return (
        <div className="filter-container">
            <div className="filter-item">
                <label>Precio:</label> 
                <select className="price-select" value={filters.precio || ''} onChange={handlePriceChange}>
                    <option value="">Selecciona un precio</option>
                    {priceOptions.map((priceOption) => (
                        <option key={priceOption} value={priceOption}>
                        Hasta ${priceOption}
                        </option>
                    ))}
                    </select>
            
            </div>

            <div className="filter-item">
                <label>Talle:</label>
                <select value={filters.talle || ''} onChange={handleSizeChange}>
                <option value="">Selecciona un talle</option>
                {variantes.map((variante) => (
                    <option key={variante.nombre} value={variante.nombre}>
                    {variante.nombre}
                    </option>
                ))}
                </select>
            </div>

            <div className="filter-item">
                <label>Categoría:</label>
                <select value={filters.categoria || ''} onChange={handleCategoryChange}>
                <option value="">Selecciona una categoría</option>
                {categorias.map((categoria) => (
                    <option key={categoria.id} value={categoria.id}>
                    {categoria.nombre}
                    </option>
                ))}
                </select>
            </div>

            <button className="reset-filters-button" onClick={resetFilters}>
                Limpiar Filtros
            </button>
        </div>
    );
};

export default FiltroCatalogo;
