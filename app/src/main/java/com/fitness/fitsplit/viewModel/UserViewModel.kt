package com.fitness.fitsplit.viewModel

import androidx.lifecycle.ViewModel
import com.fitness.fitsplit.model.User
import com.fitness.fitsplit.repository.user.UserRepo
import com.google.firebase.auth.FirebaseUser

class UserViewModel (val repo : UserRepo) : ViewModel() {
    fun login(
        email:String,
        password:String,
        callback : (Boolean, String) -> Unit
    ){
        repo.login(email, password, callback)
    }

    fun register(
        email:String,
        password:String,
        callback : (Boolean, String, String) -> Unit
    ){
        repo.register(email, password, callback)
    }

    fun addUserToDatabase(
        userId: String,
        model: User,
        callback : (Boolean, String) -> Unit
    ){
        repo.addUserToDatabase(userId, model, callback)
    }

    fun updateProfile(
        userId: String,
        model: User,
        callback : (Boolean, String) -> Unit
    ){
        repo.updateProfile(userId, model, callback)
    }

    fun getCurrentUser() : FirebaseUser? {
        return repo.getCurrentUser()
    }

    fun logout(
        callback : (Boolean, String) -> Unit
    ){
        repo.logout(callback)
    }
    fun forgetPassword(
        email: String,
        callback: (Boolean, String) -> Unit
    ){
        repo.forgetPassword(email, callback)
    }
}