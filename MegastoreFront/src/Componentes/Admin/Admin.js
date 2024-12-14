// src/Componentes/Admin/Admin.js
import React, { useState } from 'react';
import { useHistory } from 'react-router-dom';
import {  Link } from 'react-router-dom';

import './Admin.css'; // Importa el archivo CSS

const Admin = () => {
  return (
    <div className="admin-container">
      <aside className="sidebar">
        <nav>
          <ul>
            <li><Link to="/catalogo/GestionarCatalogo">Gestionar catálogo</Link></li>

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
