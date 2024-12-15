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
import java.sql.Date;
import java.util.List;
import lombok.Data;

/**
 *
 * @author maite
 */
@Entity
@Data
public class Pedido {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_pedido") // Esta columna irá en DetallePedido
    private List<DetallePedido> detallesPedido;
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
    @ManyToOne
    @JoinColumn(name = "id_direccion", nullable = false)
    private Direccion direccion;

    private Date fechaPedido;
    private Date fechaEntrega;
    private Date fechaCancelacion;
    @ManyToOne
    @JoinColumn(name = "id_estado", nullable = false)
    private Estado estado;
    private long precio;
}
