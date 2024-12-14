// src/Login.js
import React, { useState } from 'react'; // Importa useState
import "./Login.css";
import { Link, useNavigate } from 'react-router-dom';
import Cabecera from '../Cabecera/Cabecera';
import { useAuth } from '../Autenticacion/AuthContext';

const Login = ({ setIsLoggedIn }) => { // Acepta setIsLoggedIn como prop
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate(); // Para redirigir después de iniciar sesión
  const [errorMessage] = useState(''); // Para manejar errores de inicio de sesión
  const { login } = useAuth(); // Método del contexto para manejar login

  const handleSubmit = async (e) => {
    e.preventDefault(); // Evita el comportamiento por defecto del formulario
  
    if (username && password) {
      try {
        const response = await fetch('http://localhost:8080/usuario/iniciarSesion', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            correo: username,      // Cambia esto si el campo de correo es diferente
            contrasenia: password, // Cambia esto si el campo de contraseña es diferente
          }),
        });
  
        if (response.status === 401) {
          throw new Error('Usuario o contraseña incorrecta'); // Maneja el error de autenticación
        }
  
        if (!response.ok) {
          throw new Error('Error en la autenticación');
        }
        const rawResponse = await response.text(); // Extraer el contenido del body como texto

        // Si el body está anidado en un JSON, parsea el JSON primero
        const parsedResponse = JSON.parse(rawResponse);
        const data = parsedResponse.body; // Accede a la propiedad 'body'


        console.log( 'respuesta del cback en el front:',data); // Verifica qué datos estás recibiendo


         // Usa URLSearchParams para procesar el contenido del body
        const params = new URLSearchParams(data.trim());
        const rol = params.get('rol');
        const nombre = params.get('nombre');
        const apellido = params.get('apellido');
        const id = params.get('id');

        
        console.log('Rol:', rol, 'Nombre:', nombre, 'Apellido:', apellido, 'id:', id);


        login(rol, nombre, apellido, id);

      // Redirigir según el rol
      if (rol === 'admin') {
        navigate('/');
      } else if (rol === 'usuario') {
        navigate('/');
      } else {
        alert('Rol desconocido');
      }
          
    } catch (error) {
      alert(error.message);
    }
  } else {
    alert('Por favor ingresa tus credenciales.');
  }
    
  };
  

  return (
    <div className='login-container'> 
      <div><Cabecera /></div>
      <svg xmlns="http://www.w3.org/2000/svg" width="70" height="70" fill="currentColor" className="bi bi-person" viewBox="0 0 16 16">
        <path d="M8 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6m2-3a2 2 0 1 1-4 0 2 2 0 0 1 4 0m4 8c0 1-1 1-1 1H3s-1 0-1-1 1-4 6-4 6 3 6 4m-1-.004c-.001-.246-.154-.986-.832-1.664C11.516 10.68 10.289 10 8 10s-3.516.68-4.168 1.332c-.678.678-.83 1.418-.832 1.664z"/>
      </svg>
      <h1>INICIAR SESIÓN</h1> 

      <form onSubmit={handleSubmit}> {/* Añade onSubmit al formulario */}
        <div className='input-group-login'>
          <div>
            <label htmlFor="username">Usuario:</label>
            <input 
              type="text" 
              id="username" 
              required 
              value={username} 
              onChange={(e) => setUsername(e.target.value)} // Controla el estado del input
            />
          </div>
          <div>
            <label htmlFor="password">Contraseña:</label>
            <input 
              type="password" 
              id="password" 
              required 
              value={password} 
              onChange={(e) => setPassword(e.target.value)} // Controla el estado del input
            />
          </div>
        </div>
        <button type="submit">Ingresar</button>
      </form>
      {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>} {/* Mostrar mensaje de error */}
      <p className='register-prompt'>¿No tienes una cuenta? <Link to="/registro">¡Regístrate ahora!</Link></p>
    </div>
  );
};

export default Login;
