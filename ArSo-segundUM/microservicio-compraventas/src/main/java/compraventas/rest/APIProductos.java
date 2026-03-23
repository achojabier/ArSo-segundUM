package compraventas.rest;

import compraventas.dto.ProductoDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIProductos {
    
    @GET("productos/{id}")
    Call<ProductoDTO> getProducto(@Path("id") String id);
}