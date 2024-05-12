package com.example.vk_poke

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val networkService = NetworkService()
    private val pokemonService = networkService.createRetrofitService()
    private val pokemonDataHandler = PokemonDataHandler(this, coroutineScope, pokemonService)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.title = "Pokedex"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val url = intent.getStringExtra("url")
        if (url != null) {
            coroutineScope.launch {
                try {
                    val pokemonId = getPokemonIdFromUrl(url)
                    val species = pokemonService.getPokemonSpecies(pokemonId)
                    val evolutionChainId = getEvolutionChainIdFromUrl(species.evolution_chain.url)
                    val evolutionChain = pokemonService.getEvolutionChain(evolutionChainId)
                    val pokemonDetail = pokemonService.fetchPokemonDetails(pokemonId)
                    withContext(Dispatchers.Main) {
                        pokemonDataHandler.handlePokemonDetail(pokemonDetail)
                        pokemonDataHandler.handlePokemonImage(pokemonDetail.sprites.front_default) { color ->
                            pokemonDataHandler.handlePokemonAbilities(pokemonDetail.abilities, color)
                        }
                        pokemonDataHandler.handlePokemonEvolutionImages(evolutionChain.chain)
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@DetailActivity, "Произошла ошибка: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this, MainActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }

    private fun getPokemonIdFromUrl(url: String): Int {
        val parts = url.split("/")
        return parts[parts.size - 2].toInt()
    }

    private fun getEvolutionChainIdFromUrl(url: String): Int {
        val parts = url.split("/")
        return parts[parts.size - 2].toInt()
    }
}







