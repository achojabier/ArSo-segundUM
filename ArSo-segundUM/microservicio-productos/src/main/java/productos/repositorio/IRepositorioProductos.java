package productos.repositorio;

import org.springframework.data.repository.PagingAndSortingRepository;

import productos.modelo.Producto;

public interface IRepositorioProductos extends PagingAndSortingRepository<Producto, String>{

}
