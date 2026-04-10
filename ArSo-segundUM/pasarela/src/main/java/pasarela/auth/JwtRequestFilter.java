package pasarela.auth;

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

	@Override
	protected void doFilterInternal(HttpServletRequest request,
	        HttpServletResponse response, FilterChain chain) 
	        throws IOException, ServletException {
	    
	    try {
	        String authorization = request.getHeader("Authorization");
	        String jwt = null;
	        
	        if (authorization != null && authorization.startsWith("Bearer ")) {
	            jwt = authorization.substring("Bearer ".length()).trim();
	        } else if (request.getCookies() != null) {
	            for (Cookie cookie : request.getCookies()) {
	                if (cookie.getName().equals("jwt")) {
	                    jwt = cookie.getValue();
	                }
	            }
	        }
	        
	        if (jwt != null) {
	            try {
	                String tokenSinFirma = jwt.substring(0, jwt.lastIndexOf('.') + 1);
	                Claims claims = Jwts.parser().parseClaimsJwt(tokenSinFirma).getBody();
	                ArrayList<GrantedAuthority> authorities = new ArrayList<>();
	                String rolesClaim = claims.get("roles", String.class);
	                if (rolesClaim != null && !rolesClaim.isEmpty()) {
	                    String[] roles = rolesClaim.split(",");
	                    for (String rol : roles) {
	                        authorities.add(new SimpleGrantedAuthority("ROLE_" + rol.trim()));
	                    }
	                }
	                String idUsuario = claims.getSubject();
	                UsernamePasswordAuthenticationToken auth = 
	                    new UsernamePasswordAuthenticationToken(idUsuario, null, authorities);
	                SecurityContextHolder.getContext().setAuthentication(auth);
	            } catch (Exception e) {
	                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, 
	                    "Token JWT no válido: " + e.getMessage());
	                return;
	            }
	        }
	        
	        chain.doFilter(request, response);
	        
	    } catch (Exception e) {
	        System.err.println("ERROR en JwtRequestFilter: " + e.getMessage());
	        e.printStackTrace();
	        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
	            "Error interno: " + e.getMessage());
	    }
	}
}