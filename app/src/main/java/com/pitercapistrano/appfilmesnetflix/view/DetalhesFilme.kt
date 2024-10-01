package com.pitercapistrano.appfilmesnetflix.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.pitercapistrano.appfilmesnetflix.R
import com.pitercapistrano.appfilmesnetflix.databinding.ActivityDetalhesFilmeBinding

private lateinit var binding: ActivityDetalhesFilmeBinding

class DetalhesFilme : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetalhesFilmeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val capa = intent.extras?.getString("capa")
        val nome = intent.extras?.getString("nome")
        val descricao = intent.extras?.getString("descricao")
        val elenco = intent.extras?.getString("elenco")
        val video = intent.extras?.getString("video")

        Glide.with(this).load(capa).centerCrop().into(binding.capaFilme)
        binding.txtNome.setText(nome)
        binding.txtDescricao.setText("Descrição: $descricao")
        binding.txtElenco.setText("Elenco: $elenco")

        binding.btAssistir.setOnClickListener {
            val intent = Intent(this, Video::class.java)
                intent.putExtra("video", video)
            startActivity(intent)
        }
    }
}