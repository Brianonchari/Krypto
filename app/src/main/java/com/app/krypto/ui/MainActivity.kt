package com.app.krypto.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.app.krypto.R
import com.app.krypto.databinding.ActivityMainBinding
import com.app.krypto.ui.fragments.KryptoFragmentFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var fragmentFactory: KryptoFragmentFactory
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        supportFragmentManager.fragmentFactory = fragmentFactory
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val navController = Navigation.findNavController(this, R.id.navHostFragment)
        binding.bottomNavigationView.setOnNavigationItemReselectedListener {/* NO-OP */ }
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}