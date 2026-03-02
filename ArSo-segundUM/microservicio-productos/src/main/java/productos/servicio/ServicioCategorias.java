package productos.servicio;



import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import productos.modelo.Categoria;
import productos.modelo.Producto;
import productos.repositorio.FactoriaRepositorios;
import productos.repositorio.IRepositorioCategorias;
import productos.repositorio.IRepositorioCategoriasAdHoc;
import productos.repositorio.IRepositorioCategoriasSpring;
@Service
@Transactional
@ApplicationScoped
public class ServicioCategorias {
	private IRepositorioCategoriasSpring repositorioCategorias;
	
	public ServicioCategorias (IRepositorioCategoriasSpring sc) {
		this.repositorioCategorias=sc;
	}
	
	public Categoria getCategoria(String id) {
		return repositorioCategorias.getById(id);
	}
	
	public void cargarJerarquia(String ruta) {
		try {
			JAXBContext context = JAXBContext.newInstance(Categoria.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			File fichero = new File(ruta);
			Categoria raiz = (Categoria) unmarshaller.unmarshal(fichero);
			if(repositorioCategorias.getById(raiz.getId())==null) {
				vincularPadres(raiz);
				repositorioCategorias.save(raiz);
			}
			
		} catch (Exception e) {
			throw new RuntimeException("Error al cargar el XML de categorías: " + e.getMessage(), e);
		}
	}

	private void vincularPadres(Categoria raiz) {
		if(raiz.getSubcategorias() == null) {
			return;
		}
		
		for(Categoria hija: raiz.getSubcategorias()) {
			hija.setPadre(raiz);
			vincularPadres(hija);
		}
	}
	
	public void modificar(String id,String descripcion) {
		Categoria c = repositorioCategorias.getById(id);
		if(c==null) {
			throw new RuntimeException("Categoría con ID " + id+ "no encontrada.\n");
		}
		
		c.setDescripcion(descripcion);
		
		repositorioCategorias.save(c);
	}
	
	public List<Categoria> getRaices() {
		return repositorioCategorias.getCategoriasRaiz();
	}
	
	public List<Categoria> getHijos(String id){
		Categoria c = repositorioCategorias.getById(id);
		if(c==null) {
			throw new RuntimeException("Categoría con ID " + id+ "no encontrada.\n");
		}
		String patronRuta = c.getRuta() + "%";
		return repositorioCategorias.getDescendientes(patronRuta,c);
	}
	
	public List<Categoria> getTodasCategorias(){
		List<Categoria> raices = getRaices();
		List<Categoria> categorias = new ArrayList<>();
		for(Categoria c: raices) {
			categorias.add(c);
			categorias.addAll(getHijos(c.getId()));
		}
		return categorias;
	}
}
