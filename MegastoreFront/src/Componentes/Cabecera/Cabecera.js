import React, {useEffect} from 'react';
import LoginButton from '../BotonUsuario/BotonUsuario';
import BotonCarrito from '../BotonCarrito/BotonCarrito';
import { useNavigate,useLocation } from 'react-router-dom';
import './Cabecera.css';
import { useAuth } from '../Autenticacion/AuthContext';

const Cabecera = ({  carrito }) => {
  const navigate = useNavigate();
  const location = useLocation();
  const {user, isLoggedIn,hasRole,updateUser} = useAuth();

  const handleUserIconClick = () => {
    navigate('/login'); // Redirige a Login
  };

  const manejarCarrito = () => {
    if (location.pathname === '/pedido/carrito') {
      navigate('/catalogo');
      
    } else {
      navigate('/pedido/carrito', { state: { editable: true }, replace: true }); // Ir al carrito
    }
  };

  // Verificar si la ruta es '/catalogo'
  const mostrarCarrito = location.pathname === '/catalogo' || location.pathname === '/pedido/carrito';
  const noMostrarUsuario = location.pathname !== '/login';
 
     // useEffect para obtener el usuario actualizado
  useEffect(() => {
    const fetchUser = async () => {
      if (!user || !user.id) {
        console.warn("Usuario o ID no disponible, no se cargarán pedidos.");
        return;
      }

      if (isLoggedIn) {
        try {
          const response = await fetch(`http://localhost:8080/usuario/${user.id}`);
          const updatedUser = await response.json();
          if (updatedUser) {
            updateUser(updatedUser); // Actualiza el estado de user en el AuthContext
          }
        } catch (error) {
          console.error('Error al actualizar el usuario', error);
        }
      }
    };

    fetchUser();
  }, [isLoggedIn, user, updateUser]); // Ejecuta fetchUser cuando cambie isLoggedIn o user

  


  return (
    <header className="cabecera">
      {!isLoggedIn && noMostrarUsuario&&(  // Mostrar LoginButton solo si no está logueado
        <LoginButton onClick={handleUserIconClick} />
        
      )}
      {isLoggedIn && (  // Mostrar carrito solo si el usuario está logueado
        
              <div>
                {user.rol.nombre === 'usuario' && mostrarCarrito && (
                  <div className="carrito-icon-cabecera" onClick={manejarCarrito} style={{ cursor: 'pointer', display: 'inline-block', marginLeft: '20px' }}>
                    <BotonCarrito/> {Array.isArray(carrito) && carrito.length > 0 && <span>{carrito.length}</span>} {/* Muestra la cantidad de productos en el carrito */}
                  </div>
                )}
                  <div className="user-info-cabecera">
                    <LoginButton/>
                    <span>Hola, {user?.nombre }</span>
                  </div>
              </div>
            
          
      )}
    </header>
  );
};

export default Cabecera;
