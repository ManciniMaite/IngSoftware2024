/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Service.java to edit this template
 */
package com.IS2024.Megastore.services;

import com.IS2024.Megastore.entities.Rol;
import com.IS2024.Megastore.repositories.RolRepository;
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
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    public List<Rol> findAll() {
        return rolRepository.findAll();
    }

    public Optional<Rol> findById(Long id) {
        return this.rolRepository.findById(id);
    }

    public Rol save(Rol rol) {
        return rolRepository.save(rol);
    }

    public Rol update(Long id, Rol rolDetails) {
        Optional<Rol> existingRol = rolRepository.findById(id);

        if (existingRol.isPresent()) {
            Rol updatedRol = existingRol.get();
            updatedRol.setNombre(rolDetails.getNombre());

            return rolRepository.save(updatedRol);
        } else {
            throw new ResourceNotFoundException("Rol no encontrado con id: " + id);
        }
    }

    public void delete(Long id) {
        if (rolRepository.existsById(id)) {
            rolRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Rol no encontrado con id: " + id);
        }
    }
}
