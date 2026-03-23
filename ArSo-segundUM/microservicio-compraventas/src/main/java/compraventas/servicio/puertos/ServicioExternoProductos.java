package compraventas.servicio.puertos;

import org.springframework.stereotype.Component;

import compraventas.dto.ProductoDTO;
@Component
public interface ServicioExternoProductos {
    ProductoDTO obtenerProducto(String idProducto);
}