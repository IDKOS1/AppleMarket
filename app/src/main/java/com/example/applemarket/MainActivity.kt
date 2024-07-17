package com.example.applemarket

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.example.applemarket.data.ProductController
import com.example.applemarket.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                dialog("종료", "정말 종료하시겠습니까?")
            }
        })

        binding.rcvProductsList.apply {
            val products = ProductController.getProducts().toMutableList()
            val adapter = ProductAdapter(products)
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(this@MainActivity)

            val decoration = DividerItemDecoration(this@MainActivity, VERTICAL)
            this.addItemDecoration(decoration)

            adapter.itemClick = object : ProductAdapter.ItemClick {
                override fun onClick(view: View, position: Int) {
                    val intent = Intent(this@MainActivity, DetailActivity::class.java)
                    intent.putExtra("product", products[position])
                    startActivity(intent)
                }
            }
        }
    }

    private fun dialog(title: String, content: String) {

        var builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(content)
        builder.setIcon(R.mipmap.ic_launcher)

        val listener = object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                when (p1) {
                    DialogInterface.BUTTON_POSITIVE ->
                        finish()
                }
            }
        }

        builder.setPositiveButton("예", listener)
        builder.setNegativeButton("아니요", listener)

        builder.show()
    }
}