package com.albertson.spark_poc.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.albertson.spark_poc.R
import com.albertson.spark_poc.databinding.MainActivityBinding
import com.albertson.spark_poc.presentation.fragment.CommunityListFragment
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
       /* val communityListFragment = CommunityListFragment.newInstance
        *//*if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.mainContainer, communityListFragment).commitAllowingStateLoss()
        }*/
    }
}