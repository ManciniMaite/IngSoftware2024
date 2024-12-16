/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.IS2024.Megastore.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 *
 * @author maite
 */

 @Data
 @Entity
 public class Usuario {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    private String nombre;
    private String apellido;
    private String correo;
    private String contrasenia;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_usuario")
    private List<Direccion> direcciones = new ArrayList<>(); // Inicialización predeterminada

    private String nroTelefono;

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false) // Asocia con la tabla Rol
    private Rol rol;
 }
 