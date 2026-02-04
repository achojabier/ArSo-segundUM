package servicio;



import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import modelo.Categoria;
import modelo.Producto;
import repositorio.FactoriaRepositorios;
import repositorio.IRepositorioCategorias;
import repositorio.IRepositorioCategoriasAdHoc;

@ApplicationScoped
public class ServicioCategorias {
	private IRepositorioCategoriasAdHoc repositorioCategorias;
	
	public ServicioCategorias() {
        FactoriaRepositorios factoria = new FactoriaRepositorios();
        this.repositorioCategorias = factoria.getRepositorioCategorias();
    }
	
	public ServicioCategorias (IRepositorioCategoriasAdHoc sc) {
		this.repositorioCategorias=sc;
	}
	
	public Categoria getCategoria(String id) {
		return repositorioCategorias.get(id);
	}
	
	public void cargarJerarquia(String ruta) {
		try {
			JAXBContext context = JAXBContext.newInstance(Categoria.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			File fichero = new File(ruta);
			Categoria raiz = (Categoria) unmarshaller.unmarshal(fichero);
			if(repositorioCategorias.get(raiz.getId())==null) {
				vincularPadres(raiz);
				repositorioCategorias.add(raiz);
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
		Categoria c = repositorioCategorias.get(id);
		if(c==null) {
			throw new RuntimeException("Categoría con ID " + id+ "no encontrada.\n");
		}
		
		c.setDescripcion(descripcion);
		
		repositorioCategorias.update(c);
	}
	
	public List<Categoria> getRaices() {
		return repositorioCategorias.getCategoriasRaiz();
	}
	
	public List<Categoria> getHijos(String id){
		Categoria c = repositorioCategorias.get(id);
		if(c==null) {
			throw new RuntimeException("Categoría con ID " + id+ "no encontrada.\n");
		}
		
		return repositorioCategorias.getDescendientes(c);
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
