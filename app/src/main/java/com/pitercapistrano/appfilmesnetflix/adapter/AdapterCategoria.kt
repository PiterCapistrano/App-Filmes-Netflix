package com.pitercapistrano.appfilmesnetflix.adapter

// Importa as bibliotecas necessárias para o funcionamento do adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pitercapistrano.appfilmesnetflix.databinding.CategoriaItemBinding
import com.pitercapistrano.appfilmesnetflix.model.Categoria

// Declaração da classe AdapterCategoria, que serve como adaptador para exibir a lista de categorias de filmes
class AdapterCategoria(
    private val context: Context, // Armazena o contexto da aplicação, usado para acessar recursos
    val listaCategorias: MutableList<Categoria> // Recebe uma lista mutável de objetos do tipo Categoria
) : RecyclerView.Adapter<AdapterCategoria.CategoriaViewHolder>() { // Herda de RecyclerView.Adapter

    // Método responsável por inflar o layout de cada item da lista (categoria) e criar o ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        // Infla o layout XML do item da categoria usando view binding
        val itemLista = CategoriaItemBinding.inflate(LayoutInflater.from(context), parent, false)
        // Retorna uma nova instância de CategoriaViewHolder, passando o layout inflado
        return CategoriaViewHolder(itemLista)
    }

    // Método que retorna o tamanho da lista de categorias
    override fun getItemCount() = listaCategorias.size

    // Método que liga os dados de uma categoria ao ViewHolder para exibição na RecyclerView
    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
        // Define o título da categoria no TextView do item da lista
        holder.titulo.text = listaCategorias[position].titulo

        // Obtém a categoria atual da lista com base na posição
        val categoria = listaCategorias[position]

        // Configura o adaptador para o RecyclerView de filmes dentro da categoria atual
        holder.recyclerViewFilmes.adapter = AdapterFilme(context, categoria.filmes)

        // Define o layout manager para o RecyclerView de filmes como horizontal
        holder.recyclerViewFilmes.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    // Classe interna que representa o ViewHolder, que guarda as views de um item da lista
    inner class CategoriaViewHolder(binding: CategoriaItemBinding) : RecyclerView.ViewHolder(binding.root) {
        // Liga a variável título ao TextView para o título da categoria
        val titulo = binding.txtTitulo
        // Liga a variável recyclerViewFilmes ao RecyclerView para exibir os filmes da categoria
        val recyclerViewFilmes = binding.recyclerViewFilmes
    }
}
