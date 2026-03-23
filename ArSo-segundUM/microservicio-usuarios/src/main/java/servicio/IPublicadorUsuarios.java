package servicio;

public interface IPublicadorUsuarios {
	void emitirEvento(String tipoEvento, String idUsuario, String nombreNuevo, String apellidoNuevo,String email, String clave, String fechaNacimiento, String tlf);
}
