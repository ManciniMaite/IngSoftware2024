/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/RestController.java to edit this template
 */
package com.IS2024.Megastore.controller;

import com.IS2024.Megastore.Exceptions.ResourceNotFoundException;
import com.IS2024.Megastore.entities.Producto;
import com.IS2024.Megastore.services.ProductoService;
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
@RequestMapping("/producto")
public class ProductoRestController {
    @Autowired
    private ProductoService service;
    
    @GetMapping("/getAll")
    public List<Producto> list() {
        return this.service.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        Optional<Producto> producto = this.service.findById(id);
        if(producto.isPresent()){
            return new ResponseEntity<>(producto.get(),HttpStatus.OK);
        } else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable Long id, @RequestBody Producto input) {
        try {
            Producto updatedProducto = service.updateProducto(id, input);
            return ResponseEntity.ok(updatedProducto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    @PostMapping("/insert")
    public ResponseEntity<?> post(@RequestBody Producto input) {
        Producto createdProducto = service.createProducto(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProducto);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
       try {
            this.service.deleteById(id);
            Optional<Producto> producto = this.service.findById(id);
            if(!producto.isPresent()){
                return new ResponseEntity<>(HttpStatus.OK);
            } else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
