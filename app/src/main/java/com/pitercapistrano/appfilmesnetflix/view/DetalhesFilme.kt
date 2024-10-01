// Declara o pacote onde a classe está localizada
package com.pitercapistrano.appfilmesnetflix.view

// Importa as bibliotecas e classes necessárias para a atividade
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge // Permite que a atividade utilize a visualização de bordas
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide // Biblioteca para carregar imagens
import com.pitercapistrano.appfilmesnetflix.R
import com.pitercapistrano.appfilmesnetflix.databinding.ActivityDetalhesFilmeBinding // Importa a classe de binding para a atividade

// Declaração da variável late-initialized para o binding da atividade
private lateinit var binding: ActivityDetalhesFilmeBinding

// Classe DetalhesFilme, que exibe os detalhes de um filme
class DetalhesFilme : AppCompatActivity() {

    // Anotação que suprime os avisos sobre a internacionalização do texto
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // Chama o método da superclasse para inicializar a atividade
        enableEdgeToEdge() // Habilita a visualização de bordas para a atividade

        // Infla o layout da atividade usando View Binding
        binding = ActivityDetalhesFilmeBinding.inflate(layoutInflater)
        setContentView(binding.root) // Define a visualização da atividade como a raiz do binding

        // Define um listener para aplicar os insets da janela no layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            // Obtém os insets das barras do sistema (status bar, navigation bar)
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Define o padding do view principal com os insets obtidos
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets // Retorna os insets
        }

        // Obtém os dados do filme passados pela Intent
        val capa = intent.extras?.getString("capa") // URL da imagem da capa
        val nome = intent.extras?.getString("nome") // Nome do filme
        val descricao = intent.extras?.getString("descricao") // Descrição do filme
        val elenco = intent.extras?.getString("elenco") // Elenco do filme
        val video = intent.extras?.getString("video") // URL do vídeo do filme

        // Carrega a imagem da capa do filme usando Glide
        Glide.with(this).load(capa).centerCrop().into(binding.capaFilme)
        // Define o texto do nome do filme no TextView correspondente
        binding.txtNome.setText(nome)
        // Define a descrição do filme, adicionando o prefixo "Descrição: "
        binding.txtDescricao.setText("Descrição: $descricao")
        // Define o elenco do filme, adicionando o prefixo "Elenco: "
        binding.txtElenco.setText("Elenco: $elenco")

        // Define um listener para o botão "Assistir"
        binding.btAssistir.setOnClickListener {
            // Cria uma intenção para iniciar a atividade Video
            val intent = Intent(this, Video::class.java)
            // Adiciona a URL do vídeo como um extra na intenção
            intent.putExtra("video", video)
            // Inicia a atividade Video
            startActivity(intent)
        }
    }
}
