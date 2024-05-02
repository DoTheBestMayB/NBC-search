package com.dothebestmayb.nbc_search.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.add
import com.dothebestmayb.nbc_search.R
import com.dothebestmayb.nbc_search.databinding.ActivityMainBinding
import com.dothebestmayb.nbc_search.presentation.search.SearchFragment
import com.dothebestmayb.nbc_search.presentation.store.StoreFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setStatusBar()
        setListener()
        setFragment(savedInstanceState)
    }

    private fun setStatusBar() = with(binding) {
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setListener() {
        binding.btnKeep.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .add(R.id.frame_layout, StoreFragment())
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit()
        }
        binding.btnSearch.setOnClickListener {
            if (supportFragmentManager.fragments.last() is StoreFragment) {
                supportFragmentManager.popBackStack()
            }
        }
    }

    private fun setFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add<SearchFragment>(R.id.frame_layout, "init")
//                .setReorderingAllowed(true)
//                .addToBackStack(null)
                .commit()
        }
    }
}