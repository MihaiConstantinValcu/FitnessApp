package com.example.fitnessapp.fragments.add_workout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.fitnessapp.ApplicationController
import com.example.fitnessapp.R
import com.example.fitnessapp.data.AppDatabase
import com.example.fitnessapp.data.dao.UserDao
import com.example.fitnessapp.models.WorkoutModel
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate

class AddWorkoutFragment : Fragment() {

    private lateinit var dateEditText: EditText
    private lateinit var durationEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var database: AppDatabase
    private lateinit var errorMessageTextView: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var userDao: UserDao
    private lateinit var noSportTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_workout, container, false)

        database = (requireActivity().application as ApplicationController).database
        userDao = database.userDao()
        auth = FirebaseAuth.getInstance()
        dateEditText = view.findViewById(R.id.dateEditText)
        durationEditText = view.findViewById(R.id.durationEditText)
        saveButton = view.findViewById(R.id.saveButton)
        errorMessageTextView = view.findViewById(R.id.errorMessage)
        noSportTextView = view.findViewById(R.id.noSportTextView)

        val existingEmail = auth.currentUser?.email
        val user = userDao.getUserByEmail(existingEmail!!)
        if(user!!.sport.isEmpty()){
            profileNotCompleted("Please add your sport in your profile")
            return view
        }

        dateEditText.visibility = View.VISIBLE
        durationEditText.visibility = View.VISIBLE
        saveButton.visibility = View.VISIBLE

        saveButton.setOnClickListener {
            saveWorkout()
        }

        return view
    }

    private fun saveWorkout() {
        val dateStr = dateEditText.text.toString()
        val durationStr = durationEditText.text.toString()

        if (dateStr.isEmpty() || durationStr.isEmpty()) {
            showMessage("Please complete all the fields")
            return
        }

        try {
            val date = LocalDate.parse(dateStr)

            if (date.isAfter(LocalDate.now())) {
                showMessage("Date cannot be in the future")
                return
            }

            val duration = durationStr.toInt()
            if (duration < 0 || duration > 1440) {
                showMessage("Did you really trained that much?")
                return
            }

            val email = auth.currentUser?.email
            val workout = WorkoutModel(date = date, duration = duration, userEmail = email!!)

            database.workoutDao().insert(workout)
            showMessage("Workout saved successfully!")
            dateEditText.setText("")
            durationEditText.setText("")

        } catch (e: Exception) {
            showMessage("Invalid date format")
        }
    }

    private fun showMessage(message: String) {
        errorMessageTextView.apply {
            text = message
            visibility = View.VISIBLE
        }
    }

    private fun profileNotCompleted(message: String){
        noSportTextView.text = message
        noSportTextView.visibility = View.VISIBLE
    }

}