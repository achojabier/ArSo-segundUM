package productos.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsumidorEventos implements IConsumidorEventos{
	
	@Autowired
    private ServicioProductos servicioProductos;
	@Autowired
	private ServicioUsuarios servicioUsuarios;

	@Override
    public void procesarCompraventaCreada(String idProducto) {
        servicioProductos.marcarComoVendido(idProducto);
        System.out.println("Producto " + idProducto + " marcado como vendido.");
    }

    @Override
    public void procesarUsuarioModificado(String idUsuario, String nuevoNombre, String nuevoApellido) {
        servicioUsuarios.modificarUsuario(idUsuario, nuevoNombre, nuevoApellido);
        System.out.println("Datos del usuario " + idUsuario + " actualizados en sus productos.");
    }

    @Override
    public void procesarUsuarioBorrado(String idUsuario) {
        servicioProductos.borrarProductosDeVendedor(idUsuario);
        System.out.println("Productos del usuario " + idUsuario + " eliminados/desactivados.");
    }

	@Override
	public void procesarUsuarioCreado(String idUsuario, String nombre,String apellido, String email) {
		servicioUsuarios.altaUsuario(idUsuario, nombre,apellido, email);
		System.out.println("Creación del usuario " + idUsuario + " "+nombre+".");
		
	}
	

}
