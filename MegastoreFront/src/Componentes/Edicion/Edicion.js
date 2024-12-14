import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './Edicion.css';
import Cabecera from '../Cabecera/Cabecera';
import { useAuth } from '../Autenticacion/AuthContext'; // Usamos el contexto de autenticación

const Edicion = () => {
  const { user } = useAuth(); // Obtener usuario logueado desde AuthContext
  // Estado para los datos del formulario
  const [isEditing, setIsEditing] = useState(false); // Controla si el formulario está en modo edición
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    nombre: '',
    apellido: '',
    nroTelefono: '',
    correo: '',
    contrasenia: '',
    direcciones: [
      { calle: "", numero: ""}
    ], // Inicializa como un arreglo vacío
  }); 
 
  
  // Llamada al backend para obtener los datos del usuario logueado
  useEffect(() => {
    if (user?.id && !isEditing) {
      const fetchUserData = async () => {
        try {
          const response = await fetch(`http://localhost:8080/usuario/${user.id}`);
          if (response.ok) {
            const data = await response.json();
            setFormData({
              ...data,
              direcciones: Array.isArray(data.direcciones) ? data.direcciones : [],
            });
          } else {
            console.error('Error al recuperar datos del usuario');
          }
        } catch (error) {
          console.error('Error al conectar con el backend:', error);
        }
      };
      fetchUserData();
    }
  }, [user, formData.nombre]);  // Asegura que solo se haga una vez
  

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleAddressChange = (index, field, value) => {
    const updatedAddresses = Array.isArray(formData.direcciones)
      ? [...formData.direcciones]
      : []; // Si no es un arreglo, inicialízalo como vacío
    updatedAddresses[index] = {
      ...updatedAddresses[index],
      [field]: value,
    };
    setFormData({ ...formData, direcciones: updatedAddresses });
  };

  const handleSubmit = async () => {
    if (!user || !user.id) {
      console.warn("El usuario no está disponible o no tiene un ID válido.");
      return; // Salimos si no hay usuario
    }

    const userId = parseInt(user.id, 10);
    if (isNaN(userId)) {
      console.error('El ID del usuario no es válido.');
      return;
    }
    try {
      const response = await fetch(`http://localhost:8080/usuario/${userId}`, {
        method: 'PUT',
        headers: {  
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
      }); 
 
      if (response.ok) {
        alert('Perfil actualizado con éxito');
        setIsEditing(false); // Volver a modo visualización
      } else {
        console.error('Error al actualizar el perfil');
        const errorData = await response.json();
        console.error('Detalles del error:', errorData);
  
      }
    } catch (error) {
      console.error('Error al enviar datos al backend:', error);
    }
  };

  const handleCancel = () => {
    setIsEditing(false); // Salir del modo edición sin guardar cambios
  };
  const handleRemoveAddress = (index) => {
    const newDirecciones = formData.direcciones.filter((_, idx) => idx !== index);
    setFormData({ ...formData, direcciones: newDirecciones });
  };
  const handleAddAddress = () => {
    setFormData((prevFormData) => ({
      ...prevFormData,  // Mantener el estado previo
      direcciones: [...prevFormData.direcciones, { calle: "", numero: "" }]  // Solo agregar la nueva dirección
    }));
  };
  

  if (!formData) { 
    return <p>Cargando datos...</p>;
  }

  return (
    <div className="input-group-edicion">
      <Cabecera />
      <div className='titulo-edicion'>
          <svg xmlns="http://www.w3.org/2000/svg" width="70" height="70" fill="currentColor" class="bi bi-pencil-square" viewBox="0 0 16 16">
            <path d="M15.502 1.94a.5.5 0 0 1 0 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 0 1 .707 0l1.293 1.293zm-1.75 2.456-2-2L4.939 9.21a.5.5 0 0 0-.121.196l-.805 2.414a.25.25 0 0 0 .316.316l2.414-.805a.5.5 0 0 0 .196-.12l6.813-6.814z"/>
            <path fill-rule="evenodd" d="M1 13.5A1.5 1.5 0 0 0 2.5 15h11a1.5 1.5 0 0 0 1.5-1.5v-6a.5.5 0 0 0-1 0v6a.5.5 0 0 1-.5.5h-11a.5.5 0 0 1-.5-.5v-11a.5.5 0 0 1 .5-.5H9a.5.5 0 0 0 0-1H2.5A1.5 1.5 0 0 0 1 2.5z"/>
          </svg>
          <h1>EDITAR PERFIL</h1>
      </div>
      

      <div className="perfil-container-edicion">
        
          <div className="form-group-edicion">
            <label>Nombre:</label>
            <input
              type="text"
              name="nombre"
              value={formData.nombre}
              onChange={handleChange}
              disabled={!isEditing}  // Deshabilitado cuando no está editando
            />
          </div>
          <div className="form-group-edicion">
            <label>Apellido:</label>
            <input
              type="text"
              name="apellido"
              value={formData.apellido}
              onChange={handleChange}
              disabled={!isEditing}  // Deshabilitado cuando no está editando
            />
          </div>
          <div className="form-group-edicion">
            <label>Teléfono:</label>
            <input
              type="text"
              name="nroTelefono"
              value={formData.nroTelefono}
              onChange={handleChange}
              disabled={!isEditing}  // Deshabilitado cuando no está editando
            />
          </div>
          <div className="form-group-edicion">
            <label>Email:</label>
            <input
              type="email"
              name="correo"
              value={formData.correo}
              onChange={handleChange}
              disabled={!isEditing}  // Deshabilitado cuando no está editando
            />
          </div>
          <div className="form-group-edicion">
            <label>Contraseña:</label>
            <input
              type="password"
              name="contrasenia"
              value={formData.contrasenia}
              onChange={handleChange}
              disabled={!isEditing}  // Deshabilitado cuando no está editando
            />
          </div>
          <div className="form-group-edicion">
            <label>Dirección de Envío</label>
            {formData.direcciones.map((direccion, index) => (
              <div key={index} className="calle-altura-edicion">
                <input
                  type="text"
                  className="calle-input-edicion"
                  value={direccion.calle}
                  onChange={(e) => handleAddressChange(index, 'calle', e.target.value)}
                  placeholder="Calle"
                />
                <input
                  type="text"
                  className="altura-input-edicion"
                  value={direccion.numero}
                  onChange={(e) => handleAddressChange(index, 'numero', e.target.value)}
                  placeholder="Altura"
                />
                {isEditing && formData.direcciones.length > 1 && (
                  <button
                    type='button'
                    className="cancel-button"
                    onClick={() => handleRemoveAddress(index)}
                  >
                   Eliminar
                  </button>
                )}
              </div>
            ))}

            {isEditing && (
                          <button  className="add-button" onClick={handleAddAddress}>
                            
                            Agregar Dirección
                          </button>
            )}
          </div>
 
          <div className="button-group-edicion">
            {!isEditing ? (
              <button  className='editar-edicion' onClick={() => setIsEditing(true)}>
                Editar
              </button>
            ) : (
              <>
                <button  className= 'aceptar-edicion' onClick={handleSubmit}>
                  Aceptar
                </button>
                <button  className= 'cancelar-edicion' onClick={handleCancel}>
                  Cancelar
                </button>
              </>
            )}
          </div>
        
      </div>
    </div>
  );
};

export default Edicion;
