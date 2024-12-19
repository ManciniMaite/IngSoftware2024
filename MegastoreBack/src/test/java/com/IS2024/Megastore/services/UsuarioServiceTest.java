package com.IS2024.Megastore.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import com.IS2024.Megastore.entities.Usuario;
import com.IS2024.Megastore.repositories.RolRepository;
import com.IS2024.Megastore.repositories.UsuarioRepository;
import com.IS2024.Megastore.Exceptions.InvalidEntityException;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.extension.ExtendWith;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {
    /*
     * CPU 1 - validar longitud mínima de contraseña
     * /*
     */
    @InjectMocks
    private UsuarioService usuarioService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    /*
     * @Test
     * void validarLongitudMinimaContrasenia() {
     * InvalidEntityException thrown = assertThrows(InvalidEntityException.class,
     * () -> usuarioService.validarContrasenia("pass"));
     * 
     * assertEquals("La contraseña debe tener entre 8 y 20 caracteres",
     * thrown.getMessage());
     * }
     */

    /*
     * CPU 2 - validar longitud máxima de contraseña
     * 
     * @Test
     * void validarLongitudMaximaContrasenia() {
     * InvalidEntityException thrown = assertThrows(InvalidEntityException.class,
     * () -> usuarioService.validarContrasenia("contraseñaMuyLarga"));
     * 
     * assertEquals("La contraseña debe tener entre 8 y 20 caracteres",
     * thrown.getMessage());
     * }
     */

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

    }

    /*
     * CPU 5 validar formato de numero de telegfono
     * 
     * 
     * @Test
     * void validarFormatoNroTelefono() {
     * 
     * InvalidEntityException thrownTelefono =
     * assertThrows(InvalidEntityException.class,
     * () ->
     * usuarioService.validarNumeroTelefono("Formato de telefono incorrecto"));
     * 
     * assertEquals("El formato de numero de telefono no es correcto",
     * thrownTelefono.getMessage());
     * }
     */

    @Mock
    private RolRepository rolRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    /*
     * CPI 1
     * 
     * 
     * @Test
     * void registrarUsuarioConNombreMinimo() {
     * usuario = new Usuario();
     * usuario.setNombre("A");
     * usuario.setCorreo("usuario@dominio.com");
     * usuario.setContrasenia("contrasena123");
     * 
     * when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
     * 
     * usuarioService.createUsuario(usuario);
     * 
     * verify(usuarioRepository, times(1)).save(usuario);
     * 
     * assertEquals("A", usuario.getNombre()); // verifica que no sea null
     * assertEquals("usuario@dominio.com", usuario.getCorreo());
     * assertEquals("contrasena123", usuario.getContrasenia());
     * 
     * }
     */

    /* CPU 2 */
    // @Test
    // public void testRegistrarUsuarioConNombreDe50Caracteres() {
    // usuarioValido = new Usuario();
    // usuarioValido.setNombre(
    // "A".repeat(50)); // 50 caracteres
    // usuarioValido.setApellido("ApellidoTest");
    // usuarioValido.setCorreo("test@correo.com");
    // usuarioValido.setContrasenia("12345678");
    // usuarioValido.setNroTelefono("1234567890");

    // when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioValido);
    // Usuario usuarioGuardado = usuarioService.createUsuario(usuarioValido);

    // assertEquals(50, usuarioGuardado.getNombre().length(),
    // "El nombre debe tener 50 caracteres.");

    // verify(usuarioRepository, times(1)).save(usuarioValido);

    // assertEquals("test@correo.com", usuarioGuardado.getCorreo(),
    // "El correo no se guardó correctamente.");
    // assertEquals("12345678", usuarioGuardado.getContrasenia(),
    // "La contraseña no se guardó correctamente.");
    // assertEquals("1234567890", usuarioGuardado.getNroTelefono(),
    // "El número de teléfono no se guardó correctamente.");

    // }

    /* CPI 5 */

    @Test
    void registrarUsuarioConCorreoValido() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Juan");
        usuario.setCorreo("usuario@dominio.com");
        usuario.setContrasenia("contrasena123");

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario registrado = usuarioService.createUsuario(usuario);

        assertNotNull(registrado);

        assertEquals("usuario@dominio.com", registrado.getCorreo());

        assertEquals("Juan", registrado.getNombre());
        assertEquals("contrasena123", registrado.getContrasenia());

        verify(usuarioRepository, times(1)).save(usuario);
    }
}
