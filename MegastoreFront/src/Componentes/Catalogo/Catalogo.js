import React, { useState, useEffect } from 'react';
import { useAuth } from '../Autenticacion/AuthContext'; // Importar el contexto de autenticación
import Cabecera from '../Cabecera/Cabecera';
import FiltroCatalogo from '../FiltroCatalogo/FiltroCatalogo'; // Componente del filtro
import './Catalogo.css'; // Archivo CSS para estilos
import '../Cabecera/Cabecera.css';
import { navigate, useNavigate } from 'react-router-dom';



const Catalogo = () => {
    const { user,hasRole, carrito,addToCart,removeFromCart } = useAuth(); // Usar AuthContext para obtener carrito y metodos del contexto
   

    const [productos, setProductos] = useState([]);
    const [productosFiltrados, setProductosFiltrados] = useState([]);
    const [filters, setFilters] = useState({ price: null, size: null, categories: null }); // Filtros aplicados
    const [showAddForm, setShowAddForm] = useState(false);//muestra y oculta el form
    const [newProductData, setNewProductData] = useState({
        nombre:'',
        precio:'',
        stock:'',
        codigo:'',
        categoria:'',
        variantes: [], // Inicializar variantes como un array vacío
        foto: null,
    });
    const [categorias, setCategorias] = useState([]);  // Define el estado de categorías
    const [variantesBack, setVariantesBack] = useState([]);
    
    
    // Obtener productos desde la API al cargar el componente
    useEffect(() => {
        const fetchProductos = async () => {
            try {
                const response = await fetch('http://localhost:8080/producto/getAll');
                if (response.ok) {
                    const data = await response.json();
                    setProductos(data);
                    setProductosFiltrados(data);
                    console.log(" produto:", data)
                } else {
                    alert('Error al obtener los productos');
                }
            } catch (error) {
                console.error('Error al obtener productos:', error);
            }
        };
        fetchProductos();
        
         // Obtener variantes disponibles
        const fetchVariantes = async () => {
                try {
                    const response = await fetch('http://localhost:8080/varianteProducto/getAll');
                    if (response.ok) {
                        const data = await response.json();
                        console.log('variantes:', data);
                        setVariantesBack(data);  // Guardar las variantes
                        console.log('variantes:', variantesBack);
                    } else {
                        alert('Error al obtener las variantes');
                    }
                } catch (error) {
                    console.error('Error al obtener variantes:', error);
                }
            };

            fetchVariantes();
        
        
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

        fetchCategorias();
    }, []);
    
    const handleInputChange = (e) => {
        const { name, value } = e.target;
    
        setNewProductData((prevState) => ({
            ...prevState,
            [name]: value,
        }));
    };
    
    
    const handleInputChangeVar = (e, varianteId) => {
        const { checked } = e.target;
    
        setNewProductData((prevState) => {
            // Buscar el objeto variante completo en variantesBack
            const selectedVariant = variantesBack.find((variante) => variante.id === varianteId);
    
            // Si la variante es encontrada, agregarla o eliminarla de la lista de variantes completas
            const updatedVariants = checked
                ? [...prevState.variantes, selectedVariant] // Agregar el objeto variante completo
                : prevState.variantes.filter((variant) => variant.id !== varianteId); // Eliminar el objeto variante
    
            console.log("Variantes seleccionadas después de cambiar:", updatedVariants);
    
            return {
                ...prevState,
                variantes: updatedVariants, // Actualiza el estado con las variantes seleccionadas (como objetos completos)
            };
        });
    };
    
    
    
    
    
    

    const handleFileChange = (e) => {
        setNewProductData((prevState) => ({
            ...prevState,
            foto: e.target.files[0],
        }));
    };

    const handleFilterChange = (newFilters) => {
        setFilters(newFilters); // Actualiza el estado de los filtros
        filterProducts(newFilters); // Llama a la función de filtrado con los nuevos filtros
        console.log("Productos antes de filtrar:", productos);

    };

    useEffect(() => {
        console.log("Productos filtrados actualizados:", productosFiltrados);
    }, [productosFiltrados]); // Esto se ejecutará cada vez que productosFiltrados cambie

    const filterProducts = (newFilters) => {
        console.log('PRODUCTOS', productos);
        
        let filteredProducts = productos;
        
        console.log("Productos antes de aplicar filtros:", productos);
        console.log("Filtros actuales:", newFilters);
        
        

    
         // Filtrar por precio
        if (newFilters.precio) {
            filteredProducts = filteredProducts.filter(product => {
                console.log(`Producto precio: ${product.precio} <= ${newFilters.precio}`);
                return product.precio <= newFilters.precio;
            });
            console.log("Después de filtrar por precio:", filteredProducts);
        }
    
         // Filtrar por categoría
        if (newFilters.categoria) {
            filteredProducts = filteredProducts.filter(product => {
                const parsedIdCategoria = parseInt(newFilters.categoria);
                console.log(`Producto categoría: ${product.categoria.id} === ${parsedIdCategoria}`);
                console.log("Tipo de newFilters.categoria:", typeof parsedIdCategoria);
                console.log("Tipo de producto.categoria.id:", typeof product.categoria.id);
                console.log("newFilters.categoria:",  parsedIdCategoria);
                console.log("producto.categoria.id:",  product.categoria.id);
                
                return product.categoria.id === parsedIdCategoria;
                
            });
            console.log("Después de filtrar por categoría:", filteredProducts);
        }
    
        // Filtrar por talle
        if (newFilters.talle) {
            filteredProducts = filteredProducts.filter(product => 
                product.variantes.some(variante => 
                    variante.tipoVariante.codigo === 'T' && variante.nombre === newFilters.talle
                )
            );
        }

        console.log(' filteredProducts: ', filteredProducts);
        setProductosFiltrados(filteredProducts); // Actualiza los productos filtrados
        console.log('Productos Filtrados: ', productosFiltrados);
    };
    
    
    

    const handleEdit = async (id) => {
        const producto = productos.find((prod) => prod.id === id);
        const newName = prompt("Editar nombre del producto:", producto.nombre);
        const newPrice = parseFloat(prompt("Editar precio del producto:", producto.precio));

        if (newName && !isNaN(newPrice)) {
            const updatedProduct = { ...producto, nombre: newName, precio: newPrice };

            try {
                const response = await fetch(`http://localhost:8080/producto/${id}`, {
                    method: 'PUT',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(updatedProduct),
                });

                if (response.ok) {
                    setProductos((prev) =>
                        prev.map((prod) => (prod.id === id ? updatedProduct : prod))
                    );
                    alert('Producto actualizado exitosamente');
                } else {
                    alert('Error al actualizar el producto');
                }
            } catch (error) {
                console.error('Error al actualizar producto:', error);
            }
        } else {
            alert('Datos inválidos para editar.');
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm('¿Estás seguro de que deseas eliminar este producto?')) {
            try {
                const response = await fetch(`http://localhost:8080/producto/${id}`, {
                    method: 'DELETE',
                });

                if (response.ok) {
                    setProductos((prev) => prev.filter((prod) => prod.id !== id));
                    alert('Producto eliminado exitosamente.');
                } else {
                    alert('Error al eliminar el producto');
                }
            } catch (error) {
                console.error('Error al eliminar producto:', error);
            }
        }
    };

    const handleAdd = async (e) => {
        e.preventDefault();
       
        const { nombre, precio, stock, codigo, categoria, foto, variantes } = newProductData;

       // Log para verificar el estado
        console.log("Variantes seleccionadas:", variantes); 
        if (nombre && !isNaN(precio) && !isNaN(stock) && codigo && categoria) {
            // Filtrar las variantes seleccionadas por su ID
            
             
            const newProduct = new FormData();
            newProduct.append('nombre', nombre);
            newProduct.append('precio', precio);
            newProduct.append('stock', stock);
            newProduct.append('codigo', codigo);
            newProduct.append('categoria', categoria);
            if (foto) {
                newProduct.append('foto', foto);
            }
            // Incluir variantes seleccionadas
            newProduct.append('variantes', JSON.stringify(variantes)); // Serializar variantes
            console.log('FormData contenido:');
            newProduct.forEach((value, key) => {
                console.log(`${key}: ${value}`);
            });
            try {
                const response = await fetch('http://localhost:8080/producto/insert', {
                    method: 'POST',
                    body: newProduct,
                });

                if (response.ok) {
                    const createdProduct = await response.json();
                    setProductos((prev) => [...prev, createdProduct]);
                    alert('Producto agregado exitosamente');
                    setShowAddForm(false); // Ocultar el formulario después de agregar
                   window.location.reload()

                    // Limpiar el formulario
                    setNewProductData({
                        nombre: '',
                        precio: '',
                        stock: '',
                        codigo: '',
                        categoria: '',
                        foto: null,
                    });
                    setVariantesBack([]); // Limpiar las variantes seleccionadas
                } else {
                    alert('Error al agregar el producto');
                }
            } catch (error) {
                console.error('Error al agregar producto:', error);
            }
        } else {
            alert('Datos inválidos para agregar producto.');
        }
    };
    const handleAddToCart = (producto) => {
        addToCart(producto);
        console.log(`${producto.nombre} agregado al carrito`);
      };
    const handleRemoveFromCart = (productoId) =>{
        removeFromCart(productoId);
        console.log('producto con id:', {productoId}, 'eliminado del carrito')
    }
      
    // Verificar si un producto ya está en el carrito
    const isProductInCart = (productoId) => {
        if (!carrito || !Array.isArray(carrito)){
            console.error('Carrito no esta disponible o no es un array:', carrito);
            return false;
        }
        return carrito.some(item => item.id === productoId);
    };
      
   
      

    
    

    return (
        <div className="catalog-container">
            <Cabecera />
            <h2>{(hasRole('admin')) ? 'Gestión de Catálogo' : 'Catálogo de Productos'}</h2>
            
            <FiltroCatalogo onFilterChange={handleFilterChange}  filters={filters} />

            {(hasRole('admin') || (user?.rol?.nombre === 'admin')) && (
                <button
                    className={`add-button-catalogo ${showAddForm ? 'cancel' : ''}`} // Agregar 'cancel' cuando showAddForm sea true
                    onClick={() => setShowAddForm(!showAddForm)}
                >
                    {showAddForm ? 'Cancelar' : 'Agregar Producto'}
              </button>
            )}

            {showAddForm && (hasRole('admin')||user.rol.nombre==='admin') && (
                <form onSubmit={handleAdd} encType="multipart/form-data" className="add-product-form">
                    <label htmlFor="nombre">Nombre:</label>
                    <input
                        type="text"
                        id="nombre"
                        name="nombre"
                        value={newProductData.nombre}
                        onChange={handleInputChange}
                        required
                    />

                    <label htmlFor="precio">Precio:</label>
                    <input
                        type="number"
                        id="precio"
                        name="precio"
                        value={newProductData.precio}
                        onChange={handleInputChange}
                        required
                    />

                    <label htmlFor="stock">Stock:</label>
                    <input
                        type="number"
                        id="stock"
                        name="stock"
                        value={newProductData.stock}
                        onChange={handleInputChange}
                        required
                    />

                    <label htmlFor="codigo">Código:</label>
                    <input
                        type="text"
                        id="codigo"
                        name="codigo"
                        value={newProductData.codigo}
                        onChange={handleInputChange}
                        required
                    />

                    <label htmlFor="categoria">Categoría:</label>
                    <select
                        id="categoria"
                        name="categoria"
                        value={newProductData.categoria}
                        onChange={handleInputChange}
                        required
                    >
                        <option value="">Seleccione una categoría</option>
                        {categorias.map((categoria) => (
                            <option key={categoria.id} value={categoria.id}>
                                {categoria.nombre}
                            </option>
                        ))}
                    </select>
                         {/* Mostrar variantes */}
                    {/* Mostrar variantes de tipo color debajo del precio */}
                    <label htmlFor="colores">Colores:</label>
                    <div className="variantes-container">
                        {variantesBack.filter((variante) => variante.tipoVariante.codigo === 'C').map((variante) => (
                            <div key={variante.id} className="variantes-item">
                                <input
                                    type="checkbox"
                                    id={`variante-${variante.id}`}
                                    name="variantes"
                                    value={variante.id}
                                    checked={newProductData.variantes.some((v) => v.id === variante.id)} // Compara si la variante está seleccionada

                                    onChange={(e) => {
                                        handleInputChangeVar(e, variante.id);
                                        console.log("Variantes seleccionadas después de cambiar:", newProductData.variantes); // Log aquí
                                    }}
                                />
                                <label htmlFor={`variante-${variante.id}`}>{variante.nombre}</label>
                            </div>
                        ))}
                    </div>

                    {/* Mostrar variantes de tipo talle debajo de las variantes de color */}
                    <label htmlFor="talles">Talles:</label>
                    <div className="variantes-container">
                        {variantesBack.filter((variante) => variante.tipoVariante.codigo === 'T').map((variante) => (
                            <div key={variante.id} className="variantes-item">
                                <input
                                    type="checkbox"
                                    id={`variante-${variante.id}`}
                                    name="variantes"
                                    value={variante.id}
                                    checked={newProductData.variantes.some((v) => v.id === variante.id)} // Compara si la variante está seleccionada

                                    onChange={(e) => {
                                        handleInputChangeVar(e, variante.id);
                                        console.log("Variantes seleccionadas después de cambiar:", newProductData.variantes); // Log aquí
                                    }}
                                />
                                <label htmlFor={`variante-${variante.id}`}>{variante.nombre}</label>
                            </div>
                        ))}
                    </div>


                    <label htmlFor="foto">Foto:</label>
                    <input
                        type="file"
                        id="foto"
                        name="foto"
                        onChange={handleFileChange}
                    />

                    <button className="add-button-catalogo2" type="submit">Agregar </button>
                </form>
            )}

            <div className="grid-container">
                
                {productosFiltrados.map((item) => (
                    <div key={item.id} className="product-card">
                        {/* Verifica si la foto está presente y es Base64, si no, usa una imagen predeterminada */}
                        {item.foto ? (
                            <img src={`data:image/jpeg;base64,${item.foto}`} alt={item.nombre} className="product-image" />
                        ) : (
                            <img src="ruta-a-imagen-por-defecto.jpg" alt="Imagen no disponible" className="product-image" />
                        )}
                        <h5 className="product-name">{item.nombre}</h5>
                        
                        <p className="product-price">${item.precio}</p>
                        
                         
                        
                        <div className="product-variantes">
                            {/* Desplegable para variantes de tipo 'Color' */}
                            {item.variantes.some(variante => variante.tipoVariante.codigo === 'C') && (
                                <div className="variantes-dropdown">
                                    <label htmlFor="color">Color</label>
                                    <select id="color" className="variantes-select">
                                        <option value="">Selecciona un color</option>
                                        {item.variantes
                                            .filter(variante => variante.tipoVariante.codigo === 'C')
                                            .map((variante) => (
                                                <option key={variante.id} value={variante.id}>
                                                    {variante.nombre}
                                                </option>
                                            ))}
                                    </select>
                                </div>
                            )}

                            {/* Desplegable para variantes de tipo 'Talle' */}
                            {item.variantes.some(variante => variante.tipoVariante.codigo === 'T') && (
                                <div className="variantes-dropdown">
                                    <label htmlFor="talle">Talle</label>
                                    <select id="talle" className="variantes-select">
                                        <option value="">Selecciona un talle</option>
                                        {item.variantes
                                            .filter(variante => variante.tipoVariante.codigo === 'T')
                                            .map((variante) => (
                                                <option key={variante.id} value={variante.id}>
                                                    {variante.nombre}
                                                </option>
                                            ))}
                                    </select>
                                </div>
                            )}
                        </div>

                        
                        {(hasRole('admin')||user.rol.nombre==='admin') &&( // Si el rol es admin, muestra los botones
                            <td>
                                <p className="product-stock">Stock: {item.stock}</p> {/* Muestra el stock */}
                                <button className="editar-btn-catalogo" onClick={() => handleEdit(item.id)}>Editar</button>
                                <button className="eliminar-btn-catalogo" onClick={() => handleDelete(item.id)}>Eliminar</button>
                            </td>
                        )}
                        {!(hasRole('admin')||user.rol.nombre==='admin') && ( 
                            <td>
                                 {isProductInCart(item.id) ? (
                                        
                                    <button className="eliminar-btn-carrito" onClick={() => handleRemoveFromCart(item.id)}>
                                        <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" fill="currentColor" class="bi bi-cart-dash-fill" viewBox="0 0 16 16">
                                            <path d="M.5 1a.5.5 0 0 0 0 1h1.11l.401 1.607 1.498 7.985A.5.5 0 0 0 4 12h1a2 2 0 1 0 0 4 2 2 0 0 0 0-4h7a2 2 0 1 0 0 4 2 2 0 0 0 0-4h1a.5.5 0 0 0 .491-.408l1.5-8A.5.5 0 0 0 14.5 3H2.89l-.405-1.621A.5.5 0 0 0 2 1zM6 14a1 1 0 1 1-2 0 1 1 0 0 1 2 0m7 0a1 1 0 1 1-2 0 1 1 0 0 1 2 0M6.5 7h4a.5.5 0 0 1 0 1h-4a.5.5 0 0 1 0-1"/>
                                        </svg>
                                    </button>
                                ) : (

                                 
                                        <button className="agregar-btn-carrito" onClick={() => handleAddToCart(item)}>
                                            <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" fill="currentColor" class="bi bi-cart-plus" viewBox="0 0 16 16">
                                                <path d="M9 5.5a.5.5 0 0 0-1 0V7H6.5a.5.5 0 0 0 0 1H8v1.5a.5.5 0 0 0 1 0V8h1.5a.5.5 0 0 0 0-1H9z"/>
                                                <path d="M.5 1a.5.5 0 0 0 0 1h1.11l.401 1.607 1.498 7.985A.5.5 0 0 0 4 12h1a2 2 0 1 0 0 4 2 2 0 0 0 0-4h7a2 2 0 1 0 0 4 2 2 0 0 0 0-4h1a.5.5 0 0 0 .491-.408l1.5-8A.5.5 0 0 0 14.5 3H2.89l-.405-1.621A.5.5 0 0 0 2 1zm3.915 10L3.102 4h10.796l-1.313 7zM6 14a1 1 0 1 1-2 0 1 1 0 0 1 2 0m7 0a1 1 0 1 1-2 0 1 1 0 0 1 2 0"/>
                                            </svg>
                                    </button>

)}
                            </td>
                        )}
                        
                    </div>
                ))}
            </div>
        </div>
    );
};

export default Catalogo;
