package com.example.vk_poke

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class PokemonDataHandler(
    private val activity: DetailActivity,
    private val coroutineScope: CoroutineScope,
    private val service: PokemonService
) {

    private val pokemonTypeColor = PokemonTypeColor()
    private var pokemonColor: Int = android.graphics.Color.GRAY

    private fun getTypeColor(type: String): Int {
        return pokemonTypeColor.getTypeColor(type)
    }

    private fun setViewStyle(view: View, color: Int) {
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.RECTANGLE
        drawable.cornerRadius = 10f * activity.resources.displayMetrics.density
        drawable.setColor(color)

        view.background = drawable
    }

    fun handlePokemonDetail(pokemonDetail: PokemonDetail) {
        val nameTextView: TextView = activity.findViewById(R.id.name)
        val type1TextView: TextView = activity.findViewById(R.id.type1)
        val type2TextView: TextView = activity.findViewById(R.id.type2)
        val typeTextView: TextView = activity.findViewById(R.id.type)
        val spriteImageView: ImageView = activity.findViewById(R.id.sprite)
        val pokemonMainInfo: ConstraintLayout = activity.findViewById(R.id.pokemonMainInfo)
        val scrollView: ScrollView = activity.findViewById(R.id.scrollView)

        nameTextView.text =
            pokemonDetail.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

        pokemonColor = getTypeColor(pokemonDetail.types[0].type.name)

        when (pokemonDetail.types.size) {
            2 -> {
                styleBadge(type1TextView, pokemonDetail.types[0].type.name)
                styleBadge(type2TextView, pokemonDetail.types[1].type.name)
                typeTextView.visibility = View.INVISIBLE
            }

            1 -> {
                styleBadge(typeTextView, pokemonDetail.types[0].type.name)
                type1TextView.visibility = View.INVISIBLE
                type2TextView.visibility = View.INVISIBLE
            }

            else -> {
                type1TextView.visibility = View.INVISIBLE
                type2TextView.visibility = View.INVISIBLE
                typeTextView.visibility = View.INVISIBLE
            }
        }

        pokemonMainInfo.background = getGradientDrawable(pokemonDetail.types)

        val constraintLayoutSpeed: ConstraintLayout =
            activity.findViewById(R.id.constraintLayoutSpeed)
        setViewStyle(constraintLayoutSpeed, pokemonColor)

        val constraintLayoutSpDefence: ConstraintLayout =
            activity.findViewById(R.id.constraintLayoutSpDeffence)
        setViewStyle(constraintLayoutSpDefence, pokemonColor)

        val constraintLayoutSpAttack: ConstraintLayout =
            activity.findViewById(R.id.constraintLayoutSpAttack)
        setViewStyle(constraintLayoutSpAttack, pokemonColor)

        val constraintLayoutDefence: ConstraintLayout =
            activity.findViewById(R.id.constraintLayoutDefence)
        setViewStyle(constraintLayoutDefence, pokemonColor)

        val constraintLayoutAttack: ConstraintLayout =
            activity.findViewById(R.id.constraintLayoutAttack)
        setViewStyle(constraintLayoutAttack, pokemonColor)

        val constraintLayoutHP: ConstraintLayout = activity.findViewById(R.id.constraintLayoutHP)
        setViewStyle(constraintLayoutHP, pokemonColor)

        handlePokemonStats(pokemonDetail.stats)
    }

    fun handlePokemonStats(stats: List<Stat>) {
        val textViewHPStat: TextView = activity.findViewById(R.id.textViewHPStat)
        val textViewAttackStat: TextView = activity.findViewById(R.id.textViewAttackStat)
        val textViewDefenceStat: TextView = activity.findViewById(R.id.textViewDefenceStat)
        val textViewSpAttackStat: TextView = activity.findViewById(R.id.textViewSpAttackStat)
        val textViewSpDefence: TextView = activity.findViewById(R.id.textViewSpDefenceStat)
        val textViewSpeedStat: TextView = activity.findViewById(R.id.textViewSpeedStat)

        for (stat in stats) {
            when (stat.stat.name) {
                "hp" -> textViewHPStat.text = stat.base_stat.toString()
                "attack" -> textViewAttackStat.text = stat.base_stat.toString()
                "defense" -> textViewDefenceStat.text = stat.base_stat.toString()
                "special-attack" -> textViewSpAttackStat.text = stat.base_stat.toString()
                "special-defense" -> textViewSpDefence.text = stat.base_stat.toString()
                "speed" -> textViewSpeedStat.text = stat.base_stat.toString()
            }
        }
    }

    fun handlePokemonImage(url: String, onColorReady: (Int) -> Unit) {
        val spriteImageView: ImageView = activity.findViewById(R.id.sprite)
        val scrollView: ScrollView = activity.findViewById(R.id.scrollView)

        Glide.with(activity).load(url).into(spriteImageView)

        Glide.with(activity).asBitmap().load(url).into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(
                resource: Bitmap, transition: Transition<in Bitmap>?
            ) {
                Palette.from(resource).generate { palette ->
                    val dominantColor = palette?.getDominantColor(android.graphics.Color.GRAY)
                    dominantColor?.let { color ->
                        val hsv = FloatArray(3)
                        Color.colorToHSV(color, hsv)
                        hsv[1] = hsv[1] * 0.7f
                        val softerColor = Color.HSVToColor(hsv)

                        scrollView.setBackgroundColor(softerColor)
                        pokemonColor = softerColor
                        onColorReady(softerColor)
                    }
                }
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }
        })
    }

    fun handlePokemonAbilities(abilities: List<Ability>, color: Int) {
        val ability1TextView: TextView = activity.findViewById(R.id.TextAbility1)
        val ability2TextView: TextView = activity.findViewById(R.id.TextAbility2)

        when (abilities.size) {
            0 -> {
                ability1TextView.text = "У этого покемона нет способностей"
                ability2TextView.visibility = View.GONE
            }

            1 -> {
                ability1TextView.text = abilities[0].ability.name
                ability2TextView.visibility = View.GONE
            }

            else -> {
                ability1TextView.text = abilities[0].ability.name
                ability2TextView.text = abilities[1].ability.name
            }
        }

        setViewStyle(ability1TextView, color)
        setViewStyle(ability2TextView, color)
    }

    private fun getGradientDrawable(types: List<Type>): GradientDrawable {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.shape = GradientDrawable.RECTANGLE
        gradientDrawable.orientation = GradientDrawable.Orientation.LEFT_RIGHT
        gradientDrawable.cornerRadius = 40f
        val colors = when {
            types.size > 1 -> types.map { getTypeColor(it.type.name) }
            types.isNotEmpty() -> listOf(
                getTypeColor(types[0].type.name), getTypeColor(types[0].type.name)
            )

            else -> listOf(android.graphics.Color.GRAY, android.graphics.Color.DKGRAY)
        }

        gradientDrawable.colors = colors.toIntArray()

        return gradientDrawable
    }

    fun styleBadge(textView: TextView, type: String) {
        textView.text = type
        textView.visibility = View.VISIBLE

        val drawable = GradientDrawable()
        drawable.setColor(getTypeColor(type))
        drawable.cornerRadius = 16f
        drawable.setStroke(4, android.graphics.Color.BLACK)
        textView.background = drawable

        textView.textSize = 18f
        textView.gravity = Gravity.CENTER
        textView.setPadding(16, 16, 16, 16)
    }

    fun handlePokemonEvolutionImages(chain: Chain) {
        val linearLayout: LinearLayout = activity.findViewById(R.id.linearPokeEvolution)
        linearLayout.removeAllViews()
        val evolutionImages = mutableListOf<Pair<String, Int>>()
        handleEvolutionChainRecursively(evolutionImages, chain)
        evolutionImages.sortBy { it.second }
        coroutineScope.launch {
            val imageViews = evolutionImages.map { (url, _) ->
                async { createPokemonImageView(url) }
            }.awaitAll()
            withContext(Dispatchers.Main) {
                for (imageView in imageViews) {
                    linearLayout.addView(imageView)
                }
            }
        }
    }

    private fun handleEvolutionChainRecursively(
        evolutionImages: MutableList<Pair<String, Int>>, chain: Chain
    ) {
        evolutionImages.add(Pair(chain.species.url, getPokemonIdFromUrl(chain.species.url)))
        for (evolvesTo in chain.evolves_to) {
            handleEvolvesToRecursively(evolutionImages, evolvesTo)
        }
    }

    private fun handleEvolvesToRecursively(
        evolutionImages: MutableList<Pair<String, Int>>, evolvesTo: EvolvesTo
    ) {
        evolutionImages.add(Pair(evolvesTo.species.url, getPokemonIdFromUrl(evolvesTo.species.url)))
        for (nextEvolvesTo in evolvesTo.evolves_to) {
            handleEvolvesToRecursively(evolutionImages, nextEvolvesTo)
        }
    }

    private suspend fun createPokemonImageView(url: String): ImageView {
        val pokemon = service.fetchPokemonDetails(getPokemonIdFromUrl(url))
        val imageView = ImageView(activity)
        imageView.layoutParams =
            LinearLayout.LayoutParams(400, 400)
        withContext(Dispatchers.Main) {
            Glide.with(activity).load(pokemon.sprites.front_default).into(imageView)
            imageView.setOnClickListener {
                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra("url", url)
                activity.startActivity(intent)
            }
        }
        return imageView
    }


    private fun getPokemonIdFromUrl(url: String): Int {
        val parts = url.split("/")
        return parts[parts.size - 2].toInt()
    }
}



