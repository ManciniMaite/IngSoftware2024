/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/RestController.java to edit this template
 */
package com.IS2024.Megastore.controller;

import com.IS2024.Megastore.Exceptions.ResourceNotFoundException;
import com.IS2024.Megastore.entities.CategoriaProducto;
import com.IS2024.Megastore.entities.Producto;
import com.IS2024.Megastore.entities.VarianteProducto;
import com.IS2024.Megastore.repositories.ProductoRepository;
import com.IS2024.Megastore.services.ProductoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.IS2024.Megastore.services.CategoriaProductoService;

import java.io.IOException;
import java.util.Base64;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author maite
 */
@RestController
@RequestMapping("/producto")
public class ProductoRestController {
    @Autowired
    private ProductoService service;
    @Autowired
    private CategoriaProductoService categoriaService; // Inyectamos el servicio de categorías

    @GetMapping("/getAll")
    public List<Producto> list() {
        return this.service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        Optional<Producto> producto = this.service.findById(id);
        if (producto.isPresent()) {
            return new ResponseEntity<>(producto.get(), HttpStatus.OK);
        } else {
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
    public ResponseEntity<?> post(@RequestParam("nombre") String nombre,
            @RequestParam("precio") long precio,
            @RequestParam("stock") int stock,
            @RequestParam("codigo") String codigo,
            @RequestParam("categoria") Long categoriaId,
            @RequestParam(value = "foto", required = false) MultipartFile foto,
            @RequestParam(value = "variantes", required = false) String variantesJson) { // Recibe variantes como JSON

        // Obtener la categoría por ID
        Optional<CategoriaProducto> categoriaOptional = categoriaService.findById(categoriaId);
        if (!categoriaOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Categoría no válida");
        }
        CategoriaProducto categoria = categoriaOptional.get();

        // Crear el objeto Producto
        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setStock(stock);
        producto.setCodigo(codigo);
        producto.setCategoria(categoria);

        // Manejar la foto
        if (foto != null && !foto.isEmpty()) {
            try {
                byte[] bytes = foto.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(bytes);
                producto.setFoto(base64Image);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar la imagen");
            }
        }

        // Manejar las variantes (si se reciben en formato JSON)
        if (variantesJson != null && !variantesJson.isEmpty()) {
            // Convertir el JSON de variantes a una lista de objetos VarianteProducto
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                List<VarianteProducto> variantes = objectMapper.readValue(variantesJson,
                        new TypeReference<List<VarianteProducto>>() {
                        });
                producto.setVariantes(variantes); // Establecer las variantes en el producto
            } catch (JsonProcessingException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al procesar las variantes");
            }
        }

        // Guardar el producto en la base de datos
        Producto createdProducto = service.createProducto(producto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdProducto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            this.service.deleteById(id);
            Optional<Producto> producto = this.service.findById(id);
            if (!producto.isPresent()) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Nuevo endpoint para obtener las categorías
    @GetMapping("/categorias")
    public ResponseEntity<?> getCategorias() {
        return new ResponseEntity<>(categoriaService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/filtrarPorPrecio")
    public List<Producto> filtrarPorPrecio(@RequestParam("precio") Double precio) {
        return service.findProductosByPrecio(precio);
    }

}
