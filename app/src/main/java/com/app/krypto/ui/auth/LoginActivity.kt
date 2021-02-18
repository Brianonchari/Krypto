package com.app.krypto.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.krypto.databinding.ActivityLoginBinding
import com.app.krypto.ui.MainActivity
import com.app.krypto.utils.launchActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mUser: FirebaseAuth
    private lateinit var binding:ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()
        mUser = FirebaseAuth.getInstance()
        if (mUser.currentUser!=null) {
          launchActivity<MainActivity> {
              finish()
          }
        }
        binding.btnSignIn.setOnClickListener{
            val visibility = if (binding.loginProgress.visibility == View.GONE) View.VISIBLE else View.GONE
            binding.loginProgress.visibility = visibility
            loginUser()
        }
        binding.bottomContainer.setOnClickListener {
            launchActivity<SignupActivity> {
                finish()
            }
        }
    }

    private fun loginUser(){
        val emailAddress = binding.loginEmailAddress.text.toString()
        val password = binding.loginPasswordEt.text.toString()
        if(emailAddress.isNotEmpty() && password.isNotEmpty()){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    mAuth.signInWithEmailAndPassword(emailAddress,password).await()
                    withContext(Dispatchers.Main){
                        checkLoggedInState()
                    }
                }catch (ex: Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@LoginActivity, ex.message  , Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun checkLoggedInState() {
        if(mAuth.currentUser==null){
            Toast.makeText(this, "You are not logged in", Toast.LENGTH_SHORT).show()
        }else{
          launchActivity<MainActivity> {
              finish()
          }
        }
    }

    override fun onStart() {
        super.onStart()
        checkLoggedInState()
    }
}