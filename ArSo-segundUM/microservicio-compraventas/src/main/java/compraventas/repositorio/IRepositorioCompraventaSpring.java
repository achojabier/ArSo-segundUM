package compraventas.repositorio;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import compraventas.modelo.Compraventa;

public interface IRepositorioCompraventaSpring extends MongoRepository<Compraventa, String>{
	List<Compraventa> findByIdComprador(String idComprador);
    List<Compraventa> findByIdVendedor(String idVendedor);
    List<Compraventa> findByIdCompradorAndIdVendedor(String idComprador, String idVendedor);
}
