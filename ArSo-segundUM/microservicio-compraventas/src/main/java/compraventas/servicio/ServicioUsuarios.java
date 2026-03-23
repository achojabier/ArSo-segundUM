package compraventas.servicio;

import java.io.IOException;

import org.springframework.stereotype.Component;

import compraventas.dto.UsuarioDTO;
import compraventas.rest.APIUsuarios;
import compraventas.servicio.puertos.ServicioExternoUsuarios;
import retrofit2.Response;

@Component
public class ServicioUsuarios implements ServicioExternoUsuarios {
	private final APIUsuarios api;
	public ServicioUsuarios(APIUsuarios api) {
		this.api=api;
	}
	@Override
	public UsuarioDTO obtenerUsuario(String idUsuario) {
		try {
            Response<UsuarioDTO> respuesta = api.getNombreUsuario(idUsuario).execute();
            if (respuesta.isSuccessful() && respuesta.body() != null) {
                return respuesta.body();
            } else {
            	System.out.println("ERROR EN RETROFIT AL LLAMAR A USUARIOS:");
                System.out.println("Código HTTP: " + respuesta.code());
                System.out.println("Mensaje: " + respuesta.message());
                System.out.println("URL intentada: " + respuesta.raw().request().url());
                UsuarioDTO dummy = new UsuarioDTO();
                dummy.setNombre("Desconocido (" + idUsuario + ")");
                return dummy;
            }
        } catch (IOException e) {
            throw new RuntimeException("Error de comunicación con Usuarios", e);
        }
	}
	

}
