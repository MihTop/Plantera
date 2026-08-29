package com.example.plantera.utils

object EmojiUtils {
    fun getPlantEmoji(plantName: String): String {
        return when (plantName.lowercase()) {
            "картофель" -> "🥔"
            "перец" -> "🌶️"
            "огурец" -> "🥒"
            "клубника" -> "🍓"
            "капуста" -> "🥬"
            "томат" -> "🍅"
            "лук" -> "🧅"
            "чеснок" -> "🧄"
            "морковь" -> "🥕"
            "яблоко" -> "🍎"
            "груша" -> "🍐"
            "виноград" -> "🍇"
            "арбуз" -> "🍉"
            "дыня" -> "🍈"
            "банан" -> "🍌"
            "апельсин" -> "🍊"
            "лимон" -> "🍋"
            else -> "🌱"
        }
    }
}