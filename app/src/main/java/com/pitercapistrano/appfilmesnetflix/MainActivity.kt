package com.pitercapistrano.appfilmesnetflix // Declara o pacote onde a classe está localizada

import android.content.Intent // Importa a classe Intent para iniciar outras atividades
import android.graphics.Color // Importa a classe Color para manipulação de cores
import android.os.Bundle // Importa a classe Bundle para passar dados entre atividades
import android.os.Handler // Importa a classe Handler para executar código após um atraso
import android.os.Looper // Importa a classe Looper para obter o loop de mensagens do thread principal
import androidx.activity.enableEdgeToEdge // Permite a interface de tela cheia
import androidx.appcompat.app.AppCompatActivity // Importa a classe AppCompatActivity para compatibilidade de recursos
import androidx.core.view.ViewCompat // Importa a classe ViewCompat para manipulação de visualizações
import androidx.core.view.WindowInsetsCompat // Importa a classe WindowInsetsCompat para gerenciar insets da janela
import com.pitercapistrano.appfilmesnetflix.view.FormLogin // Importa a Activity FormLogin

class MainActivity : AppCompatActivity() { // Declara a classe MainActivity que herda de AppCompatActivity
    override fun onCreate(savedInstanceState: Bundle?) { // Método chamado quando a activity é criada
        super.onCreate(savedInstanceState) // Chama o método da superclasse
        enableEdgeToEdge() // Habilita o modo de tela cheia
        setContentView(R.layout.activity_main) // Define o layout da activity como activity_main.xml

        // Configura o listener para aplicar insets da janela
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()) // Obtém os insets das barras do sistema
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom) // Define o padding da view principal
            insets // Retorna os insets
        }

        // Configura a cor da barra de status como preta
        window.statusBarColor = Color.parseColor("#000000") // Altera a cor da barra de status para preta
        window.decorView.systemUiVisibility = 0 // Define a visibilidade do sistema UI para o padrão

        // Cria um Handler que executa uma tarefa após um atraso
        Handler(Looper.getMainLooper()).postDelayed({ // Posta a tarefa no thread principal após 2 segundos
            val intent = Intent(this, FormLogin::class.java) // Cria um Intent para a Activity FormLogin
            startActivity(intent) // Inicia a Activity FormLogin
            finish() // Fecha a MainActivity
        }, 2000) // Define um atraso de 2000 milissegundos (2 segundos)
    }
}
