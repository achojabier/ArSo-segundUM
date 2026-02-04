package repositorio;

import java.util.List;

import modelo.Categoria;
import modelo.EstadoProducto;
import modelo.Producto;

public interface IRepositorioProductosAdHoc extends IRepositorioProducto {
	public List<Producto> historialDelMes(int m, int a);
    
    public List<Producto> filtrar(Categoria c, String descripcion, EstadoProducto estado, double precio);
    
    public List<Producto> getProductosPorVendedor(String idVendedor);
}
