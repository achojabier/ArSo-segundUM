package test;

import java.io.File;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import repositorio.FactoriaRepositorios;
import modelo.Categoria;
import modelo.EstadoProducto;
import modelo.Producto;
import servicio.ServicioCategorias;
import servicio.ServicioProductos;
import servicio.ServicioUsuarios;

public class Programa {
	public static void main(String args[]) {
		FactoriaRepositorios factoria = new FactoriaRepositorios();
		ServicioUsuarios servicioUsuarios = new ServicioUsuarios(factoria.getRepositorioUsuarios());
        ServicioCategorias servicioCategorias = new ServicioCategorias(factoria.getRepositorioCategorias());
        ServicioProductos servicioProductos = new ServicioProductos(factoria.getRepositorioProductos(),factoria.getRepositorioCategorias(),factoria.getRepositorioUsuarios());
        try {
            String idUsuario1 = servicioUsuarios.altaUsuario("Paco", "García", "paco@um.es");

            
            String idUsuario2 = servicioUsuarios.altaUsuario("Ana", "Lopez", "ana@um.es");

            String idUsuario3 = servicioUsuarios.altaUsuario("Alberto", "Pérez", "alberto@um.es");

            
            String idUsuario4 = servicioUsuarios.altaUsuario("Javier", "León", "javier@um.es");
            

            
			File carpetaCategorias = new File("src/main/resources/Categorias");
			            
			            File[] archivosXML = carpetaCategorias.listFiles((dir, nombre) -> nombre.endsWith(".xml"));
			            
			            if (archivosXML != null) {
			                for (File archivo : archivosXML) {
			                    String rutaRecurso = "src/main/resources/Categorias/" + archivo.getName();
			                    
			                    try {
			                        System.out.print("Cargando " + archivo.getName() + "... ");
			                        servicioCategorias.cargarJerarquia(rutaRecurso);
			                        System.out.println("Cargado");
			                    } catch (Exception e) {
			                        System.out.println("ERROR al cargar " + archivo.getName() + "-> " + e.getMessage());
			                    }
			                }
			            } else {
			                System.err.println("No se encontraron archivos XML");
			            }
            
            
			            System.out.println("Intentando cargar 'Arte_y_ocio.xml' de nuevo (debe ser ignorado)...");
			            servicioCategorias.cargarJerarquia("src/main/resources/Categorias/Arte_y_ocio.xml");
			            
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
