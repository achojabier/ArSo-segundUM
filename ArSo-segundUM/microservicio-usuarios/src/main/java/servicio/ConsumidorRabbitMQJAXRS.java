package servicio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@WebListener
public class ConsumidorRabbitMQJAXRS implements ServletContextListener {

    private static final String EXCHANGE_NAME = "bus";
    private static final String QUEUE_NAME = "usuarios.cola";
    private static final String ROUTING_KEY = "bus.compraventas.compraventa-creada";
    private static final String HOST = "localhost";

    private Connection connection;
    private Channel channel;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private IConsumidorUsuarios consumidorEventos; 

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Arrancando el consumidor de RabbitMQ para Usuarios...");

        this.consumidorEventos = new ConsumidorUsuarios(); 

        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(HOST);
            
            connection = factory.newConnection();
            channel = connection.createChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, "topic", true);
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String jsonMensaje = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println("Usuarios recibe evento raw: " + jsonMensaje);
                
                try {
                    if (jsonMensaje.startsWith("\"")) {
                        jsonMensaje = objectMapper.readValue(jsonMensaje, String.class);
                    }

                    Map<String, Object> evento = objectMapper.readValue(jsonMensaje, Map.class);
                    
                    String idComprador = (String) evento.get("idComprador");
                    String idVendedor = (String) evento.get("idVendedor");
                    
                    if (idComprador != null && idVendedor != null) {
                        consumidorEventos.procesarNuevaCompraventa(idComprador, idVendedor);
                        System.out.println("Usuarios: Compraventa procesada con éxito.");
                    }
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                } catch (Exception e) {
                    System.err.println("Error procesando el JSON en Usuarios: " + e.getMessage());
                    e.printStackTrace();
                }
            };

            channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> { });
            

        } catch (Exception e) {
            System.err.println("Error conectando Usuarios a RabbitMQ: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Cuando apagues el servidor, cerramos las conexiones limpiamente
        try {
            if (channel != null && channel.isOpen()) channel.close();
            if (connection != null && connection.isOpen()) connection.close();
            System.out.println("🛑 Conexión de RabbitMQ cerrada en Usuarios.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}