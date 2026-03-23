package productos.rest;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import productos.rest.dto.ProductoDTO;
import productos.servicio.ProductoResumen;

@Component
public class ProductoDTOAssembler implements RepresentationModelAssembler<ProductoDTO, EntityModel<ProductoDTO>> {

	@Override
	public EntityModel<ProductoDTO> toModel(ProductoDTO producto) {
		try {
			EntityModel<ProductoDTO> resultado = EntityModel.of(producto,linkTo(methodOn(ControladorProductos.class).getProductoById(producto.getId())).withSelfRel());
			resultado.add(linkTo(methodOn(ControladorProductos.class).getProductosPaginado(null)).withRel("coleccion"));
			return resultado;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return EntityModel.of(producto);
		}
		
	}

}
