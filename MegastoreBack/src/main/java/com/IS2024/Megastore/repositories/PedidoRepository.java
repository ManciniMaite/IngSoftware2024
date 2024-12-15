/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Repository.java to edit this template
 */
package com.IS2024.Megastore.repositories;

import com.IS2024.Megastore.entities.Pedido;
import com.IS2024.Megastore.entities.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.*;

/**
 *
 * @author maite
 */

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuarioId(Long usuarioId);

    // Método para obtener pedidos filtrados por estado y rango de fechas
    List<Pedido> findByEstadoAndFechaEntregaBetween(Estado estado, LocalDate fechaInicio, LocalDate fechaFin);

    List<Pedido> findByEstadoAndFechaCancelacionBetween(Estado estado, LocalDate fechaInicio, LocalDate fechaFin);

}
