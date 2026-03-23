package productos.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import productos.modelo.Categoria;
import productos.servicio.ServicioCategorias;

@RestController
@RequestMapping("/categorias")
public class ControladorCategorias {
	private ServicioCategorias sc;
	
	@Autowired
    public ControladorCategorias(ServicioCategorias servicio) {
        this.sc = servicio;
    }
	
	@GetMapping
    public ResponseEntity<List<Categoria>> getCategorias() {
        
        List<Categoria> categorias = sc.getTodasCategorias();
        
        return ResponseEntity.ok(categorias);
    }
	
	@GetMapping("/raices")
    public ResponseEntity<List<Categoria>> getRaices() {
        List<Categoria> categorias = sc.getRaices();
        
        return ResponseEntity.ok(categorias);
    }
	
	@GetMapping("/{id}")
    public ResponseEntity<List<Categoria>> getHijos(@PathVariable String id) {
        List<Categoria> categorias = sc.getHijos(id);
        
        return ResponseEntity.ok(categorias);
    }
}
