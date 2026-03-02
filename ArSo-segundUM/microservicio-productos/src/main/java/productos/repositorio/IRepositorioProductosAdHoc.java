package productos.repositorio;

import java.util.List;

import productos.modelo.Categoria;
import productos.modelo.EstadoProducto;
import productos.modelo.Producto;

public interface IRepositorioProductosAdHoc extends IRepositorioProducto {
	public List<Producto> historialDelMes(int m, int a);
    
    public List<Producto> filtrar(Categoria c, String descripcion, EstadoProducto estado, double precio);
    
    public List<Producto> getProductosPorVendedor(String idVendedor);
}
