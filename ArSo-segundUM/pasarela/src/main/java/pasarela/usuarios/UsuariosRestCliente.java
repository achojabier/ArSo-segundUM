package pasarela.usuarios;

import java.util.Map;

import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UsuariosRestCliente {
    @POST("usuarios/login") 
    Call<ResponseBody> verificarLogin(@Body Credenciales credenciales);

}