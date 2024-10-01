// Declara o pacote onde as classes de modelo estão localizadas
package com.pitercapistrano.appfilmesnetflix.model

// Importa a anotação SerializedName da biblioteca Gson para mapear os nomes dos campos JSON
import com.google.gson.annotations.SerializedName

// Define a classe de dados Categoria para representar uma categoria de filmes
data class Categoria(
    // Mapeia o campo "titulo" do JSON para a variável titulo
    @SerializedName("titulo") val titulo: String? = null,

    // Mapeia o campo "capas" do JSON para uma lista de objetos Filme
    @SerializedName("capas") val filmes: MutableList<Filme> = mutableListOf() // Inicializa como uma lista vazia por padrão
)

// Define a classe de dados Filme para representar as informações de um filme
data class Filme(
    // Mapeia o campo "url_imagem" do JSON para a variável capa, que guarda a URL da imagem da capa do filme
    @SerializedName("url_imagem") val capa: String? = null,

    // Mapeia o campo "id" do JSON para a variável id, que representa o identificador único do filme
    @SerializedName("id") val id: Int = 0, // Inicializa o id com valor 0 por padrão

    // Declara variáveis para nome, descrição, elenco e vídeo do filme, sem necessidade de mapeamento explícito
    val nome: String? = null,
    val descricao: String? = null,
    val elenco: String? = null,
    val video: String? = null
)

// Define a classe de dados Categorias, que contém uma lista de objetos Categoria
data class Categorias(
    // Mapeia o campo "categoria" do JSON para a variável categorias, que guarda uma lista de categorias de filmes
    @SerializedName("categoria") val categorias: MutableList<Categoria> = mutableListOf() // Inicializa como uma lista vazia por padrão
)
