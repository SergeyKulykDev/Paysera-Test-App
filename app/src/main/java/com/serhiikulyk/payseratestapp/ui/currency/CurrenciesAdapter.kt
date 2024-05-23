package com.serhiikulyk.payseratestapp.ui.currency

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.serhiikulyk.payseratestapp.databinding.ItemCurrencyBinding

class CurrenciesAdapter(private val onClick: (CurrencyItem) -> Unit): ListAdapter<CurrencyItem, CurrenciesAdapter.CurrencyViewHolder>(
    CurrencyItemDiffUtil
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val binding = ItemCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    inner class CurrencyViewHolder(private val binding: ItemCurrencyBinding): RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: CurrencyItem) {
            binding.currency.apply {
                text = item.code
                setOnClickListener { onClick(item) }
            }
        }

    }

}

object CurrencyItemDiffUtil: DiffUtil.ItemCallback<CurrencyItem>() {
    override fun areItemsTheSame(oldItem: CurrencyItem, newItem: CurrencyItem): Boolean {
        return oldItem.code == newItem.code
    }

    override fun areContentsTheSame(oldItem: CurrencyItem, newItem: CurrencyItem): Boolean {
        return oldItem == newItem
    }

}