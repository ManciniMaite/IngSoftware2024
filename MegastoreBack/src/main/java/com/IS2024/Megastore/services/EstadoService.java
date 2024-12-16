/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Service.java to edit this template
 */
package com.IS2024.Megastore.services;

import com.IS2024.Megastore.entities.Estado;
import com.IS2024.Megastore.repositories.EstadoRepository;
import com.IS2024.Megastore.Exceptions.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author maite
 */
@Service
public class EstadoService {

    @Autowired
    private EstadoRepository estadoRepository;

    public List<Estado> findAll() {
        return estadoRepository.findAll();
    }

    public Optional<Estado> findById(Long id) {
        return this.estadoRepository.findById(id);
    }

    public Estado createEstado(Estado estado) {
        return estadoRepository.save(estado);
    }

    public Estado updateEstado(Long id, Estado estadoDetails) {
        Optional<Estado> existingEstado = estadoRepository.findById(id);

        if (existingEstado.isPresent()) {
            Estado updatedEstado = existingEstado.get();
            updatedEstado.setNombre(estadoDetails.getNombre());
            updatedEstado.setCodigo(estadoDetails.getCodigo());

            return estadoRepository.save(updatedEstado);
        } else {
            throw new ResourceNotFoundException("Estado no encontrado con id: " + id);
        }
    }

    public void deleteById(Long id) {
        if (estadoRepository.existsById(id)) {
            estadoRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Estado no encontrado con id: " + id);
        }
    }
    
    public Optional<Estado> findByCodigo(String codigo){
        return this.estadoRepository.findByCodigo(codigo);
    }
}
