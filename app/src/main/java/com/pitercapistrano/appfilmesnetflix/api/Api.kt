// Declara o pacote onde a classe está localizada
package com.pitercapistrano.appfilmesnetflix.api

// Importa o modelo de dados "Categorias" e as classes necessárias do Retrofit
import com.pitercapistrano.appfilmesnetflix.model.Categorias
import retrofit2.Call
import retrofit2.http.GET

// Define a interface da API que será usada para fazer chamadas HTTP
interface Api {

    // Método que faz uma requisição GET para obter uma lista de categorias de filmes
    // A URL parcial "filmes.json" será usada com a URL base da API configurada
    // A query string "?alt=media&token=43a14bdb-33d6-4480-94e8-bdb75ffc9ccc" é um parâmetro de acesso aos dados
    @GET("filmes.json?alt=media&token=43a14bdb-33d6-4480-94e8-bdb75ffc9ccc")
    fun listaCategorias(): Call<Categorias> // Define que a resposta será um objeto Call do tipo Categorias

}
