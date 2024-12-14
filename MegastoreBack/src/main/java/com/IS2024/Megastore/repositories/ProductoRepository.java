/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Repository.java to edit this template
 */
package com.IS2024.Megastore.repositories;

import com.IS2024.Megastore.entities.Producto;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author maite
 */
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Optional<Producto> findByCodigo(String codigo);

    @Query("SELECT p FROM Producto p LEFT JOIN FETCH p.variantes v WHERE p.id = :id")
    Producto findByIdWithVariantes(@Param("id") Long id);

    List<Producto> findByPrecioLessThanEqual(Double precio);

}
