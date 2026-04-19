package productos.servicio;



import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	@Autowired
	public ServicioCategorias (IRepositorioCategoriasSpring sc) {
		this.repositorioCategorias=sc;
	}
	
	
	public Categoria getCategoria(String id) {
		return repositorioCategorias.findById(id).orElse(null);
	}
	
	public void cargarJerarquia(String ruta) {
		try {
			JAXBContext context = JAXBContext.newInstance(Categoria.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			InputStream is = getClass().getResourceAsStream(ruta);
			if (is == null) {
				throw new RuntimeException("No se ha encontrado el archivo: " + ruta);
			}
			Categoria raiz = (Categoria) unmarshaller.unmarshal(is);
			System.out.println("XML Parseado. ID raíz: " + raiz.getId() + " | Nombre: " + raiz.getNombre());
			if(!repositorioCategorias.existsById(raiz.getId())) {
				vincularPadres(raiz);
				repositorioCategorias.save(raiz);
				System.out.println(" Guardada en base de datos la categoría: " + raiz.getNombre());
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
		Categoria c = repositorioCategorias.findById(id).orElse(null);
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
		Categoria c = repositorioCategorias.findById(id).orElse(null);
		if(c==null) {
			throw new RuntimeException("Categoría con ID " + id+ "no encontrada.\n");
		}
		String patronRuta = c.getRuta() + "%";
		return repositorioCategorias.getDescendientes(patronRuta,c.getId());
	}
	
	public List<Categoria> getTodasCategorias(){
		return repositorioCategorias.findAll();
	}
}
