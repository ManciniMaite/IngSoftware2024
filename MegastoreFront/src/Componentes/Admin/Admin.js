// src/Componentes/Admin/Admin.js
import React from 'react';
import {  Link } from 'react-router-dom';

import './Admin.css'; // Importa el archivo CSS

const Admin = () => {
  return (
    <div className="admin-container">
      <aside className="sidebar">
        <nav> 
          <ul>
            <li><Link to="/">Inicio</Link></li>
            <li><Link to="/catalogo">Gestionar catálogo</Link></li>

            <li><Link to="/admin/Pedidos">Gestionar pedido</Link></li>

          </ul>
        </nav>
      </aside>
      <main className="admin-content">
        
      </main>
    </div>
  );
};

export default Admin;
