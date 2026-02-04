package modelo;

public enum EstadoProducto {
	NUEVO("Nuevo"),
    COMO_NUEVO("Como nuevo"),
    BUEN_ESTADO("Buen estado"),
    ACEPTABLE("Aceptable"),
    PARA_PIEZAS_O_REPARAR("Para piezas o reparar");
	
	private final String nombre;

    EstadoProducto(String nombre) {
        this.nombre = nombre;
    }
    public String getNombre() {
        return nombre;
    }
    @Override
    public String toString() {
        return nombre;
    }
}
