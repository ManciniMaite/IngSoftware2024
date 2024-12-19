/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Service.java to edit this template
 */
package com.IS2024.Megastore.services;

import com.IS2024.Megastore.Exceptions.InvalidEntityException;
import com.IS2024.Megastore.Exceptions.ResourceNotFoundException;
import com.IS2024.Megastore.entities.DetallePedido;
import com.IS2024.Megastore.entities.Direccion;
import com.IS2024.Megastore.entities.Estado;
import com.IS2024.Megastore.entities.Pedido;
import com.IS2024.Megastore.repositories.PedidoRepository;
import com.IS2024.Megastore.repositories.DireccionRepository;
import com.IS2024.Megastore.repositories.EstadoRepository;
import jakarta.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author maite
 */
@Service
public class PedidoService {
    @Autowired
    private PedidoRepository repository;
    @Autowired
    private DetallePedidoService serviceDetalle;
    @Autowired
    private ProductoService serviceProducto;
    @Autowired
    private EstadoService serviceEstado;
    @Autowired
    private DireccionRepository direccionRepository;
    @Autowired
    private EstadoRepository estadoRepository;

    public Optional<Pedido> findById(Long id) {
        return this.repository.findById(id);
    }

    @SuppressWarnings("deprecation")
    public Pedido getById(Long id) {
        return this.repository.getById(id);
    }

    public void cambiarEstadoPedido(Long pedidoId, String nuevoEstado) {
        Pedido pedido = repository.findById(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado"));
        Estado estado = estadoRepository.findByNombre(nuevoEstado)
                .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado"));
        pedido.setEstado(estado);
        repository.save(pedido);
    }

    public List<Pedido> findAll() {
        return this.repository.findAll();
    }

    public Pedido updatePedido(Long id, Pedido pedido) {
        Optional<Pedido> existingPedido = this.repository.findById(id);
        if (existingPedido.isPresent()) {
            Pedido updatedPedido = existingPedido.get();

            // Detalles del pedido existente
            List<DetallePedido> detallesGuardados = updatedPedido.getDetallesPedido();
            // Detalles que vienen en el pedido a actualizar
            List<DetallePedido> detallesActualizados = pedido.getDetallesPedido();

            // Detalles a eliminar
            List<DetallePedido> detallesEliminar = new ArrayList<>();
            // Detalles a actualizar
            List<DetallePedido> detallesActualizar = new ArrayList<>();
            // Detalles a crear
            List<DetallePedido> detallesCrear = new ArrayList<>();

            // IDENTIFICAR DETALLES A ELIMINAR -> tienen que estar en
            // updatedPedido.getDetallesPedido() y no en pedido.getDetallesPedido();
            for (DetallePedido dp : detallesGuardados) {
                if (!detallesActualizados.stream().anyMatch(detalle -> detalle.getId().equals(dp.getId()))) {
                    detallesEliminar.add(dp);
                }
            }

            // IDENTIFICAR DETALLES A ACTUALIZAR Y CREAR
            // Para ACTUALIZAR id existente y no tiene que estar en los eliminar
            // Para CREAR sin ID o ID 0
            for (DetallePedido dp : detallesActualizados) {
                if (dp.getId() == 0 || dp.getId() == null) {
                    detallesCrear.add(dp);
                } else {
                    if (!detallesEliminar.stream().anyMatch(d -> d.getId().equals(dp.getId()))) {
                        detallesActualizar.add(dp);
                    }
                }
            }

            // ELIMINAR DETALLES
            for (DetallePedido dp : detallesEliminar) {
                this.serviceDetalle.deleteById(dp.getId());
            }

            // ACTUALIZAR DETALLES
            for (DetallePedido dp : detallesActualizar) {
                this.serviceDetalle.updateDetallePedido(dp.getId(), dp);
            }

            // Al hacer this.repository.save(updatedPedido); los detalles que no esten se
            // crean
            // Pero les hace falta setear el precio y actualizar el stock del producto
            for (DetallePedido dp : detallesCrear) {
                if (this.serviceProducto.tieneStock(dp.getProducto().getId(), dp.getCantidad())) {
                    dp.setPrecio(this.serviceDetalle.setTotal(dp));
                    // Actualizar stock de producto
                    this.serviceProducto.actualizarStock(dp.getProducto().getId(), (dp.getCantidad() * (-1)));
                } else {
                    throw new InvalidEntityException("NO HAY STOCK");
                }
            }

            // PRECIO
            updatedPedido.setPrecio(this.calcularTotalPedido(pedido));

            // El usuario y fecha no se modifica
            // El estado solo lo modifica el administrador y para eso hay otro metodo

            return this.repository.save(updatedPedido);
        } else {
            throw new ResourceNotFoundException("Pedido no encontrado con id: " + id);
        }
    }

    @Transactional
    public Pedido createPedido(Pedido pedido) {
        // FECHA
        LocalDate localDate = LocalDate.now();
        Date sqlDate = Date.valueOf(localDate);
        pedido.setFechaPedido(sqlDate);

        // Validar dirección
        Long direccionId = pedido.getDireccion().getId();
        Optional<Direccion> direccion = direccionRepository.findById(direccionId);
        if (direccion.isEmpty()) {
            throw new ResourceNotFoundException("La dirección no existe");
        }
        pedido.setDireccion(direccion.get());

        // Precio de detalles
        for (DetallePedido dp : pedido.getDetallesPedido()) {
            if (this.serviceProducto.tieneStock(dp.getProducto().getId(), dp.getCantidad())) {
                dp.setPrecio(this.serviceDetalle.setTotal(dp));
                // Actualizar stock de producto
                this.serviceProducto.actualizarStock(dp.getProducto().getId(), (dp.getCantidad() * (-1)));
            } else {
                throw new InvalidEntityException("NO HAY STOCK");
            }
        }

        // PRECIO
        pedido.setPrecio(this.calcularTotalPedido(pedido));

        // Estado
        Optional<Estado> estado = this.serviceEstado.findByCodigo("PN");
        if (estado.isPresent()) {
            pedido.setEstado(estado.get());
        } else {
            throw new IllegalStateException("OCURRIO UN ERROR AL BUSCAR EL ESTADO");
        }

        return this.repository.save(pedido);
    }

    public long calcularTotalPedido(Pedido pedido) {
        long total = 0;

        for (DetallePedido detalle : pedido.getDetallesPedido()) {
            total += detalle.getPrecio();
        }

        return total;
    }

    public Pedido asignarFechaEntrega(Pedido pedido) {
        // Verificamos si el estado del pedido es "ET" (Entregado)
        if ("ET".equals(pedido.getEstado().getCodigo())) {

            // FECHA
            LocalDate localDate = LocalDate.now();
            Date sqlDate = Date.valueOf(localDate);
            pedido.setFechaEntrega(sqlDate);
            // Setear la fecha de entrega cuando el estado sea "ET"
        }
        return pedido;
    }

    public Pedido asignarFechaCancelacion(Pedido pedido) {
        // Verificamos si el estado del pedido es "ET" (Entregado)
        if ("CN".equals(pedido.getEstado().getCodigo())) {

            // FECHA
            LocalDate localDate = LocalDate.now();
            Date sqlDate = Date.valueOf(localDate);
            pedido.setFechaCancelacion(sqlDate);
            // Setear la fecha de entrega cuando el estado sea "ET"
        }
        return pedido;
    }

    public Pedido actualizarEstado(Pedido pedido, String codigoEstado) {
        Optional<Pedido> pedidoExistente = this.repository.findById(pedido.getId());
        if (pedidoExistente.isPresent()) {
            Pedido p = pedidoExistente.get();

            Optional<Estado> estado = this.serviceEstado.findByCodigo(codigoEstado);

            if (estado.isPresent()) {
                switch (codigoEstado) {
                    case "PN": // solo se puede setear si es null o esta en preparacion
                        if (p.getEstado() != null && !p.getEstado().getCodigo().equals("EP")) {
                            throw new IllegalStateException("No se puede pasar a pendiente");
                        }
                        break;
                    case "EP":
                        System.out.println(
                                "Estado actual: " + (p.getEstado() == null ? "null" : p.getEstado().getCodigo()));
                        if (p.getEstado() == null || !p.getEstado().getCodigo().equals("PN")) {
                            throw new IllegalStateException("No se puede pasar a enPreparacion");
                        }
                        break;

                    case "ET": // solo se puede pasar desde enPreparacion
                        if (p.getEstado() == null && !p.getEstado().getCodigo().equals("EP")) {
                            throw new IllegalStateException("No se puede entregar");
                        }

                        break;
                    case "CN": // no se puede cancelar si esta entregado
                        if (p.getEstado() == null && p.getEstado().getCodigo().equals("EN")) {
                            throw new IllegalStateException("No se puede cancelar");
                        }
                        break;
                }
                p.setEstado(estado.get());
                asignarFechaEntrega(p);
                asignarFechaCancelacion(p);

            } else {
                throw new ResourceNotFoundException("Estado no encontrado con código: " + codigoEstado);
            }
            return this.repository.save(p);
        } else {
            throw new ResourceNotFoundException("Pedido no encontrado con id: " + pedido.getId());
        }
    }

    public void deleteById(Long id) {
        if (this.repository.existsById(id)) {
            this.repository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Pedido no encontrado con id: " + id);
        }
    }

    public List<Pedido> findPedidosByUsuarioId(Long userId) {
        try {
            // Lógica para obtener los pedidos del usuario
            return repository.findByUsuarioId(userId); // Suponiendo que tienes un repositorio
        } catch (Exception e) {
            // Log para cualquier error en el servicio
            System.err.println("Error al buscar pedidos para el usuario con ID: " + userId);
            e.printStackTrace();
            throw e; // Rethrow si necesitas capturar en el controlador
        }
    }

    // Método que obtiene los pedidos filtrados por estado y fecha de entrega
    public List<Pedido> obtenerPedidosPorEstadoYFecha(LocalDate fechaInicio, LocalDate fechaFin) {
        // Estado "ET" para entregado
        Estado estadoEntregado = estadoRepository.findByCodigo("ET")
                .orElseThrow(() -> new RuntimeException("Estado no encontrado")); // Manejo de error si no se encuentra
                                                                                  // de Estado

        // Llamada al repositorio para filtrar por estado "ET" y fechas
        return repository.findByEstadoAndFechaEntregaBetween(estadoEntregado, fechaInicio, fechaFin);
    }

    public List<Pedido> obtenerPedidosCanceladosPorFecha(LocalDate fechaInicio, LocalDate fechaFin) {
        // Estado "CN" para cancelado
        Estado estadoCancelado = estadoRepository.findByCodigo("CN")
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));

        // Log para depuración
        System.out.println("Estado cancelado: " + estadoCancelado);
        System.out.println("Fechas: " + fechaInicio + " a " + fechaFin);

        // Llamada al repositorio para filtrar por estado "CN" y fechas
        List<Pedido> pedidosCancelados = repository.findByEstadoAndFechaCancelacionBetween(estadoCancelado, fechaInicio,
                fechaFin);

        // Log para verificar la cantidad de pedidos encontrados
        System.out.println("Pedidos cancelados encontrados: " + pedidosCancelados.size());

        return pedidosCancelados;
    }

    public List<Map<String, Object>> obtenerCategoriasMasVendidas(LocalDate fechaDesde, LocalDate fechaHasta) {
        // Obtener el estado "ET" (Entregado)
        Estado estadoEntregado = estadoRepository.findByCodigo("ET")
                .orElseThrow(() -> new RuntimeException("Estado 'Entregado' no encontrado"));

        // Obtener todos los pedidos entregados en el rango de fechas
        List<Pedido> pedidosEntregados = repository.findByEstadoAndFechaEntregaBetween(estadoEntregado, fechaDesde,
                fechaHasta);

        // Crear un mapa para almacenar las categorías y sus cantidades acumuladas
        Map<String, Integer> categoriasCantidad = new HashMap<>();

        // Recorrer los pedidos entregados
        for (Pedido pedido : pedidosEntregados) {
            // Recorrer los detalles de cada pedido
            for (DetallePedido detalle : pedido.getDetallesPedido()) {
                // Obtener la categoría del producto
                String categoria = detalle.getProducto().getCategoria().getNombre();

                // Sumar la cantidad de productos al acumulado de la categoría
                categoriasCantidad.put(categoria,
                        categoriasCantidad.getOrDefault(categoria, 0) + detalle.getCantidad());
            }
        }

        // Convertir el mapa en una lista de mapas para retornar como respuesta
        List<Map<String, Object>> resultados = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : categoriasCantidad.entrySet()) {
            Map<String, Object> categoriaData = new HashMap<>();
            categoriaData.put("categoria", entry.getKey()); // Nombre de la categoría
            categoriaData.put("cantidad", entry.getValue()); // Cantidad acumulada
            resultados.add(categoriaData);
        }

        // Opcional: ordenar las categorías por cantidad descendente
        resultados.sort((a, b) -> Integer.compare((int) b.get("cantidad"), (int) a.get("cantidad")));

        return resultados;
    }

    // CANCELAR PEDIDO
    public void cancelarPedido(Long pedidoId) {
        Pedido pedido = this.repository.findById(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id " + pedidoId));
        if ("en preparación".equals(pedido.getEstado().getCodigo())
                || "pendiente".equals(pedido.getEstado().getCodigo())) {
            Estado estadoCancelado = this.serviceEstado.findByCodigo("cancelado")
                    .orElseThrow(() -> new ResourceNotFoundException("Estado 'cancelado' no encontrado"));
            pedido.setEstado(estadoCancelado);
            this.repository.save(pedido);
        } else {
            throw new InvalidEntityException("Solo se puede cancelar un pedido en estado 'preparación' o 'pendiente' ");
        }
    }

}
