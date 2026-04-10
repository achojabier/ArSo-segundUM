package productos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import productos.modelo.EstadoProducto;
import productos.servicio.ServicioCategorias;
import productos.servicio.ServicioProductos;

@SpringBootApplication
public class ProductosApp {
	public static void main(String[] args) {
		SpringApplication.run(ProductosApp.class, args);
	}
	
	@Bean
	CommandLineRunner initCategorias(ServicioCategorias servicioCategorias) {
		return args -> {
			System.out.println("Buscando archivos XML de categorías en la carpeta resources/Categorias...");
			
			PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			
			try {
				Resource[] archivosXml = resolver.getResources("classpath*:Categorias/*.xml");
				
				if (archivosXml.length == 0) {
					System.out.println("No se encontró ningún archivo XML en la carpeta Categorias.");
				} else {
					for (Resource archivo : archivosXml) {
						String nombreArchivo = archivo.getFilename();
						System.out.println("▶Procesando archivo: " + nombreArchivo);
						
						servicioCategorias.cargarJerarquia("/Categorias/" + nombreArchivo);
					}
					System.out.println("Categorías cargadas.");
				}
				
			} catch (Exception e) {
				System.err.println("Error al escanear la carpeta de categorías: " + e.getMessage());
				e.printStackTrace();
			}
		};
	}
}
