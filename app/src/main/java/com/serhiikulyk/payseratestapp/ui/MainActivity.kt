package com.serhiikulyk.payseratestapp.ui

import android.os.Bundle
import android.text.InputFilter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.serhiikulyk.payseratestapp.R
import com.serhiikulyk.payseratestapp.data.local.model.formatted
import com.serhiikulyk.payseratestapp.databinding.ActivityMainBinding
import com.serhiikulyk.payseratestapp.extensions.dpToPx
import com.serhiikulyk.payseratestapp.extensions.setVisible
import com.serhiikulyk.payseratestapp.extensions.setVisibleOrInvisible
import com.serhiikulyk.payseratestapp.extensions.showMaterialAlertDialog
import com.serhiikulyk.payseratestapp.ui.adapter.BalancesAdapter
import com.serhiikulyk.payseratestapp.ui.adapter.HorizontalSpaceItemDecoration
import com.serhiikulyk.payseratestapp.ui.currency.selectCurrency
import com.serhiikulyk.payseratestapp.ui.other.MoneyValueFilter
import com.serhiikulyk.payseratestapp.use_cases.ConversionResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()
    private val balancesAdapter = BalancesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()
        collectStates()
    }

    private fun initUi() {
        initMyBalances()
        initSell()
        initReceive()
        initSubmit()
    }

    private fun initMyBalances() {
        binding.myBalances.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = balancesAdapter
            addItemDecoration(HorizontalSpaceItemDecoration(32.dpToPx(this@MainActivity)))
        }
    }

    private fun initSell() {
        binding.sell.sellEdit.apply {
            filters = arrayOf(MoneyValueFilter(), InputFilter.LengthFilter(10))
            doAfterTextChanged { viewModel.onSellChange(it.toString()) }
        }
        binding.sell.sellCurrency.setOnClickListener {
            selectCurrency {
                viewModel.onSellCurrencyChanged(it)
            }
        }
    }

    private fun initReceive() {
        binding.receive.receiveCurrency.setOnClickListener {
            selectCurrency {
                viewModel.onReceiveCurrencyChanged(it)
            }
        }
    }

    private fun initSubmit() {
        binding.submit.setOnClickListener {
            viewModel.submit()
        }
    }

    private fun collectStates() {
        collectReceiveState()
        collectSellCurrencyState()
        collectReceiveCurrencyState()
        collectBalancesState()
        collectConversionResultState()
        collectIsSubmitAvailableState()
        collectSyncErrorState()
        collectIsSyncingState()
        collectErrorState()
    }

    private fun collectReceiveState() {
        lifecycleScope.launch {
            viewModel.uiState.map { it.receive }.collect { state ->
                binding.receive.receive.apply {
                    val color = if (state > 0.0) R.color.green else R.color.button_text_color
                    setTextColor(getColor(color))
                    text = if (state > 0.0) "+${state.formatted()}" else state.formatted()
                }

            }
        }
    }

    private fun collectSellCurrencyState() {
        lifecycleScope.launch {
            viewModel.uiState.map { it.sellCurrency }.collect { state ->
                binding.sell.sellCurrency.text = state
            }
        }
    }

    private fun collectReceiveCurrencyState() {
        lifecycleScope.launch {
            viewModel.uiState.map { it.receiveCurrency }.collect { state ->
                binding.receive.receiveCurrency.text = state
            }
        }
    }

    private fun collectBalancesState() {
        lifecycleScope.launch {
            viewModel.uiState.map { it.balances }.collect { state ->
                balancesAdapter.submitList(state)
            }
        }
    }

    private fun collectConversionResultState() {
        lifecycleScope.launch {
            viewModel.uiState.mapNotNull { it.conversionResult }.collect { state ->
                binding.sell.sellEdit.setText("")
                showSuccessDialog(state)
                viewModel.onConvertedShown()
            }
        }
    }

    private fun collectIsSubmitAvailableState() {
        lifecycleScope.launch {
            viewModel.uiState.mapNotNull { it.isSubmitAvailable }.collect { state ->
                binding.submit.isEnabled = state
            }
        }
    }

    private fun collectSyncErrorState() {
        lifecycleScope.launch {
            viewModel.uiState.map { it.syncError }.collect { state ->
                binding.synchronizationError.setVisible(state)
            }
        }
    }

    private fun collectIsSyncingState() {
        lifecycleScope.launch {
            viewModel.uiState.map { it.isSyncing }.collect { state ->
                binding.progress.setVisibleOrInvisible(state)
            }
        }
    }

    private fun collectErrorState() {
        lifecycleScope.launch {
            viewModel.uiState.mapNotNull { it.error }.collect { state ->
                Snackbar.make(this@MainActivity, binding.root, state, Snackbar.LENGTH_LONG).show()
                viewModel.onErrorShown()
            }
        }
    }

    private fun showSuccessDialog(conversionResult: ConversionResult) {
        showMaterialAlertDialog(
            title = getString(R.string.currency_converted_title),
            message = conversionResult.formatted(),
            positiveButtonText = getString(R.string.done),
            onPositiveButtonClick = {}
        )
    }

    private fun ConversionResult.formatted(): String {
        val sellFormatted = sell.formatted(sellCurrency)
        val buyFormatted = buy.formatted(buyCurrency)
        val str = getString(R.string.currency_converted_description, sellFormatted, buyFormatted)

        val stringBuilder = StringBuilder(str)

        if (commission != null && commission != 0.0) {
            val commissionFormatted = commission.formatted(sellCurrency)

            stringBuilder.append(" ")
            stringBuilder.append(getString(R.string.currency_converted_description_commission, commissionFormatted))
        }
        return stringBuilder.toString()
    }

}
