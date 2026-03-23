package compraventas.servicio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.eclipse.persistence.jpa.jpql.parser.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import compraventas.IPublicadorEventos;
import compraventas.dto.ProductoDTO;
import compraventas.dto.UsuarioDTO;
import compraventas.modelo.Compraventa;
import compraventas.repositorio.IRepositorioCompraventaSpring;
import compraventas.servicio.puertos.ServicioExternoProductos;
import compraventas.servicio.puertos.ServicioExternoUsuarios;

@Service
public class ServicioCompraventas {

    private final IRepositorioCompraventaSpring repositorio;
    private final ServicioExternoProductos servicioProductos; 
    private final ServicioExternoUsuarios servicioUsuarios;
    
    @Autowired
    private IPublicadorEventos publicadorEventos;

    public ServicioCompraventas(IRepositorioCompraventaSpring repositorio,
                                ServicioExternoProductos servicioProductos,
                                ServicioExternoUsuarios servicioUsuarios) {
        this.repositorio = repositorio;
        this.servicioProductos = servicioProductos;
        this.servicioUsuarios = servicioUsuarios;
    }


    public Compraventa realizarCompra(String idProducto, String idComprador) {

        ProductoDTO prod = servicioProductos.obtenerProducto(idProducto);
        
        if(prod.isVendido()) {
        	throw new IllegalArgumentException("El producto ya ha sido vendido");
        }
        
        UsuarioDTO comprador = servicioUsuarios.obtenerUsuario(idComprador);

        UsuarioDTO vendedor = servicioUsuarios.obtenerUsuario(prod.getIdVendedor());

        String textoRecogida = prod.isEnvioDisponible() ? "Envío a domicilio" : "Recogida local";

        Compraventa nuevaVenta = new Compraventa(
        		UUID.randomUUID().toString(),
                prod.getId(),
                prod.getTitulo(),
                prod.getPrecio(),
                textoRecogida,
                prod.getIdVendedor(),
                vendedor.getNombre(),
                idComprador,
                comprador.getNombre(),
                LocalDateTime.now()
        );
        publicadorEventos.emitirEvento(
        	    "compraventa-creada", 
        	    nuevaVenta.getId(), 
        	    nuevaVenta.getIdProducto(), 
        	    nuevaVenta.getIdComprador(), 
        	    nuevaVenta.getIdVendedor()
        	);
        return repositorio.save(nuevaVenta);
    }


    public List<Compraventa> getComprasDeUsuario(String idComprador) {
        return repositorio.findByIdComprador(idComprador);
    }

    public List<Compraventa> getVentasDeUsuario(String idVendedor) {
        return repositorio.findByIdVendedor(idVendedor);
    }
    
    public List<Compraventa> getHistorialEntreUsuarios(String idComprador, String idVendedor) {
        return repositorio.findByIdCompradorAndIdVendedor(idComprador, idVendedor);
    }
    
    public void actualizarNombreUsuario(String idUsuario, String nuevoNombre, String nuevoApellido) {
        List<Compraventa> compras = repositorio.findByIdComprador(idUsuario);
        for(Compraventa c : compras) {
            c.setNombreComprador(nuevoNombre);
            repositorio.save(c);
        }

        List<Compraventa> ventas = repositorio.findByIdVendedor(idUsuario);
        for(Compraventa v : ventas) {
            v.setNombreVendedor(nuevoNombre);
            repositorio.save(v);
        }
    }
}