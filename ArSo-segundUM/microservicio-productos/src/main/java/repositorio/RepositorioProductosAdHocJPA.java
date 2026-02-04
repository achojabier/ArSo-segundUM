package repositorio;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import modelo.Categoria;
import modelo.EstadoProducto;
import modelo.Producto;

public class RepositorioProductosAdHocJPA extends RepositorioProductosJPA implements IRepositorioProductosAdHoc{

	public RepositorioProductosAdHocJPA(EntityManagerFactory emf) {
		super(emf);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public List<Producto> historialDelMes(int m, int a) {
		EntityManager em = this.emf.createEntityManager();
		try {
			String jpql = "SELECT * from productos p WHERE MONTH(p.fechaPublicacion) = ? AND YEAR(p.fechaPublicacion) = ?  ORDER BY p.visualizaciones DESC";
			Query query = em.createNativeQuery(jpql,Producto.class);
			query.setParameter(1, m);
			query.setParameter(2, a);
			return query.getResultList();
		} finally {
			em.close();
		}
	}

    @Override
    public List<Producto> filtrar(Categoria c, String descripcion, EstadoProducto estado, double precio) {
        EntityManager em = this.emf.createEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT p FROM Producto p");
            boolean whereAdded = false;
            Map<String, Object> parametros = new HashMap<>();

            if (c != null) {
                jpql.append(" WHERE p.categoria.ruta LIKE :ruta");
                parametros.put("ruta", c.getRuta() + "%"); 
                whereAdded = true;
            }
            if (descripcion != null && !descripcion.trim().isEmpty()) {
                jpql.append(whereAdded ? " AND" : " WHERE");
                jpql.append(" p.descripcion LIKE :d");
                parametros.put("d", "%" + descripcion + "%"); 
                whereAdded = true;
            }
            if (precio > 0) { 
                jpql.append(whereAdded ? " AND" : " WHERE");
                jpql.append(" p.precio <= :precioMax");
                parametros.put("precioMax", precio);
                whereAdded = true;
            }
            if (estado != null) {
                jpql.append(whereAdded ? " AND" : " WHERE");
                jpql.append(" p.estado IN :estadosValidos");
                parametros.put("estadosValidos", getEstadosValidos(estado));
                whereAdded = true;
            }

            TypedQuery<Producto> query = em.createQuery(jpql.toString(), Producto.class);
            for (Map.Entry<String, Object> entry : parametros.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    private List<EstadoProducto> getEstadosValidos(EstadoProducto estado) {
        List<EstadoProducto> estados = new ArrayList<>();
        switch (estado) {
            case PARA_PIEZAS_O_REPARAR:
                estados.add(EstadoProducto.PARA_PIEZAS_O_REPARAR);
            case ACEPTABLE:
                estados.add(EstadoProducto.ACEPTABLE);
            case BUEN_ESTADO:
                estados.add(EstadoProducto.BUEN_ESTADO);
            case COMO_NUEVO:
                estados.add(EstadoProducto.COMO_NUEVO);
            case NUEVO:
                estados.add(EstadoProducto.NUEVO);
        }
        return estados;
    }

    @Override
    public List<Producto> getProductosPorVendedor(String idVendedor) {
        EntityManager em = this.emf.createEntityManager();
        try {
            String jpql = "SELECT p FROM Producto p WHERE p.vendedor.id = :idVendedor";
            TypedQuery<Producto> query = em.createQuery(jpql, Producto.class);
            query.setParameter("idVendedor", idVendedor);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
