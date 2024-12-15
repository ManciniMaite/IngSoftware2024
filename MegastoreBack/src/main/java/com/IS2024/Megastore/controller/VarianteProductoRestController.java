/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/RestController.java to edit this template
 */
package com.IS2024.Megastore.controller;

import com.IS2024.Megastore.Exceptions.ResourceNotFoundException;
import com.IS2024.Megastore.services.VarianteProductoService;
import com.IS2024.Megastore.entities.VarianteProducto;
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
@RequestMapping("/varianteProducto")
public class VarianteProductoRestController {
    @Autowired
    private VarianteProductoService service;

    @GetMapping("/getAll")
    public List<VarianteProducto> list() {
        return this.service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        Optional<VarianteProducto> clase = this.service.findById(id);
        if (clase.isPresent()) {
            return new ResponseEntity<>(clase.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable Long id, @RequestBody VarianteProducto input) {
        try {
            VarianteProducto updatedVarianteProducto = service.updateVarianteProducto(id, input);
            return ResponseEntity.ok(updatedVarianteProducto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/insert")
    public ResponseEntity<?> post(@RequestBody VarianteProducto input) {
        VarianteProducto createdVarianteProducto = service.createVarianteProducto(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVarianteProducto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.deleteById(id);
            Optional<VarianteProducto> varianteProducto = this.service.findById(id);
            if (!varianteProducto.isPresent()) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/variantes/tipo-codigo")
    public List<VarianteProducto> getVariantesByTipoCodigo(String codigo) {
        return service.findByTipoVarianteCodigo(codigo);
    }
}
