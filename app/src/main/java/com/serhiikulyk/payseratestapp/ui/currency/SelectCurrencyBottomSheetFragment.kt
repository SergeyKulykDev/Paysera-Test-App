package com.serhiikulyk.payseratestapp.ui.currency

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.serhiikulyk.payseratestapp.databinding.BottomSheetSelectCurrencyBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SelectCurrencyBottomSheetFragment(private val onSelect: (String) -> Unit): BottomSheetDialogFragment() {

    private var _binding: BottomSheetSelectCurrencyBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SelectCurrencyViewModel>()

    private val currenciesAdapter = CurrenciesAdapter {
        onSelect(it.code)
        dismiss()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetSelectCurrencyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        collectStates()
    }

    private fun initUi() {
        binding.currencies.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = currenciesAdapter
        }
    }

    private fun collectStates() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                currenciesAdapter.submitList(state)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

fun AppCompatActivity.selectCurrency(onSelect: (String) -> Unit) {
    val dialog = SelectCurrencyBottomSheetFragment(onSelect)
    dialog.show(supportFragmentManager, "SelectCurrencyBottomSheetFragment")
}
