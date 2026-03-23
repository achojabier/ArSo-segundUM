package rest;



import javax.ws.rs.PathParam;


import modelo.UsuarioDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIUsuarios {
	@GET("usuarios/{id}/nombre")
	Call<UsuarioDTO> getNombreUsuario(@Path("id") String id);
}
