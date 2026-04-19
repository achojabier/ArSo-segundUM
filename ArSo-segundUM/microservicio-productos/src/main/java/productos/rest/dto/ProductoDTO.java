package productos.rest.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import productos.modelo.Categoria;
import productos.modelo.EstadoProducto;
import productos.modelo.LugarDeRecogida;
import productos.modelo.Producto;
import productos.modelo.Usuario;

@Schema(description="DTO de Producto")
public class ProductoDTO {
	@Schema(description="Identificador")
	private String id;
	@Schema(description="Título del producto")
    private String titulo;
	@Schema(description="Precio")
    private Double precio;
	@Schema(description="Estado del producto")
    private EstadoProducto estado;
	@Schema(description="ID del vendedor")
	private String idVendedor;
    public ProductoDTO() {}
    public ProductoDTO(String id, String titulo, Double precio, 
            EstadoProducto estado,String idVendedor) {
			this.id = id;
			this.titulo = titulo;
			this.precio = precio;
			this.estado = estado;
			this.idVendedor=idVendedor;
		}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public Double getPrecio() {
		return precio;
	}
	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	public EstadoProducto getEstado() {
		return estado;
	}
	public void setEstado(EstadoProducto estado) {
		this.estado = estado;
	}
	public static ProductoDTO fromEntity(Producto p) {
		String idVendedor = (p.getVendedor() != null) ? p.getVendedor().getId() : null;
		return new ProductoDTO(p.getId(),p.getTitulo(),p.getPrecio(),p.getEstado(),idVendedor);
	}
	public String getIdVendedor() {
		return idVendedor;
	}
	public void setIdVendedor(String idVendedor) {
		this.idVendedor = idVendedor;
	}
    
}
