package productos.rest;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import productos.modelo.EstadoProducto;
import productos.modelo.Producto;
import productos.repositorio.IRepositorioCategoriasSpring;
import productos.repositorio.IRepositorioProductosSpring;
import productos.repositorio.IRepositorioUsuariosSpring;
import productos.rest.dto.CrearProductoDTO;
import productos.rest.dto.ModificarProductoDTO;
import productos.rest.dto.ProductoDTO;
import productos.rest.dto.RecogidaDTO;
import productos.servicio.ProductoResumen;
import productos.servicio.ServicioCategorias;
import productos.servicio.ServicioProductos;
import productos.servicio.ServicioUsuarios;

@RestController
@RequestMapping("/productos")
public class ControladorProductos {
	private ServicioProductos sp;
	private ServicioCategorias sc;
	private ServicioUsuarios su;
	@Autowired
	private PagedResourcesAssembler<ProductoResumen> pagedResourcesAssembler;
	@Autowired
	private ProductoResumenAssembler resumenAssembler;
	@Autowired
	private ProductoDTOAssembler dtoAssembler;
	
	
	
	@Autowired
	public ControladorProductos(ServicioProductos p, ServicioCategorias c, ServicioUsuarios u) {
		this.sp=p;
		this.sc=c;
		this.su=u;
	}
	
	@PostMapping
	public ResponseEntity<Void> crearProducto(@Valid @RequestBody CrearProductoDTO producto){
		String id = this.sp.altaProducto(producto.getTitulo(), producto.getDescripcion(),producto.getPrecio(), producto.getEstado(), producto.getIdCategoria(), producto.isEnvioDisponible(),producto.getIdVendedor());
		URI nuevaURL = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
		return ResponseEntity.created(nuevaURL).build();
	}
	
	@GetMapping("/{id}")
	public EntityModel<ProductoDTO> getProductoById(@PathVariable String id) throws Exception {
		 
		Producto p = this.sp.obtenerProducto(id);
		ProductoDTO pDTO = ProductoDTO.fromEntity(p);
		 
		return dtoAssembler.toModel(pDTO);
	}
	
	@GetMapping
	public PagedModel<EntityModel<ProductoResumen>> getProductosPaginado(@ParameterObject Pageable paginacion){
		Page<ProductoResumen> resultado = this.sp.getListadoPaginado(paginacion);
		return this.pagedResourcesAssembler.toModel(resultado, resumenAssembler);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Void> modificarProducto(@Valid @RequestBody ModificarProductoDTO producto, @PathVariable String id){
		sp.modificarProducto(id, producto.getPrecio(), producto.getDescripcion());
		return ResponseEntity.noContent().build();
	}
	
    @PutMapping("/{id}/recogida")
    public ResponseEntity<Void> asignarRecogida(@PathVariable String id, @RequestBody RecogidaDTO dto) {
        this.sp.asignarLugarDeRecogida(id, dto.getLongitud(), dto.getLatitud(), dto.getDescripcion());
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/busqueda")
    public ResponseEntity<List<ProductoDTO>> buscarProductos( @RequestParam(required = false) String categoria, @RequestParam(required = false) String descripcion, @RequestParam(required = false) EstadoProducto estado, @RequestParam(required = false) Double precioMax) {
            
        List<Producto> resultados = this.sp.buscarProductos(categoria, descripcion, estado, precioMax);
        
        List<ProductoDTO> dtos = resultados.stream()
                .map(ProductoDTO::fromEntity)
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(dtos);
    }
}
