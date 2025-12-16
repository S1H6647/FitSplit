package com.fitness.fitsplit.model

data class Exercise(
    val id: String? = null,
    val dayId: String? = null,
    val name: String? = null,
    val sets: Int? = null,
    val reps: String? = null,
    val order: Int? = null,
    val notes: String? = null
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "dayId" to dayId,
            "name" to name,
            "sets" to sets,
            "reps" to reps,
            "order" to order,
            "notes" to notes
        )
    }
}
