package compraventas.rest;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import compraventas.modelo.Compraventa;
import compraventas.servicio.ServicioCompraventas;

@RestController
@RequestMapping("/compraventas")
public class ControladorCompraventas {

    private final ServicioCompraventas servicio;

    public ControladorCompraventas(ServicioCompraventas servicio) {
        this.servicio = servicio;
    }

    //POST /compraventas?idProducto=...&idComprador=...
    @PostMapping
    @PreAuthorize("hasRole('USUARIO') and #idComprador == principal")
    public ResponseEntity<Void> realizarCompra(@RequestParam String idProducto, 
                                               @RequestParam String idComprador) {
        
        Compraventa c = servicio.realizarCompra(idProducto, idComprador);
        
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(c.getId())
                .toUri();
        
        return ResponseEntity.created(location).build();
    }

    //GET /compraventas/compras/{idUsuario}
    @GetMapping("/compras/{idUsuario}")
    @PreAuthorize("hasRole('USUARIO') and #idUsuario == principal")
    public ResponseEntity<List<Compraventa>> getCompras(@PathVariable String idUsuario) {
        return ResponseEntity.ok(servicio.getComprasDeUsuario(idUsuario));
    }
    
    //GET /compraventas/ventas/{idUsuario}
    @GetMapping("/ventas/{idUsuario}")
    @PreAuthorize("hasRole('USUARIO') and #idUsuario == principal")
    public ResponseEntity<List<Compraventa>> getVentas(@PathVariable String idUsuario) {
        return ResponseEntity.ok(servicio.getVentasDeUsuario(idUsuario));
    }
    //GET /compraventas/entre/{idComprador}/{idVendedor}
    @GetMapping("/entre/{idComprador}/{idVendedor}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<Compraventa>> getCompraventasEntre(
            @PathVariable String idComprador, 
            @PathVariable String idVendedor) {
        return ResponseEntity.ok(servicio.getHistorialEntreUsuarios(idComprador, idVendedor));
    }
}