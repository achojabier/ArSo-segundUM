package productos.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import productos.modelo.*;

@Repository
public interface IRepositorioProductosSpring extends IRepositorioProductos, JpaRepository<Producto, String>{
	
	@Query("SELECT p FROM Producto p WHERE FUNCTION('MONTH', p.fechaPublicacion) = :month AND FUNCTION('YEAR', p.fechaPublicacion) = :year")
	public List<Producto> monthlyHistory(@Param("month")int month,@Param("year")int year);
	
	@Query("SELECT p FROM Producto p WHERE (:categoria IS NULL OR p.categoria = :categoria) AND (:descripcion IS NULL OR p.descripcion LIKE %:descripcion%) AND (:estado IS NULL OR p.estado = :estado) AND (:maxPrecio IS NULL OR p.precio <= :maxPrecio)")
	public List<Producto> filter(@Param("categoria") Categoria c, @Param("descripcion") String descripcion, @Param("estado")EstadoProducto estado, @Param("maxPrecio")Double MaxPrecio);
	
	@Query("SELECT p FROM Producto p WHERE p.vendedor.id = :idVendedor")
	public List<Producto> productsBySeller(@Param("idVendedor")String id);
}
