package repositorio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import modelo.Categoria;
import modelo.EstadoProducto;
import modelo.Producto;

public class RepositorioProductosJPA implements IRepositorioProducto {
protected EntityManagerFactory emf;
	
	public RepositorioProductosJPA(EntityManagerFactory emf) {
		this.emf = emf;
	}
	@Override
	public void add(Producto p) {
		EntityManager em = this.emf.createEntityManager();
		
		EntityTransaction et = em.getTransaction();
		
		try {
			et.begin();
			
			em.persist(p);
			
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
	public Producto get(String id) {
		EntityManager em = this.emf.createEntityManager();
		try {
			Producto p = em.find(Producto.class, id);
			return p;
		} finally {
			em.close();
		}
	}

	@Override
	public void update(Producto p) {
		EntityManager em = this.emf.createEntityManager();
		
		EntityTransaction et = em.getTransaction();
		
		try {
			et.begin();
			
			em.merge(p);
			
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
