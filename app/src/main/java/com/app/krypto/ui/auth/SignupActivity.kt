package com.app.krypto.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.krypto.data.model.Profile
import com.app.krypto.databinding.ActivitySignupBinding
import com.app.krypto.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SignupActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: ActivitySignupBinding
    private val profileCollectionRef = Firebase.firestore.collection("profile")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()

        binding.btnSignUp.setOnClickListener {
            val email = binding.signupEmailAddress.text.toString()
            val profile = Profile(email)
            saveProfile(profile)
            registerUser()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.bottomContainer.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }


    /**Save User profile to firebase datastore**/
    private fun saveProfile(profile: Profile) = CoroutineScope(Dispatchers.IO).launch {
        try {
            profileCollectionRef.add(profile).await()
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    this@SignupActivity,
                    "Profile saved successfully",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        } catch (ex: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@SignupActivity, "${ex.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**Register user with firebase email and password **/
    private fun registerUser() {
        val emailAddress = binding.signupEmailAddress.text.toString()
        val password = binding.signupPasswordEt.text.toString()
        if (emailAddress.isNotEmpty() && password.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    mAuth.createUserWithEmailAndPassword(emailAddress, password)
                    withContext(Dispatchers.Main) {
                        checkLoggedInState()
                    }
                } catch (ex: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@SignupActivity, ex.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun checkLoggedInState() {
        if (mAuth.currentUser == null) {
            Toast.makeText(this, "You are not logged in", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "You are logged in", Toast.LENGTH_SHORT).show()
        }
    }
}