package com.example.vk_poke

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface PokemonService {
    @GET("pokemon")
    suspend fun getPokemons(@Query("offset") offset: Int, @Query("limit") limit: Int): PokemonResponse

    @GET
    suspend fun getPokemonDetail(@Url url: String): PokemonDetail

    @GET("evolution-chain/{id}")
    suspend fun getEvolutionChain(@Path("id") id: Int): EvolutionChainResponse

    @GET("pokemon/{id}")
    suspend fun fetchPokemonDetails(@Path("id") id: Int): PokemonDetail

    @GET("pokemon-species/{id}")
    suspend fun getPokemonSpecies(@Path("id") id: Int): PokemonSpecies
}
