package com.ikersoft.cryptotest.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ikersoft.cryptotest.R
import com.ikersoft.cryptotest.databinding.ItemCryptoBinding
import com.ikersoft.cryptotest.model.Crypto

class CryptoAdapter(
    private val context: Context,
) : ListAdapter<Crypto, CryptoAdapter.ViewHolder>(ListAdapterCallback()) {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), context, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: ItemCryptoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var context: Context

        fun bind(
            crypto: Crypto,
            context: Context,
            position: Int
        ) {
            binding.name.text = crypto.name


            //round prices
            val priceUsd = crypto.priceUsd.toFloat()
            val changePercent24Hr = crypto.changePercent24Hr.toFloat()

            binding.price.text = String.format("%.2f", priceUsd)
            binding.percent.text = String.format("%.2f%%", changePercent24Hr)

            //set percent color
            val color: Int = if (changePercent24Hr > 0){
                Color.parseColor("#179E24")
            } else{
                Color.parseColor("#AB0000")
            }
            binding.percent.setTextColor(color)

            //set odd/even backgrounds
            if (position % 2 == 0){
                binding.name.background = ContextCompat.getDrawable(context, R.drawable.cell_background_even)
                binding.price.background = ContextCompat.getDrawable(context, R.drawable.cell_background_even)
                binding.percent.background = ContextCompat.getDrawable(context, R.drawable.cell_background_even)
            }
            else{
                binding.name.background = ContextCompat.getDrawable(context, R.drawable.cell_background_odd)
                binding.price.background = ContextCompat.getDrawable(context, R.drawable.cell_background_odd)
                binding.percent.background = ContextCompat.getDrawable(context, R.drawable.cell_background_odd)
            }

            this.context = context

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemCryptoBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    class ListAdapterCallback : DiffUtil.ItemCallback<Crypto>() {
        override fun areItemsTheSame(oldItem: Crypto, newItem: Crypto): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Crypto, newItem: Crypto): Boolean {
            return oldItem == newItem
        }
    }

}