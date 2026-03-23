package compraventas;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AdaptadorPublicadorEventos implements IPublicadorEventos{

	@Autowired
    private RabbitTemplate rabbitTemplate;
	
	@Autowired
    private ObjectMapper objectMapper;
	private static final String EXCHANGE_NAME = "bus";
	
	@Override
    public void emitirEvento(String tipoEvento, String idCompraventa, String idProducto, String idComprador, String idVendedor) {
        try {
            Map<String, Object> evento = new HashMap<>();
            evento.put("tipoEvento", tipoEvento);
            evento.put("idCompraventa", idCompraventa);
            evento.put("idProducto", idProducto);
            evento.put("idComprador", idComprador);
            evento.put("idVendedor", idVendedor);
            evento.put("fechaHora", LocalDateTime.now().toString());
            
            String jsonMensaje = objectMapper.writeValueAsString(evento);
            
            String routingKey = "bus.compraventas." + tipoEvento;
            
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, routingKey, jsonMensaje);
            System.out.println("Evento emitido: " + routingKey);
            
        } catch (JsonProcessingException e) {
            System.err.println("Error al serializar el evento a JSON: " + e.getMessage());
        }
    }

    
}