package productos.repositorio;

import productos.modelo.Usuario;

public interface IRepositorioUsuariosAdHoc extends IRepositorioUsuarios {
	public Usuario getByEmail(String email);
}
