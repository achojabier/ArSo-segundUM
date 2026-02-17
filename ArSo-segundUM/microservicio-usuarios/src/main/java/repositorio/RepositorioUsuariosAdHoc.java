package repositorio;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import modelo.Usuario;

public class RepositorioUsuariosAdHoc extends RepositorioUsuariosJPA implements IRepositorioUsuariosAdHoc{

	public RepositorioUsuariosAdHoc(EntityManagerFactory emf) {
		super(emf);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Usuario getByEmail(String email) {
		EntityManager em = this.emf.createEntityManager();
		try {
			String jpql = "SELECT u FROM Usuario u WHERE u.email = :email";
			TypedQuery<Usuario> query = em.createQuery(jpql,Usuario.class);
			query.setParameter("email", email);
			
			List<Usuario> resultado = query.getResultList();
			
			if (resultado.isEmpty()) {
                return null;
            } else {
                return resultado.get(0);
            }
		} finally {
			em.close();
		}
	}

	@Override
	public List<Usuario> listadoUsuarios() {
		EntityManager em = this.emf.createEntityManager();
		try {
			String jpql = "SELECT u FROM Usuario u";
			TypedQuery<Usuario> query = em.createQuery(jpql,Usuario.class);
			
			List<Usuario> resultado = query.getResultList();
			
			if (resultado.isEmpty()) {
                return null;
            } else {
                return resultado;
            }
		} finally {
			em.close();
		}
	}
	

}
