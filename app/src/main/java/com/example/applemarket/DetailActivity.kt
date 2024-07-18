package com.example.applemarket

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidtask.data.Product
import com.example.applemarket.data.ProductController
import com.example.applemarket.databinding.ActivityDetailBinding
import java.text.DecimalFormat

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

        val getProduct = intent.getParcelableExtra<Product>("product")
        val product: Product? = ProductController.findProductId(getProduct!!.id)

        if (product != null) {
            Log.i("isLiked", "${product.isLiked}")
            Log.i("isLiked", "${product.likes}")
            binding.ivProductImg.setImageResource(product.image)
            binding.tvNickname.text = product.seller
            binding.tvAdress.text = product.address
            binding.tvTitle.text = product.name
            binding.tvContent.text = product.description
            binding.tbLike.isChecked = product.isLiked
            val decimal = DecimalFormat("#,###")
            binding.tvPrice.text = "${decimal.format(product.price)}원"
        }

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.tbLike.setOnCheckedChangeListener() { _, isChecked ->
            product!!.let { ProductController.clickedLike(it, isChecked) }
            Log.i("isLiked", "${product.likes}")

        }


    }
}