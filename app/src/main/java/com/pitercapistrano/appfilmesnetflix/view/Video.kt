package com.pitercapistrano.appfilmesnetflix.view

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pitercapistrano.appfilmesnetflix.R
import com.pitercapistrano.appfilmesnetflix.databinding.ActivityVideoBinding

class Video : AppCompatActivity() {

    private lateinit var binding: ActivityVideoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Esconder a status bar e a barra de navegação para fullscreen imersivo
        hideSystemUI()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtendo o URL do vídeo passado pela Intent
        val videoUrl = intent.getStringExtra("video")

        // Verificando se o URL do vídeo é válido
        if (videoUrl != null) {
            val videoUri = Uri.parse(videoUrl)
            binding.videoView.setVideoURI(videoUri)

            // Configurando os controles de mídia (play, pause, etc.)
            val mediaController = MediaController(this)
            mediaController.setAnchorView(binding.videoView)
            binding.videoView.setMediaController(mediaController)

            // Iniciando o vídeo
            binding.videoView.setOnPreparedListener { mediaPlayer ->
                mediaPlayer.isLooping = true
                binding.videoView.start()
            }
        }
    }

    // Método para esconder a barra de status e a barra de navegação
    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN // Esconde a status bar
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // Esconde a barra de navegação
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY // Mantém a interface imersiva
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE // Mantém a barra oculta após a interação
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )
    }

    // Método para restaurar a barra de status e navegação (caso precise)
    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                )
    }
}