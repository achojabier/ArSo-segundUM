package rest;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.http.HttpHeaders;

import auth.Credenciales;
import auth.JwtUtils;
import io.jsonwebtoken.Claims;
import modelo.UsuarioDTO;
import rest.Listado;
import rest.Listado.ResumenExtendido;
import modelo.Usuario;
import repositorio.FactoriaRepositorios;
import servicio.FactoriaServicios;
import servicio.ServicioUsuarios;;
@Path("usuarios")
public class ControladorUsuarios {
	FactoriaRepositorios factoria = new FactoriaRepositorios();
	private ServicioUsuarios servicio = new ServicioUsuarios(factoria.getRepositorioUsuarios());
	@Context
	private HttpServletRequest servletRequest;
	
	@Context
	private UriInfo uriInfo;
	
	@POST
	@Path("/login")
	@PermitAll
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response login(Credenciales credenciales) {
		try {
            Usuario usuario = servicio.buscar(credenciales.getEmail(), credenciales.getClave());
            
            if (usuario != null) {
                Map<String, Object> claims = new HashMap<>();
                claims.put("sub", usuario.getId());
                claims.put("roles", "USUARIO");     
                claims.put("nombre_completo", usuario.getNombre()+" "+usuario.getApellidos());
                
               
                String token = JwtUtils.generateToken(claims);
                

                return Response.ok()
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .entity(token) 
                        .build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Credenciales incorrectas")
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
	}
	
	@GET
	@RolesAllowed("USUARIO")	
	@Produces(MediaType.APPLICATION_JSON)
	public Response getListadoUsuarios(@QueryParam("email") String email, @QueryParam("clave") String clave) throws Exception {
		
		if((email!=null && clave==null) || (email==null && clave!=null)) {
			throw new IllegalArgumentException("El email o la clave son erroneos.");
		}
		
		if(email !=null && clave!=null) {
			if(servicio.buscar(email, clave)==null) {
				throw new IllegalArgumentException("El email o la clave son erroneos.");
			}
			String id = servicio.buscar(email, clave).getId();
			URI destino = this.uriInfo.getAbsolutePathBuilder().path(id).build();
			return Response.seeOther(destino).build();
		}
		
		List<UsuarioDTO> resultado = servicio.obtenerListado();
		
		LinkedList<ResumenExtendido> extendido = new LinkedList<>();
		for (UsuarioDTO usuarioResumen : resultado) {
			ResumenExtendido resumenExtendido = new ResumenExtendido();
			resumenExtendido.setDTO(usuarioResumen);
			String id = usuarioResumen.getId();
			URI nuevaURL = this.uriInfo.getAbsolutePathBuilder().path(id).build();
			resumenExtendido.setUrl(nuevaURL.toString());

			extendido.add(resumenExtendido);
		}
		Listado listado = new Listado();
		listado.setUsuario(extendido);
		return Response.ok(listado).build();
	}
	
	@GET
	@Path("{id}")
	@RolesAllowed("USUARIO")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsuario(@PathParam("id") String id) throws Exception{
		if(servicio.obtener(id)==null) {
			throw new IllegalArgumentException("El usuario con id: "+id+" no se ha encontrado.");
		}
		return Response.status(Response.Status.OK).entity(servicio.obtener(id)).build();
	}
	
	@POST
	@PermitAll
	@Consumes(MediaType.APPLICATION_JSON)
	public Response altaUsuario(Usuario u) throws Exception{
		try {
			String id = servicio.altaUsuario(u.getNombre(), u.getApellidos(), u.getEmail(), u.getClave(), u.getFechaNacimiento(), u.getTelefono());
			URI nuevaURL = this.uriInfo.getAbsolutePathBuilder().path(id).build();
			return Response.created(nuevaURL).build();
		} catch(Exception e){
				e.printStackTrace();
				throw new IllegalArgumentException("Usuario erróneo, compruebe que todos los datos están bien.");
			}
	}
	
	@PUT
	@RolesAllowed("USUARIO")
	@Path("{id}")
	public Response modificarUsuario(@PathParam("id") String id, Usuario u) {
		Claims claims = (Claims) servletRequest.getAttribute("claims");
		String tokenId = claims.getSubject();
		if(!tokenId.equals(id)) {
			return Response.status(Response.Status.FORBIDDEN).entity("Solo puedes modificar tus propios datos").build();
		}
		try {
			servicio.modificarUsuario(id, u.getNombre(), u.getApellidos(), u.getClave(), u.getFechaNacimiento(), u.getTelefono());
			return Response.status(Response.Status.NO_CONTENT).build();
		} catch(Exception e){
			throw new IllegalArgumentException("Usuario erróneo, compruebe que todos los datos están bien.");
		}
	}
	
	@GET
	@PermitAll
	@Path("/{id}/nombre")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNombreUsuario(@PathParam("id") String id) {
		Usuario u = servicio.obtener(id);
	    return Response.ok(Collections.singletonMap("nombre", u.getNombre() +" " + u.getApellidos())).build();
	}
}
