/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Service.java to edit this template
 */
package com.IS2024.Megastore.services;

import com.IS2024.Megastore.Exceptions.InvalidEntityException;
import com.IS2024.Megastore.Exceptions.ResourceNotFoundException;
import com.IS2024.Megastore.entities.DetallePedido;
import com.IS2024.Megastore.entities.Estado;
import com.IS2024.Megastore.entities.Pedido;
import com.IS2024.Megastore.entities.Producto;
import com.IS2024.Megastore.repositories.PedidoRepository;
import com.IS2024.Megastore.repositories.ProductoRepository;
import jakarta.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDate;
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
    private ProductoRepository productoRepository;

    public Optional<Pedido> findById(Long id) {
        return this.repository.findById(id);
    }

    public Pedido getById(Long id) {
        return this.repository.getById(id);
    }

    public List<Pedido> findAll() {
        return this.repository.findAll();
    }

    // confirmar pedido y actualizar stock

    public Pedido confirmarPedido(Pedido pedido) {
        /*
         * for (DetallePedido detalle : pedido.getDetallesPedido()) {
         * Optional<Producto> productoOptional =
         * productoRepository.findById(detalle.getProducto().getId());
         * }
         * if (productoOptional.isPresent()) {
         * Producto producto = productoOptional.get();
         * int nuevoStock = producto.getStock() - detalle.getCantidad();
         * if (nuevoStock<0){
         * throw new InvalidEntityException("No hay suficiente stock del producto");
         * + producto.getNombre();}
         * producto.setStock(nuevoStock);
         * productoRepository.save(producto);}
         * else {
         * throw new ResourceNotFoundException("Producto no encontrado con id:" +
         * detalle.getProducto().getId());
         * }
         * return repository.save(pedido);
         * }
         * 
         * 
         * Optional<Producto> productoOptional =
         * productoRepository.findById(pedido.getProducto().getId());
         * if (productoOptional.isPresent()) {
         * Producto producto = productoOptional.get();
         * int nuevoStock = producto.getStock() - pedido.getCantidad();
         * if (nuevoStock < 0) {
         * throw new InvalidEntityException("No hay suficiente stock para el producto: "
         * + producto.getNombre());
         * }
         * producto.setStock(nuevoStock);
         * productoRepository.save(producto);
         * return repository.save(pedido);
         * } else {
         * throw new ResourceNotFoundException("Producto no encontrado con id: " +
         * pedido.getProducto().getId());
         * }
         */
        Optional<Producto> productoOptional = productoRepository.findById(pedido.getProducto().getId());
        if (productoOptional.isPresent()) {
            Producto producto = productoOptional.get();
            int nuevoStock = producto.getStock() - pedido.getCantidad();
            if (nuevoStock < 0) {
                throw new InvalidEntityException("No hay suficiente stock para el producto: " + producto.getNombre());
            }
            producto.setStock(nuevoStock);
            productoRepository.save(producto);
            return repository.save(pedido);
        } else {
            throw new ResourceNotFoundException("Producto no encontrado con id: " + pedido.getProducto().getId());
        }
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

    public Pedido actualizarEstado(Pedido pedido, String codigoEstado) {
        Optional<Pedido> pedidoExistente = this.repository.findById(pedido.getId());
        if (pedidoExistente.isPresent()) {
            Pedido p = pedidoExistente.get();

            Optional<Estado> estado = this.serviceEstado.findByCodigo(codigoEstado);

            if (estado.isPresent()) {
                switch (codigoEstado) {
                    case "PN": // solo se puede setear si es null o esta en preparacion
                        if (pedido.getEstado() != null && !pedido.getEstado().getCodigo().equals("EP")) {
                            throw new IllegalStateException("No se puede pasar a pendiente");
                        }
                        break;
                    case "EP": // solo se puede pasar desde el estao Pendiente
                        if (pedido.getEstado() == null || !pedido.getEstado().getCodigo().equals("PN")) {
                            throw new IllegalStateException("No se puede pasar a enPreparacion");
                        }
                        break;
                    case "ET": // solo se puede pasar desde enPreparacion
                        if (pedido.getEstado() == null || !pedido.getEstado().getCodigo().equals("EP")) {
                            throw new IllegalStateException("No se puede entregar");
                        }
                        break;
                    case "CN": // no se puede cancelar si esta entregado
                        if (pedido.getEstado() == null && pedido.getEstado().getCodigo().equals("EN")) {
                            throw new IllegalStateException("No se puede cancelar");
                        }
                        break;
                }
                p.setEstado(estado.get());
            } else {
                throw new ResourceNotFoundException("estado no encontrado con codigo: " + codigoEstado);
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
}
