package productos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import productos.modelo.EstadoProducto;
import productos.servicio.ServicioProductos;

@SpringBootApplication
public class ProductosApp {
	public static void main(String[] args) {
		SpringApplication.run(ProductosApp.class, args);
	}
}
