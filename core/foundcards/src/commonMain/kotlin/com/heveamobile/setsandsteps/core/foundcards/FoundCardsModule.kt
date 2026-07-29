package com.heveamobile.setsandsteps.core.foundcards

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val foundCardsModule = module {
    viewModelOf(::FoundCardsViewModel)
}
