package com.example.spotify.presentation.setup

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.spotify.R
import com.example.spotify.databinding.ActivityFinalBinding

class FinalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFinalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFinalBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val fullName = intent.getStringExtra("full_name")
        val day = intent.getIntExtra("day", 0)
        val month = intent.getIntExtra("month", 0)
        val year = intent.getIntExtra("year", 0)

        binding.nameEntryField.setText(fullName ?: "")
    }
}