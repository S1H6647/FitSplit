package com.fitness.fitsplit.model

data class WorkoutLog(
    val id: String? = null,
    val userId: String? = null,
    val splitId: String? = null,
    val dayId: String? = null,
    val date: String? = null,
    val completed: Boolean = false,
    val note: String? = null,
    val timestamp: Long = System.currentTimeMillis()
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "userId" to userId,
            "splitId" to splitId,
            "dayId" to dayId,
            "date" to date,
            "completed" to completed,
            "note" to note,
            "timestamp" to timestamp
        )
    }
}
