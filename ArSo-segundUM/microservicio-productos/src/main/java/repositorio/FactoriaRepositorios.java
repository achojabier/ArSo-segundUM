package repositorio;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class FactoriaRepositorios {
	private static EntityManagerFactory emf;
	
	public FactoriaRepositorios() {
		if (emf == null || !emf.isOpen()) {
            emf = Persistence.createEntityManagerFactory("segundum.productos");
        }
	}
	
	public IRepositorioUsuariosAdHoc getRepositorioUsuarios() {
		return new RepositorioUsuariosAdHoc(emf);
		
	}
	
	public IRepositorioCategoriasAdHoc getRepositorioCategorias() {
		return new RepositorioCategoriasAdHocJPA(emf);
	}
	
	public IRepositorioProductosAdHoc getRepositorioProductos() {
		return new RepositorioProductosAdHocJPA(emf);
	}
	
	public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
