package com.pitercapistrano.appfilmesnetflix.view

import android.app.Activity // Importa a classe Activity para interação com atividades do Android
import android.content.Intent // Importa a classe Intent para iniciar outras atividades
import android.graphics.Color // Importa a classe Color para manipulação de cores
import android.os.Bundle // Importa a classe Bundle para passar dados entre atividades
import android.os.Handler // Importa a classe Handler para agendar tarefas
import android.view.View // Importa a classe View para manipulação de elementos da interface
import android.widget.Toast // Importa a classe Toast para exibir mensagens curtas
import androidx.activity.enableEdgeToEdge // Permite a interface de tela cheia
import androidx.appcompat.app.AppCompatActivity // Importa a classe AppCompatActivity para compatibilidade de recursos
import androidx.core.view.ViewCompat // Importa a classe ViewCompat para manipulação de visualizações
import androidx.core.view.WindowInsetsCompat // Importa a classe WindowInsetsCompat para gerenciar insets da janela
import com.google.android.gms.auth.api.signin.GoogleSignIn // Importa a classe GoogleSignIn para autenticação do Google
import com.google.android.gms.auth.api.signin.GoogleSignInClient // Importa a classe GoogleSignInClient para interagir com o cliente de login do Google
import com.google.android.gms.auth.api.signin.GoogleSignInOptions // Importa a classe GoogleSignInOptions para configurar opções de login do Google
import com.google.android.gms.common.api.ApiException // Importa a classe ApiException para lidar com exceções de API
import com.google.firebase.auth.FirebaseAuth // Importa a classe FirebaseAuth para autenticação com Firebase
import com.google.firebase.auth.GoogleAuthProvider // Importa a classe GoogleAuthProvider para autenticação do Google com Firebase
import com.google.firebase.firestore.FirebaseFirestore // Importa a classe FirebaseFirestore para manipulação de Firestore
import com.pitercapistrano.appfilmesnetflix.R // Importa recursos da pasta R
import com.pitercapistrano.appfilmesnetflix.databinding.ActivityFormLoginBinding // Importa o binding da Activity de login

class FormLogin : AppCompatActivity() {

    private lateinit var binding: ActivityFormLoginBinding // Declara uma variável para o binding da activity
    private lateinit var googleSignInClient: GoogleSignInClient // Declara uma variável para o cliente de login do Google

    // Constantes usadas para identificar códigos de requisição
    companion object {
        private const val LOGIN_REQUEST_CODE = 1001 // Código para identificar a requisição de login
        private const val RC_SIGN_IN = 9001 // Código para identificar a requisição de login com Google
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // Chama o método da superclasse
        enableEdgeToEdge() // Habilita o modo de tela cheia
        binding = ActivityFormLoginBinding.inflate(layoutInflater) // Infla o layout da activity usando binding
        setContentView(binding.root) // Define a view da activity
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()) // Obtém os insets das barras do sistema
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom) // Define o padding da view principal
            insets // Retorna os insets
        }

        // Configura as opções de login com Google, solicitando ID e email
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_server_client_id))  // Solicita token de ID
            .requestEmail()  // Solicita email
            .build() // Constrói as opções de login

        // Inicializa o cliente de login do Google
        googleSignInClient = GoogleSignIn.getClient(this, gso) // Obtém o cliente de login do Google com as opções configuradas

        // Define o comportamento de clique no botão de login com Google
        binding.btGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent  // Cria o Intent de login do Google
            startActivityForResult(signInIntent, RC_SIGN_IN)  // Inicia a Activity de login
            Toast.makeText(this, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show() // Exibe mensagem de sucesso
        }

        binding.editEmail.requestFocus() // Define o foco no campo de email

        binding.btEntrar.setOnClickListener {
            val email = binding.editEmail.text.toString() // Obtém o texto do campo de email
            val senha = binding.editSenha.text.toString() // Obtém o texto do campo de senha

            when {
                email.isEmpty() -> { // Verifica se o email está vazio
                    binding.containerEmail.helperText = "Preencha o e-mail!" // Define a mensagem de ajuda
                    binding.containerEmail.boxStrokeColor = Color.parseColor("FF9800") // Define a cor da borda
                }
                senha.isEmpty() -> { // Verifica se a senha está vazia
                    binding.containerSenha.helperText = "Preencha a senha!" // Define a mensagem de ajuda
                    binding.containerEmail.boxStrokeColor = Color.parseColor("FF9800") // Define a cor da borda
                }
                else -> {
                    autenticacao(email, senha) // Chama o método de autenticação
                }
            }
        }

        binding.txtCadastrar.setOnClickListener { // Define o clique no texto de cadastrar
            val intent = Intent(this, FormCadastro::class.java) // Cria um Intent para a Activity de cadastro
            startActivity(intent) // Inicia a Activity de cadastro
        }
    }

    override fun onStart() {
        super.onStart() // Chama o método da superclasse

        val usuarioAtual = FirebaseAuth.getInstance().currentUser // Obtém o usuário autenticado atual

        if (usuarioAtual != null) { // Verifica se há um usuário autenticado
            goToHome() // Chama o método para ir para a tela inicial
        }
    }

    private fun autenticacao(email: String, senha: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha).addOnCompleteListener { autenticacao ->
            if (autenticacao.isSuccessful) { // Verifica se a autenticação foi bem-sucedida
                Toast.makeText(this, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show() // Exibe mensagem de sucesso
                goToHome() // Chama o método para ir para a tela inicial
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Erro ao efetuar o login do usuário!", Toast.LENGTH_SHORT).show() // Exibe mensagem de erro
        }
    }

    // Método que redireciona o usuário para a Activity Home
    private fun goToHome() {
        val intent = Intent(this, Home::class.java)  // Cria um Intent para a Home
        startActivityForResult(intent, LOGIN_REQUEST_CODE)  // Inicia a Activity Home
        finish()  // Fecha a Activity de login
    }

    // Método para tratar o resultado das Activities iniciadas
    @Deprecated("Deprecated in Java") // Indica que o método está obsoleto
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data) // Chama o método da superclasse
        if (requestCode == LOGIN_REQUEST_CODE) { // Verifica se o código de requisição é o de login
            if (resultCode == Activity.RESULT_OK) { // Verifica se o resultado é OK
                val intent = Intent(this, Home::class.java)  // Cria um Intent para a Home
                startActivity(intent) // Inicia a Activity Home
                finish()  // Fecha a Activity de login
            }
        } else if (requestCode == RC_SIGN_IN) { // Verifica se o código de requisição é o de login com Google
            // Lida com o retorno do login com Google
            val task = GoogleSignIn.getSignedInAccountFromIntent(data) // Obtém a conta Google autenticada
            try {
                val account = task.getResult(ApiException::class.java)!!  // Obtém a conta Google autenticada
                firebaseAuthWithGoogle(account.idToken!!)  // Autentica com Firebase usando Google
            } catch (e: ApiException) {
                Toast.makeText(this, "Erro ao logar!", Toast.LENGTH_SHORT).show() // Log de falha de login
            }
        }
    }

    // Método para autenticar com Firebase usando o token de login do Google
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(
            idToken,
            null
        )  // Cria a credencial de login com o token
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) { // Verifica se a autenticação foi bem-sucedida
                    // Recupera o usuário logado
                    val user = FirebaseAuth.getInstance().currentUser // Obtém o usuário autenticado
                    val displayName = user?.displayName // Obtém o nome do usuário

                    // Salva o nome do usuário no Firestore se for não nulo
                    if (displayName != null) {
                        val db = FirebaseFirestore.getInstance() // Obtém a instância do Firestore
                        val userId = FirebaseAuth.getInstance().currentUser?.uid // Obtém o ID do usuário
                        userId?.let { uid -> // Se o ID do usuário não for nulo
                            val userMap = hashMapOf(
                                "name" to displayName  // Mapeia o nome do usuário
                            )
                            // Salva os dados do usuário no Firestore
                            db.collection("Users").document(uid).set(userMap)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "DocumentSnapshot escrito com sucesso!!", Toast.LENGTH_SHORT).show() // Exibe mensagem de sucesso
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Erro ao escrever documento", Toast.LENGTH_SHORT).show() // Exibe mensagem de erro
                                }
                        }
                    }
                    binding.progressBar.visibility = View.VISIBLE  // Exibe a barra de progresso
                    Handler().postDelayed({
                        goToHome()  // Redireciona para a Home após 1 segundo
                    }, 1000)
                } else {
                    Toast.makeText(this, "Erro ao autenticar!", Toast.LENGTH_SHORT).show() // Exibe mensagem de erro
                }
            }
    }
}
