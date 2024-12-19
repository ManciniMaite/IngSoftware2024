package com.IS2024.Megastore.services;

import com.IS2024.Megastore.Exceptions.InvalidEntityException;
import com.IS2024.Megastore.Exceptions.ResourceNotFoundException;
import com.IS2024.Megastore.entities.*;
import com.IS2024.Megastore.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PedidoServiceTest {

    @InjectMocks
    private PedidoService pedidoService;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private EstadoService estadoService;

    @Mock
    private ProductoService productoService;

    @Mock
    private DetallePedidoService detallePedidoService;

    @Mock
    private DireccionRepository direccionRepository;

    @Mock
    private EstadoRepository estadoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // CPU01 - Crear Pedido Exitoso
    @Test
    void testCreatePedidoSuccess() {
        Pedido pedido = new Pedido();
        Direccion direccion = new Direccion();
        direccion.setId(1L);
        pedido.setDireccion(direccion);

        DetallePedido detalle = new DetallePedido();
        Producto producto = new Producto();
        producto.setId(1L);
        detalle.setProducto(producto);
        detalle.setCantidad(5);

        pedido.setDetallesPedido(Collections.singletonList(detalle));

        Estado estadoPendiente = new Estado();
        estadoPendiente.setCodigo("PN");

        Mockito.when(direccionRepository.findById(1L)).thenReturn(Optional.of(direccion));
        Mockito.when(productoService.tieneStock(1L, 5)).thenReturn(true);
        Mockito.when(detallePedidoService.setTotal(any())).thenReturn(100L);
        Mockito.when(estadoService.findByCodigo("PN")).thenReturn(Optional.of(estadoPendiente));
        Mockito.when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pedido result = pedidoService.createPedido(pedido);

        assertNotNull(result);
        assertEquals(1, result.getDetallesPedido().size());
        assertEquals(100L, result.getDetallesPedido().get(0).getPrecio());
        assertEquals("PN", result.getEstado().getCodigo());
    }

    // CPU02 - Crear Pedido - Dirección No Encontrada
    @Test
    void testCreatePedidoDireccionNotFound() {
        Pedido pedido = new Pedido();
        Direccion direccion = new Direccion();
        direccion.setId(1L);
        pedido.setDireccion(direccion);

        Mockito.when(direccionRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> pedidoService.createPedido(pedido));
        assertEquals("La dirección no existe", exception.getMessage());
    }

    // CPU03 - Actualizar Pedido - No Encontrado
    @Test
    void testUpdatePedidoNotFound() {
        Mockito.when(pedidoRepository.findById(anyLong())).thenReturn(Optional.empty());

        Pedido pedido = new Pedido();

        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> pedidoService.updatePedido(1L, pedido));
        assertEquals("Pedido no encontrado con id: 1", exception.getMessage());
    }

    // CPU04 - Actualizar Estado Exitoso
    @Test
    void testActualizarEstadoSuccess() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);

        Estado estadoActual = new Estado();
        estadoActual.setCodigo("PN");
        pedido.setEstado(estadoActual);

        Estado estadoNuevo = new Estado();
        estadoNuevo.setCodigo("EP");

        Mockito.when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        Mockito.when(estadoService.findByCodigo("EP")).thenReturn(Optional.of(estadoNuevo));
        Mockito.when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pedido result = pedidoService.actualizarEstado(pedido, "EP");

        assertNotNull(result);
        assertEquals("EP", result.getEstado().getCodigo());
    }

    // CPU05 - Actualizar Estado - Transición Inválida
    @Test
    void testActualizarEstadoInvalidTransition() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);

        Estado estadoActual = new Estado();
        estadoActual.setCodigo("EN");
        pedido.setEstado(estadoActual);

        Estado estadoNuevo = new Estado();
        estadoNuevo.setCodigo("EP");

        Mockito.when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        Mockito.when(estadoService.findByCodigo("EP")).thenReturn(Optional.of(estadoNuevo));

        Exception exception = assertThrows(IllegalStateException.class,
                () -> pedidoService.actualizarEstado(pedido, "EP"));
        assertEquals("No se puede pasar a enPreparacion", exception.getMessage());
    }

    @Test
    void testActualizarEstadoEstadoNotFound() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);

        Estado estadoActual = new Estado();
        estadoActual.setCodigo("PN");
        pedido.setEstado(estadoActual);

        Mockito.when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        Mockito.when(estadoService.findByCodigo("EP")).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> pedidoService.actualizarEstado(pedido, "EP"));
        assertEquals("Estado no encontrado con código: EP", exception.getMessage());
    }

    @Test
    void testCancelarPedidoSuccess() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);

        // Estado inicial permitido
        Estado estadoActual = new Estado();
        estadoActual.setCodigo("pendiente"); // Cambiar a "pendiente" para coincidir con la lógica
        pedido.setEstado(estadoActual);

        // Estado final
        Estado estadoCancelado = new Estado();
        estadoCancelado.setCodigo("cancelado");

        // Configuración de los mocks
        Mockito.when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        Mockito.when(estadoService.findByCodigo("cancelado")).thenReturn(Optional.of(estadoCancelado));
        Mockito.when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Ejecución del método
        pedidoService.cancelarPedido(1L);

        // Verificaciones
        assertEquals("cancelado", pedido.getEstado().getCodigo()); // Verificar el estado actualizado
        Mockito.verify(pedidoRepository).save(pedido);
    }

    @Test
    void testCancelarPedidoInvalidState() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);

        Estado estadoActual = new Estado();
        estadoActual.setCodigo("ET");
        pedido.setEstado(estadoActual);

        Mockito.when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        Exception exception = assertThrows(InvalidEntityException.class, () -> pedidoService.cancelarPedido(1L));
        assertEquals("Solo se puede cancelar un pedido en estado 'preparación' o 'pendiente' ", exception.getMessage());
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

    /* CPI 11 */
    private Pedido pedido;
    private Estado pendiente;
    private Estado enviado;
    private Estado entregado;

    @Test
    public void testTransicionDeEstadoPedido() {

        MockitoAnnotations.openMocks(this);
        Estado estadoPendiente = new Estado(1L, "Pendiente");
        pedido = new Pedido();
        pedido.setId(1L);
        pedido.setEstado(estadoPendiente);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(estadoRepository.findByNombre("Enviado")).thenReturn(Optional.of(new Estado(2L, "Enviado")));
        when(estadoRepository.findByNombre("Entregado")).thenReturn(Optional.of(new Estado(3L, "Entregado")));
    }

    @Test
    public void testTransicionDeEstado() {
        Estado estadoPendiente = new Estado();
        estadoPendiente.setId(1L);
        estadoPendiente.setNombre("Pendiente");
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setEstado(estadoPendiente);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(estadoRepository.findByNombre("Enviado")).thenReturn(Optional.of(new Estado(2L, "Enviado")));
        when(estadoRepository.findByNombre("Entregado")).thenReturn(Optional.of(new Estado(3L, "Entregado")));

        assertNotNull(pedido.getEstado(), "El estado del pedido no debe ser null");
        assertEquals("Pendiente", pedido.getEstado().getNombre(), "El estado debe ser Pendiente");

        pedidoService.cambiarEstadoPedido(1L, "Enviado");
        verify(pedidoRepository).save(pedido);
        assertEquals("Enviado", pedido.getEstado().getNombre(), "El estado debe haber cambiado a Enviado");

        pedidoService.cambiarEstadoPedido(1L, "Entregado");
        verify(pedidoRepository, times(2)).save(pedido);
        assertEquals("Entregado", pedido.getEstado().getNombre(), "El estado debe haber cambiado a Entregado");
    }

}
