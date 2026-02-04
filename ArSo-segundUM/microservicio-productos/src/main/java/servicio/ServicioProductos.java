package servicio;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;

import modelo.Categoria;
import modelo.EstadoProducto;
import modelo.LugarDeRecogida;
import modelo.Producto;
import modelo.Usuario;
import repositorio.FactoriaRepositorios;
import repositorio.IRepositorioCategorias;
import repositorio.IRepositorioCategoriasAdHoc;
import repositorio.IRepositorioProducto;
import repositorio.IRepositorioProductosAdHoc;
import repositorio.IRepositorioUsuarios;
@ApplicationScoped
public class ServicioProductos implements Serializable{
	private IRepositorioProductosAdHoc repositorioProductos;
	private IRepositorioCategoriasAdHoc repositorioCategorias;
	private IRepositorioUsuarios repositorioUsuarios;
	
	public ServicioProductos() {
        FactoriaRepositorios factoria = new FactoriaRepositorios();
        this.repositorioProductos = factoria.getRepositorioProductos();
        this.repositorioCategorias = factoria.getRepositorioCategorias();
        this.repositorioUsuarios = factoria.getRepositorioUsuarios();
    }
	
	public ServicioProductos(IRepositorioProductosAdHoc rp, IRepositorioCategoriasAdHoc sc, IRepositorioUsuarios ru) {
		this.repositorioProductos=rp;
		this.repositorioCategorias=sc;
		this.repositorioUsuarios = ru;
	}
	
	public String altaProducto(String titulo, String descripcion, double precio, EstadoProducto estado, String idCategoria, boolean envio, String idVendedor) {
		Usuario u = repositorioUsuarios.get(idVendedor);
		if (u==null) {
			throw new RuntimeException("No hay ningún usuario vinculado al id "+idVendedor+".\n");
		}
		Categoria c = repositorioCategorias.get(idCategoria);
		if (c==null) {
			throw new RuntimeException("No hay ninguna categoría vinculada al id "+idCategoria+".\n");
		}
		
		if (titulo == null || titulo.trim().isEmpty()) {
	        throw new IllegalArgumentException("El título del producto no puede estar vacío.");
	    }
	    
	    if (descripcion == null || descripcion.trim().isEmpty()) {
	        throw new IllegalArgumentException("La descripción del producto no puede estar vacía.");
	    }

	    if (estado == null) {
	        throw new IllegalArgumentException("El estado del producto no puede ser nulo.");
	    }

	    if (idCategoria == null || idCategoria.trim().isEmpty()) {
	        throw new IllegalArgumentException("El ID de la categoría no puede estar vacío.");
	    }
	    
	    if (idVendedor == null || idVendedor.trim().isEmpty()) {
	        throw new IllegalArgumentException("El ID del vendedor no puede estar vacío.");
	    }

	    if (precio < 0) {
	        throw new IllegalArgumentException("El precio no puede ser negativo.");
	    }
		
		Producto p = new Producto();
		p.setTitulo(titulo);
		p.setDescripcion(descripcion);
		p.setPrecio(precio);
		p.setEstado(estado);
		p.setCategoria(c);
		p.setVendedor(u);
		p.setEnvioDisponible(envio);
		
		p.setId(UUID.randomUUID().toString());
		p.setFechaPublicacion(LocalDateTime.now());
		p.setVisualizaciones(0);
		repositorioProductos.add(p);
		System.out.println("producto"+p.getId()+"\n");
		return p.getId();
	}
	
	public void asignarLugarDeRecogida(String id, double longitud, double latitud, String descripcion) {
		if(descripcion == null || descripcion.trim().isEmpty()) {
			throw new IllegalArgumentException("Descripción vacía.\n");
		}
		Producto p = repositorioProductos.get(id);
		if(p == null) {
			throw new RuntimeException("Producto no encontrado con ID: " + id);
		}
		p.establecerRecogida(longitud, latitud, descripcion);
		repositorioProductos.update(p);
	}
	
	public void modificarProducto(String id, Double precio, String descripcion) {
		Producto p = repositorioProductos.get(id);
		if(p == null) {
			throw new RuntimeException("Producto no encontrado con ID: " + id);
		}
		if(precio != null) {
	        if (precio >= 0) { 
	            p.setPrecio(precio);
	        } else {
	            throw new IllegalArgumentException("El precio no puede ser negativo.");
	            
	        }
	    }
		if(descripcion != null) {
			p.setDescripcion(descripcion);
		}
		repositorioProductos.update(p);
	}
	
	public void sumarVisualizacion(String id) {
		Producto p = repositorioProductos.get(id);
		if(p == null) {
			throw new RuntimeException("Producto no encontrado con ID: " + id);
		}
		int visitas = p.getVisualizaciones();
		p.setVisualizaciones(visitas+1);
		repositorioProductos.update(p);
	}
	public List<Producto> historialDelMes(int m, int a){
		return repositorioProductos.historialDelMes(m, a);
	}
	public List<Producto> buscarProductos(String idCategoria, String descripcion, EstadoProducto estado, double precioMax){
		Categoria c = null;
		if(idCategoria!=null && !idCategoria.trim().isEmpty()) {
			c = repositorioCategorias.get(idCategoria);
			if(c==null) {
				throw new RuntimeException("Categoría de búsqueda no encontrada: " + idCategoria);
			}
		}
		return repositorioProductos.filtrar(c, descripcion, estado, precioMax);
	}
	public List<Producto> recuperarProductosVendedor(String idVendedor) {
		return repositorioProductos.getProductosPorVendedor(idVendedor);
	}
	public Producto obtenerProducto(String id) {
		return repositorioProductos.get(id);
    }
}
