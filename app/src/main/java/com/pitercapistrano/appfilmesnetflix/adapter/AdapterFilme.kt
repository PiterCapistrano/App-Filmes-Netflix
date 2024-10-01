package com.pitercapistrano.appfilmesnetflix.adapter

// Importa as bibliotecas necessárias para o funcionamento do adapter
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pitercapistrano.appfilmesnetflix.databinding.FilmeItemBinding
import com.pitercapistrano.appfilmesnetflix.model.Filme
import com.pitercapistrano.appfilmesnetflix.view.DetalhesFilme

// Declaração da classe AdapterFilme, que serve como adaptador para exibir a lista de filmes
class AdapterFilme(
    private val context: Context, // Armazena o contexto da aplicação, usado para acessar recursos
    private val listaFilmes: MutableList<Filme> // Recebe uma lista mutável de objetos do tipo Filme
) : RecyclerView.Adapter<AdapterFilme.FilmeViewHolder>() { // Herda de RecyclerView.Adapter

    // Método responsável por inflar o layout de cada item da lista (filme) e criar o ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmeViewHolder {
        // Infla o layout XML do item do filme usando view binding
        val itemLista = FilmeItemBinding.inflate(LayoutInflater.from(context), parent, false)
        // Retorna uma nova instância de FilmeViewHolder, passando o layout inflado
        return FilmeViewHolder(itemLista)
    }

    // Método que retorna o tamanho da lista de filmes
    override fun getItemCount() = listaFilmes.size

    // Método que liga os dados de um filme ao ViewHolder para exibição na RecyclerView
    override fun onBindViewHolder(holder: FilmeViewHolder, position: Int) {
        // Obtém o filme atual da lista com base na posição
        val filme = listaFilmes[position]

        // Carrega a imagem da capa do filme usando a biblioteca Glide
        Glide.with(context).load(filme.capa).centerCrop().into(holder.capa)

        // Define um evento de clique para a imagem da capa, que abre a tela de detalhes do filme
        holder.capa.setOnClickListener {
            // Cria uma intenção para iniciar a activity DetalhesFilme
            val intent = Intent(context, DetalhesFilme::class.java)
            // Adiciona dados do filme como extras na intenção (capa, nome, descrição, elenco, vídeo)
            intent.putExtra("capa", filme.capa)
            intent.putExtra("nome", filme.nome)
            intent.putExtra("descricao", filme.descricao)
            intent.putExtra("elenco", filme.elenco)
            intent.putExtra("video", filme.video)
            // Inicia a activity DetalhesFilme passando os dados do filme
            context.startActivity(intent)
        }
    }

    // Classe interna que representa o ViewHolder, que guarda as views de um item da lista
    inner class FilmeViewHolder(val binding: FilmeItemBinding) : RecyclerView.ViewHolder(binding.root) {
        // Liga a variável capa ao ImageView para a capa do filme
        val capa = binding.capaFilme
    }
}
