/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.IS2024.Megastore.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import java.util.List;
import lombok.Data;

/**
 *
 * @author maite
 */
@Data
@Entity
public class Producto {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    private String nombre;
    private int stock;
    private Long precio;
    private String codigo;
    @ManyToOne
    @JoinColumn(name="id_categoria", nullable=false)
    private CategoriaProducto categoria;
    @ManyToMany
    @JoinTable(
        name = "producto_x_variante",
        joinColumns = @JoinColumn(name = "id_producto"),
        inverseJoinColumns = @JoinColumn(name = "id_variante")
    )
    private List<VarianteProducto> variantes;
    @Lob 
    private String foto;
}
