import React, { useState} from 'react';
//import { useNavigate } from 'react-router-dom';
import Cabecera from '../Cabecera/Cabecera';
import './GestionarCatalogo.css'; // Archivo CSS para estilos
import '../Cabecera/Cabecera.css';

const ProductCard = ({ id, name, price, onEdit, onDelete }) => {
    return (
        <div className="product-card">
            
            <h5 className="product-name">{name}</h5>
            <p className="product-price">${price}</p>
            <button className="edit-button" onClick={() => onEdit({ id, name, price })}>
                Editar  
            </button>
            <button className="delete-button" onClick={() => onDelete(id)}>
                Eliminar
            </button>
        </div>
    );
};

const GestionarCatalogo = () => {

    const [productos, setProductos] = useState([
        { id: 1, name: 'Camisa', price: 29.99 },
        { id: 2, name: 'Pantalón', price: 49.99},
        { id: 3, name: 'Zapatos', price: 79.99},
        { id: 4, name: 'Chaqueta', price: 99.99},
        { id: 5, name: 'Camisa', price: 29.99},
        { id: 6, name: 'Pantalón', price: 49.99},
        { id: 7, name: 'Zapatos', price: 79.99 },
        { id: 8, name: 'Chaqueta', price: 99.99 },
    ]);

    //  actualizar un producto
    const handleUpdate = (updatedProduct) => {
        setProductos((prevProductos) =>
            prevProductos.map((product) =>
                product.id === updatedProduct.id ? updatedProduct : product
            )
        );
    };

    ///  gestionar edicion
    const handleEdit = (product) => {
        const newName = prompt("Editar nombre del producto:", product.name);
        const newPrice = parseFloat(prompt("Editar precio del producto:", product.price));
        if (newName && !isNaN(newPrice)) {
            const updatedProduct = { ...product, name: newName, price: newPrice };
            handleUpdate(updatedProduct);
        }
        else {
            alert("No tienes permiso para editar productos.");
        }
    };

    // Función para manejar la eliminación de un producto
    const handleDelete = (id) => {
        // Confirmación de eliminación
        if (window.confirm('¿Estás seguro de que deseas eliminar este producto?')) {
            setProductos(productos.filter((producto) => producto.id !== id));
            alert('Producto eliminado exitosamente');
        }
    };

    return (
        <div className="catalog-container">
           
               <Cabecera /> {/* Renderiza la cabecera aquí */} 
               <h2 className="catalog-title">Vista de administrador</h2> 
            
            <div className="filter-container">
                <button className="filter-button">
                    <div className='filter-content'>
                        <svg xmlns="http://www.w3.org/2000/svg" width="28" height="28" fill="currentColor" className="filtro-logo" viewBox="0 0 16 16">
                            <path d="M1.5 1.5A.5.5 0 0 1 2 1h12a.5.5 0 0 1 .5.5v2a.5.5 0 0 1-.128.334L10 8.692V13.5a.5.5 0 0 1-.342.474l-3 1A.5.5 0 0 1 6 14.5V8.692L1.628 3.834A.5.5 0 0 1 1.5 3.5zm1 .5v1.308l4.372 4.858A.5.5 0 0 1 7 8.5v5.306l2-.666V8.5a.5.5 0 0 1 .128-.334L13.5 3.308V2z" />
                        </svg>
                        <h3>FILTRAR</h3>
                    </div>
                </button>
            </div>

            <div className="grid-container">
                {productos.map(item => (
                    <ProductCard
                        key={item.id}
                        
                        name={item.name}
                        price={item.price}
                        onEdit={() => handleEdit(item)}
                        onDelete={() => handleDelete(item.id)}
                    />
                ))}
            </div>
            
        </div>
    );
};

export default GestionarCatalogo;