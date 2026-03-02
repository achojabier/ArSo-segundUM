package productos;

import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import productos.modelo.EstadoProducto;
import productos.modelo.Producto;
import productos.repositorio.*;
import productos.servicio.ServicioProductos;

public class ProgramaServicio {
public static void main(String[] args) {
		
		ConfigurableApplicationContext contexto = SpringApplication.run(ProductosApp.class, args);
		
		ServicioProductos servicio = contexto.getBean(ServicioProductos.class);
		String id = servicio.altaProducto("Coche", "Coche", 1, EstadoProducto.ACEPTABLE, "0", false, "3");
		
		servicio.sumarVisualizacion(id);
		
		System.out.println("Visualizaciones: " + servicio.obtenerProducto(id).getVisualizaciones());
		
		contexto.close();
	}
}
