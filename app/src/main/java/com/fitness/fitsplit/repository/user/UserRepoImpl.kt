package com.fitness.fitsplit.repository.user

import com.fitness.fitsplit.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.collections.toMap

class UserRepoImpl : UserRepo {
    val auth : FirebaseAuth = FirebaseAuth.getInstance()
    val database : FirebaseDatabase = FirebaseDatabase.getInstance()
    var userRef : DatabaseReference = database.getReference("users")

    override fun login(
        email: String,
        password: String,
        callback: (Boolean, String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {  response ->
                if (response.isSuccessful) {
                    callback(true, "Login Successful")
                } else {
                    callback(false, "${response.exception?.message}")
                }
            }
    }

    override fun register(
        email: String,
        password: String,
        callback: (Boolean, String, String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { response ->
                if (response.isSuccessful){
                    callback(true, "Registration Successful", "${auth.currentUser?.uid}")
                } else {
                    callback(false, "${response.exception?.message}", "")
            }
        }
    }

    fun updateDisplayName(name: String) {
        val user = auth.currentUser ?: return
        val profileUpdates = com.google.firebase.auth.UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .build()
        user.updateProfile(profileUpdates)
    }

    override fun addUserToDatabase(
        userId: String,
        model: User,
        callback: (Boolean, String) -> Unit
    ) {
        userRef.child(userId).setValue(model)
            .addOnCompleteListener { response ->0
                if (response.isSuccessful){
                    callback(true, "User Added Successfully")
                } else {
                    callback(false, "${response.exception?.message}")
                }
            }
    }

    override fun updateProfile(
        userId: String,
        model: User,
        callback: (Boolean, String) -> Unit
    ) {
        userRef.child(userId).updateChildren(model.toMap())
            .addOnCompleteListener { response ->
                if (response.isSuccessful){
                    callback(true, "User Added Successfully")
                } else {
                    callback(false, "${response.exception?.message}")
                }

            }
    }

    override fun logout(callback: (Boolean, String) -> Unit) {
        try {
            auth.signOut()
            callback(true, "Logout Successfully")
        } catch (e : Exception) {
            callback(false, "${e.message}")
        }
    }

    override fun forgetPassword(
        email: String,
        callback: (Boolean, String) -> Unit
    ) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener {  response ->
                if (response.isSuccessful) {
                    callback(true, "Password reset successfully")
                } else {
                    callback(false, "${response.exception?.message}")
                }
            }
    }

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
}