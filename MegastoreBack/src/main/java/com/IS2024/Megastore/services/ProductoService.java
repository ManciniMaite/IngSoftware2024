/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Service.java to edit this template
 */
package com.IS2024.Megastore.services;

import com.IS2024.Megastore.Exceptions.InvalidEntityException;
import com.IS2024.Megastore.Exceptions.ResourceNotFoundException;
import com.IS2024.Megastore.entities.Producto;
import com.IS2024.Megastore.entities.CategoriaProducto;
import com.IS2024.Megastore.entities.VarianteProducto;
import com.IS2024.Megastore.repositories.ProductoRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author maite
 */
@Service
public class ProductoService {
    @Autowired
    private ProductoRepository repository;

    @Autowired
    private CategoriaProductoService categoriaService;

    @Autowired
    private VarianteProductoService varianteService;

    public Optional<Producto> findById(Long id) {
        return this.repository.findById(id);
    }

    @SuppressWarnings("deprecation")
    public Producto getById(Long id) {
        return this.repository.getById(id);
    }

    public List<Producto> findAll() {
        return this.repository.findAll();
    }

    public Producto updateProducto(Long id, Producto producto) {
        Optional<Producto> existingProducto = this.repository.findById(id);
        if (existingProducto.isPresent()) {
            Producto updatedProducto = existingProducto.get();

            updatedProducto.setNombre(producto.getNombre());
            updatedProducto.setStock(producto.getStock());
            updatedProducto.setPrecio(producto.getPrecio());
            updatedProducto.setFoto(producto.getFoto());

            // CODIGO, debe ser unico.
            if (!producto.getCodigo().equals(updatedProducto.getCodigo())) { // Se esta cambiando el codigo de producto
                Optional<Producto> p = this.repository.findByCodigo(producto.getCodigo());
                if (p.isPresent()) {
                    throw new ResourceNotFoundException(
                            "Ya existe un producto con codigo: " + producto.getCategoria().getId());
                } else {
                    updatedProducto.setCodigo(producto.getCodigo());
                }
            }

            // CATEGORIA
            Optional<CategoriaProducto> categoria = categoriaService.findById(producto.getCategoria().getId());
            if (categoria.isPresent()) {
                updatedProducto.setCategoria(categoria.get());
            } else {
                throw new ResourceNotFoundException(
                        "No existe una categoria con id: " + producto.getCategoria().getId());
            }

            // VARIANTES
            if (producto.getVariantes() != null && !producto.getVariantes().isEmpty()) {
                // por si no hay variantes en el producto tener inicializado el array por si hay
                // que agregar variantes
                if (updatedProducto.getVariantes() == null) {
                    updatedProducto.setVariantes(new ArrayList<>());
                }
                for (VarianteProducto vp : producto.getVariantes()) {
                    Optional<VarianteProducto> variante = varianteService.findById(vp.getId());
                    if (variante.isPresent()) {
                        VarianteProducto v = variante.get();
                        if (!producto.getVariantes().contains(v)) {
                            updatedProducto.getVariantes().add(v);
                        }

                    } else {
                        throw new ResourceNotFoundException("No existe una variante con id: " + vp.getId());
                    }
                }
            }

            return this.repository.save(updatedProducto);
        } else {
            throw new ResourceNotFoundException("Producto no encontrado con id: " + id);
        }
    }

    public Producto createProducto(Producto producto) {
        // Control codigo unico
        Optional<Producto> p = this.repository.findByCodigo(producto.getCodigo());
        if (p.isPresent()) {
            throw new ResourceNotFoundException("Ya existe un producto con codigo: " + producto.getCodigo());
        }

        // Control de que exista la categoria
        Optional<CategoriaProducto> categoria = categoriaService.findById(producto.getCategoria().getId());
        if (!categoria.isPresent()) {
            throw new ResourceNotFoundException("No existe una categoria con id: " + producto.getCategoria().getId());

        }

        // Control de que existan las variantes
        if (producto.getVariantes() != null && !producto.getVariantes().isEmpty()) {
            for (VarianteProducto vp : producto.getVariantes()) {
                Optional<VarianteProducto> variante = varianteService.findById(vp.getId());
                if (!variante.isPresent()) {
                    throw new ResourceNotFoundException("No existe una variante con id: " + vp.getId());
                }
            }
        }

        return this.repository.save(producto);
    }

    // cantidad puede ser negativo en caso de creacion de pedido y positivo en caso
    // de que se elimine un detalle o un pedido
    public void actualizarStock(Long id, int cantidad) {
        Optional<Producto> producto = this.repository.findById(id);
        if (producto.isPresent()) {
            Producto updatedProducto = producto.get();
            int stock = updatedProducto.getStock() + (cantidad);
            if (stock >= 0) {
                updatedProducto.setStock(stock);
            } else {
                throw new InvalidEntityException("El stock no puede ser negativo");
            }

            this.repository.save(updatedProducto);

        } else {
            throw new ResourceNotFoundException("Producto no encontrado con id: " + id);
        }
    }

    public boolean tieneStock(Long id, int cantidad) {
        Optional<Producto> producto = this.repository.findById(id);
        if (producto.isPresent()) {
            Producto p = producto.get();
            int stock = p.getStock() - cantidad;
            if (stock >= 0) {
                return true;
            } else {
                return false;
            }
        } else {
            throw new ResourceNotFoundException("Producto no encontrado con id: " + id);
        }
    }

    public void deleteById(Long id) {
        if (this.repository.existsById(id)) {
            this.repository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Producto no encontrado con id: " + id);
        }
    }

    public List<Producto> findProductosByPrecio(Double precio) {
        if (precio == null || precio < 0) {
            throw new InvalidEntityException("El precio debe ser un valor positivo.");
        }
        return this.repository.findByPrecioLessThanEqual(precio);
    }

}
