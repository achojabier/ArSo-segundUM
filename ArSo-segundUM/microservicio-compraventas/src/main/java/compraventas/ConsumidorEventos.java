package compraventas;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import compraventas.servicio.ServicioCompraventas;
import compraventas.servicio.ServicioProductos;

@Component
public class ConsumidorEventos implements IConsumidorCompraventas{
	
	@Autowired
    private ServicioCompraventas servicioCompraventas;

	@Override
	public void procesarUsuarioModificado(String idUsuario, String nuevoNombre, String nuevoApellido) {
		// TODO Auto-generated method stub
		
	}
}