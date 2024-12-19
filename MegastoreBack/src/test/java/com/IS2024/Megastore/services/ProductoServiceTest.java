package com.IS2024.Megastore.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.IS2024.Megastore.Exceptions.InvalidEntityException;
import com.IS2024.Megastore.entities.Producto;
import com.IS2024.Megastore.entities.CategoriaProducto;
import com.IS2024.Megastore.repositories.ProductoRepository;

public class ProductoServiceTest {

    /* CPU 3 - Validar precio mínimo */

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaProductoService categoriaService;

    @Mock
    private VarianteProductoService varianteService;

    @InjectMocks
    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidarPrecioMenorQueCero() {
        Producto producto = new Producto();
        CategoriaProducto categoria = new CategoriaProducto();
        categoria.setId(1L);
        producto.setCategoria(categoria);
        producto.setVariantes(Collections.emptyList());
        producto.setPrecio(-1L);

        when(productoRepository.findByCodigo(producto.getCodigo())).thenReturn(Optional.empty());
        when(categoriaService.findById(1L)).thenReturn(Optional.of(categoria));

        InvalidEntityException thrown = assertThrows(InvalidEntityException.class,
                () -> productoService.createProducto(producto));

        assertEquals("El precio del producto no puede ser menor a 0", thrown.getMessage());

    }
}
