package servicio;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

@ApplicationScoped
public class PublicadorUsuarios implements IPublicadorUsuarios{
	private static final String EXCHANGE_NAME = "bus";
    private static final String HOST = "localhost";
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    public PublicadorUsuarios() {
    	
    }
    
    @Override
    public void emitirEvento(String tipoEvento, String idUsuario, String nombreNuevo, String apellidoNuevo,String email, String clave, String fechaNacimiento, String tlf) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
                
            channel.exchangeDeclare(EXCHANGE_NAME, "topic", true);
            
            Map<String, Object> evento = new HashMap<>();
            evento.put("tipoEvento", tipoEvento);
            evento.put("idUsuario", idUsuario);
            evento.put("fechaHora", LocalDateTime.now().toString());

            if (nombreNuevo != null && !nombreNuevo.trim().isEmpty()) {
                evento.put("nombreNuevo", nombreNuevo);
            }
            if (apellidoNuevo != null && !apellidoNuevo.trim().isEmpty()) {
                evento.put("apellido", apellidoNuevo);
            }
            if (clave != null && !clave.trim().isEmpty()) {
                evento.put("clave", clave);
            }
            if (email != null && !email.trim().isEmpty()) {
                evento.put("email", email);
            }
            if (fechaNacimiento != null && !fechaNacimiento.trim().isEmpty()) {
                evento.put("fechaNacimiento", fechaNacimiento);
            }
            if (tlf != null && !tlf.trim().isEmpty()) {
                evento.put("tlf", tlf);
            }

            String jsonMensaje = objectMapper.writeValueAsString(evento);

            String routingKey = "bus.usuarios." + tipoEvento;

            channel.basicPublish(EXCHANGE_NAME, routingKey, null, jsonMensaje.getBytes(StandardCharsets.UTF_8));
            System.out.println("Usuarios emite evento: [" + routingKey + "] " + jsonMensaje);

        } catch (Exception e) {
            System.err.println("Error publicando en RabbitMQ desde Usuarios: " + e.getMessage());
        }
    }

}
