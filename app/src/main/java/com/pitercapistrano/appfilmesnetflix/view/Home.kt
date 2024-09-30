package com.pitercapistrano.appfilmesnetflix.view

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.pitercapistrano.appfilmesnetflix.R
import com.pitercapistrano.appfilmesnetflix.adapter.AdapterCategoria
import com.pitercapistrano.appfilmesnetflix.databinding.ActivityHomeBinding
import com.pitercapistrano.appfilmesnetflix.model.Categoria
import com.pitercapistrano.appfilmesnetflix.model.Filme

class Home : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var adapterCategoria: AdapterCategoria
    private val listaCategorias: MutableList<Categoria> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerViewFilmes = binding.recyclerViewFilmes
        recyclerViewFilmes.layoutManager = LinearLayoutManager(this)
        recyclerViewFilmes.setHasFixedSize(true)
        adapterCategoria = AdapterCategoria(this, listaCategorias)
        recyclerViewFilmes.adapter = adapterCategoria
        getCategorias()

        binding.txtSair.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, FormLogin::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this, "Usuário deslogado com sucesso!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCategorias(){
        val categoria1 = Categoria("Categoria 1")
        listaCategorias.add(categoria1)

        val categoria2 = Categoria("Categoria 2")
        listaCategorias.add(categoria2)

        val categoria3 = Categoria("Categoria 3")
        listaCategorias.add(categoria3)

        val categoria4 = Categoria("Categoria 4")
        listaCategorias.add(categoria4)
    }


}