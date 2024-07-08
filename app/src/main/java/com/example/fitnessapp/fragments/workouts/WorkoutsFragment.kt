package com.example.fitnessapp.fragments.workouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.ApplicationController
import com.example.fitnessapp.R
import com.example.fitnessapp.adapters.WorkoutAdapter
import com.example.fitnessapp.data.AppDatabase
import com.example.fitnessapp.data.dao.WorkoutDao
import com.example.fitnessapp.models.WorkoutModel
import com.google.firebase.auth.FirebaseAuth

class WorkoutsFragment : Fragment() {
    private lateinit var workoutsRecyclerView: RecyclerView
    private lateinit var adapter: WorkoutAdapter
    private lateinit var database: AppDatabase
    private lateinit var workoutDao: WorkoutDao
    private lateinit var workoutsList : List<WorkoutModel>
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_workouts, container, false)
        auth = FirebaseAuth.getInstance()
        workoutsRecyclerView = view.findViewById(R.id.workoutsRecyclerView)

        database = (requireActivity().application as ApplicationController).database
        workoutDao = database.workoutDao()

        workoutsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val existingEmail = auth.currentUser?.email
        workoutsList = workoutDao.getAllWorkoutsByUserEmail(existingEmail!!)

        adapter = WorkoutAdapter(workoutsList)
        workoutsRecyclerView.adapter = adapter

        return view
    }
}