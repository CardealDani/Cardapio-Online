package com.cardeal.cardapioonline

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cardeal.cardapioonline.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Declaração das variáveis
    private lateinit var binding: ActivityMainBinding
    private var valueTotal = 0.0f
    private lateinit var foodItems: List<FoodItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Infla o layout da atividade usando ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa a lista de itens de comida
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

        // Configura os listeners de mudança de estado para os CheckBoxes
        for (foodItem in foodItems) {
            foodItem.checkBox?.setOnCheckedChangeListener { _, isChecked ->
                foodItem.isChecked = isChecked
                updateTotal()
            }
        }

        // Configura o listener de clique para o botão "Enviar Pedido"
        val buttonTotal = findViewById<Button>(R.id.button_total)
        buttonTotal.setOnClickListener {
            if (valueTotal > 0) {
                sendOrderToKitchen()
            } else {
                showToast("Selecione pelo menos um item antes de enviar o pedido.")
            }
        }
    }

    // Atualiza o valor total com base nos itens de comida selecionados
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

    // Converte uma string de valor monetário para um número float
    private fun strToNum(formattedValue: String): Float {
        // Remove o prefixo "R$" e quaisquer outros caracteres não numéricos
        val numericValueStr = formattedValue.replace(Regex("[^0-9.]"), "")

        // Converte a string numérica para um valor float
        return if (numericValueStr.isNotEmpty()) numericValueStr.toFloat() else 0.0f
    }

    // Envia o pedido para a cozinha e reinicia os itens de comida selecionados
    private fun sendOrderToKitchen() {
        showToast("Pedido enviado para a cozinha!")
        reset()
    }

    // Reinicia os itens de comida selecionados e o campo de observação
    private fun reset() {
        for (foodItem in foodItems) {
            foodItem.checkBox?.isChecked = false
            foodItem.isChecked = false
        }
        binding.textObservation.setText("")
        binding.textObservation.hint = "Escreva alguma observação( S/Cebola...)"
        hideKeyboard(this, binding.textObservation)

    }

    // Esconde o teclado virtual
    private fun hideKeyboard(context: Context, editText: View) {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    // Mostra um toast com a mensagem fornecida
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}

// Data class para representar um item de comida
data class FoodItem(
    val checkBox: CheckBox?, // CheckBox associado ao item de comida
    val value: Int, // ID do TextView que contém o valor do item de comida
    var isChecked: Boolean // Estado de seleção do item de comida
)
