package com.example.vk_poke

data class Pokemon(
    var id: Int,
    val name: String,
    val url: String,
    var imageUrl: String? = null,
    val sprites: Sprites? = null,
    val order: Int
)

data class PokemonResponse(
    val results: List<Pokemon>
)

data class PokemonDetail(
    val name: String,
    val abilities: List<Ability>,
    val id: Int,
    val moves: List<Move>,
    val types: List<Type>,
    val weight: Int,
    val stats: List<Stat>,
    val sprites: Sprites
)

data class Ability(
    val ability: AbilityDetail,
    val is_hidden: Boolean,
    val slot: Int
)

data class AbilityDetail(
    val name: String,
    val url: String
)

data class Move(
    val move: MoveDetail
)

data class MoveDetail(
    val name: String,
    val url: String
)

data class Type(
    val slot: Int,
    val type: TypeDetail
)

data class TypeDetail(
    val name: String,
    val url: String
)

data class Stat(
    val base_stat: Int,
    val effort: Int,
    val stat: StatDetail
)

data class StatDetail(
    val name: String,
    val url: String
)

data class Sprites(
    val back_default: String,
    val back_female: String?,
    val back_shiny: String,
    val back_shiny_female: String?,
    val front_default: String,
    val front_female: String?,
    val front_shiny: String,
    val front_shiny_female: String?
)

data class EvolutionChainResponse(
    val chain: Chain
)

data class Chain(
    val species: Species,
    val evolves_to: List<EvolvesTo>
)

data class EvolvesTo(
    val species: Species,
    val evolves_to: List<EvolvesTo>
)

data class Species(
    val name: String,
    val url: String
)

data class PokemonSpecies(
    val evolution_chain: EvolutionChainLink
)

data class EvolutionChainLink(
    val url: String
)

