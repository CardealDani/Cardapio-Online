package com.cardeal.cardapioonline

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cardeal.cardapioonline.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var valueTotal = 0.0f
    private lateinit var foodItems: List<FoodItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        foodItems = listOf(
            FoodItem(binding.checkBolinhasDeQueijo, R.id.text_value_bolinhas, false),
            FoodItem(binding.checkBatataFrita, R.id.text_value_batata, false),
            FoodItem(binding.checkPasteizinhos, R.id.text_value_pasteizinhos, false),
            FoodItem(binding.checkMoqueca, R.id.text_value_moqueca, false),
            FoodItem(binding.checkLasanha, R.id.text_value_lasanha, false),
            FoodItem(binding.checkPeixe, R.id.text_value_peixe, false),
            FoodItem(binding.checkRefrigerante, R.id.text_value_refrigerante, false),
            FoodItem(binding.checkSuco, R.id.text_value_suco, false),
            FoodItem(binding.checkAgua, R.id.text_value_agua, false),
            FoodItem(binding.checkPudim, R.id.text_value_pudim, false),
            FoodItem(binding.checkMousse, R.id.text_value_mousse, false),
            FoodItem(binding.checkPetitGateau, R.id.text_value_petit_gateau, false)
        )

        for (foodItem in foodItems) {
            foodItem.checkBox?.setOnCheckedChangeListener { _, isChecked ->
                foodItem.isChecked = isChecked
                updateTotal()
            }
        }
        val buttonTotal = findViewById<Button>(R.id.button_total)
        buttonTotal.setOnClickListener {
            if (valueTotal > 0) {
                sendOrderToKitchen()
            } else {
                showToast("Selecione pelo menos um item antes de enviar o pedido.")
            }
        }
    }

    private fun updateTotal() {
        valueTotal = 0.0f

        for (foodItem in foodItems) {
            if (foodItem.isChecked) {
                val valueTextView = findViewById<TextView>(foodItem.value)
                val valueString = valueTextView.text.toString()
                if (valueString.isNotEmpty()) {
                    val value = strToNum(valueTextView.text.toString())
                    valueTotal += value
                }
            }
        }

        // Atualiza o TextView com o novo valor total
        val totalValueStr = "R$${"%.2f".format(valueTotal)}"
        binding.valueTotal.text = totalValueStr
    }


    private fun strToNum(formattedValue: String): Float {
        // Remove o prefixo "R$" e quaisquer outros caracteres não numéricos
        val numericValueStr = formattedValue.replace(Regex("[^0-9.]"), "")

        // Converte a string numérica para um valor float
        return if (numericValueStr.isNotEmpty()) numericValueStr.toFloat() else 0.0f
    }
    private fun updateTotalTextView() {
        val totalValueStr = "R$${"%.2f".format(valueTotal)}"
        binding.valueTotal.text = totalValueStr
    }

    private fun sendOrderToKitchen() {
        showToast("Pedido enviado para a cozinha!")
        resetCheckBoxes()
    }

    private fun resetCheckBoxes() {
        for (foodItem in foodItems) {
            foodItem.checkBox?.isChecked = false
            foodItem.isChecked = false
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}


data class FoodItem(
    val checkBox: CheckBox?,
    val value: Int, // ID do TextView de valor
    var isChecked: Boolean
)
