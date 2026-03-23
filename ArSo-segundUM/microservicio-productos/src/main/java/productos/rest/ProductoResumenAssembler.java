package productos.rest;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import productos.modelo.Producto;
import productos.rest.dto.ProductoDTO;
import productos.servicio.ProductoResumen;

@Component
public class ProductoResumenAssembler implements RepresentationModelAssembler<ProductoResumen, EntityModel<ProductoResumen>> {

	@Override
	public EntityModel<ProductoResumen> toModel(ProductoResumen productoResumen) {
		try {
	        EntityModel<ProductoResumen> resultado = EntityModel.of(productoResumen, 
	                linkTo(methodOn(ControladorProductos.class).getProductoById(productoResumen.getId())).withSelfRel());
	        resultado.add(linkTo(methodOn(ControladorProductos.class).getProductosPaginado(null)).withRel("coleccion"));
	        return resultado;
	    } 
    	catch(Exception e) {
	    	return  EntityModel.of(productoResumen);
	    }
	}
}
