/*
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './LoginAdmin.css'; // Archivo CSS para estilos, si lo necesitas
import Cabecera from '../Cabecera/Cabecera';

const LoginAdmin = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();

        /*
        // Verificación de las credenciales
        if (username === 'admin' && password === 'admin') {
            // Redirigir a la página de administración si es admin
            navigate('/admin');
        } else {
            // Mostrar error si las credenciales no son válidas
            setError('Usuario o contraseña incorrectos');
        }
    };
        const requestBody = {
            correo: username,
            contrasenia: password
        };

        try {
            // Enviar la solicitud al backend (cambia la URL por la correcta)
            const response = await fetch('http://localhost:8080/usuario/iniciarSesion', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(requestBody),
            });

            // Si la respuesta es exitosa, redirigir a /admin
            if (response.ok) {
                const userData = await response.json();
                if (userData && userData.rol === 'admin') {
                    navigate('/admin');
                } else {
                    setError('No tienes permiso de administrador');
                }
            } else {
                setError('Usuario o contraseña incorrectos');
            }
        } catch (error) {
            console.error('Error al iniciar sesión:', error);
            setError('Hubo un problema al conectar con el servidor');
        }
    };
        

    return (
        <div className="login-container">
             <div><Cabecera/></div>
             <svg xmlns="http://www.w3.org/2000/svg" width="70" height="70" ></svg>
             <svg xmlns="http://www.w3.org/2000/svg" width="70" height="70" fill="currentColor" class="bi bi-person" viewBox="0 0 16 16">
              <path d="M8 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6m2-3a2 2 0 1 1-4 0 2 2 0 0 1 4 0m4 8c0 1-1 1-1 1H3s-1 0-1-1 1-4 6-4 6 3 6 4m-1-.004c-.001-.246-.154-.986-.832-1.664C11.516 10.68 10.289 10 8 10s-3.516.68-4.168 1.332c-.678.678-.83 1.418-.832 1.664z"/>
      </svg>

            <h1>Iniciar Sesión</h1>
            <form onSubmit={handleLogin}>
                <div className="form-group">
                    <label htmlFor="username">Usuario</label>
                    <input
                        type="text"
                        id="username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="password">Contraseña</label>
                    <input
                        type="password"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                {error && <p className="error-message">{error}</p>}
                <button type="submit" className="login-button">Ingresar</button>
            </form>
        </div>
    );
};

export default LoginAdmin;
*/