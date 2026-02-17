package repositorio;

import java.util.List;

import modelo.Usuario;
import modelo.UsuarioDTO;

public interface IRepositorioUsuariosAdHoc extends IRepositorioUsuarios {
	public Usuario getByEmail(String email);
	public List<Usuario> listadoUsuarios();
}
