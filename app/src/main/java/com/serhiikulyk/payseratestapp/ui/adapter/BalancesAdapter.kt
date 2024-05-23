package com.serhiikulyk.payseratestapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.serhiikulyk.payseratestapp.databinding.ItemBalanceBinding
import com.serhiikulyk.payseratestapp.ui.BalanceItem

class BalancesAdapter: ListAdapter<BalanceItem, BalancesAdapter.BalanceViewHolder>(
    BalanceItemDiffUtil
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BalanceViewHolder {
        val binding = ItemBalanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BalanceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BalanceViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    inner class BalanceViewHolder(private val binding: ItemBalanceBinding): RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: BalanceItem) {
            binding.balance.text = item.formatted
        }

    }

}

object BalanceItemDiffUtil: DiffUtil.ItemCallback<BalanceItem>() {
    override fun areItemsTheSame(oldItem: BalanceItem, newItem: BalanceItem): Boolean {
        return oldItem.currency == newItem.currency
    }

    override fun areContentsTheSame(oldItem: BalanceItem, newItem: BalanceItem): Boolean {
        return oldItem == newItem
    }

}