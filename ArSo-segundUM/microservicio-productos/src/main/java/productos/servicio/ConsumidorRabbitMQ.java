package productos.servicio;

import java.util.Map;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;

import productos.servicio.IConsumidorEventos;

@Component
public class ConsumidorRabbitMQ {

    @Autowired
    private IConsumidorEventos consumidorEventos;

    @Autowired
    private ObjectMapper objectMapper;
    
    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "productos.cola.usuarios", durable = "true"),
        exchange = @Exchange(value = "bus", type = ExchangeTypes.TOPIC),
        key = "bus.usuarios.*"
    ))
    public void recibirEventoUsuario(Message mensaje) {
        try {
        	String jsonMensaje = new String(mensaje.getBody());
            System.out.println("Productos recibe evento de Usuario: " + jsonMensaje);
            
            
            Map<String, Object> evento = objectMapper.readValue(jsonMensaje, new TypeReference<Map<String, Object>>() {});
            
            String tipoEvento = (String) evento.get("tipoEvento");
            
            
            if ("usuario-creado".equals(tipoEvento)) {
            	String idUsuario = (String) evento.get("idUsuario");
                String nombre = (String) evento.get("nombreNuevo"); 
                if (nombre == null) nombre = (String) evento.get("nombre");
                String email = (String) evento.get("email");
                String apellido = (String) evento.get("apellido");
                
                consumidorEventos.procesarUsuarioCreado(idUsuario, nombre, apellido, email);
            }
            
        } catch (Exception e) {
            System.err.println("Error leyendo evento en Productos: " + e.getMessage());
        }
    }
    
    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "productos.cola.compraventas", durable = "true"),
        exchange = @Exchange(value = "bus", type = ExchangeTypes.TOPIC),
        key = "bus.compraventas.*"
    ))
    public void recibirEventoCompraventa(Message mensajeAmqp) {
        try {
            String jsonMensaje = new String(mensajeAmqp.getBody());
            System.out.println("Productos recibe evento de Compraventa: " + jsonMensaje);
            
            if (jsonMensaje.startsWith("\"")) {
                jsonMensaje = objectMapper.readValue(jsonMensaje, String.class);
            }
            Map<String, Object> evento = objectMapper.readValue(jsonMensaje, new TypeReference<Map<String, Object>>() {});
            
            String idProducto = (String) evento.get("idProducto");
            
            if (idProducto != null) {
                consumidorEventos.procesarCompraventaCreada(idProducto);
            }
        } catch (Exception e) {
            System.err.println("Error leyendo evento de Compraventa en Productos: " + e.getMessage());
        }
    }
}