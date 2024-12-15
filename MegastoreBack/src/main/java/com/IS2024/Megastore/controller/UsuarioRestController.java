/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/RestController.java to edit this template
 */
package com.IS2024.Megastore.controller;

import com.IS2024.Megastore.Exceptions.ResourceNotFoundException;
import com.IS2024.Megastore.services.UsuarioService;
import com.IS2024.Megastore.entities.Direccion;
import com.IS2024.Megastore.entities.Usuario;
import com.IS2024.Megastore.model.iniciarSesionRq;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author maite
 */
@RestController
@RequestMapping("/usuario")
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
        // Validación de campos con logs
        if (input.getNombre() == null || input.getNombre().isEmpty()) {
            System.out.println("Campo 'nombre' faltante o vacío.");
            return ResponseEntity.badRequest().body("El campo 'nombre' es obligatorio.");
        }
        if (input.getApellido() == null || input.getApellido().isEmpty()) {
            System.out.println("Campo 'apellido' faltante o vacío.");
            return ResponseEntity.badRequest().body("El campo 'apellido' es obligatorio.");
        }
        // Agrega validaciones similares para otros campos...
        if (input.getNroTelefono() == null || input.getNroTelefono().isEmpty()) {
            System.out.println("Campo 'apellido' faltante o vacío.");
            return ResponseEntity.badRequest().body("El campo 'apellido' es obligatorio.");
        }
        try {
            Usuario updatedUsuario = service.updateUsuario(id, input);
            return ResponseEntity.ok(updatedUsuario);
        } catch (ResourceNotFoundException e) {
            System.out.println("Usuario no encontrado: " + e.getMessage());
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
            Optional<Usuario> usuario = this.service.findById(id);
            if (!usuario.isPresent()) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/iniciarSesion")
    public ResponseEntity<?> iniciarSesion(@RequestBody iniciarSesionRq input) {
        ResponseEntity<String> rol = service.iniciarSesion(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(rol);
    }

    @GetMapping("/{userId}/direcciones")
    public ResponseEntity<List<Direccion>> getDireccionesByUsuario(@PathVariable Long userId) {
        System.out.println("Buscando direcciones para el usuario con ID: " + userId);

        Optional<Usuario> usuarioOpt = service.findById(userId);

        if (usuarioOpt.isEmpty()) {
            // En lugar de devolver un Map con el error, devolvemos una lista vacía
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of());
        }

        Usuario usuario = usuarioOpt.get();
        List<Direccion> direcciones = usuario.getDirecciones();

        if (direcciones.isEmpty()) {
            // Si no hay direcciones, devolvemos una lista vacía
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of());
        }

        return ResponseEntity.ok(direcciones);
    }

}
