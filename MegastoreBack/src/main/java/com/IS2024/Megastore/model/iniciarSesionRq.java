/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.IS2024.Megastore.model;

import lombok.Data;

 /**
 * Clase para el request de iniciar sesión.
 * @author maite
 */
@Data
public class iniciarSesionRq {
    private String correo;
    private String contrasenia;
}
