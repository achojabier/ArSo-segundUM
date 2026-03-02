package productos.repositorio;

import java.util.List;

import productos.modelo.Categoria;

public interface IRepositorioCategorias {
	
	public void add(Categoria c);
	
	public Categoria get(String id);
	
	public void update(Categoria c);
}
