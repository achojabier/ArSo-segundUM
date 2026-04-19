package auth;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import modelo.Usuario;
import repositorio.FactoriaRepositorios;
import servicio.ServicioUsuarios;

@Path("auth")
public class ControladorAuth {
	private ServicioUsuarios servicio = new ServicioUsuarios(new FactoriaRepositorios().getRepositorioUsuarios());
	@POST
	@Path("/login")
	public Response login(@FormParam("username") String username, @FormParam("password") String password) {

		Map<String, Object> claims = verificarCredenciales(username, password);
		if (claims != null) {
			String token = JwtUtils.generateToken(claims);
			return Response.ok(token).build();
		} else {
			return Response.status(Response.Status.UNAUTHORIZED).entity("Credenciales inválidas").build();
		}

	}

	private Map<String, Object> verificarCredenciales(String username, String password) {
		Usuario usuario = servicio.buscar(username, password);
		
		if(usuario!=null) {
			HashMap<String, Object> claims = new HashMap<String, Object>();
			claims.put("sub", usuario.getId());
			claims.put("roles", usuario.isAdministrador()? "ADMINISTRADOR": "USUARIO");
			claims.put("nombre", usuario.getNombre()+" "+usuario.getApellidos());
			return claims;
		}
		
		return null;
	}
}