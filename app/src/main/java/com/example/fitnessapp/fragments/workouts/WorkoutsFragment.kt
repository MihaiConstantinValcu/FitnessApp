package com.example.fitnessapp.fragments.workouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.fitnessapp.ApplicationController
import com.example.fitnessapp.R
import com.example.fitnessapp.adapters.WorkoutAdapter
import com.example.fitnessapp.data.AppDatabase
import com.example.fitnessapp.data.dao.UserDao
import com.example.fitnessapp.data.dao.WorkoutDao
import com.example.fitnessapp.models.WorkoutModel
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONArray
import java.time.LocalDate

class WorkoutsFragment : Fragment() {
    private lateinit var workoutsRecyclerView: RecyclerView
    private lateinit var adapter: WorkoutAdapter
    private lateinit var database: AppDatabase
    private lateinit var workoutDao: WorkoutDao
    private lateinit var workoutsList : MutableList<WorkoutModel>
    private lateinit var auth: FirebaseAuth
    private lateinit var syncButton: Button
    private lateinit var messageTextView: TextView
    private lateinit var userDao: UserDao
    private lateinit var syncMessageTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_workouts, container, false)
        auth = FirebaseAuth.getInstance()
        workoutsRecyclerView = view.findViewById(R.id.workoutsRecyclerView)
        syncButton = view.findViewById(R.id.syncButton)
        messageTextView = view.findViewById(R.id.messageTextView)
        syncMessageTextView = view.findViewById(R.id.syncMessageTextView)

        database = (requireActivity().application as ApplicationController).database
        workoutDao = database.workoutDao()
        userDao = database.userDao()

        workoutsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val existingEmail = auth.currentUser?.email
        workoutsList = workoutDao.getAllWorkoutsByUserEmail(existingEmail!!).toMutableList()

        val user = userDao.getUserByEmail(existingEmail)
        if(user!!.sport.isEmpty()){
            showMessage("Please add your sport in your profile")
            return view
        }

        syncButton.setOnClickListener {
            syncWorkouts()
        }

        if(workoutsList.isEmpty()){
            showMessage("No workouts yet!")
            return view
        }

        adapter = WorkoutAdapter(workoutsList)
        workoutsRecyclerView.adapter = adapter


        return view
    }

    private fun syncWorkouts() {
        val url = "https://my-json-server.typicode.com/MihaiConstantinValcu/FakeApi/workouts"

        val stringRequest = object : StringRequest(
            Method.GET,
            url,
            { response ->
                processResponse(response)
            },
            {
                showSyncMessage("There was an error handling the request")
            }
        ) {}
        Volley.newRequestQueue(requireContext()).add(stringRequest)
    }

    private fun processResponse(response: String?) {
        try{
            val jsonArray = JSONArray(response)
            val workouts = mutableListOf<WorkoutModel>()

            for(i in 0 until jsonArray.length()){
                val jsonObject = jsonArray.getJSONObject(i)
                val date = jsonObject.getString("date")
                val duration = jsonObject.getInt("duration")
                val email = jsonObject.getString("userEmail")

                val workoutExists = workoutsList.any { it.date == LocalDate.parse(date) && it.duration == duration }
                if(!workoutExists){
                    val workout = WorkoutModel(date = LocalDate.parse(date), duration = duration, userEmail = email)
                    workoutDao.insert(workout)
                    workouts.add(workout)
                }

            }

            workoutsList.addAll(workouts)

            if(workoutsList.isNotEmpty()){
                adapter = WorkoutAdapter(workoutsList)
                workoutsRecyclerView.adapter = adapter
                messageTextView.visibility = View.INVISIBLE
            }


        } catch (e: Exception) {
            showSyncMessage(e.toString())

        }
    }

    private fun showMessage(message: String){
        messageTextView.text = message
        messageTextView.visibility = View.VISIBLE
    }

    private fun showSyncMessage(message: String){
        syncMessageTextView.text = message
        syncMessageTextView.visibility = View.VISIBLE
    }
}