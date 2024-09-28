import React, { useState } from 'react';
import Cabecera from '../Cabecera/Cabecera';
import FiltroCatalogo from '../FiltroCatalogo/FiltroCatalogo';
import './Catalogo.css'; // Archivo CSS para estilos
import '../Cabecera/Cabecera.css';

const ProductCard = ({ image, name, price }) => {
    return (
        <div className="product-card">
            <img src={image} alt={name} className="product-image" />
            <h5 className="product-name">{name}</h5>
            <p className="product-price">${price}</p>
        </div>
    );
};

const Catalogo = () => {
    const [filteredItems, setFilteredItems] = useState([]);
    const [filters, setFilters] = useState({ price: null, colors: [], categories: [] });

    const clothingItems = [
        { id: 1, name: 'Camisa', price: 29.99, image: 'https://via.placeholder.com/150', color: 'Rojo', category: 'Camisa' },
        { id: 2, name: 'Pantalón', price: 49.99, image: 'https://via.placeholder.com/150', color: 'Azul', category: 'Pantalón' },
        { id: 3, name: 'Zapatos', price: 79.99, image: 'https://via.placeholder.com/150', color: 'Negro', category: 'Zapatos' },
        { id: 4, name: 'Chaqueta', price: 99.99, image: 'https://via.placeholder.com/150', color: 'Verde', category: 'Chaqueta' },
        { id: 5, name: 'Camisa', price: 29.99, image: 'https://via.placeholder.com/150', color: 'Blanco', category: 'Camisa' },
        { id: 6, name: 'Pantalón', price: 49.99, image: 'https://via.placeholder.com/150', color: 'Negro', category: 'Pantalón' },
        { id: 7, name: 'Zapatos', price: 79.99, image: 'https://via.placeholder.com/150', color: 'Rojo', category: 'Zapatos' },
        { id: 8, name: 'Chaqueta', price: 99.99, image: 'https://via.placeholder.com/150', color: 'Azul', category: 'Chaqueta' },
        { id: 9, name: 'Camisa', price: 29.99, image: 'https://via.placeholder.com/150', color: 'Rojo', category: 'Camisa' },
        { id: 10, name: 'Pantalón', price: 49.99, image: 'https://via.placeholder.com/150', color: 'Blanco', category: 'Pantalón' },
        { id: 11, name: 'Zapatos', price: 79.99, image: 'https://via.placeholder.com/150', color: 'Verde', category: 'Zapatos' },
        { id: 12, name: 'Chaqueta', price: 99.99, image: 'https://via.placeholder.com/150', color: 'Negro', category: 'Chaqueta' }
    ];

    const handleFilterChange = (newFilters) => {
        setFilters(newFilters);
        applyFilters(newFilters);
    };

    const applyFilters = (newFilters) => {
        let filtered = clothingItems;

        const { price, colors, categories } = newFilters;

        if (price) {
            filtered = filtered.filter(item => item.price <= price);
        }

        if (colors.length > 0) {
            filtered = filtered.filter(item => colors.includes(item.color));
        }

        if (categories.length > 0) {
            filtered = filtered.filter(item => categories.includes(item.category));
        }

        setFilteredItems(filtered);
        console.log('Productos Filtrados:', filtered); // Depuración
    };

    const resetFilters = () => {
        setFilters({ price: null, colors: [], categories: [] });
        setFilteredItems([]); // Resetea los filtros
    };

    // Determina qué elementos mostrar: filtrados o el mensaje de no encontrado
    const itemsToDisplay = filteredItems.length > 0 ? filteredItems : clothingItems; // Muestra todos si no hay filtrados

    return (
        <div className="catalog-container">
            <Cabecera />
            <FiltroCatalogo onFilterChange={handleFilterChange} resetFilters={resetFilters} filters={filters} />
            
            <div className="grid-container">
                {itemsToDisplay.length > 0 ? (
                    itemsToDisplay.map(item => (
                        <ProductCard
                            key={item.id}
                            image={item.image}
                            name={item.name}
                            price={item.price}
                        />
                    ))
                ) : (
                    <p>Producto no encontrado</p>
                )}
            </div>
        </div>
    );
};

export default Catalogo;