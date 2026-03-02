package test;

import java.io.File;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import repositorio.FactoriaRepositorios;
import servicio.ServicioUsuarios;

public class Programa {
	public static void main(String args[]) {
		FactoriaRepositorios factoria = new FactoriaRepositorios();
		ServicioUsuarios servicioUsuarios = new ServicioUsuarios(factoria.getRepositorioUsuarios());
        try {
        	String idUsuario1 = servicioUsuarios.altaUsuario("Paco", "García", "paco@um.es", "1234", new Date(), "666111222");

            
            String idUsuario2 = servicioUsuarios.altaUsuario("Ana", "Lopez", "ana@um.es", "5678", new Date(), null);
            
			            
			            System.out.println("Fin.");

        } catch (Exception e) {
            System.err.println("¡ERROR EN LA EJECUCIÓN! -> " + e.getMessage());
            e.printStackTrace();
        } finally {
            factoria.close();
            System.out.println("\n--- Conexión a BBDD cerrada. Fin del programa. ---");
        }
	}
}
