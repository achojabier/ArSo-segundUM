package productos.servicio;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;

import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import productos.modelo.Categoria;
import productos.modelo.EstadoProducto;
import productos.modelo.LugarDeRecogida;
import productos.modelo.Producto;
import productos.modelo.Usuario;
import productos.repositorio.FactoriaRepositorios;
import productos.repositorio.IRepositorioCategorias;
import productos.repositorio.IRepositorioCategoriasAdHoc;
import productos.repositorio.IRepositorioCategoriasSpring;
import productos.repositorio.IRepositorioProducto;
import productos.repositorio.IRepositorioProductosAdHoc;
import productos.repositorio.IRepositorioProductosSpring;
import productos.repositorio.IRepositorioUsuarios;
import productos.repositorio.IRepositorioUsuariosSpring;
import productos.rest.dto.ProductoDTO;

@Service
@Transactional
public class ServicioProductos{
	private final IRepositorioProductosSpring repositorioProductos;
	private IRepositorioCategoriasSpring repositorioCategorias;
	private IRepositorioUsuariosSpring repositorioUsuarios;
	@Autowired
	private IPublicadorEventos publicadorEventos;
	
	public ServicioProductos(IRepositorioProductosSpring rp, IRepositorioCategoriasSpring sc, IRepositorioUsuariosSpring ru) {
		this.repositorioProductos=rp;
		this.repositorioCategorias=sc;
		this.repositorioUsuarios = ru;
	}
	
	public String altaProducto(String titulo, String descripcion, double precio, EstadoProducto estado, String idCategoria, boolean envio, String idVendedor) {
		Usuario u = repositorioUsuarios.findById(idVendedor).orElse(null);
		if (u==null) {
			throw new RuntimeException("No hay ningún usuario vinculado al id "+idVendedor+".\n");
		}
		Categoria c = repositorioCategorias.findById(idCategoria).orElse(null);
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
		repositorioProductos.save(p);
		System.out.println("producto"+p.getId()+"\n");
		publicadorEventos.emitirEvento("alta-producto", p.getId(), p.getVendedor().getId());
		return p.getId();
	}
	
	public void asignarLugarDeRecogida(String id, double longitud, double latitud, String descripcion) {
		if(descripcion == null || descripcion.trim().isEmpty()) {
			throw new IllegalArgumentException("Descripción vacía.\n");
		}
		Producto p = repositorioProductos.findById(id).orElse(null);
		if(p == null) {
			throw new RuntimeException("Producto no encontrado con ID: " + id);
		}
		p.establecerRecogida(longitud, latitud, descripcion);
		repositorioProductos.save(p);
		publicadorEventos.emitirEvento("asignar-recogida", id, p.getVendedor().getId());
	}
	
	public void modificarProducto(String id, Double precio, String descripcion) {
		Producto p = repositorioProductos.findById(id).orElse(null);
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
		repositorioProductos.save(p);
		publicadorEventos.emitirEvento("modificar-producto", id, p.getVendedor().getId());
	}
	
	public void sumarVisualizacion(String id) {
		Producto p = repositorioProductos.findById(id).orElse(null);
		if(p == null) {
			throw new RuntimeException("Producto no encontrado con ID: " + id);
		}
		int visitas = p.getVisualizaciones();
		p.setVisualizaciones(visitas+1);
		repositorioProductos.save(p);
	}
	public List<Producto> historialDelMes(int m, int a){
		return repositorioProductos.monthlyHistory(m, a);
	}
	public List<Producto> buscarProductos(String idCategoria, String descripcion, EstadoProducto estado, double precioMax){
		Categoria c = null;
		if(idCategoria!=null && !idCategoria.trim().isEmpty()) {
			c = repositorioCategorias.findById(idCategoria).orElse(null);
			if(c==null) {
				throw new RuntimeException("Categoría de búsqueda no encontrada: " + idCategoria);
			}
		}
		return repositorioProductos.filter(c, descripcion, estado, precioMax);
	}
	public List<Producto> recuperarProductosVendedor(String idVendedor) {
		return repositorioProductos.productsBySeller(idVendedor);
	}
	public Producto obtenerProducto(String id) {
		return repositorioProductos.findById(id).orElse(null);
    }
	
	public Page<ProductoResumen> getListadoPaginado(Pageable pageable) {
		 
		 return this.repositorioProductos.findAll(pageable).map(encuesta -> {
			 ProductoResumen resumen = new ProductoResumen();
			 resumen.setId(encuesta.getId());
			 resumen.setTitulo(encuesta.getTitulo());
			 return resumen;
		 });
	}

	public void marcarComoVendido(String idProducto) {
		Producto p = repositorioProductos.findById(idProducto).orElse(null);
		p.setVendido(true);
		repositorioProductos.save(p);
	}

	public void borrarProductosDeVendedor(String idUsuario) {
		List<Producto> productos = recuperarProductosVendedor(idUsuario);
		for(Producto p: productos) {
			borrarProducto(p.getId());
		}
	}
	
	public void borrarProducto(String id) {
		repositorioProductos.deleteById(id);
	}
}
