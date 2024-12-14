package com.IS2024.Megastore.PruebasUnitarias;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.checkerframework.checker.units.qual.s;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Collections;

import com.IS2024.Megastore.entities.Usuario;
import com.IS2024.Megastore.repositories.UsuarioRepository;
import com.IS2024.Megastore.services.UsuarioService;
import com.IS2024.Megastore.Exceptions.InvalidEntityException;
import com.IS2024.Megastore.entities.Direccion;

public class UsuarioServiceTest {
    /* CPU 1 - validar longitud mínima de contraseña */

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validarLongitudMinimaContrasenia() {
        InvalidEntityException thrown = assertThrows(InvalidEntityException.class,
                () -> usuarioService.validarContrasenia("pass"));

        assertEquals("La contraseña debe tener entre 8 y 20 caracteres", thrown.getMessage());
    }

    /* CPU 2 - validar longitud máxima de contraseña */

    @Test
    void validarLongitudMaximaContrasenia() {
        InvalidEntityException thrown = assertThrows(InvalidEntityException.class,
                () -> usuarioService.validarContrasenia("contraseñaMuyLarga"));

        assertEquals("La contraseña debe tener entre 8 y 20 caracteres", thrown.getMessage());
    }

    /* CPU4 - validar campos vacíos en el formulario de registro de usuario */

    @Test
    void validarCamposVaciosEnFrmulario() {
        // caso correo vacío
        Usuario usuarioConCorreoVacio = new Usuario();
        usuarioConCorreoVacio.setNombre("Juan");
        usuarioConCorreoVacio.setApellido("Perez");
        usuarioConCorreoVacio.setContrasenia("password123");
        usuarioConCorreoVacio.setDirecciones(Collections.emptyList());
        usuarioConCorreoVacio.setNroTelefono("123456789");

        InvalidEntityException thrownCorreo = assertThrows(InvalidEntityException.class,
                () -> usuarioService.createUsuario(usuarioConCorreoVacio));
        assertEquals("El correo es un campo obligatorio.", thrownCorreo.getMessage());

        // caso campos de direcciones vacío
        Usuario usuarioConDireccionVacia = new Usuario();
        usuarioConDireccionVacia.setNombre("Juan");
        usuarioConDireccionVacia.setApellido("Perez");
        usuarioConDireccionVacia.setCorreo("juan.perez@example.com");
        usuarioConDireccionVacia.setContrasenia("password123");

        InvalidEntityException thrownDireccion = assertThrows(InvalidEntityException.class,
                () -> usuarioService.createUsuario(usuarioConDireccionVacia));
        assertEquals("El domicilio es un campo obligatorio", thrownDireccion.getMessage());
    }

    /*
     * CPU 5 validar formato de numero de telegfono
     */

    @Test
    void validarFormatoNroTelefono() {

        InvalidEntityException thrownTelefono = assertThrows(InvalidEntityException.class,
                () -> usuarioService.validarNumeroTelefono("Formato de telefono incorrecto"));

        assertEquals("El formato de numero de telefono no es correcto", thrownTelefono.getMessage());
    }

}
