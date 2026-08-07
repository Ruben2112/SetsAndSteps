package com.heveamobile.setsandsteps.core.domain.model

data class CardSetUserData(
    val id: String,
    val isOwned: Boolean = false,
    val isActive: Boolean = false,
    val currentSteps: Long = 0,
    val currentLevel: Int = 1,
    val calculatedDistance: Long = 0,
    val currentSetPoints: Long = 0,
    val totalCardCount: Int = 0,
    val totalCardsFound: Int = 0,
    val commonCardCount: Int = 0,
    val commonCardsFound: Int = 0,
    val uncommonCardCount: Int = 0,
    val uncommonCardsFound: Int = 0,
    val rareCardCount: Int = 0,
    val rareCardsFound: Int = 0,
    val epicCardCount: Int = 0,
    val epicCardsFound: Int = 0,
    val legendaryCardCount: Int = 0,
    val legendaryCardsFound: Int = 0,
) {
    fun formatProgress(rarity: Rarity?): String {
        val visited: Int
        val total: Int

        when (rarity) {
            Rarity.Common -> {
                visited = commonCardsFound
                total = commonCardCount
            }

            Rarity.Uncommon -> {
                visited = uncommonCardsFound
                total = uncommonCardCount
            }

            Rarity.Rare -> {
                visited = rareCardsFound
                total = rareCardCount
            }

            Rarity.Epic -> {
                visited = epicCardsFound
                total = epicCardCount
            }

            Rarity.Legendary -> {
                visited = legendaryCardsFound
                total = legendaryCardCount
            }

            null -> {
                visited = totalCardsFound
                total = totalCardCount
            }
        }

        val progressPercentage = if (total == 0) 0 else ((visited * 100) / total)
        return "$visited / $total ($progressPercentage%)"
    }
}

fun CardSetUserData.costPerPack(distanceMultiplier: Double): Long =
    (calculatedDistance * distanceMultiplier).toLong()

fun CardSetUserData.packsAvailable(costPerPack: Long): Int =
    if (costPerPack == 0L) 1 else currentSteps
        .floorDiv(costPerPack)
        .toInt()