package servicio;

import java.util.Date;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;

import modelo.Usuario;
import repositorio.FactoriaRepositorios;
import repositorio.IRepositorioUsuarios;
import repositorio.IRepositorioUsuariosAdHoc;

@ApplicationScoped
public class ServicioUsuarios {
	private IRepositorioUsuariosAdHoc repositorioUsuarios;
	
	public ServicioUsuarios() {
        FactoriaRepositorios factoria = new FactoriaRepositorios();
        this.repositorioUsuarios = factoria.getRepositorioUsuarios();
    }
	
	public ServicioUsuarios(IRepositorioUsuariosAdHoc ru) {
		this.repositorioUsuarios = ru;
	}
	
	public String altaUsuario(String nombre, String apellidos, String email, String clave, Date fechaNacimiento, String tlf) {
		if (nombre == null || nombre.trim().isEmpty()) throw new IllegalArgumentException("El nombre es obligatorio");
        if (apellidos == null || apellidos.trim().isEmpty()) throw new IllegalArgumentException("Los apellidos son obligatorios");
        if (email == null || email.trim().isEmpty()) throw new IllegalArgumentException("El email es obligatorio");
        if (clave == null || clave.trim().isEmpty()) throw new IllegalArgumentException("La clave es obligatoria");
        if (fechaNacimiento == null) throw new IllegalArgumentException("La fecha de nacimiento es obligatoria");
        
        if(repositorioUsuarios.getByEmail(email)!=null) {
        	throw new IllegalArgumentException("El usuario con email " + email + " ya existe.");
        }
		Usuario usuario = new Usuario(nombre,apellidos,email, clave,fechaNacimiento,tlf);
		
		String id = UUID.randomUUID().toString();
		
		usuario.setId(id);
		
		this.repositorioUsuarios.add(usuario);
		return id;
	}
	
	public void modificarUsuario(String id, String nombre, String apellidos, String clave, Date fechaNacimiento, String tlf) {
		Usuario usuario = this.repositorioUsuarios.get(id);
		if(usuario==null) {
			throw new RuntimeException("Usuario con ID " + id+ "no encontrado\n");
		}
		if (nombre != null && !nombre.trim().isEmpty()) {
	        usuario.setNombre(nombre);
	    }
	    
	    if (apellidos != null && !apellidos.trim().isEmpty()) {
	        usuario.setApellidos(apellidos);
	    }
	    
	    if (clave != null && !clave.trim().isEmpty()) {
	        usuario.setClave(clave);
	    }
	    
	    if (fechaNacimiento != null) {
	        usuario.setFechaNacimiento(fechaNacimiento);
	    }
	    
	    usuario.setTelefono(tlf);
	    
	    this.repositorioUsuarios.update(usuario);
	}
	
	public Usuario buscar(String email, String password) {
		Usuario usuario = repositorioUsuarios.getByEmail(email);
		
		if(usuario == null) {
			throw new RuntimeException("Usuario con email " + email+ "no encontrado\n");
		}
		
		if(usuario.getClave().equals(password)) {
			return usuario;
		} else {
			throw new RuntimeException("Contraseña errónea.\n");
		}
	}
}
