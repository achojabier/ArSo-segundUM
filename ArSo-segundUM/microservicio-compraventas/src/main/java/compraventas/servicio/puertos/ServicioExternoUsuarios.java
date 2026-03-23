package compraventas.servicio.puertos;

import org.springframework.stereotype.Component;

import compraventas.dto.UsuarioDTO;

@Component
public interface ServicioExternoUsuarios {
    UsuarioDTO obtenerUsuario(String idUsuario);
}