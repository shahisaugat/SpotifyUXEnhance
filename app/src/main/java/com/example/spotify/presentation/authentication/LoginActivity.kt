package com.example.spotify.presentation.authentication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.spotify.R
import com.example.spotify.databinding.ActivityLoginBinding
import com.example.spotify.presentation.OnboardActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var dialog: AlertDialog
    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mAuth = FirebaseAuth.getInstance()

        setupGoogleSignIn()

        binding.backButton.setOnClickListener {
            val intent = Intent(this, OnboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        val supportButtonText = "If You Need Any Support Click Here"
        val spanStringFirst = SpannableString(supportButtonText)
        val greenSpan = ForegroundColorSpan(Color.rgb(98, 205, 93))
        spanStringFirst.setSpan(greenSpan, 24, 34, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.supportButton.text = spanStringFirst

        val registerButtonText = "Not A Member? Register Now"
        val spanStringSecond = SpannableString(registerButtonText)
        spanStringSecond.setSpan(greenSpan, 14, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.registerNowButton.text = spanStringSecond

        binding.registerNowButton.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        binding.signInButton.setOnClickListener {
            showProcessingDialog()
            val emailAddress = binding.emailEntryField.text.toString()
            val password = binding.passwordEntryField.text.toString()

            mAuth.signInWithEmailAndPassword(emailAddress, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    dialog.dismiss()
                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                } else {
                    dialog.dismiss()
                    Toast.makeText(this, "Login Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.googleLoginButton.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun setupGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // Ensure you use the right client ID
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(Exception::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: Exception) {
                Log.e("GoogleSignIn", "SignIn failed: ${e.message}")
                Toast.makeText(this, "Google Sign-In Failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Google Sign-In Successful!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Google Sign-In Failed!", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun showProcessingDialog() {
        val dialogView: View = LayoutInflater.from(this).inflate(R.layout.processing_dialog, null)
        val textView = dialogView.findViewById<View>(R.id.textView) as TextView
        textView.text = "Logging you in.."
        dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()
        dialog.show()
    }
}
