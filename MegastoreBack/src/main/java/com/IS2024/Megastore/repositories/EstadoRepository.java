/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Repository.java to edit this template
 */
package com.IS2024.Megastore.repositories;

import com.IS2024.Megastore.entities.Estado;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author maite
 */
public interface EstadoRepository extends JpaRepository<Estado, Long> {
    Optional<Estado> findByCodigo(String codigo);

    Optional<Estado> findByNombre(String codigo);

}
