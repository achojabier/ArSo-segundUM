package pasarela.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SecuritySuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		DefaultOAuth2User usuarioGitHub = (DefaultOAuth2User) authentication.getPrincipal();
		
		String idGithub = String.valueOf(usuarioGitHub.getAttributes().get("id")); 
        String nombre = (String) usuarioGitHub.getAttributes().get("name");
		
		if(nombre == null) {
			nombre = (String) usuarioGitHub.getAttributes().get("login");
		}
		
		Map<String,Object> claims = new HashMap<>();
		
		claims.put("id", idGithub);
		claims.put("roles", "USUARIO");
		claims.put("sub", idGithub);
		
		String token = JwtUtils.generateToken(claims);
		
		Cookie cookie = new Cookie("jwt",token);
		cookie.setMaxAge(3600);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		response.addCookie(cookie);
		
		Map<String, Object> respuesta = new HashMap<>();
		respuesta.put("token", token);
		respuesta.put("identificador", idGithub);
		respuesta.put("nombre_completo", nombre);
        respuesta.put("roles", "USUARIO");
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(respuesta));
	}

}
