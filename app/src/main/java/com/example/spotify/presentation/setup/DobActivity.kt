package com.example.spotify.presentation.setup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.spotify.R
import com.example.spotify.databinding.ActivityDobBinding
import com.example.spotify.databinding.ActivitySignupBinding
import java.util.Calendar

class DobActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDobBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDobBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.monthPicker.minValue = 1
        binding.monthPicker.maxValue = 12
        binding.monthPicker.displayedValues = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

        binding.dayPicker.minValue = 1
        binding.dayPicker.maxValue = 31

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        binding.yearPicker.minValue = 1900
        binding.yearPicker.maxValue = currentYear

        binding.goToNext.setOnClickListener {
            val selectedDay = binding.dayPicker.value
            val selectedMonth = binding.monthPicker.value
            val selectedYear = binding.yearPicker.value
            val fullName = intent.getStringExtra("full_name")
            val emailAddress = intent.getStringExtra("email_address")
            val password = intent.getStringExtra("password")

            val intent = Intent(this, FinalActivity::class.java).apply {
                putExtra("full_name", fullName)
                putExtra("day", selectedDay)
                putExtra("month", selectedMonth)
                putExtra("year", selectedYear)
                putExtra("email_address", emailAddress)
                putExtra("password", password)
            }
            startActivity(intent)
        }
    }
}