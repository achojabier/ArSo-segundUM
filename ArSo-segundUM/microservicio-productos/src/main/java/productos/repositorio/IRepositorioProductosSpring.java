package productos.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import productos.modelo.*;

@Repository
public interface IRepositorioProductosSpring extends JpaRepository<Producto, String>{
	public List<Producto> monthlyHistory(int month, int year);
	
	public List<Producto> filter(Categoria c, String descripcion, EstadoProducto estado, Double MaxPrecio);
	
	@Query("SELECT p FROM Producto p WHERE p.vendedor.id = :idVendedor")
	public List<Producto> productsBySeller(@Param("idVendedor")String id);
}
