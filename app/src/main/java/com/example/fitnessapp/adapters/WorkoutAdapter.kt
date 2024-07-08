package com.example.fitnessapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.R
import com.example.fitnessapp.models.WorkoutModel
import java.time.format.DateTimeFormatter

class WorkoutAdapter(private val workouts: List<WorkoutModel>) :
    RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_workout, parent, false)
        return WorkoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val workout = workouts[position]
        holder.bind(workout)
    }

    override fun getItemCount(): Int {
        return workouts.size
    }

    inner class WorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        private val durationTextView: TextView = itemView.findViewById(R.id.durationTextView)

        fun bind(workout: WorkoutModel) {
            val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            dateTextView.text = workout.date.format(dateFormatter)
            durationTextView.text = workout.duration.toString()+ "min"
        }
    }
}