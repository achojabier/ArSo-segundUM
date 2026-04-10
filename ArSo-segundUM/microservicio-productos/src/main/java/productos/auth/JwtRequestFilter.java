package productos.auth;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
    	String token = null;
        // Comprueba que la petición lleve el token JWT y lo valida
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
        	token = authorization.substring("Bearer ".length()).trim();
        }
        
        else if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("jwt")) {
                    token = cookie.getValue();
                }
            }
        }
        
        if (token != null) {
            try {
                String tokenSinFirma = token.substring(0, token.lastIndexOf('.') + 1);
                Claims claims = Jwts.parser().parseClaimsJwt(tokenSinFirma).getBody();

                String[] roles = claims.get("roles", String.class).split(",");
                
                String idUsuario = claims.getSubject();

                ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
                for (String rol : roles) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + rol));
                }

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        idUsuario, null, authorities);
                
                SecurityContextHolder.getContext().setAuthentication(auth);
                
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "El token JWT no es correcto");  
                return;
            }
        }
        
        chain.doFilter(request, response);
        
    }
}