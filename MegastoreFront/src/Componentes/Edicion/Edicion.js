import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './Edicion.css';
import Cabecera from '../Cabecera/Cabecera';

const Edicion = ({ user }) => {
  const [formData, setFormData] = useState({
    nombre: user?.nombre || '',
    apellido: user?.apellido || '',
    telefono: user?.telefono || '',
    direccionesEnvio: user?.direccionesEnvio || [{ calle: '', altura: '' }],
    email: user?.email || '',
    contrasena: '',
    confirmarContrasena: ''
  });
  const [setErrorMessage] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    // Actualiza el estado del formulario si el usuario cambia
    setFormData({
      nombre: user?.nombre || '',
      apellido: user?.apellido || '',
      telefono: user?.telefono || '',
      direccionesEnvio: user?.direccionesEnvio || [{ calle: '', altura: '' }],
      email: user?.email || '',
      contrasena: '',
      confirmarContrasena: ''
    });
  }, [user]);


  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleAddressChange = (index, field, value) => {
    const newDireccionesEnvio = [...formData.direccionesEnvio];
    newDireccionesEnvio[index][field] = value;
    setFormData({ ...formData, direccionesEnvio: newDireccionesEnvio });
  };

  const handleAddAddress = () => {
    setFormData({ ...formData, direccionesEnvio: [...formData.direccionesEnvio, { calle: '', altura: '' }] });
  };

  const handleRemoveAddress = (index) => {
    const newDireccionesEnvio = formData.direccionesEnvio.filter((_, i) => i !== index);
    setFormData({ ...formData, direccionesEnvio: newDireccionesEnvio });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log(formData);
        // Redirige después de guardar
        navigate('/Home');
    if (formData.contrasena !== formData.confirmarContrasena) {
      setErrorMessage('Las contraseñas no coinciden');
      return;
    }
  };

  const handleCancel = () => {
    navigate('/Home'); // Redirige al inicio
  };

  return (
    <div className='edit-profile-container'>
      <Cabecera/>
      <h2>EDITAR PERFIL</h2>
      <form onSubmit={handleSubmit}>

        <div>
          <label htmlFor="nombre">Nombre</label>
          <input
            type="text"
            id="nombre"
            name="nombre"
            value={formData.nombre}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label htmlFor="apellido">Apellido:</label>
          <input
            type="text"
            id="apellido"
            name="apellido"
            value={formData.apellido}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label htmlFor="telefono">Teléfono</label>
          <input
            type="text"
            id="telefono"
            name="telefono"
            value={formData.telefono}
            onChange={handleChange}
            required
          />
        </div>

        <div className='direcciones-envio'>
          <label>Direcciones de Envío</label>
          {formData.direccionesEnvio.map((direccion, index) => (
            <div key={index} className="direccion-envio-group">
              <input
                type="text"
                value={direccion.calle}
                onChange={(e) => handleAddressChange(index, 'calle', e.target.value)}
                required
                placeholder={`Calle ${index + 1}`}
              />
              <input
                type="text"
                value={direccion.altura}
                onChange={(e) => handleAddressChange(index, 'altura', e.target.value)}
                required
                placeholder={`Altura ${index + 1}`}
              />
              <button type="button" onClick={() => handleRemoveAddress(index)}>
                Eliminar
              </button>
            </div>
          ))}
          <button type="button" onClick={handleAddAddress}>Agregar Dirección</button>
        </div>

        <div className='input-group'>
          <div>
            <label htmlFor="email">Email</label>
            <input
              type="email"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              required
            />
          </div>
        </div>
        <div>
          <label htmlFor="contrasena">Contraseña</label>
          <input
            type="password"
            id="contrasena"
            name="contrasena"
            value={formData.contrasena}
            onChange={handleChange}
          />
        </div>
        <div>
          <label htmlFor="confirmarContrasena">Confirmar Contraseña</label>
          <input
            type="password"
            id="confirmarContrasena"
            name="confirmarContrasena"
            value={formData.confirmarContrasena}
            onChange={handleChange}
          />
        </div>
        <div className="button-group">
          <button type="submit">Guardar Cambios</button>
          <button type="button" onClick={handleCancel}>Cancelar</button>
        </div>
      </form>
    </div>
  );
};

export default Edicion;
