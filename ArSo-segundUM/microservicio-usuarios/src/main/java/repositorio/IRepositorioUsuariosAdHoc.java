package repositorio;

import modelo.Usuario;

public interface IRepositorioUsuariosAdHoc extends IRepositorioUsuarios {
	public Usuario getByEmail(String email);
}
