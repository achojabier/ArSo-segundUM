package productos.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import productos.modelo.Categoria;

@Repository
public interface IRepositorioCategoriasSpring extends JpaRepository<Categoria, String>{
	
	@Query("SELECT c FROM Categoria c WHERE c.padre IS NULL")
	public List<Categoria> getCategoriasRaiz();
	
	@Query("SELECT cat FROM Categoria cat WHERE cat.ruta LIKE :patron AND cat.id <> :idPadre")
	public List<Categoria> getDescendientes(@Param("patron") String patron,@Param("idPadre") String idPadre);

}
