package com.pitercapistrano.appfilmesnetflix.view

import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

        binding.btCadastrar.setOnClickListener {
            val email = binding.editCdEmail.text.toString()

            if (email.isEmpty()){
                binding.containerCdEmail.helperText = "O e-mail é obrigatório!"
                binding.containerCdEmail.boxStrokeColor = Color.parseColor("#FF0000")
            }
        }
    }
}