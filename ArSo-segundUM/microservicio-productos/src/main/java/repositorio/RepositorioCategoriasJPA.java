package repositorio;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import modelo.Categoria;
import modelo.Usuario;

public class RepositorioCategoriasJPA implements IRepositorioCategorias {

	protected EntityManagerFactory emf;
	
	public RepositorioCategoriasJPA(EntityManagerFactory emf) {
		this.emf = emf;
	}
	
	
	@Override
	public void add(Categoria c) {
		EntityManager em = this.emf.createEntityManager();
		
		EntityTransaction et = em.getTransaction();
		
		try {
			et.begin();
			
			em.persist(c);
			
			et.commit();
		} catch (Exception e) {
			if(et.isActive()) {
				et.rollback();
			}
			
			e.printStackTrace();
		} finally {
			em.close();
		}
	}

	@Override
	public Categoria get(String id) {
		EntityManager em = this.emf.createEntityManager();
		try {
			Categoria c = em.find(Categoria.class, id);
			return c;
		} finally {
			em.close();
		}
	}

	@Override
	public void update(Categoria c) {
		EntityManager em = this.emf.createEntityManager();
		
		EntityTransaction et = em.getTransaction();
		
		try {
			et.begin();
			
			em.merge(c);
			
			et.commit();
		} catch (Exception e) {
			if(et.isActive()) {
				et.rollback();
			}
			
			e.printStackTrace();
		} finally {
			em.close();
		}
	}
	
}
