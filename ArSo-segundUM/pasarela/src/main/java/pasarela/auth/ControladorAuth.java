package pasarela.auth;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import okhttp3.ResponseBody;
import pasarela.usuarios.Credenciales;
import pasarela.usuarios.UsuariosRestCliente;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@RestController
@RequestMapping("/auth")
public class ControladorAuth {

    private UsuariosRestCliente usuariosClient;

    public ControladorAuth(@Value("${ZUUL_ROUTES_USUARIOS_UR}") String urlUsuarios) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlUsuarios) 
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        
        this.usuariosClient = retrofit.create(UsuariosRestCliente.class);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Credenciales credenciales, HttpServletResponse response) {
        try {
            Call<ResponseBody> llamada = usuariosClient.verificarLogin(credenciales);
            Response<ResponseBody> retrofitResponse = llamada.execute(); 
            
            if (retrofitResponse.isSuccessful() && retrofitResponse.body() != null) {
                String token = retrofitResponse.body().string();
                
                Cookie cookie = new Cookie("jwt", token);
                cookie.setMaxAge(3600);
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                response.addCookie(cookie);

                String tokenSinFirma = token.substring(0, token.lastIndexOf('.') + 1);
                io.jsonwebtoken.Claims claims = io.jsonwebtoken.Jwts.parser().parseClaimsJwt(tokenSinFirma).getBody();

                Map<String, Object> respuesta = new HashMap<>();
                respuesta.put("token", token);
                respuesta.put("identificador", claims.getSubject());
                respuesta.put("roles", claims.get("roles"));

                return ResponseEntity.ok(respuesta);
            } else {
                System.out.println("Fallo devuelto por JAX-RS: " + retrofitResponse.code());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}