package productos.repositorio;

import java.util.List;

import productos.modelo.Categoria;
import productos.modelo.EstadoProducto;
import productos.modelo.Producto;

public interface IRepositorioProducto {
	
	public void add(Producto p);
	
	public Producto get(String id);
	
	public void update(Producto p);
	
}
