import React, { createContext, useContext, useState, useEffect } from 'react';

const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({ children }) => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [user, setUser] = useState(null);
  const [carrito, setCarrito] = useState([]);
  const updateUser = (newUser) => {
    setUser(newUser); // Actualiza el usuario con los nuevos datos
  };
  // Restaurar sesión y carrito desde localStorage
  useEffect(() => {
    const session = localStorage.getItem('isLoggedIn');
    const savedUser = localStorage.getItem('user');
    const savedCarrito = localStorage.getItem('carrito');

    if (session === 'true' && savedUser) {
      
      setIsLoggedIn(true);
      setUser(JSON.parse(savedUser));
    }

    if (savedCarrito) {
      try {
        setCarrito(JSON.parse(savedCarrito));
      } catch (error) {
        console.error("Error al parsear el carrito desde localStorage:", error);
        setCarrito([]); // En caso de error, inicializar el carrito vacío
      }
    }
  }, []);

  // Persistir carrito en localStorage
  useEffect(() => {
    localStorage.setItem('carrito', JSON.stringify(carrito));
  }, [carrito]);

  const login = (rol, nombre, apellido,id) => {
    setIsLoggedIn(true);
    const userData = { rol, nombre, apellido,id };
    setUser(userData);
    localStorage.setItem('isLoggedIn', 'true');
    localStorage.setItem('user', JSON.stringify(userData));
  };

  const logout = () => {
    setIsLoggedIn(false);
    setUser(null);
    localStorage.removeItem('isLoggedIn');
    localStorage.removeItem('user');
    setCarrito([]); // Limpiar carrito al cerrar sesión
  };

  const hasRole = (role) => user?.rol === role;

  const addToCart = (producto) => {
    setCarrito((prevCarrito) => {
      const productoExistente = prevCarrito.find((item) => item.id === producto.id);

      if (productoExistente) {
        return prevCarrito.map((item) =>
          item.id === producto.id ? { ...item, cantidad: item.cantidad + 1 } : item
        );
      } else {
        return [...prevCarrito, { ...producto, cantidad: 1 }];
      }
    });
  };

  const removeFromCart = (productoId) => {
    setCarrito((prevCarrito) => prevCarrito.filter((item) => item.id !== productoId));
  };

  const updateProductQuantity = (productoId, cantidad) => {
    setCarrito((prevCarrito) =>{
      if (cantidad === 0) {
        return prevCarrito.filter((item) => item.id !== productoId);
      }
      return prevCarrito.map((item) =>
        item.id === productoId ? { ...item, cantidad } : item
      )
  });
  };

  return (
    <AuthContext.Provider value={{
      isLoggedIn,
      user,
      login,
      logout,
      hasRole,
      carrito,
      setCarrito,
      addToCart,
      removeFromCart,
      updateProductQuantity,
      updateUser
    }}>
      {children}
    </AuthContext.Provider>
  );
};
