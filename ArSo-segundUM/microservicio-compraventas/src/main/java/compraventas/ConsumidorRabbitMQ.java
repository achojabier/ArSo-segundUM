package compraventas;

import java.util.Map;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ConsumidorRabbitMQ {

    @Autowired
    private IConsumidorCompraventas consumidorEventos;

    @Autowired
    private ObjectMapper objectMapper;

    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "compraventas.cola", durable = "true"),
        exchange = @Exchange(value = "bus", type = ExchangeTypes.TOPIC),
        key = "bus.usuarios.usuario-modificado"
    ))
    public void recibirMensaje(String jsonMensaje) {
        try {
            System.out.println("Compraventas recibe evento: " + jsonMensaje);
            
            Map<String, Object> evento = objectMapper.readValue(jsonMensaje, new TypeReference<Map<String, Object>>() {});
            
            String idUsuario = (String) evento.get("idUsuario");
            String nuevoNombre = (String) evento.get("nuevoNombre"); 
            String nuevoApellido = (String) evento.get("nuevoApellido");
            
 
            if(idUsuario != null && nuevoNombre != null) {
                consumidorEventos.procesarUsuarioModificado(idUsuario, nuevoNombre,nuevoApellido);
            }

        } catch (JsonProcessingException e) {
            System.err.println("Error al leer el JSON del evento: " + e.getMessage());
        }
    }
}