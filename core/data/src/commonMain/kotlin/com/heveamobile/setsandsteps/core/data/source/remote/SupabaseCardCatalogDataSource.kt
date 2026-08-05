package com.heveamobile.setsandsteps.core.data.source.remote

import com.heveamobile.setsandsteps.core.data.source.remote.dto.CardRarityCountDto
import com.heveamobile.setsandsteps.core.data.source.remote.dto.CardSetDto
import com.heveamobile.setsandsteps.core.data.source.remote.dto.CollectableCardDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest

interface SupabaseCardCatalogDataSource {
    suspend fun getCardSets(): List<CardSetDto>
    suspend fun getCardRarityCounts(): List<CardRarityCountDto>
    suspend fun getCards(setId: String): List<CollectableCardDto>
}

class SupabaseCardCatalogDataSourceImpl(
    private val client: SupabaseClient,
) : SupabaseCardCatalogDataSource {
    override suspend fun getCardSets(): List<CardSetDto> {
        return client.postgrest
            .from("card_sets")
            .select()
            .decodeList()
    }

    override suspend fun getCardRarityCounts(): List<CardRarityCountDto> {
        return client.postgrest
            .rpc("get_card_rarity_counts")
            .decodeList()
    }

    override suspend fun getCards(setId: String): List<CollectableCardDto> {
        return client.postgrest
            .from("cards")
            .select {
                filter {
                    eq(
                        "set_id",
                        setId,
                    )
                }
            }
            .decodeList()
    }
}
