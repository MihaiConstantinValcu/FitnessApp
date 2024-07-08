package com.example.fitnessapp.fragments.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.fitnessapp.ApplicationController
import com.example.fitnessapp.R
import com.example.fitnessapp.data.AppDatabase
import com.example.fitnessapp.data.dao.UserDao
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONArray

class StatisticsFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var usersTextView: TextView
    private lateinit var popularSportTextView: TextView
    private lateinit var usersNumberTextView: TextView
    private lateinit var popularSportNumberTextView: TextView
    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var errorMessageTextView: TextView
    private lateinit var userCity: String
    private lateinit var userSport: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_statistics, container, false)

        usersTextView = view.findViewById(R.id.usersTextView)
        usersNumberTextView = view.findViewById(R.id.usersNumberTextView)
        popularSportTextView = view.findViewById(R.id.popularSportTextView)
        popularSportNumberTextView = view.findViewById(R.id.popularSportNumberTextView)
        errorMessageTextView = view.findViewById(R.id.errorMessage)
        database = (requireActivity().application as ApplicationController).database
        userDao = database.userDao()
        auth = FirebaseAuth.getInstance()

        val existingEmail = auth.currentUser?.email
        val user = existingEmail?.let { userDao.getUser(it) }
        if (user == null || user.sport.isEmpty() || user.city.isEmpty()){
            errorMessageTextView.text = "Please complete your profile first!"
            errorMessageTextView.visibility = View.VISIBLE
            return view
        }

        userSport = user.sport
        userCity = user.city

        getUsers()

        return view
    }

    private fun getUsers() {
        val url = "https://my-json-server.typicode.com/MihaiConstantinValcu/FakeApi/users"

        val stringRequest = object : StringRequest(
            Method.GET,
            url,
            { response ->
                processResponse(response)
            },
            {
               showError("There was an error handling the request")
            }
        ) {}
        Volley.newRequestQueue(requireContext()).add(stringRequest)
    }

    private fun processResponse(response: String) {
        try {

            val jsonArray = JSONArray(response)

            var boxUsersInBucharest = 0
            val sportCounts = mutableMapOf<String, Int>()

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val sport = jsonObject.getString("sport")
                val city = jsonObject.getString("city")

                if (sport == userSport && city == userCity) {
                    boxUsersInBucharest++
                }

                if (city == userCity) {
                    sportCounts[sport] = sportCounts.getOrDefault(sport, 0) + 1
                }
            }

            val mostPopularSport = sportCounts.maxByOrNull { it.value }?.key ?: ""

            usersTextView.text = "Users playing $userSport in $userCity:"
            usersNumberTextView.text = "$boxUsersInBucharest"
            popularSportTextView.text = "Most popular sport in $userCity:"
            popularSportNumberTextView.text = "$mostPopularSport"

            return

        } catch (e: Exception) {
            showError(e.toString())
        }
    }

    private fun showError(message: String){
        errorMessageTextView.text = message
        errorMessageTextView.visibility = View.VISIBLE

    }
}