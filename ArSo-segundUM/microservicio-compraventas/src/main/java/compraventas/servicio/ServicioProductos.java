package compraventas.servicio;

import java.io.IOException;

import org.springframework.stereotype.Component;

import compraventas.dto.ProductoDTO;
import compraventas.rest.APIProductos;
import compraventas.servicio.puertos.ServicioExternoProductos;
import retrofit2.Response;

@Component
public class ServicioProductos implements ServicioExternoProductos{
	private final APIProductos api;
	public ServicioProductos(APIProductos api) {
		this.api=api;
	}
	@Override
    public ProductoDTO obtenerProducto(String id) {
        try {
            Response<ProductoDTO> respuesta = api.getProducto(id).execute();
            if (respuesta.isSuccessful() && respuesta.body() != null) {
                return respuesta.body();
            } else {
                throw new RuntimeException("No se pudo obtener el producto: " + id);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error de comunicación con Productos", e);
        }
    }
}
