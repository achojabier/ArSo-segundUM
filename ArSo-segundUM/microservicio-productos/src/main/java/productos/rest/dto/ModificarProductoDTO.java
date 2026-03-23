package productos.rest.dto;

public class ModificarProductoDTO {
	private String descripcion;
	private double precio;
	public ModificarProductoDTO() {
		
	}
	
	public ModificarProductoDTO(String d, double p) {
		this.descripcion=d;
		this.precio=p;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}
	
	
}
