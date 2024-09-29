package com.pitercapistrano.appfilmesnetflix.view

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.pitercapistrano.appfilmesnetflix.R
import com.pitercapistrano.appfilmesnetflix.databinding.ActivityFormLoginBinding

class FormLogin : AppCompatActivity() {

    private lateinit var binding: ActivityFormLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient


    // Constantes usadas para identificar códigos de requisição
    companion object {
        private const val LOGIN_REQUEST_CODE = 1001
        private const val RC_SIGN_IN = 9001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFormLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configura as opções de login com Google, solicitando ID e email
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_server_client_id))  // Solicita token de ID
            .requestEmail()  // Solicita email
            .build()

        // Inicializa o cliente de login do Google
        googleSignInClient = GoogleSignIn.getClient(this, gso)


        // Define o comportamento de clique no botão de login com Google
        binding.btGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent  // Cria o Intent de login do Google
            startActivityForResult(signInIntent, RC_SIGN_IN)  // Inicia a Activity de login
            Toast.makeText(this, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show()
        }

        binding.editEmail.requestFocus()

        binding.btEntrar.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val senha = binding.editSenha.text.toString()

            when{
                email.isEmpty() -> {
                    binding.containerEmail.helperText = "Preencha o e-mail!"
                    binding.containerEmail.boxStrokeColor = Color.parseColor("FF9800")
                }
                senha.isEmpty() -> {
                    binding.containerSenha.helperText = "Preencha a senha!"
                    binding.containerEmail.boxStrokeColor = Color.parseColor("FF9800")
                }
                else -> {
                    autenticacao(email, senha)
                }
            }
        }

        binding.txtCadastrar.setOnClickListener {
            val intent = Intent(this, FormCadastro::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        val usuarioAtual = FirebaseAuth.getInstance().currentUser

        if (usuarioAtual != null){
            goToHome()
        }
    }

    private fun autenticacao(email: String, senha: String){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha).addOnCompleteListener {autenticacao ->
            if (autenticacao.isSuccessful){
                Toast.makeText(this, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show()
                goToHome()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Erro ao efetuar o login do usuário!", Toast.LENGTH_SHORT).show()
        }
    }

    // Método que redireciona o usuário para a Activity Home
    private fun goToHome() {
        val intent = Intent(this, Home::class.java)  // Cria um Intent para a Home
        startActivityForResult(intent, LOGIN_REQUEST_CODE)  // Inicia a Activity Home
        finish()
    }

    // Método para tratar o resultado das Activities iniciadas
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOGIN_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val intent = Intent(this, Home::class.java)  // Volta para a Home se o login foi bem-sucedido
                startActivity(intent)
                finish()  // Fecha a Activity de login
            }
        } else if (requestCode == RC_SIGN_IN) {
            // Lida com o retorno do login com Google
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
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
                if (task.isSuccessful) {
                    // Recupera o usuário logado
                    val user = FirebaseAuth.getInstance().currentUser
                    val displayName = user?.displayName

                    // Salva o nome do usuário no Firestore se for não nulo
                    if (displayName != null) {
                        val db = FirebaseFirestore.getInstance()
                        val userId = FirebaseAuth.getInstance().currentUser?.uid
                        userId?.let { uid ->
                            val userMap = hashMapOf(
                                "name" to displayName  // Mapeia o nome do usuário
                            )
                            // Salva os dados do usuário no Firestore
                            db.collection("Users").document(uid).set(userMap)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "DocumentSnapshot escrito com sucesso!!", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Erro ao escrever documento", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                    binding.progressBar.visibility = View.VISIBLE  // Exibe a barra de progresso
                    Handler().postDelayed({
                        goToHome()  // Redireciona para a Home após 1 segundo
                    }, 1000)
                } else {
                    Toast.makeText(this, "Erro ao autenticar!", Toast.LENGTH_SHORT).show()
                }
            }
    }
}