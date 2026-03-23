package productos.servicio;

public interface IPublicadorEventos {
	public void emitirEvento(String tipoEvento, String idProducto, String idVendedor);
}
