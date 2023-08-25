package com.flannery.androidtools.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.flannery.androidtools.databinding.ActivityCustomViewPagerBinding

class CustomViewPagerActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, CustomViewPagerActivity::class.java))
        }
    }

    private lateinit var binding: ActivityCustomViewPagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCustomViewPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.index0.setOnClickListener(this)
        binding.index1.setOnClickListener(this)
        binding.index2.setOnClickListener(this)
        binding.index3.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.index0 -> {
                binding.customViewPager.jump2Screen(0)
            }
            binding.index1 -> {
                binding.customViewPager.jump2Screen(1)
            }
            binding.index2 -> {
                binding.customViewPager.jump2Screen(2)
            }
            binding.index3 -> {
                binding.customViewPager.jump2Screen(3)
            }
        }
    }
}