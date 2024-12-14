/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/RestController.java to edit this template
 */
package com.IS2024.Megastore.controller;

import com.IS2024.Megastore.Exceptions.ResourceNotFoundException;
import com.IS2024.Megastore.entities.DetallePedido;
import com.IS2024.Megastore.services.DetallePedidoService;
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
@RequestMapping("/detallePedido")
public class DetallePedidoRestController {
    @Autowired
    private DetallePedidoService service;

    @GetMapping("/getAll")
    public List<DetallePedido> list() {
        return this.service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        Optional<DetallePedido> detallePedido = this.service.findById(id);
        if (detallePedido.isPresent()) {
            return new ResponseEntity<>(detallePedido.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable Long id, @RequestBody DetallePedido input) {
        try {
            DetallePedido updatedDetallePedido = service.updateDetallePedido(id, input);
            return ResponseEntity.ok(updatedDetallePedido);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/insert")
    public ResponseEntity<?> post(@RequestBody DetallePedido input) {
        DetallePedido createdDetallePedido = service.createDetallePedido(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDetallePedido);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            this.service.deleteById(id);
            Optional<DetallePedido> detallePedido = this.service.findById(id);
            if (!detallePedido.isPresent()) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
