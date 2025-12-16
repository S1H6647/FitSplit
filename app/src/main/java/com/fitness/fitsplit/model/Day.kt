package com.fitness.fitsplit.model

data class Day(
    val id: String? = null,
    val splitId: String? = null,
    val dayNumber: Int? = null,
    val dayName: String? = null,
    val dayOfWeek: String? = null,
    val order: Int? = null
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "splitId" to splitId,
            "dayNumber" to dayNumber,
            "dayName" to dayName,
            "dayOfWeek" to dayOfWeek,
            "order" to order
        )
    }
}
