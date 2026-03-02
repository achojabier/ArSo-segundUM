package productos.repositorio;

import productos.modelo.Usuario;

public interface IRepositorioUsuarios {
	
	public void add(Usuario u);
	
	public Usuario get(String id);
	
	public void update(Usuario u);
	
}
