package com.pitercapistrano.appfilmesnetflix.view

import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import com.pitercapistrano.appfilmesnetflix.databinding.ActivityVideoBinding

class Video : AppCompatActivity() {

    private lateinit var binding: ActivityVideoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
}