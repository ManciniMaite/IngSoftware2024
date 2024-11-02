// src/Menu.js
import React from 'react';
import { Link } from 'react-router-dom';
import './Menu.css'; // Archivo CSS para estilos del menú
import { useEffect } from 'react';

function Menu({ isVisible, toggleSidebar }) {

    useEffect(() => {
        if (isVisible) {
            document.body.style.overflow = 'hidden'; // Deshabilita el desplazamiento
        } else {
            document.body.style.overflow = 'auto';   // Habilita el desplazamiento nuevamente
        }

        // Limpieza para restaurar el overflow cuando el componente se desmonte
        return () => {
            document.body.style.overflow = 'auto';   // Asegurarse de restaurar el overflow
        };
    }, [isVisible]);

    return (
        <div className={`sidebar ${isVisible ? 'visible' : 'hidden'}`}>
            <button
                className={`toggle-button`}
                onClick={toggleSidebar}
                aria-expanded={isVisible}
            >
                <svg className='menu-logo' xmlns="http://www.w3.org/2000/svg" width="30" height="30" fill="currentColor" viewBox="0 0 16 16">
                    <path fillRule="evenodd" d="M2.5 12a.5.5 0 0 1 .5-.5h10a.5.5 0 0 1 0 1H3a.5.5 0 0 1-.5-.5m0-4a.5.5 0 0 1 .5-.5h10a.5.5 0 0 1 0 1H3a.5.5 0 0 1-.5-.5m0-4a.5.5 0 0 1 .5-.5h10a.5.5 0 0 1 0 1H3a.5.5 0 0 1-.5-.5" />
                </svg>
            </button>

            {isVisible && (
                <ul>
                    <li>
                        <Link to="/"> {/* Cambia aquí a "/" para el enlace de Inicio */}
                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" className="bi bi-house" viewBox="0 0 16 16">
                                <path d="M8.707 1.5a1 1 0 0 0-1.414 0L.646 8.146a.5.5 0 0 0 .708.708L2 8.207V13.5A1.5 1.5 0 0 0 3.5 15h9a1.5 1.5 0 0 0 1.5-1.5V8.207l.646.647a.5.5 0 0 0 .708-.708L13 5.793V2.5a.5.5 0 0 0-.5-.5h-1a.5.5 0 0 0-.5.5v1.293zM13 7.207V13.5a.5.5 0 0 1-.5.5h-9a.5.5 0 0 1-.5-.5V7.207l5-5z" />
                            </svg>
                            Inicio
                        </Link>
                    </li>
                    <li>
                        <Link to="/catalogo">
                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" className="bi bi-shop" viewBox="0 0 16 16">
                                <path d="M2.97 1.35A1 1 0 0 1 3.73 1h8.54a1 1 0 0 1 .76.35l2.609 3.044A1.5 1.5 0 0 1 16 5.37v.255a2.375 2.375 0 0 1-4.25 1.458A2.37 2.37 0 0 1 9.875 8 2.37 2.37 0 0 1 8 7.083 2.37 2.37 0 0 1 6.125 8a2.37 2.37 0 0 1-1.875-.917A2.375 2.375 0 0 1 0 5.625V5.37a1.5 1.5 0 0 1 .361-.976zm1.78 4.275a1.375 1.375 0 0 0 2.75 0 .5.5 0 0 1 1 0 1.375 1.375 0 0 0 2.75 0 .5.5 0 0 1 1 0 1.375 1.375 0 1 0 2.75 0V5.37a.5.5 0 0 0-.12-.325L12.27 2H3.73L1.12 5.045A.5.5 0 0 0 1 5.37v.255a1.375 1.375 0 0 0 2.75 0 .5.5 0 0 1 1 0M1.5 8.5A.5.5 0 0 1 2 9v6h1v-5a1 1 0 0 1 1-1h3a1 1 0 0 1 1 1v5h6V9a.5.5 0 0 1 1 0v6h.5a.5.5 0 0 1 0 1H.5a.5.5 0 0 1 0-1H1V9a.5.5 0 0 1 .5-.5M4 15h3v-5H4zm5-5a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v3a1 1 0 0 1-1 1h-2a1 1 0 0 1-1-1zm3 0h-2v3h2z" />
                            </svg>
                            Catálogo
                        </Link>
                    </li>
                    <li>
                        <Link to="/sobre-nosotros">
                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-people" viewBox="0 0 16 16">
                                <path d="M15 14s1 0 1-1-1-4-5-4-5 3-5 4 1 1 1 1zm-7.978-1L7 12.996c.001-.264.167-1.03.76-1.72C8.312 10.629 9.282 10 11 10c1.717 0 2.687.63 3.24 1.276.593.69.758 1.457.76 1.72l-.008.002-.014.002zM11 7a2 2 0 1 0 0-4 2 2 0 0 0 0 4m3-2a3 3 0 1 1-6 0 3 3 0 0 1 6 0M6.936 9.28a6 6 0 0 0-1.23-.247A7 7 0 0 0 5 9c-4 0-5 3-5 4q0 1 1 1h4.216A2.24 2.24 0 0 1 5 13c0-1.01.377-2.042 1.09-2.904.243-.294.526-.569.846-.816M4.92 10A5.5 5.5 0 0 0 4 13H1c0-.26.164-1.03.76-1.724.545-.636 1.492-1.256 3.16-1.275ZM1.5 5.5a3 3 0 1 1 6 0 3 3 0 0 1-6 0m3-2a2 2 0 1 0 0 4 2 2 0 0 0 0-4" />
                            </svg>
                            Sobre Nosotros
                        </Link>
                    </li>
                    <li>
                        <Link to="/sucursales">
                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-pin-map" viewBox="0 0 16 16">
                                <path fill-rule="evenodd" d="M3.1 11.2a.5.5 0 0 1 .4-.2H6a.5.5 0 0 1 0 1H3.75L1.5 15h13l-2.25-3H10a.5.5 0 0 1 0-1h2.5a.5.5 0 0 1 .4.2l3 4a.5.5 0 0 1-.4.8H.5a.5.5 0 0 1-.4-.8z" />
                                <path fill-rule="evenodd" d="M8 1a3 3 0 1 0 0 6 3 3 0 0 0 0-6M4 4a4 4 0 1 1 4.5 3.969V13.5a.5.5 0 0 1-1 0V7.97A4 4 0 0 1 4 3.999z" />
                            </svg>
                            Sucursales
                        </Link>
                    </li>
                    <li>
                        <div>
                            <li><Link to="/LoginAdmin">
                                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" className="bi bi-geo-alt" viewBox="0 0 16 16">
                                    <path d="M8 0a4 4 0 0 0-4 4c0 2.14 1.373 4.236 3.252 5.963 1.248 1.356 2.57 2.63 3.748 4.037A.5.5 0 0 0 12 13c1.04 0 2.065-.615 3.128-1.763A6.15 6.15 0 0 0 16 9c0-3.236-4-9-8-9zm0 12a.5.5 0 0 0-.5.5 1.5 1.5 0 0 0 1 1.415A1.5 1.5 0 0 0 8 14.5a.5.5 0 0 0-.5-.5z" />
                                </svg>
                                Ingreso como administrador</Link>
                            </li>
                        </div>
                        <Link to="/edicion"> {/* Agregamos el enlace a la página de editar perfil */}
                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" className="bi bi-pencil" viewBox="0 0 16 16">
                                <path d="M12.146 0 15.5 3.354l-1.646 1.646L10.5 1.5 12.146 0zM0 13.5V16h2.5L13.854 6.646 12.208 5 0 17.208V13.5z" />
                            </svg>
                            Editar Perfil
                        </Link>
                    </li>
                </ul>
            )}
        </div>
    );
}

export default Menu;
