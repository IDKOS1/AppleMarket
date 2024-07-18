package com.example.applemarket

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidtask.data.Product
import com.example.applemarket.databinding.ActivityDetailBinding
import java.text.DecimalFormat
import java.text.NumberFormat

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val product: Product? = intent.getParcelableExtra("product")

        binding.ivBack.setOnClickListener{
            finish()
        }

        if(product != null){
            binding.ivProductImg.setImageResource(product.image)
            binding.tvNickname.text = product.seller
            binding.tvAdress.text = product.address
            binding.tvTitle.text = product.name
            binding.tvContent.text = product.description
            val decimal = DecimalFormat("#,###")
            binding.tvPrice.text = "${decimal.format(product.price)}원"
        }
    }
}