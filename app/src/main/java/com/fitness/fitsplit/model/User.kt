package com.fitness.fitsplit.model

data class User(
    val id: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "email" to email,
            "createdAt" to createdAt
        )
    }
    
    fun getFullName(): String {
        return "$firstName $lastName".trim()
    }
}
