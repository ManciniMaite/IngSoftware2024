package com.IS2024.Megastore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.IS2024.Megastore.repositories.RolRepository;
import com.IS2024.Megastore.repositories.UsuarioRepository;
import com.IS2024.Megastore.repositories.CategoriaProductoRepository;
import com.IS2024.Megastore.repositories.EstadoRepository;
import com.IS2024.Megastore.repositories.ProductoRepository;
import com.IS2024.Megastore.repositories.PedidoRepository;
import com.IS2024.Megastore.repositories.VarianteProdutoRepository;
import com.IS2024.Megastore.repositories.TipoVarianteProductoRepository;
import com.IS2024.Megastore.entities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class MegastoreApplication implements CommandLineRunner {

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaProductoRepository categoriaProductoRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    @Qualifier("varianteProdutoRepository")
    private VarianteProdutoRepository varianteProductoRepository;

    @Autowired
    private TipoVarianteProductoRepository tipoVarianteProductoRepository;

    public static void main(String[] args) {
        SpringApplication.run(MegastoreApplication.class, args);
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Override
    public void run(String... args) throws Exception {
        // Precargar tipos de variante
        if (!tipoVarianteProductoRepository.findByCodigo("C").isPresent()) {
            TipoVarianteProducto tipoColor = new TipoVarianteProducto();
            tipoColor.setNombre("Color");
            tipoColor.setCodigo("C");
            tipoVarianteProductoRepository.save(tipoColor);
            System.out.println("Tipo de variante 'Color' precargado.");
        }

        if (!tipoVarianteProductoRepository.findByCodigo("T").isPresent()) {
            TipoVarianteProducto tipoTalle = new TipoVarianteProducto();
            tipoTalle.setNombre("Talle");
            tipoTalle.setCodigo("T");
            tipoVarianteProductoRepository.save(tipoTalle);
            System.out.println("Tipo de variante 'Talle' precargado.");
        }

        // Precargar variantes para 'Color'
        TipoVarianteProducto tipoColor = tipoVarianteProductoRepository.findByCodigo("C").get();
        TipoVarianteProducto tipoTalle = tipoVarianteProductoRepository.findByCodigo("T").get();

        // Cambiar el uso de isPresent() para la lista
        List<VarianteProducto> variantesColor = varianteProductoRepository.findByNombre("Rojo");
        if (variantesColor.isEmpty()) {
            VarianteProducto varianteColor1 = new VarianteProducto();
            varianteColor1.setNombre("Rojo");
            varianteColor1.setTipoVariante(tipoColor);
            varianteProductoRepository.save(varianteColor1);
            System.out.println("Variante de color " + varianteColor1.getNombre() + " precargado.");
        }

        // Precargar variantes para 'Color'
        if (varianteProductoRepository.findByNombre("Azul").isEmpty()) {
            VarianteProducto varianteColor2 = new VarianteProducto();
            varianteColor2.setNombre("Azul");
            varianteColor2.setTipoVariante(tipoColor);
            varianteProductoRepository.save(varianteColor2);
            System.out.println("Variante de color " + varianteColor2.getNombre() + " precargado.");
        }

        if (varianteProductoRepository.findByNombre("Verde").isEmpty()) {
            VarianteProducto varianteColor3 = new VarianteProducto();
            varianteColor3.setNombre("Verde");
            varianteColor3.setTipoVariante(tipoColor);
            varianteProductoRepository.save(varianteColor3);
            System.out.println("Variante de color " + varianteColor3.getNombre() + " precargado.");
        }

        if (varianteProductoRepository.findByNombre("Morado").isEmpty()) {
            VarianteProducto varianteColor4 = new VarianteProducto();
            varianteColor4.setNombre("Morado");
            varianteColor4.setTipoVariante(tipoColor);
            varianteProductoRepository.save(varianteColor4);
            System.out.println("Variante de color " + varianteColor4.getNombre() + " precargado.");
        }

        if (varianteProductoRepository.findByNombre("Naranja").isEmpty()) {
            VarianteProducto varianteColor5 = new VarianteProducto();
            varianteColor5.setNombre("Naranja");
            varianteColor5.setTipoVariante(tipoColor);
            varianteProductoRepository.save(varianteColor5);
            System.out.println("Variante de color " + varianteColor5.getNombre() + " precargado.");
        }

        if (varianteProductoRepository.findByNombre("Turquesa").isEmpty()) {
            VarianteProducto varianteColor6 = new VarianteProducto();
            varianteColor6.setNombre("Turquesa");
            varianteColor6.setTipoVariante(tipoColor);
            varianteProductoRepository.save(varianteColor6);
            System.out.println("Variante de color " + varianteColor6.getNombre() + " precargado.");
        }

        // Precargar variantes para 'Talle'
        if (varianteProductoRepository.findByNombre("S").isEmpty()) {
            VarianteProducto varianteTalle1 = new VarianteProducto();
            varianteTalle1.setNombre("S");
            varianteTalle1.setTipoVariante(tipoTalle);
            varianteProductoRepository.save(varianteTalle1);
            System.out.println("Variante de Talle " + varianteTalle1.getNombre() + " precargado.");
        }

        if (varianteProductoRepository.findByNombre("L").isEmpty()) {
            VarianteProducto varianteTalle2 = new VarianteProducto();
            varianteTalle2.setNombre("L");
            varianteTalle2.setTipoVariante(tipoTalle);
            varianteProductoRepository.save(varianteTalle2);
            System.out.println("Variante de Talle " + varianteTalle2.getNombre() + " precargado.");
        }

        if (varianteProductoRepository.findByNombre("M").isEmpty()) {
            VarianteProducto varianteTalle3 = new VarianteProducto();
            varianteTalle3.setNombre("M");
            varianteTalle3.setTipoVariante(tipoTalle);
            varianteProductoRepository.save(varianteTalle3);
            System.out.println("Variante de Talle " + varianteTalle3.getNombre() + " precargado.");
        }

        if (varianteProductoRepository.findByNombre("XL").isEmpty()) {
            VarianteProducto varianteTalle4 = new VarianteProducto();
            varianteTalle4.setNombre("XL");
            varianteTalle4.setTipoVariante(tipoTalle);
            varianteProductoRepository.save(varianteTalle4);
            System.out.println("Variante de Talle " + varianteTalle4.getNombre() + " precargado.");
        }

        if (varianteProductoRepository.findByNombre("XXL").isEmpty()) {
            VarianteProducto varianteTalle5 = new VarianteProducto();
            varianteTalle5.setNombre("XXL");
            varianteTalle5.setTipoVariante(tipoTalle);
            varianteProductoRepository.save(varianteTalle5);
            System.out.println("Variante de Talle " + varianteTalle5.getNombre() + " precargado.");
        }

        // Precargar roles
        if (!rolRepository.findByNombre("admin").isPresent()) {
            Rol adminRol = new Rol();
            adminRol.setNombre("admin");
            rolRepository.save(adminRol);
            System.out.println("Rol admin precargado.");
        }

        if (!rolRepository.findByNombre("usuario").isPresent()) {
            Rol usuarioRol = new Rol();
            usuarioRol.setNombre("usuario");
            rolRepository.save(usuarioRol);
            System.out.println("Rol usuario precargado.");
        }

        // Precargar un usuario administrador
        if (!usuarioRepository.findByCorreo("admin@example.com").isPresent()) {
            Usuario admin = new Usuario();
            admin.setNroTelefono("1111111111");
            admin.setCorreo("admin@example.com");
            admin.setContrasenia("admin123"); // Considera encriptar la contraseña
            admin.setNombre("Administrador");
            admin.setApellido("Sistema");
            admin.setRol(rolRepository.findByNombre("admin").get());
            usuarioRepository.save(admin);
        }

        // Precargar usuarios normales con direcciones
        if (!usuarioRepository.findByCorreo("user@example.com").isPresent()) {
            // Crear y guardar el primer usuario
            Usuario user1 = new Usuario();
            user1.setNroTelefono("2222222222");
            user1.setCorreo("user@example.com");
            user1.setContrasenia("user123"); // Considera encriptar la contraseña
            user1.setNombre("Usuario");
            user1.setApellido("Estandar");
            user1.setRol(rolRepository.findByNombre("usuario").get());

            // Crear y asociar direcciones al primer usuario
            Direccion direccion1 = new Direccion();
            direccion1.setCalle("Calle Falsa ");
            direccion1.setNumero("12345");

            Direccion direccion2 = new Direccion();
            direccion2.setCalle("Avenida Siempreviva ");
            direccion2.setNumero("11111");

            List<Direccion> direcciones1 = new ArrayList<>();
            direcciones1.add(direccion1);
            direcciones1.add(direccion2);
            user1.setDirecciones(direcciones1);

            usuarioRepository.save(user1);
            System.out.println("Usuario user@example.com precargado.");
        }

        // Precargar otros 5 usuarios normales con direcciones
        String[] correos = { "user1@example.com", "user2@example.com", "user3@example.com", "user4@example.com",
                "user5@example.com" };
        String[] nombres = { "User1", "User2", "User3", "User4", "User5" };
        String[] apellidos = { "Apellido1", "Apellido2", "Apellido3", "Apellido4", "Apellido5" };
        String[] telefonos = { "2222222233", "2222222244", "2222222255", "2222222266", "2222222277" };

        for (int i = 0; i < 5; i++) {
            String correo = correos[i];
            if (!usuarioRepository.findByCorreo(correo).isPresent()) {
                Usuario user = new Usuario();
                user.setNroTelefono(telefonos[i]);
                user.setCorreo(correo);
                user.setContrasenia("user123"); // Considera encriptar la contraseña
                user.setNombre(nombres[i]);
                user.setApellido(apellidos[i]);
                user.setRol(rolRepository.findByNombre("usuario").get());

                // Crear y asociar direcciones al usuario
                Direccion direccion = new Direccion();
                direccion.setCalle("Calle " + nombres[i]);
                direccion.setNumero("100" + telefonos[i].substring(9));

                List<Direccion> direcciones = new ArrayList<>();
                direcciones.add(direccion);
                user.setDirecciones(direcciones);

                // Guardar el usuario (también guarda las direcciones si hay cascada)
                usuarioRepository.save(user);
                System.out.println("Usuario " + correo + " precargado.");
            }
        }

        // Precargar categorías de productos
        String[] categorias = { "remeras", "pantalones", "shorts", "camperas", "buzos", "vestidos", "tops" };
        for (String nombre : categorias) {
            if (!categoriaProductoRepository.findByNombre(nombre).isPresent()) {
                CategoriaProducto categoria = new CategoriaProducto();
                categoria.setNombre(nombre);
                categoria.setDescripcion("Descripción de la categoría " + nombre);
                categoriaProductoRepository.save(categoria);
                System.out.println("Categoría " + nombre + " precargada.");
            }
        }

        // Precargar estados
        String[] estados = { "Pendiente", "En Preparación", "Cancelado", "Entregado" };
        String[] codigos = { "PN", "EP", "CN", "ET" }; // Códigos correspondientes

        for (int i = 0; i < estados.length; i++) {
            String estadoNombre = estados[i];
            String estadoCodigo = codigos[i];
            if (!estadoRepository.findByCodigo(estadoCodigo).isPresent()) {
                Estado estado = new Estado();
                estado.setNombre(estadoNombre);
                estado.setCodigo(estadoCodigo);
                estadoRepository.save(estado);
                System.out.println("Estado " + estadoNombre + " (" + estadoCodigo + ") precargado.");
            }
        }

        // Precargar un producto
        if (!productoRepository.findByCodigo("SPH-12345").isPresent()) {
            CategoriaProducto categoriaPantalones = categoriaProductoRepository.findByNombre("pantalones")
                    .orElseThrow(() -> new RuntimeException("Categoría 'pantalones' no encontrada"));

            // Crear un producto
            Producto producto = new Producto();
            producto.setNombre("Pantalon Vogue");
            producto.setStock(50);
            producto.setPrecio(500000); // 500000 como ejemplo de precio en centavos
            producto.setCodigo("SPH-12345");
            producto.setCategoria(categoriaPantalones);
            producto.setFoto(null);

            // Buscar las variantes de color
            List<VarianteProducto> variantesRojo = varianteProductoRepository.findByNombre("Rojo");
            List<VarianteProducto> variantesAzul = varianteProductoRepository.findByNombre("Azul");
            List<VarianteProducto> variantesVerde = varianteProductoRepository.findByNombre("Verde");

            // Buscar las variantes de talle
            List<VarianteProducto> variantesS = varianteProductoRepository.findByNombre("S");
            List<VarianteProducto> variantesM = varianteProductoRepository.findByNombre("M");
            List<VarianteProducto> variantesL = varianteProductoRepository.findByNombre("L");

            // Crear una lista con todas las variantes encontradas
            List<VarianteProducto> todasLasVariantes = new ArrayList<>();
            todasLasVariantes.addAll(variantesRojo);
            todasLasVariantes.addAll(variantesAzul);
            todasLasVariantes.addAll(variantesVerde);
            todasLasVariantes.addAll(variantesS);
            todasLasVariantes.addAll(variantesM);
            todasLasVariantes.addAll(variantesL);

            // Verificar que haya al menos una variante encontrada
            if (todasLasVariantes.isEmpty()) {
                throw new RuntimeException("No se encontraron variantes de productos necesarias.");
            }

            // Asociar las variantes al producto
            producto.setVariantes(todasLasVariantes);

            productoRepository.save(producto);
            System.out.println("Producto Pantalon precargado.");
        }

        System.out.println("Datos iniciales precargados exitosamente.");
    }
}
