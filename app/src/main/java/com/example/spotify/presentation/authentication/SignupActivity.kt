package com.example.spotify.presentation.authentication

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.spotify.R
import com.example.spotify.databinding.ActivitySignupBinding
import com.example.spotify.presentation.setup.DobActivity
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignupBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mAuth = FirebaseAuth.getInstance()

        binding.createAccountButton.setOnClickListener {
            showProcessingDialog()
            val fullName = binding.fullNameEntryField.text.toString()
            val emailAddress = binding.emailEntryField.text.toString()
            val password = binding.passwordEntryField.text.toString()

            Handler(Looper.getMainLooper()).postDelayed({
                mAuth.createUserWithEmailAndPassword(emailAddress, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            dialog.dismiss()
                            val intent = Intent(this, DobActivity::class.java).apply {
                                if (fullName.isNotEmpty()) {
                                    putExtra("full_name", fullName)
                                }
                            }
                            startActivity(intent)
                            finish()
                        }
                    }
            }, 3000)
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