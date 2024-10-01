package com.pitercapistrano.appfilmesnetflix.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.MediaController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pitercapistrano.appfilmesnetflix.databinding.FilmeItemBinding
import com.pitercapistrano.appfilmesnetflix.model.Filme
import com.pitercapistrano.appfilmesnetflix.view.DetalhesFilme

class AdapterFilme(private val context: Context, private val listaFilmes: MutableList<Filme>):
    RecyclerView.Adapter<AdapterFilme.FilmeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmeViewHolder {
        val itemLista = FilmeItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return FilmeViewHolder(itemLista)
    }

    override fun getItemCount() = listaFilmes.size

    override fun onBindViewHolder(holder: FilmeViewHolder, position: Int) {
        val filme = listaFilmes[position]

        // Carregar imagem da capa com Glide
        Glide.with(context).load(filme.capa).centerCrop().into(holder.capa)

        // Evento de clique para abrir a tela de detalhes
        holder.capa.setOnClickListener {
            val intent = Intent(context, DetalhesFilme::class.java)
            intent.putExtra("capa", filme.capa)
            intent.putExtra("nome", filme.nome)
            intent.putExtra("descricao", filme.descricao)
            intent.putExtra("elenco", filme.elenco)
            intent.putExtra("video", filme.video)
            context.startActivity(intent)
        }
    }

    inner class FilmeViewHolder(val binding: FilmeItemBinding): RecyclerView.ViewHolder(binding.root) {
        val capa = binding.capaFilme
    }
}