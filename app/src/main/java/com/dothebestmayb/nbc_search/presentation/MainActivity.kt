package com.dothebestmayb.nbc_search.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dothebestmayb.nbc_search.R
import com.dothebestmayb.nbc_search.databinding.ActivityMainBinding
import com.dothebestmayb.nbc_search.presentation.search.SearchFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setStatusBar()
        setFragment()
    }

    private fun setStatusBar() = with(binding) {
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.frame_layout, SearchFragment())
            .commitNow()
    }

}