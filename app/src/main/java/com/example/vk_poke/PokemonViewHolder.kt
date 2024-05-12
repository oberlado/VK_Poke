package com.example.vk_poke

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale

class PokemonViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.pokemon_list_item, parent, false)) {

    private val pokemonName: TextView = itemView.findViewById(R.id.pokemonName)
    private val spriteImageView: ImageView = itemView.findViewById(R.id.pokemonImage)
    private val service = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PokemonService::class.java)

    @SuppressLint("SetTextI18n")
    fun bind(pokemon: Pokemon, onClick: (Pokemon) -> Unit) {
        itemView.setOnClickListener { onClick(pokemon) }
        val idFromUrl = pokemon.url.trimEnd('/').substringAfterLast("/").toInt()
        pokemon.id = idFromUrl
        pokemonName.text = "${pokemon.id}. ${pokemon.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(
            Locale.getDefault()) else it.toString() }}"

        val imageUrl = pokemon.imageUrl
        if (imageUrl == null || !imageUrl.contains("/${pokemon.id}/")) {
            pokemon.imageUrl = null
            CoroutineScope(Dispatchers.IO).launch {
                val pokemonDetail = service.getPokemonDetail(pokemon.url)
                pokemon.imageUrl = pokemonDetail.sprites.front_default
                withContext(Dispatchers.Main) {
                    Glide.with(itemView).load(pokemon.imageUrl).into(spriteImageView)
                }
            }
        } else {
            Glide.with(itemView).load(imageUrl).into(spriteImageView)
        }
    }

}

