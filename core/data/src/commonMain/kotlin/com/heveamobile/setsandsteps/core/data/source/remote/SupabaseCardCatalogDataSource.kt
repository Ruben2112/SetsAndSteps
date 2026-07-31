package com.heveamobile.setsandsteps.core.data.source.remote

import com.heveamobile.setsandsteps.core.data.source.remote.dto.CardDto
import com.heveamobile.setsandsteps.core.data.source.remote.dto.CardSetDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest

interface SupabaseCardCatalogDataSource {
    suspend fun getCardSets(): List<CardSetDto>
    suspend fun getCards(): List<CardDto>
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

    override suspend fun getCards(): List<CardDto> {
        return client.postgrest
            .from("cards")
            .select()
            .decodeList()
    }
}
