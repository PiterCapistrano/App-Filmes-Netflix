// Declara o pacote onde a classe está localizada
package com.pitercapistrano.appfilmesnetflix.view

// Importa as bibliotecas e classes necessárias
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge // Habilita a visualização de bordas
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase // Importa a classe base do Firebase
import com.google.firebase.FirebaseNetworkException // Exceção para erros de rede no Firebase
import com.google.firebase.auth.FirebaseAuth // Classe para autenticação de usuários
import com.google.firebase.auth.FirebaseAuthUserCollisionException // Exceção para e-mail já cadastrado
import com.google.firebase.auth.FirebaseAuthWeakPasswordException // Exceção para senhas fracas
import com.pitercapistrano.appfilmesnetflix.R
import com.pitercapistrano.appfilmesnetflix.databinding.ActivityFormCadastroBinding // Importa a classe de binding para a atividade

// Classe FormCadastro que representa a tela de cadastro de usuários
class FormCadastro : AppCompatActivity() {

    // Declaração da variável late-initialized para o binding da atividade
    private lateinit var binding: ActivityFormCadastroBinding

    // Método chamado quando a atividade é criada
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // Chama o método da superclasse para inicializar a atividade
        enableEdgeToEdge() // Habilita a visualização de bordas para a atividade

        // Infla o layout da atividade usando View Binding
        binding = ActivityFormCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root) // Define a visualização da atividade como a raiz do binding

        // Define um listener para aplicar os insets da janela no layout principal
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            // Obtém os insets das barras do sistema (status bar, navigation bar)
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Define o padding do view principal com os insets obtidos
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets // Retorna os insets
        }

        // Define a cor da barra de status como branca
        window.statusBarColor = Color.parseColor("#FFFFFF")
        // Solicita o foco no campo de entrada de e-mail
        binding.editCdEmail.requestFocus()

        // Configura o listener para o botão "Vamos lá"
        binding.btVamosLa.setOnClickListener {
            // Obtém o texto do campo de entrada de e-mail
            val email = binding.editCdEmail.text.toString()

            // Verifica se o e-mail não está vazio
            if (!email.isEmpty()) {
                // Torna visível o container da senha e oculta o botão "Vamos lá"
                binding.containerCdSenha.visibility = View.VISIBLE
                binding.btVamosLa.visibility = View.GONE
                // Atualiza os textos de título e descrição
                binding.txtTitulo.setText("Um mundo de séries e filmes \n ilimitados espera por você.")
                binding.txtDescricao.setText("Crie uma conta para saber mais sobre \n o nosso app de filmes.")
                // Torna visível o botão "Continuar" e atualiza as cores do container de e-mail
                binding.btContinuar.visibility = View.VISIBLE
                binding.containerCdEmail.boxStrokeColor = Color.parseColor("#FF018786") // Cor de borda do campo de e-mail
                binding.containerCdEmail.helperText = "" // Limpa a mensagem de ajuda
                binding.containerHeader.visibility = View.VISIBLE // Torna o cabeçalho visível
            } else {
                // Exibe uma mensagem de erro se o e-mail estiver vazio
                binding.containerCdEmail.helperText = "O e-mail é obrigatório!"
                binding.containerCdEmail.boxStrokeColor = Color.parseColor("#FF0000") // Muda a borda para vermelho
            }
        }

        // Configura o listener para o botão "Continuar"
        binding.btContinuar.setOnClickListener {
            // Obtém o texto dos campos de e-mail e senha
            val email = binding.editCdEmail.text.toString()
            val senha = binding.editCdSenha.text.toString()

            // Verifica se ambos os campos estão preenchidos
            if (!email.isEmpty() && !senha.isEmpty()) {
                // Chama a função de cadastro com o e-mail e senha
                Cadastro(email, senha)
            } else if (senha.isEmpty()) {
                // Exibe uma mensagem de erro se a senha estiver vazia
                binding.containerCdSenha.helperText = "A senha é obrigatória!"
                binding.containerCdSenha.boxStrokeColor = Color.parseColor("#FF0000") // Muda a borda para vermelho
                binding.containerCdEmail.boxStrokeColor = Color.parseColor("#FF018786") // Restaura a cor do campo de e-mail
            } else if (email.isEmpty()) {
                // Exibe uma mensagem de erro se o e-mail estiver vazio
                binding.containerCdEmail.helperText = "O e-mail é obrigatório!"
                binding.containerCdEmail.boxStrokeColor = Color.parseColor("#FF0000") // Muda a borda para vermelho
            }
        }

        // Configura o listener para o link de login
        binding.txtEntrar.setOnClickListener {
            // Cria uma intenção para iniciar a atividade de login
            val intent = Intent(this, FormLogin::class.java)
            // Inicia a atividade de login
            startActivity(intent)
        }
    }

    // Função que realiza o cadastro do usuário
    private fun Cadastro(email: String, senha: String) {
        // Chama a função de autenticação do Firebase para criar um novo usuário
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener { cadastro ->
            // Verifica se o cadastro foi bem-sucedido
            if (cadastro.isSuccessful) {
                // Exibe uma mensagem de sucesso
                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                // Limpa as mensagens de ajuda e restaura as cores das bordas
                binding.containerCdSenha.helperText = ""
                binding.containerCdSenha.boxStrokeColor = Color.parseColor("#FF018786")
                binding.containerCdEmail.boxStrokeColor = Color.parseColor("#FF018786")
                binding.containerCdEmail.helperText = ""
                // Cria uma intenção para iniciar a atividade inicial (Home)
                val intent = Intent(this, Home::class.java)
                // Inicia a atividade Home
                startActivity(intent)
                finish() // Finaliza a atividade atual
            }
        }.addOnFailureListener {
            // Captura o erro ao tentar cadastrar
            val erro = it

            // Verifica o tipo de erro e exibe mensagens apropriadas
            when {
                erro is FirebaseAuthWeakPasswordException -> {
                    // Mensagem para senha fraca
                    binding.containerCdSenha.helperText = "Digite uma senha com no mínimo 6 caracteres!"
                    binding.containerCdSenha.boxStrokeColor = Color.parseColor("#FF0000") // Muda a borda para vermelho
                }
                erro is FirebaseAuthUserCollisionException -> {
                    // Mensagem para e-mail já cadastrado
                    binding.containerCdEmail.helperText = "E-mail já cadastrado!"
                    binding.containerCdEmail.boxStrokeColor = Color.parseColor("#FF0000") // Muda a borda para vermelho
                }
                erro is FirebaseNetworkException -> {
                    // Mensagem para erro de rede
                    binding.containerCdSenha.helperText = "Sem conexão com a internet!"
                    binding.containerCdSenha.boxStrokeColor = Color.parseColor("#FF0000") // Muda a borda para vermelho
                    binding.containerCdEmail.boxStrokeColor = Color.parseColor("#FF0000") // Muda a borda para vermelho
                }
                else -> {
                    // Mensagem genérica para outros erros
                    Toast.makeText(this, "Erro ao cadastrar o usuário!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
