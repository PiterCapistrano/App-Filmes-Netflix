package com.pitercapistrano.appfilmesnetflix.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.pitercapistrano.appfilmesnetflix.R
import com.pitercapistrano.appfilmesnetflix.databinding.ActivityFormCadastroBinding

class FormCadastro : AppCompatActivity() {

    private lateinit var binding: ActivityFormCadastroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFormCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        window.statusBarColor = Color.parseColor("#FFFFFF")
        binding.editCdEmail.requestFocus()



        binding.btVamosLa.setOnClickListener {
            val email = binding.editCdEmail.text.toString()

            if (!email.isEmpty()){
                binding.containerCdSenha.visibility = View.VISIBLE
                binding.btVamosLa.visibility = View.GONE
                binding.txtTitulo.setText("Um mundo de séries e filmes \n ilimitados espera por você.")
                binding.txtDescricao.setText("Crie uma conta para saber mais sobre \n o nosso app de filmes.")
                binding.btContinuar.visibility = View.VISIBLE
                binding.containerCdEmail.boxStrokeColor = Color.parseColor("#FF018786")
                binding.containerCdEmail.helperText =""
                binding.containerHeader.visibility = View.VISIBLE
            } else{
                binding.containerCdEmail.helperText = "O e-mail é obrigatório!"
                binding.containerCdEmail.boxStrokeColor = Color.parseColor("#FF0000")
            }
        }

        binding.btContinuar.setOnClickListener {
            val email = binding.editCdEmail.text.toString()
            val senha = binding.editCdSenha.text.toString()

            if (!email.isEmpty() && !senha.isEmpty()) {
                Cadastro(email, senha)
            } else if (senha.isEmpty()) {
                binding.containerCdSenha.helperText = "A senha é obrigatório!"
                binding.containerCdSenha.boxStrokeColor = Color.parseColor("#FF0000")
                binding.containerCdEmail.boxStrokeColor = Color.parseColor("#FF018786")
            } else if (email.isEmpty()){
                binding.containerCdEmail.helperText = "O e-mail é obrigatório!"
                binding.containerCdEmail.boxStrokeColor = Color.parseColor("#FF0000")
            }
        }
        binding.txtEntrar.setOnClickListener {
            val intent = Intent(this, FormLogin::class.java)
            startActivity(intent)
        }
    }

    private fun Cadastro(email: String, senha: String){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener { cadastro ->
            if (cadastro.isSuccessful){
                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                binding.containerCdSenha.helperText = ""
                binding.containerCdSenha.boxStrokeColor = Color.parseColor("#FF018786")
                binding.containerCdEmail.boxStrokeColor = Color.parseColor("#FF018786")
                binding.containerCdEmail.helperText = ""
        }
        }.addOnFailureListener {
            val erro = it

            when{
                erro is FirebaseAuthWeakPasswordException -> {
                    binding.containerCdSenha.helperText = "Digite uma senha com no minnimo 6 caracteres!"
                    binding.containerCdSenha.boxStrokeColor = Color.parseColor("#FF0000")
                }
                erro is FirebaseAuthUserCollisionException -> {
                    binding.containerCdEmail.helperText = "E-mail já cadastrado!"
                    binding.containerCdEmail.boxStrokeColor = Color.parseColor("#FF0000")
                }
                erro is FirebaseNetworkException -> {
                    binding.containerCdSenha.helperText = "Sem conexão com a internet!"
                    binding.containerCdSenha.boxStrokeColor = Color.parseColor("#FF0000")
                    binding.containerCdEmail.boxStrokeColor = Color.parseColor("#FF0000")
                }
                else -> {
                    Toast.makeText(this, "Erro ao cadastrar o usuário!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}