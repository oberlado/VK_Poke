package com.example.vk_poke

class PokemonTypeColor {

    fun getTypeColor(type: String): Int {
        return when (type) {
            "normal" -> android.graphics.Color.parseColor("#A8A77A")
            "fire" -> android.graphics.Color.parseColor("#EE8130")
            "water" -> android.graphics.Color.parseColor("#6390F0")
            "fighting" -> android.graphics.Color.parseColor("#C22E28")
            "flying" -> android.graphics.Color.parseColor("#A98FF3")
            "poison" -> android.graphics.Color.parseColor("#A33EA1")
            "ground" -> android.graphics.Color.parseColor("#E2BF65")
            "rock" -> android.graphics.Color.parseColor("#B6A136")
            "bug" -> android.graphics.Color.parseColor("#A6B91A")
            "ghost" -> android.graphics.Color.parseColor("#735797")
            "steel" -> android.graphics.Color.parseColor("#B7B7CE")
            "grass" -> android.graphics.Color.parseColor("#7AC74C")
            "electric" -> android.graphics.Color.parseColor("#F7D02C")
            "psychic" -> android.graphics.Color.parseColor("#F95587")
            "ice" -> android.graphics.Color.parseColor("#96D9D6")
            "dragon" -> android.graphics.Color.parseColor("#6F35FC")
            "dark" -> android.graphics.Color.parseColor("#705746")
            "fairy" -> android.graphics.Color.parseColor("#D685AD")
            "stellar" -> android.graphics.Color.parseColor("#A8A77A")
            "unknown" -> android.graphics.Color.parseColor("#A8A77A")
            else -> android.graphics.Color.GRAY
        }
    }
}