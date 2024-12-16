package com.IS2024.Megastore.services;

import com.IS2024.Megastore.Exceptions.ResourceNotFoundException;
import com.IS2024.Megastore.entities.Estado;
import com.IS2024.Megastore.repositories.EstadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class EstadoServiceTest {

    @InjectMocks
    private EstadoService estadoService;

    @Mock
    private EstadoRepository estadoRepository;

    private Estado estado1;
    private Estado estado2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        estado1 = new Estado();
        estado1.setId(1L);
        estado1.setCodigo("PN");
        estado1.setNombre("Pendiente");

        estado2 = new Estado();
        estado2.setId(2L);
        estado2.setCodigo("EP");
        estado2.setNombre("En Preparación");
    }

    // CPU07 - Obtener todos los estados
    @Test
    void testFindAll() {
        Mockito.when(estadoRepository.findAll()).thenReturn(Arrays.asList(estado1, estado2));

        var estados = estadoService.findAll();

        assertNotNull(estados);
        assertEquals(2, estados.size());
        assertEquals("PN", estados.get(0).getCodigo());
        assertEquals("EP", estados.get(1).getCodigo());
    }

    // CPU08 - Buscar un estado por ID (Éxito)
    @Test
    void testFindByIdSuccess() {
        Mockito.when(estadoRepository.findById(1L)).thenReturn(Optional.of(estado1));

        var estado = estadoService.findById(1L);

        assertTrue(estado.isPresent());
        assertEquals("PN", estado.get().getCodigo());
    }

    // CPU09 - Buscar un estado por ID (No Encontrado)
    @Test
    void testFindByIdNotFound() {
        Mockito.when(estadoRepository.findById(1L)).thenReturn(Optional.empty());

        var estado = estadoService.findById(1L);

        assertFalse(estado.isPresent());
    }

    // CPU10 - Crear un nuevo estado
    @Test
    void testCreateEstado() {
        Mockito.when(estadoRepository.save(any(Estado.class))).thenReturn(estado1);

        var createdEstado = estadoService.createEstado(estado1);

        assertNotNull(createdEstado);
        assertEquals("PN", createdEstado.getCodigo());
    }

    // CPU11 - Actualizar un estado existente (Éxito)
    @Test
    void testUpdateEstadoSuccess() {
        Mockito.when(estadoRepository.findById(1L)).thenReturn(Optional.of(estado1));
        Mockito.when(estadoRepository.save(any(Estado.class))).thenAnswer(invocation -> invocation.getArgument(0));

        estado1.setNombre("Pendiente Actualizado");
        var updatedEstado = estadoService.updateEstado(1L, estado1);

        assertNotNull(updatedEstado);
        assertEquals("Pendiente Actualizado", updatedEstado.getNombre());
    }

    // CPU12 - Actualizar un estado existente (No Encontrado)
    @Test
    void testUpdateEstadoNotFound() {
        Mockito.when(estadoRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            estadoService.updateEstado(1L, estado1);
        });

        assertEquals("Estado no encontrado con id: 1", exception.getMessage());
    }

    // CPU13 - Eliminar un estado por ID (Éxito)
    @Test
    void testDeleteByIdSuccess() {
        Mockito.when(estadoRepository.existsById(1L)).thenReturn(true);
        Mockito.doNothing().when(estadoRepository).deleteById(1L);

        assertDoesNotThrow(() -> estadoService.deleteById(1L));
    }

    // CPU14 - Eliminar un estado por ID (No Encontrado)
    @Test
    void testDeleteByIdNotFound() {
        Mockito.when(estadoRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            estadoService.deleteById(1L);
        });

        assertEquals("Estado no encontrado con id: 1", exception.getMessage());
    }

    // CPU15 - Buscar estado por código (Éxito)
    @Test
    void testFindByCodigoSuccess() {
        Mockito.when(estadoRepository.findByCodigo("PN")).thenReturn(Optional.of(estado1));

        var estado = estadoService.findByCodigo("PN");

        assertTrue(estado.isPresent());
        assertEquals("PN", estado.get().getCodigo());
    }

    // CPU16 - Eliminar un estado por ID (No Encontrado)
    @Test
    void testFindByCodigoNotFound() {
        Mockito.when(estadoRepository.findByCodigo("XX")).thenReturn(Optional.empty());

        var estado = estadoService.findByCodigo("XX");

        assertFalse(estado.isPresent());
    }
}
