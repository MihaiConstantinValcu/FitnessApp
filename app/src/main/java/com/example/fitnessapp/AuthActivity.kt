package com.example.fitnessapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnessapp.fragments.login.LoginFragment
import com.google.firebase.auth.FirebaseAuth

class AuthActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        setContentView(R.layout.activity_auth)
        title = "Authentication"

        supportFragmentManager.beginTransaction()
            .replace(R.id.activityAuth, LoginFragment())
            .commit()
    }

    fun goToLogin() {
        val fragment = LoginFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.activityAuth, fragment)
            .addToBackStack(null)
            .commit()
    }
}

