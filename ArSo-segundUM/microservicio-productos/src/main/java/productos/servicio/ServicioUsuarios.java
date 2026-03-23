package productos.servicio;

import java.util.Date;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import productos.modelo.Usuario;
import productos.repositorio.FactoriaRepositorios;
import productos.repositorio.IRepositorioUsuarios;
import productos.repositorio.IRepositorioUsuariosAdHoc;
import productos.repositorio.IRepositorioUsuariosSpring;

@Service
@Transactional
@ApplicationScoped
public class ServicioUsuarios {
	private IRepositorioUsuariosSpring repositorioUsuarios;
	
	public ServicioUsuarios(IRepositorioUsuariosSpring ru) {
		this.repositorioUsuarios = ru;
	}
	
	public String altaUsuario(String id, String nombre, String apellidos, String email) {
		if (nombre == null || nombre.trim().isEmpty()) throw new IllegalArgumentException("El nombre es obligatorio");
        if (apellidos == null || apellidos.trim().isEmpty()) throw new IllegalArgumentException("Los apellidos son obligatorios");
        if (email == null || email.trim().isEmpty()) throw new IllegalArgumentException("El email es obligatorio");
        
        if(repositorioUsuarios.getByEmail(email)!=null) {
        	throw new IllegalArgumentException("El usuario con email " + email + " ya existe.");
        }
		Usuario usuario = new Usuario(nombre,apellidos,email);

		usuario.setId(id);
		
		this.repositorioUsuarios.save(usuario);
		return id;
	}
	
	public void modificarUsuario(String id, String nombre, String apellidos) {
		Usuario usuario = this.repositorioUsuarios.getById(id);
		if(usuario==null) {
			throw new RuntimeException("Usuario con ID " + id+ "no encontrado\n");
		}
		if (nombre != null && !nombre.trim().isEmpty()) {
	        usuario.setNombre(nombre);
	    }
	    
	    if (apellidos != null && !apellidos.trim().isEmpty()) {
	        usuario.setApellidos(apellidos);
	    }
	    
	    this.repositorioUsuarios.save(usuario);
	}
	
	public Usuario buscar(String email, String password) {
		Usuario usuario = repositorioUsuarios.getByEmail(email);
		
		if(usuario == null) {
			throw new RuntimeException("Usuario con email " + email+ "no encontrado\n");
		}
		
		return usuario;
	}
}
