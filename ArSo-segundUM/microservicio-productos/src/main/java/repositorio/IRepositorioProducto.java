package repositorio;

import java.util.List;

import modelo.Categoria;
import modelo.EstadoProducto;
import modelo.Producto;

public interface IRepositorioProducto {
	
	public void add(Producto p);
	
	public Producto get(String id);
	
	public void update(Producto p);
	
}
