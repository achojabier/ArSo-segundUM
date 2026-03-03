package productos.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;

import productos.modelo.Categoria;
import productos.modelo.EstadoProducto;
import productos.modelo.LugarDeRecogida;
import productos.modelo.Usuario;

public class ProductoDTO implements Serializable{
	private String id;
    private String titulo;
    private String descripcion;
    private Double precio;
    private EstadoProducto estado;
    private boolean envioDisponible;
    private int visualizaciones;
    private LocalDateTime fechaPublicacion;
    private Categoria categoria;
    private Usuario vendedor;
    private LugarDeRecogida recogida;
    public ProductoDTO() {}
    public ProductoDTO(String id, String titulo, String descripcion, Double precio, 
            EstadoProducto estado, boolean envioDisponible, int visualizaciones, 
            LocalDateTime fechaPublicacion, Categoria categoria, 
			            Usuario vendedor, LugarDeRecogida recogida) {
			this.id = id;
			this.titulo = titulo;
			this.descripcion = descripcion;
			this.precio = precio;
			this.estado = estado;
			this.envioDisponible = envioDisponible;
			this.visualizaciones = visualizaciones;
			this.fechaPublicacion = fechaPublicacion;
			this.categoria = categoria;
			this.vendedor = vendedor;
			this.recogida = recogida;
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
	public boolean isEnvioDisponible() {
		return envioDisponible;
	}
	public void setEnvioDisponible(boolean envioDisponible) {
		this.envioDisponible = envioDisponible;
	}
	public int getVisualizaciones() {
		return visualizaciones;
	}
	public void setVisualizaciones(int visualizaciones) {
		this.visualizaciones = visualizaciones;
	}
	public LocalDateTime getFechaPublicacion() {
		return fechaPublicacion;
	}
	public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
		this.fechaPublicacion = fechaPublicacion;
	}
	public Categoria getCategoria() {
		return categoria;
	}
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	public Usuario getVendedor() {
		return vendedor;
	}
	public void setVendedor(Usuario vendedor) {
		this.vendedor = vendedor;
	}
	public LugarDeRecogida getRecogida() {
		return recogida;
	}
	public void setRecogida(LugarDeRecogida recogida) {
		this.recogida = recogida;
	}
    
}
