/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/RestController.java to edit this template
 */
package com.IS2024.Megastore.controller;

import com.IS2024.Megastore.Exceptions.ResourceNotFoundException;
import com.IS2024.Megastore.services.UsuarioService;
import com.IS2024.Megastore.entities.Usuario;
import com.IS2024.Megastore.model.iniciarSesionRq;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author maite
 */

@RestController
@RequestMapping("/usuario")
// @CrossOrigin(origins = "http://localhost:3000")
public class UsuarioRestController {

    @Autowired
    private UsuarioService service;

    @GetMapping("/getAll")
    public List<Usuario> list() {
        return this.service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        Optional<Usuario> usuario = this.service.findById(id);
        if (usuario.isPresent()) {
            return new ResponseEntity<>(usuario.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable Long id, @RequestBody Usuario input) {
        try {
            Usuario updatedUsuario = service.updateUsuario(id, input);
            return ResponseEntity.ok(updatedUsuario);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/insert")
    public ResponseEntity<?> post(@RequestBody Usuario input) {
        Usuario createdUsuario = service.createUsuario(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUsuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.deleteById(id);
            /*
             * Optional<Usuario> usuario = this.service.findById(id);
             * if(!usuario.isPresent()){
             * return new ResponseEntity<>(HttpStatus.OK);
             * } else{
             * return new ResponseEntity<>(HttpStatus.NOT_FOUND);
             * }
             * } catch (ResourceNotFoundException e) {
             * return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
             * }
             */
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @PostMapping("/usuario/iniciarSesion")
    public ResponseEntity<?> iniciarSesion(@RequestBody iniciarSesionRq input) {
        // Verificar si el correo y la contraseña son "admin"
        if ("admin".equals(input.getCorreo()) && "admin".equals(input.getContrasenia())) {
            // Si son admin, se puede devolver una respuesta específica
            return new ResponseEntity<>("Redirigir a la página de administración", HttpStatus.OK);
        }

        // Buscar usuario por correo registrado
        Optional<Usuario> usuarioOpt = service.findByCorreo(input.getCorreo());

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            // Verificar la contraseña
            if (usuario.getContrasenia().equals(input.getContrasenia())) {
                // Responder con el usuario si la autenticación es exitosa
                return new ResponseEntity<>(usuario, HttpStatus.OK);
            } else {
                // Contraseña incorrecta
                return new ResponseEntity<>("Contraseña incorrecta", HttpStatus.UNAUTHORIZED);
            }
        } else {
            // Usuario no existe
            return new ResponseEntity<>("El usuario no existe", HttpStatus.NOT_FOUND);
        }
    }

}