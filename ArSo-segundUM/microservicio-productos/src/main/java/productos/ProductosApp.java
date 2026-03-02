package productos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import productos.modelo.EstadoProducto;
import productos.servicio.ServicioProductos;

@SpringBootApplication
public class ProductosApp {
	@Autowired
    private ServicioProductos servicioProductos;
	public static void main(String[] args) {
		SpringApplication.run(ProductosApp.class, args);
	}
	public void run(String... args) throws Exception {
        System.out.println("\n--- INICIANDO PRUEBA DE BASE DE DATOS ---");

        try {
            // ⚠️ IMPORTANTE: Pon aquí un ID de categoría y de vendedor que SEPAS 
            // que existen en tu tabla de base de datos para que pase tus validaciones.
            String idCategoriaReal = "1"; // Sustituye por un ID real
            String idVendedorReal = "1";  // Sustituye por un ID real

            // 1. Llamamos a TU función altaProducto exacta
            System.out.println("Intentando dar de alta un producto...");
            String idGenerado = servicioProductos.altaProducto(
                "Bicicleta estática",          // titulo
                "Casi nueva, solo usada de perchero", // descripcion
                120.50,                        // precio
                EstadoProducto.NUEVO,          // estado (ajusta al valor de tu Enum)
                idCategoriaReal,               // idCategoria
                false,                         // envio
                idVendedorReal                 // idVendedor
            );
            System.out.println("✅ ¡Éxito! Producto dado de alta con ID: " + idGenerado);

            // 2. Llamamos a TU función para obtenerlo 
            // (Asumo que devuelve un Producto, cambia el tipo si devuelve ProductoDTO)
            var productoRecuperado = servicioProductos.obtenerProducto(idGenerado);
            System.out.println("✅ ¡Éxito! Producto recuperado de BD.");
            
        } catch (Exception e) {
            System.err.println("❌ Error en la prueba: " + e.getMessage());
        }

        System.out.println("-----------------------------------------\n");
    }
}
