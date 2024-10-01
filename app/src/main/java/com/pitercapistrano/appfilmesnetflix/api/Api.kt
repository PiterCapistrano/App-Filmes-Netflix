package com.pitercapistrano.appfilmesnetflix.api


import com.pitercapistrano.appfilmesnetflix.model.Categorias
import retrofit2.Call
import retrofit2.http.GET

interface Api {
    @GET("filmes.json?alt=media&token=43a14bdb-33d6-4480-94e8-bdb75ffc9ccc")
    fun listaCategorias(): Call<Categorias>

}