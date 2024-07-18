package com.example.applemarket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.applemarket.data.ProductController
import com.example.androidtask.data.Product
import com.example.applemarket.databinding.ItemProductBinding
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

class ProductAdapter(val mItems: MutableList<Product>) : RecyclerView.Adapter<ProductAdapter.Holder>() {

    interface ItemClick {
        fun onClick(view : View, position : Int)
    }

    var itemClick : ItemClick? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.setOnClickListener {  //클릭이벤트추가부분
            itemClick?.onClick(it, position)
        }
        val product = ProductController.getProducts()[position]
        val format = DecimalFormat("#,###")

        holder.productImage.setImageResource(product.image)
        holder.title.text = product.name
        holder.adress.text = product.address
        holder.price.text = format.format(product.price) + "원"
        holder.comment.text = product.chat.toString()
        holder.like.text = product.likes.toString()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    inner class Holder(binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        val productImage = binding.ivProduct
        val title = binding.tvProductTitle
        var price = binding.tvPrice
        var comment = binding.tvCountComment
        var adress = binding.tvAddress
        var like = binding.tvCountLike
    }
}