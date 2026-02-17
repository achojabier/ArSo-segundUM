package rest;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

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
	private UriInfo uriInfo;
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getListadoUsuarios() throws Exception {
		
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
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getUsuario(@PathParam("id") String id) throws Exception{
		return Response.status(Response.Status.OK).entity(servicio.obtener(id)).build();
	}
	
}
