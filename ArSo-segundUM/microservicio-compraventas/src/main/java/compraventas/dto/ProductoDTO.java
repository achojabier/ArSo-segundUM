package compraventas.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description="DTO de Producto")
public class ProductoDTO {
	@Schema(description="Identificador")
	private String id;
	@Schema(description="Título del producto")
    private String titulo;
	@Schema(description="Precio")
    private Double precio;
	@Schema(description="Identificador del vendedor")
	private String idVendedor;
	@Schema(description="Indica si el producto tiene opción de envío a domicilio")
	private boolean envioDisponible;
	@Schema(description="Inidica si el producto ha sido vendido")
	private boolean vendido;
    public ProductoDTO() {}
    public ProductoDTO(String id, String titulo, Double precio, String idVendedor, boolean envioDisponible) {
			this.id = id;
			this.titulo = titulo;
			this.precio = precio;
			this.idVendedor = idVendedor;
			this.envioDisponible = envioDisponible;
			vendido = false;
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
	public String getIdVendedor() {
		return idVendedor;
	}	
	public void setIdVendedor(String idVendedor) {
		this.idVendedor = idVendedor;
	}
	public boolean isEnvioDisponible() {
		return envioDisponible;
	}
	public void setEnvioDisponible(boolean envioDisponible) {
		this.envioDisponible = envioDisponible;
	}
	public boolean isVendido() {
		return vendido;
	}
	public void setVendido(boolean vendido) {
		this.vendido = vendido;
	}
	
}
