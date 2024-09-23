package com.example.spotify.presentation.setup

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.spotify.R
import com.example.spotify.databinding.ActivityFinalBinding
import com.example.spotify.presentation.authentication.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate

class FinalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFinalBinding
    private lateinit var dialog: AlertDialog
    private lateinit var mAuth: FirebaseAuth

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

        mAuth = FirebaseAuth.getInstance()

        val fullName = intent.getStringExtra("full_name")
        val day = intent.getIntExtra("day", 0)
        val month = intent.getIntExtra("month", 0)
        val year = intent.getIntExtra("year", 0)
        val emailAddress = intent.getStringExtra("email_address")
        val password = intent.getStringExtra("password")
        val dob = "$day/$month/$year"

        binding.nameEntryField.setText(fullName ?: "")
        val localDate = LocalDate.of(year, month, day)

        binding.createAccountButton.setOnClickListener {
            showProcessingDialog()
            Handler(Looper.getMainLooper()).postDelayed({
                if (emailAddress != null && password != null) {
                    mAuth.createUserWithEmailAndPassword(emailAddress, password).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            dialog.dismiss()
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }, 2000)
        }
    }

    @SuppressLint("InflateParams")
    private fun showProcessingDialog() {
        val dialogView: View = LayoutInflater.from(this).inflate(R.layout.processing_dialog, null)
        dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()
        dialog.show()
    }
}