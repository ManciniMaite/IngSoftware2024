/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/RestController.java to edit this template
 */
package com.IS2024.Megastore.controller;

import com.IS2024.Megastore.Exceptions.ResourceNotFoundException;
import com.IS2024.Megastore.entities.DetallePedido;
import com.IS2024.Megastore.entities.Pedido;
import com.IS2024.Megastore.services.PedidoService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author maite
 */
@RestController
@RequestMapping("/pedido")
public class PedidoRestController {
    @Autowired
    private PedidoService service;

    @GetMapping("/getAll")
    public List<Pedido> list() {
        return this.service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        Optional<Pedido> pedido = this.service.findById(id);
        if (pedido.isPresent()) {
            return new ResponseEntity<>(pedido.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable Long id, @RequestBody Pedido input) {
        try {
            Pedido updatedPedido = service.updatePedido(id, input);
            return ResponseEntity.ok(updatedPedido);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/insert")
    public ResponseEntity<?> post(@RequestBody Pedido input) {
        try {
            // Validar que el pedido incluya una dirección
            if (input.getDireccion() == null || input.getDireccion().getId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El pedido debe incluir una dirección válida.");
            }

            // Delegar al servicio para crear el pedido
            Pedido createdPedido = service.createPedido(input);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPedido);

        } catch (ResourceNotFoundException e) {
            // Si ocurre un error porque la dirección no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // Manejo genérico de errores
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear el pedido: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            this.service.deleteById(id);
            Optional<Pedido> pedido = this.service.findById(id);
            if (!pedido.isPresent()) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/actualizarEstado/{codigoEstado}")
    public ResponseEntity<?> actualizarEstado(@RequestBody Pedido input, @PathVariable String codigoEstado) {
        try {
            Pedido updatedPedido = service.actualizarEstado(input, codigoEstado);
            return ResponseEntity.ok(updatedPedido);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{userId}/pedidos")
    public ResponseEntity<List<Pedido>> getPedidosByUsuario(@PathVariable Long userId) {
        System.out.println("Buscando pedidos para el usuario con ID: " + userId);
        try {
            // Verificar si el usuario existe
            if (userId == null || userId <= 0) {
                System.out.println("UserId Back: " + userId);

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(List.of());
            }

            List<Pedido> pedidos = service.findPedidosByUsuarioId(userId);
            System.out.println("pedidos para el usuario con ID: " + pedidos);
            if (pedidos.isEmpty()) {
                // Devuelve un estado 404 con una lista vacía
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of());
            }
            // Devuelve la lista de pedidos con estado 200
            return ResponseEntity.ok(pedidos);
        } catch (Exception e) {
            // Mejor manejo de errores y logging
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }

    // Método para obtener los detalles de un pedido
    @GetMapping("/{id}/detalles")
    public ResponseEntity<?> getDetallesPedido(@PathVariable Long id) {
        Optional<Pedido> pedido = service.findById(id);
        if (pedido.isPresent()) {
            List<DetallePedido> detallesPedido = pedido.get().getDetallesPedido();
            return new ResponseEntity<>(detallesPedido, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint que recibe el estado y las fechas
    @GetMapping("/pedidoFiltrado")
    public List<Pedido> obtenerVentasPorPeriodo(

            @RequestParam("fechaDesde") String fechaDesde,
            @RequestParam("fechaHasta") String fechaHasta) {
        // Convertir las fechas de String a LocalDate
        LocalDate fechaInicio = LocalDate.parse(fechaDesde);
        LocalDate fechaFin = LocalDate.parse(fechaHasta);

        // Obtener los pedidos filtrados por estado y rango de fecha
        return service.obtenerPedidosPorEstadoYFecha(fechaInicio, fechaFin);
    }

    @GetMapping("/tasaCancelacion")
    public ResponseEntity<Map<String, Object>> getTasaCancelacion(
            @RequestParam("fechaDesde") String fechaDesdeStr,
            @RequestParam("fechaHasta") String fechaHastaStr) {

        // Convertir las fechas de String a LocalDate
        LocalDate fechaDesde = LocalDate.parse(fechaDesdeStr);
        LocalDate fechaHasta = LocalDate.parse(fechaHastaStr);

        // Obtener los pedidos entregados
        List<Pedido> entregados = service.obtenerPedidosPorEstadoYFecha(fechaDesde, fechaHasta);

        // Obtener los pedidos cancelados
        List<Pedido> cancelados = service.obtenerPedidosCanceladosPorFecha(fechaDesde, fechaHasta);

        // Calcular las métricas
        int totalEntregados = entregados.size();
        int totalCancelados = cancelados.size();

        // Calcular tasa de cancelación
        double tasaCancelacion = totalEntregados > 0 ? (double) totalCancelados / totalEntregados * 100 : 0;

        // Aquí podrías devolver la información de forma más adecuada
        Map<String, Object> response = new HashMap<>();
        response.put("entregados", totalEntregados);
        response.put("cancelados", totalCancelados);
        response.put("tasaCancelacion", tasaCancelacion);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/categoriasMasVendidas")
    public ResponseEntity<List<Map<String, Object>>> obtenerCategoriasMasVendidas(
            @RequestParam("fechaDesde") String fechaDesdeStr,
            @RequestParam("fechaHasta") String fechaHastaStr) {
        LocalDate fechaDesde = LocalDate.parse(fechaDesdeStr);
        LocalDate fechaHasta = LocalDate.parse(fechaHastaStr);
        List<Map<String, Object>> categorias = service.obtenerCategoriasMasVendidas(fechaDesde, fechaHasta);
        return ResponseEntity.ok(categorias);
    }

}
