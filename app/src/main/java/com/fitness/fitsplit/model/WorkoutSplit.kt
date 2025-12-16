package com.fitness.fitsplit.model

data class WorkoutSplit(
    val id: String? = null,
    val userId: String? = null,
    val name: String? = null,
    val numberOfDays: Int? = null,
    val selectedDays: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
) {
    // Convert to Map for Firebase
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "userId" to userId,
            "name" to name,
            "numberOfDays" to numberOfDays,
            "selectedDays" to selectedDays,
            "createdAt" to createdAt
        )
    }
}
