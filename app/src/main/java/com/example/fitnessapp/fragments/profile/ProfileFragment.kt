package com.example.fitnessapp.fragments.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.fitnessapp.ApplicationController
import com.example.fitnessapp.AuthActivity
import com.example.fitnessapp.R
import com.example.fitnessapp.data.AppDatabase
import com.example.fitnessapp.data.dao.UserDao
import com.example.fitnessapp.models.UserModel
import com.firebase.ui.auth.data.model.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao

    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var sportEditText: EditText
    private lateinit var cityEditText: EditText
    private lateinit var saveProfileButton: Button
    private lateinit var signOutButton: Button
    private lateinit var successMessageTextView: TextView
    private lateinit var errorMessageTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        auth = FirebaseAuth.getInstance()
        database = (requireActivity().application as ApplicationController).database
        userDao = database.userDao()

        usernameEditText = view.findViewById(R.id.username)
        emailEditText = view.findViewById(R.id.email)
        sportEditText = view.findViewById(R.id.sport)
        cityEditText = view.findViewById(R.id.city)
        saveProfileButton = view.findViewById(R.id.saveProfile)
        signOutButton = view.findViewById(R.id.signOut)
        successMessageTextView = view.findViewById(R.id.successMessage)
        errorMessageTextView = view.findViewById(R.id.errorMessage)

        emailEditText.setText(auth.currentUser?.email)

        saveProfileButton.setOnClickListener {
            saveUserProfile()
        }

        signOutButton.setOnClickListener {
            signOut()
        }

        loadUserProfile()

        return view
    }

    private fun signOut() {
        auth.signOut()
        val intent = Intent(activity, AuthActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    private fun saveUserProfile() {
        val username = usernameEditText.text.toString()
        val email = emailEditText.text.toString()
        val sport = sportEditText.text.toString()
        val city = cityEditText.text.toString()

        if (username.isEmpty() || sport.isEmpty() || city.isEmpty()) {
            showMessage("Please do not leave empty fields")
            return
        }

        val userProfile = UserModel(username = username, email = email, city = city, sport = sport)

        val existingEmail = auth.currentUser?.email
        val existingUser = existingEmail?.let { userDao.getUser(it) }
        if (existingUser != null) {
            userProfile.id = existingUser.id
            userDao.updateUser(userProfile)
        } else {
            userDao.insertUser(userProfile)
        }
        successMessageTextView.visibility = View.VISIBLE
        errorMessageTextView.visibility = View.INVISIBLE
    }

    private fun loadUserProfile() {
        val existingEmail = auth.currentUser?.email
        val userProfile = existingEmail?.let { userDao.getUser(it) }

        if(userProfile == null){
            val userProfile = UserModel(username = "", email = "", city = "", sport = "")
            userDao.insertUser(userProfile)
        }

        userProfile?.let {
            usernameEditText.setText(it.username)
            sportEditText.setText(it.sport)
            cityEditText.setText(it.city)
        }
    }

    private fun showMessage(message: String) {
        successMessageTextView.apply {
            visibility = View.INVISIBLE
        }

        errorMessageTextView.apply {
            text = message
            visibility = View.VISIBLE
        }
    }
}