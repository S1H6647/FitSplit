package com.fitness.fitsplit.repository.user

import com.fitness.fitsplit.model.User
import com.google.firebase.auth.FirebaseUser

interface UserRepo {
    fun login(
        email:String,
        password:String,
        callback : (Boolean, String) -> Unit
    )

    fun register(
        email:String,
        password:String,
        callback : (Boolean, String, String) -> Unit
    )

    fun addUserToDatabase(
        userId: String,
        model: User,
        callback : (Boolean, String) -> Unit
    )

    fun updateProfile(
        userId: String,
        model: User,
        callback : (Boolean, String) -> Unit
    )

    fun logout(
        callback : (Boolean, String) -> Unit
    )

    fun forgetPassword(
        email: String,
        callback: (Boolean, String) -> Unit
    )

    fun getCurrentUser() : FirebaseUser?
}