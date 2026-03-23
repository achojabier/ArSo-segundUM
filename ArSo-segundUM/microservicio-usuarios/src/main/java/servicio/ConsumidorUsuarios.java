package servicio;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ConsumidorUsuarios implements IConsumidorUsuarios {

    @Inject
    private ServicioUsuarios servicioUsuarios = new ServicioUsuarios();

    @Override
    public void procesarNuevaCompraventa(String idComprador, String idVendedor) {
    	servicioUsuarios.sumarCompra(idComprador);
        servicioUsuarios.sumarVenta(idVendedor);
        
        System.out.println("Usuarios: Contadores actualizados. Comprador: " + idComprador + " | Vendedor: " + idVendedor);
    }
}