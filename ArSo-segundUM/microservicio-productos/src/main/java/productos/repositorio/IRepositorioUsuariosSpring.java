package productos.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import productos.modelo.Usuario;

@Repository
public interface IRepositorioUsuariosSpring  extends JpaRepository<Usuario, String>{

	public Usuario getByEmail(String email);

}
