package compraventas;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface IPublicadorEventos {
	public void emitirEvento(String tipoEvento, String idCompraventa, String idProducto, String idComprador, String idVendedor);
}
