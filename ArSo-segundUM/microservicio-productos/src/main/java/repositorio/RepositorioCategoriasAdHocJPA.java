package repositorio;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import modelo.Categoria;

public class RepositorioCategoriasAdHocJPA extends RepositorioCategoriasJPA implements IRepositorioCategoriasAdHoc {

	public RepositorioCategoriasAdHocJPA(EntityManagerFactory emf) {
		super(emf);
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public List<Categoria> getCategoriasRaiz() {
        EntityManager em = this.emf.createEntityManager(); 
        try {
            String jpql = "SELECT c FROM Categoria c WHERE c.padre IS NULL";
            TypedQuery<Categoria> query = em.createQuery(jpql, Categoria.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Categoria> getDescendientes(Categoria c) {
        EntityManager em = this.emf.createEntityManager();
        try {
            String patronRuta = c.getRuta() + "%";
            String jpql = "SELECT cat FROM Categoria cat WHERE cat.ruta LIKE :patron AND cat.id <> :idPadre";
            
            TypedQuery<Categoria> query = em.createQuery(jpql, Categoria.class);
            query.setParameter("patron", patronRuta);
            query.setParameter("idPadre", c.getId());
            
            return query.getResultList();
        } finally {
            em.close();
        }
    }

}
