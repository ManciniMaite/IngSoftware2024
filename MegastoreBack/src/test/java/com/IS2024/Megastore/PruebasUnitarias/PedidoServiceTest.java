package com.IS2024.Megastore.PruebasUnitarias;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;

import com.IS2024.Megastore.entities.Pedido;
import com.IS2024.Megastore.entities.Estado;
import com.IS2024.Megastore.repositories.PedidoRepository;
import com.IS2024.Megastore.services.PedidoService;
import com.IS2024.Megastore.services.EstadoService;
import com.IS2024.Megastore.Exceptions.InvalidEntityException;
import com.IS2024.Megastore.Exceptions.ResourceNotFoundException;

import java.util.Optional;

public class PedidoServiceTest {
    @InjectMocks
    PedidoService pedidoService;

    @Mock
    PedidoRepository pedidoRepository;

    @Mock
    private EstadoService estadoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /* CPU 6 - cancelar pedido en preparación */
    @Test
    void cancelarPedidoEnPreparacion() {
        // caso 1: pedido en preparacion
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        Estado estadoEnPreparacion = new Estado();
        estadoEnPreparacion.setCodigo("en preparación");
        pedido.setEstado(estadoEnPreparacion);

        Estado estadoCancelado = new Estado();
        estadoCancelado.setCodigo("cancelado");

        // mockeo del repositorio y del servicio

        Mockito.when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        Mockito.when(estadoService.findByCodigo("cancelado")).thenReturn(Optional.of(estadoCancelado));

        // cancelar pedido
        pedidoService.cancelarPedido(1L);

        // verificar que el estado cambie a cancelado
        assertEquals("cancelado", pedido.getEstado().getCodigo());

    }

    @Test
    void cancelarPedidoNoEnPreparacion() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        Estado estadoEnviado = new Estado();
        estadoEnviado.setCodigo("enviado");
        pedido.setEstado(estadoEnviado);

        // mockeo de repositorio

        Mockito.when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        // intentar cancear el pedido y verificar que se lance la excepcion
        InvalidEntityException thrown = assertThrows(InvalidEntityException.class,
                () -> pedidoService.cancelarPedido(1L));

        assertEquals("Solo se puede cancelar un pedido en estado 'preparación' o 'pendiente' ", thrown.getMessage());
    }

    /* CPU 7 cancelar pedido pendiente */

    // caso pedido pendiente
    @Test
    void cancelarPedidoPendiente() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        Estado estadoPendiente = new Estado();
        estadoPendiente.setCodigo("pendiente");
        pedido.setEstado(estadoPendiente);

        Estado estadoCancelado = new Estado();
        estadoCancelado.setCodigo("cancelado");

        // mockeo del repositorio y del servicio
        Mockito.when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        Mockito.when(estadoService.findByCodigo("cancelado")).thenReturn(Optional.of(estadoCancelado));

        // cancela pedido
        pedidoService.cancelarPedido(1L);

        // verifica que el estado cambie a cancelado
        assertEquals("cancelado", pedido.getEstado().getCodigo());
    }

    /* CPU 8 cancelar pedido entregado */
    @Test
    void cancelarPedidoEntregado() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        Estado estadoEntregado = new Estado();
        estadoEntregado.setCodigo("entregado");
        pedido.setEstado(estadoEntregado);

        // mockeo del repositorio
        Mockito.when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        // se intenta cancelar el pedido y se debe lanzar la excepcion
        InvalidEntityException thrown = assertThrows(InvalidEntityException.class,
                () -> pedidoService.cancelarPedido(1L));

        assertEquals("Solo se puede cancelar un pedido en estado 'preparación' o 'pendiente' ", thrown.getMessage());
    }

    /*
     * CPU 10 pedido pendiente a en preparación
     * 
     * @Test
     * void actualizarEstadoAEnPreparacionDesdePendiente() {
     * Pedido pedido = new Pedido();
     * pedido.setId(1L);
     * Estado estadoPendiente = new Estado();
     * estadoPendiente.setCodigo("PN");
     * pedido.setEstado(estadoPendiente);
     * 
     * Estado estadoEnPreparacion = new Estado();
     * estadoEnPreparacion.setCodigo("EP");
     * 
     * // mckeo de servicios y repo
     * Mockito.when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
     * Mockito.when(estadoService.findByCodigo("EP")).thenReturn(Optional.of(
     * estadoEnPreparacion));
     * 
     * // actualizar estado del pedido
     * Pedido pedidoActualizado = pedidoService.actualizarEstado(pedido, "EP");
     * 
     * // verficar que el estado sea "en preparacion"
     * assertEquals("EP", pedidoActualizado.getEstado().getCodigo());
     * }
     */

    @Test

    void actualizarAPreparacionDesdeNoPendiente() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        Estado estadoEnviado = new Estado();
        estadoEnviado.setCodigo("EN");
        pedido.setEstado(estadoEnviado);

        // mockeo del repository
        Mockito.when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        Estado estadoEP = new Estado();
        estadoEP.setCodigo("EP");
        Mockito.when(estadoService.findByCodigo("EP")).thenReturn(Optional.of(estadoEP));

        // intentar actualizar el estado y verificar que se lanza la excepcion
        IllegalStateException thrown = assertThrows(IllegalStateException.class,
                () -> pedidoService.actualizarEstado(pedido, "EP"));

        assertEquals("No se puede pasar a enPreparacion", thrown.getMessage());

    }
}
