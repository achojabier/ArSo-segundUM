package compraventas.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import compraventas.rest.APIProductos;
import compraventas.rest.APIUsuarios;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class RetrofitConfig {

    @Bean
    public APIProductos apiProductos() {
        return new Retrofit.Builder()
                .baseUrl("http://productos:8080/") 
                .addConverterFactory(JacksonConverterFactory.create())
                .build()
                .create(APIProductos.class);
    }

    @Bean
    public APIUsuarios apiUsuarios() {
        return new Retrofit.Builder()
                .baseUrl("http://usuarios:8082/api/") 
                .addConverterFactory(JacksonConverterFactory.create())
                .build()
                .create(APIUsuarios.class);
    }
}