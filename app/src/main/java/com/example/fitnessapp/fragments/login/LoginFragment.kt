package com.example.fitnessapp.fragments.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.fitnessapp.MainActivity
import com.example.fitnessapp.R
import com.example.fitnessapp.fragments.register.RegisterFragment
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var editTextEmailAddress: EditText
    private lateinit var editTextPassword: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        auth = FirebaseAuth.getInstance()

        editTextEmailAddress = view.findViewById(R.id.editTextEmailAddress)
        editTextPassword = view.findViewById(R.id.editTextPassword)

        val buttonRegister = view.findViewById<Button>(R.id.buttonLogin)
        buttonRegister.setOnClickListener {
            login()
        }

        val textViewRegister = view.findViewById<TextView>(R.id.textViewRegister)
        textViewRegister.setOnClickListener {
            goToRegister()
        }

        return view
    }

    private fun login() {
        val email = editTextEmailAddress.text.toString()
        val password = editTextPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            val errorMessage = "Please enter email and password."
            showMessage(errorMessage)
            return
        }

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }.addOnFailureListener { exception ->
            val errorMessage = exception.localizedMessage ?: "Authentication failed!"
            showMessage(errorMessage)
        }
    }

    private fun goToRegister() {
        val fragment = RegisterFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.activityAuth, fragment)
        transaction.commit()
    }

    private fun showMessage(message: String) {
        val errorMessageTextView = view?.findViewById<TextView>(R.id.errorMessage)
        errorMessageTextView?.apply {
            text = message
            visibility = View.VISIBLE
        }
    }
}
