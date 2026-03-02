package productos.repositorio;

import java.util.List;
import productos.modelo.Categoria;

public interface IRepositorioCategoriasAdHoc extends IRepositorioCategorias{
	public List<Categoria> getCategoriasRaiz();
	public List<Categoria> getDescendientes(Categoria c);
}
