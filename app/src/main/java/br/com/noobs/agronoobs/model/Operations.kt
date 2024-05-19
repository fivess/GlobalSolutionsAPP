package br.com.noobs.agronoobs.model

enum class Operations(val value: String) {
    HARVEST("Colheita"), IRRIGATION("Irrigação"), PLANTING("Plantio");


    companion object {
        fun fromValue(value: String): Operations {
            values().forEach {
                if (it.value == value) {
                    return it
                }
            }
            throw IllegalArgumentException("Invalid value $value")
        }
    }

}