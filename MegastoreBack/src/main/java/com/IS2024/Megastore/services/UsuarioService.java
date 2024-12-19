/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Service.java to edit this template
 */
package com.IS2024.Megastore.services;

import com.IS2024.Megastore.Exceptions.InvalidEntityException;
import com.IS2024.Megastore.Exceptions.ResourceNotFoundException;
import com.IS2024.Megastore.entities.Usuario;
import com.IS2024.Megastore.entities.Direccion;
import com.IS2024.Megastore.model.iniciarSesionRq;
import com.IS2024.Megastore.repositories.RolRepository;
import com.IS2024.Megastore.repositories.UsuarioRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 *
 * @author maite
 */
@Service
public class UsuarioService implements UsuarioRepository {

    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private DireccionService direccionService;

    @SuppressWarnings("null")
    @Override
    public Optional<Usuario> findById(Long id) {
        return this.repository.findById(id);
    }

    @SuppressWarnings({ "null", "deprecation" })
    @Override
    public Usuario getById(Long id) {
        return this.repository.getById(id);
    }

    @SuppressWarnings("null")
    @Override
    public List<Usuario> findAll() {
        return this.repository.findAll();
    }

    public Usuario updateUsuario(Long id, Usuario usuario) {
        Optional<Usuario> existingUsuario = this.repository.findById(id);
        if (existingUsuario.isPresent()) {
            Usuario updatedUsuario = existingUsuario.get();

            // Validación de campos obligatorios del usuario
            if (usuario.getNombre() == null || usuario.getNombre().isEmpty()) {
                throw new InvalidEntityException("El nombre es un campo obligatorio");
            }
            if (usuario.getApellido() == null || usuario.getApellido().isEmpty()) {
                throw new InvalidEntityException("El apellido es un campo obligatorio");
            }
            if (usuario.getCorreo() == null || usuario.getCorreo().isEmpty()) {
                throw new InvalidEntityException("El correo es un campo obligatorio");
            }

            if (usuario.getDirecciones() == null || usuario.getCorreo().isEmpty()) {
                throw new InvalidEntityException("El domicilio es un campo obligatorio");
            }

            // Validar si el correo ha cambiado y si ya existe otro usuario con el mismo
            // correo
            if (!updatedUsuario.getCorreo().equals(usuario.getCorreo())) {
                Optional<Usuario> existingUs = this.repository.findByCorreo(usuario.getCorreo());
                if (existingUs.isPresent()) {
                    throw new InvalidEntityException("Ya existe un usuario con el mismo correo");
                }
                updatedUsuario.setCorreo(usuario.getCorreo());
            }

            // Actualizar campos del usuario
            updatedUsuario.setNombre(usuario.getNombre());
            updatedUsuario.setApellido(usuario.getApellido());
            updatedUsuario.setNroTelefono(usuario.getNroTelefono());
            updatedUsuario.setContrasenia(usuario.getContrasenia());

            // Actualizar o agregar direcciones
            for (Direccion d : usuario.getDirecciones()) {
                if (d.getId() != null && d.getId() > 0) {
                    // Actualizar direcciones existentes
                    this.direccionService.updateDireccion(d.getId(), d);
                } else {
                    // Crear nuevas direcciones
                    Direccion nuevaDireccion = this.direccionService.createDireccion(d);
                    if (!updatedUsuario.getDirecciones().contains(nuevaDireccion)) {
                        updatedUsuario.getDirecciones().add(nuevaDireccion);
                    }
                }
            }

            // Guardar el usuario actualizado
            return this.repository.save(updatedUsuario);
        } else {
            throw new ResourceNotFoundException("Usuario no encontrado con id: " + id);
        }
    }

    public Usuario createUsuario(Usuario us) {
        if (us.getCorreo() == null || "".equals(us.getCorreo()) || us.getCorreo().isEmpty()) {
            throw new InvalidEntityException("El correo es un campo obligatorio.");

        } else {
            Optional<Usuario> existingUs = this.repository.findByCorreo(us.getCorreo());
            if (existingUs.isPresent()) {
                throw new InvalidEntityException("Ya existe un usuario con el mismo correo");
            } else {
                // Buscar el rol "usuario" en la base de datos
                // Rol rolUsuario = rolRepository.findByNombre("usuario")
                // .orElseThrow(() -> new ResourceNotFoundException("Rol 'usuario' no
                // encontrado"));

                // Asignar el rol al nuevo usuario
                // us.setRol(rolUsuario);

                return this.repository.save(us);

            }
        }

    }

    @SuppressWarnings("null")
    @Override
    public void deleteById(Long id) {
        if (this.repository.existsById(id)) {
            this.repository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Usuario no encontrado con id: " + id);
        }
    }

    public ResponseEntity<String> iniciarSesion(iniciarSesionRq rq) {
        Optional<Usuario> opUsuario = this.repository.findByCorreo(rq.getCorreo());
        if (opUsuario.isPresent()) {
            Usuario us = opUsuario.get();
            if (rq.getContrasenia().equals(us.getContrasenia())) {
                String response = "rol=" + us.getRol().getNombre() +
                        "&nombre=" + us.getNombre() +
                        "&apellido=" + us.getApellido() +
                        "&id=" + us.getId();
                ;
                return ResponseEntity.ok(response);

            } else {
                throw new ResourceNotFoundException("Contraseña incorrecta");
            }
        } else {
            throw new ResourceNotFoundException("Usuario incorrecto");
        }
    }

    public void validarContrasenia(String contrasenia) {
        if (contrasenia == null || contrasenia.length() >= 8 || contrasenia.length() <= 20) {
            throw new InvalidEntityException("La contraseña debe tener entre 8 y 20 caracteres");
        }

    }

    public void validarNumeroTelefono(String nroTelefono) {
        if (nroTelefono == null || !nroTelefono.matches("^\\d{10}$")) {
            throw new InvalidEntityException("El formato de numero de telefono no es correcto");
        }
    }

    // ==============================================================

    @Override
    public void flush() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @SuppressWarnings("null")
    @Override
    public <S extends Usuario> S saveAndFlush(S entity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @SuppressWarnings("null")
    @Override
    public <S extends Usuario> List<S> saveAllAndFlush(Iterable<S> entities) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @SuppressWarnings("null")
    @Override
    public void deleteAllInBatch(Iterable<Usuario> entities) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @SuppressWarnings("null")
    @Override
    public void deleteAllByIdInBatch(Iterable<Long> ids) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteAllInBatch() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @SuppressWarnings("null")
    @Override
    public Usuario getOne(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @SuppressWarnings("null")
    @Override
    public Usuario getReferenceById(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @SuppressWarnings("null")
    @Override
    public <S extends Usuario> List<S> findAll(Example<S> example) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @SuppressWarnings("null")
    @Override
    public <S extends Usuario> List<S> findAll(Example<S> example, Sort sort) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @SuppressWarnings("null")
    @Override
    public <S extends Usuario> List<S> saveAll(Iterable<S> entities) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @SuppressWarnings("null")
    @Override
    public List<Usuario> findAllById(Iterable<Long> ids) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @SuppressWarnings("null")
    @Override
    public <S extends Usuario> S save(S entity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @SuppressWarnings("null")
    @Override
    public boolean existsById(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public long count() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @SuppressWarnings("null")
    @Override
    public void delete(Usuario entity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @SuppressWarnings("null")
    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @SuppressWarnings("null")
    @Override
    public void deleteAll(Iterable<? extends Usuario> entities) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @SuppressWarnings("null")
    @Override
    public List<Usuario> findAll(Sort sort) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @SuppressWarnings("null")
    @Override
    public Page<Usuario> findAll(Pageable pageable) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @SuppressWarnings("null")
    @Override
    public <S extends Usuario> Optional<S> findOne(Example<S> example) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @SuppressWarnings("null")
    @Override
    public <S extends Usuario> Page<S> findAll(Example<S> example, Pageable pageable) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @SuppressWarnings("null")
    @Override
    public <S extends Usuario> long count(Example<S> example) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @SuppressWarnings("null")
    @Override
    public <S extends Usuario> boolean exists(Example<S> example) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @SuppressWarnings("null")
    @Override
    public <S extends Usuario, R> R findBy(Example<S> example,
            Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Optional<Usuario> findByCorreo(String correo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
