/*package com.IS2024.Megastore.test;

import org.junit.jupiter.api.Test;

import com.IS2024.Megastore.entities.Usuario;
import com.IS2024.Megastore.services.UsuarioService;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

import org.hibernate.annotations.TimeZoneStorage;
 
public class UsuarioServiceTest {
    private final UsuarioService UsuarioService = new UsuarioService();

    @Test

    public void testLongitudContraseñaMenorA8() {
        Usuario usuario = mock(Usuario.class);
        when(usuario.getContrasenia()).thenReturn("short");

        assertFalse(UsuarioService.esLongitudContraseñaValida(usuario.getContrasenia()), 
            "La contraseña debería ser inválida para menos de 8 caracteres");
    }

    @Test 
    public void testLongitudContraseñaMayorOIgualA8(){
        Usuario usuario = mock(Usuario.class);
        when(usuario.getContrasenia()).thenReturn("longpassword");

        assertTrue(UsuarioService.esLongitudContraseñaValida(usuario.getContrasenia()),
        " La contraseña debe ser válida para 8 o caracteres o más");
    }

    @Test 
    public void testRegistrarUsuarioConContraseniaInvalida(){
        Usuario usuario = mock(Usuario.class);
        when (usuario.getContrasenia().thenReturn("short"));

        Exception exception assertThrows(IllegalArgumentException.class () -> {
            usuarioService.registrarUsuario(usuario);
        });

        assertEquals("La contraseña debe tener al menos 8 caracteres.", exception.getMessage());
    }

    @Test
    public void testRegistrarUsuarioConContraseñaValida() {
        Usuario usuario = mock(Usuario.class);
        when(usuario.getContrasenia()).thenReturn("validpassword");

        assertTrue(UsuarioService.registrarUsuario(usuario), 
            "El usuario debería registrarse correctamente con una contraseña válida");
    }

    }

*/



