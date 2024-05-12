package com.example.vk_poke

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class PokemonAdapter(private val onClick: (Pokemon) -> Unit) :
    RecyclerView.Adapter<PokemonViewHolder>() {
    private var pokemons = mutableListOf<Pokemon>()

    fun addPokemons(pokemons: List<Pokemon>) {
        this.pokemons.addAll(pokemons)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PokemonViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon: Pokemon = pokemons[position]
        holder.bind(pokemon, onClick)
    }

    override fun getItemCount(): Int = pokemons.size

    fun setPokemons(pokemons: List<Pokemon>) {
        this.pokemons = pokemons.toMutableList()
        notifyDataSetChanged()
    }
}



