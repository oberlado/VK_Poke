package com.example.vk_poke

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: PokemonAdapter
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private var pokemons = mutableListOf<Pokemon>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Pokedex"
        adapter = PokemonAdapter { pokemon ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("url", pokemon.url)
            startActivity(intent)
        }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        loadPokemons()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredModelList: List<Pokemon> = filter(pokemons, newText)
                adapter.setPokemons(filteredModelList)
                return true
            }
        })
        return true
    }

    private fun filter(models: List<Pokemon>, query: String?): List<Pokemon> {
        val lowerCaseQuery = query?.toLowerCase(Locale.getDefault())
        val filteredModelList: MutableList<Pokemon> = ArrayList()
        for (model in models) {
            val text = model.name.toLowerCase(Locale.getDefault())
            if (text.contains(lowerCaseQuery.toString())) {
                model.imageUrl = null
                filteredModelList.add(model)
            }
        }
        return filteredModelList
    }


    private fun loadPokemons() {
        coroutineScope.launch {
            try {
                val service = Retrofit.Builder().baseUrl("https://pokeapi.co/api/v2/")
                    .addConverterFactory(GsonConverterFactory.create()).build()
                    .create(PokemonService::class.java)

                val response = service.getPokemons(0, 100000)
                pokemons = response.results.toMutableList()

                withContext(Dispatchers.Main) {
                    adapter.addPokemons(pokemons)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Произошла ошибка: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}




