package com.heveamobile.setsandsteps.core.data.di

import com.heveamobile.setsandsteps.core.data.config.SupabaseConfig
import com.heveamobile.setsandsteps.core.data.source.remote.SupabaseCardCatalogDataSource
import com.heveamobile.setsandsteps.core.data.source.remote.SupabaseCardCatalogDataSourceImpl
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val supabaseModule = module {
    single {
        createSupabaseClient(
            supabaseUrl = SupabaseConfig.URL,
            supabaseKey = SupabaseConfig.ANON_KEY,
        ) {
            install(Postgrest) {
                defaultSchema = "api"
            }
        }
    }

    singleOf(::SupabaseCardCatalogDataSourceImpl) { bind<SupabaseCardCatalogDataSource>() }
}
