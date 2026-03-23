package productos.rest.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import productos.modelo.Categoria;
import productos.modelo.EstadoProducto;
import productos.modelo.Usuario;

public class CrearProductoDTO {
	@NotNull
	private String titulo;
	@NotNull
    private String descripcion;
	@NotNull
    private Double precio;
	@NotNull
    private EstadoProducto estado;
	@NotNull
	private String idCategoria;
	@NotNull
    private String idVendedor;
	@NotNull
	private boolean envioDisponible;
    public CrearProductoDTO() {}
    public CrearProductoDTO(String titulo, String descripcion, Double precio, 
            EstadoProducto estado, String c,boolean envio, String v) {
			this.titulo = titulo;
			this.descripcion = descripcion;
			this.precio = precio;
			this.estado = estado;
			this.idCategoria=c;
			this.envioDisponible=envio;
			this.idVendedor=v;
		}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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
	public String getIdCategoria() {
		return idCategoria;
	}
	public void setIdCategoria(String idCategoria) {
		this.idCategoria = idCategoria;
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
	
}
