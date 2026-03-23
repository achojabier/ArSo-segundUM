package productos.servicio;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import productos.RabbitMQConfig;

@Component
public interface IConsumidorEventos {
    void procesarCompraventaCreada(String idProducto);
    void procesarUsuarioModificado(String idUsuario, String nuevoNombre, String nuevoApellido);
    void procesarUsuarioBorrado(String idUsuario);
    void procesarUsuarioCreado(String idUsuario, String nombre,String apellido, String email);
}