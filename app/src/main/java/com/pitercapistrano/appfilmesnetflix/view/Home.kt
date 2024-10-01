package com.pitercapistrano.appfilmesnetflix.view // Declara o pacote onde a classe está localizada

import android.annotation.SuppressLint // Importa a anotação para suprimir avisos de lint
import android.content.Intent // Importa a classe Intent para iniciar outras atividades
import android.os.Bundle // Importa a classe Bundle para passar dados entre atividades
import android.view.View // Importa a classe View para manipulação de elementos da interface
import android.widget.LinearLayout // Importa a classe LinearLayout para uso em layouts
import android.widget.Toast // Importa a classe Toast para exibir mensagens curtas
import androidx.activity.enableEdgeToEdge // Permite a interface de tela cheia
import androidx.appcompat.app.AppCompatActivity // Importa a classe AppCompatActivity para compatibilidade de recursos
import androidx.core.view.ViewCompat // Importa a classe ViewCompat para manipulação de visualizações
import androidx.core.view.WindowInsetsCompat // Importa a classe WindowInsetsCompat para gerenciar insets da janela
import androidx.recyclerview.widget.LinearLayoutManager // Importa a classe LinearLayoutManager para gerenciar layouts de lista
import com.google.firebase.auth.FirebaseAuth // Importa a classe FirebaseAuth para autenticação com Firebase
import com.google.firebase.auth.FirebaseUser // Importa a classe FirebaseUser para manipular usuários autenticados
import com.pitercapistrano.appfilmesnetflix.R // Importa recursos da pasta R
import com.pitercapistrano.appfilmesnetflix.adapter.AdapterCategoria // Importa o adaptador para categorias de filmes
import com.pitercapistrano.appfilmesnetflix.api.Api // Importa a interface API para chamadas de rede
import com.pitercapistrano.appfilmesnetflix.databinding.ActivityHomeBinding // Importa o binding da Activity Home
import com.pitercapistrano.appfilmesnetflix.model.Categoria // Importa a classe Categoria para representar categorias de filmes
import com.pitercapistrano.appfilmesnetflix.model.Categorias // Importa a classe Categorias para manipular uma lista de categorias
import com.pitercapistrano.appfilmesnetflix.model.Filme // Importa a classe Filme para representar um filme
import retrofit2.Call // Importa a classe Call para fazer chamadas de rede
import retrofit2.Callback // Importa a classe Callback para tratar respostas de chamadas
import retrofit2.Response // Importa a classe Response para manipular respostas de chamadas
import retrofit2.Retrofit // Importa a classe Retrofit para facilitar chamadas de rede
import retrofit2.converter.gson.GsonConverterFactory // Importa o conversor Gson para converter JSON
import retrofit2.create // Importa a extensão para criar instâncias de interfaces

class Home : AppCompatActivity() { // Declara a classe Home que herda de AppCompatActivity

    private lateinit var binding: ActivityHomeBinding // Declara uma variável para o binding da activity
    private lateinit var adapterCategoria: AdapterCategoria // Declara uma variável para o adaptador de categorias
    private val listaCategorias: MutableList<Categoria> = mutableListOf() // Declara uma lista mutável de categorias

    override fun onCreate(savedInstanceState: Bundle?) { // Método chamado quando a activity é criada
        super.onCreate(savedInstanceState) // Chama o método da superclasse
        enableEdgeToEdge() // Habilita o modo de tela cheia
        binding = ActivityHomeBinding.inflate(layoutInflater) // Infla o layout da activity usando binding
        setContentView(binding.root) // Define a view da activity

        // Configura o listener para aplicar insets da janela
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()) // Obtém os insets das barras do sistema
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom) // Define o padding da view principal
            insets // Retorna os insets
        }

        // Configura o RecyclerView para exibir a lista de filmes
        val recyclerViewFilmes = binding.recyclerViewFilmes // Obtém a referência do RecyclerView
        recyclerViewFilmes.layoutManager = LinearLayoutManager(this) // Define o layout manager como LinearLayoutManager
        recyclerViewFilmes.setHasFixedSize(true) // Define que o tamanho do RecyclerView não muda
        adapterCategoria = AdapterCategoria(this, listaCategorias) // Inicializa o adaptador de categorias
        recyclerViewFilmes.adapter = adapterCategoria // Define o adaptador no RecyclerView

        // Configura o botão de logout
        binding.txtSair.setOnClickListener {
            FirebaseAuth.getInstance().signOut() // Desloga o usuário
            val intent = Intent(this, FormLogin::class.java) // Cria um Intent para a Activity de login
            startActivity(intent) // Inicia a Activity de login
            finish() // Fecha a Activity atual
            Toast.makeText(this, "Usuário deslogado com sucesso!", Toast.LENGTH_SHORT).show() // Exibe mensagem de sucesso
        }

        // Configura o Retrofit para fazer chamadas de rede
        val retrofit = Retrofit.Builder() // Inicia a construção do Retrofit
            .addConverterFactory(GsonConverterFactory.create()) // Adiciona o conversor Gson para JSON
            .baseUrl("https://firebasestorage.googleapis.com/v0/b/appfilmesnetflix.appspot.com/o/") // Define a URL base para as chamadas
            .build() // Constrói a instância do Retrofit
            .create(Api::class.java) // Cria a interface API para chamadas

        // Faz uma chamada para obter a lista de categorias
        retrofit.listaCategorias().enqueue(object : Callback<Categorias> { // Enfileira a chamada assíncrona
            @SuppressLint("NotifyDataSetChanged") // Suprime avisos de lint para notificar dados
            override fun onResponse(call: Call<Categorias>, response: Response<Categorias>) { // Método chamado quando a resposta é recebida
                if (response.code() == 200) { // Verifica se o código da resposta é 200 (OK)
                    response.body()?.let { // Se a resposta tiver um corpo
                        adapterCategoria.listaCategorias.addAll(it.categorias) // Adiciona as categorias ao adaptador
                        adapterCategoria.notifyDataSetChanged() // Notifica o adaptador que os dados mudaram
                        binding.containerProgressBar.visibility = View.GONE // Oculta o container da barra de progresso
                        binding.progressBar.visibility = View.GONE // Oculta a barra de progresso
                        binding.txtCarregando.visibility = View.GONE // Oculta o texto de carregando
                    }
                }
            }

            override fun onFailure(call: Call<Categorias>, t: Throwable) { // Método chamado em caso de falha na chamada
                Toast.makeText(applicationContext, "Erro ao recuperar os dados no banco de dados!", Toast.LENGTH_SHORT).show() // Exibe mensagem de erro
            }
        })
    }
}
