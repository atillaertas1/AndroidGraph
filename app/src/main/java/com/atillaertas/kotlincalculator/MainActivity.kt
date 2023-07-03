package com.atillaertas.kotlincalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.atillaertas.kotlincalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // add
        val add_button = binding.addButton
        add_button.setOnClickListener {
            calculate_and_display_result(::add,binding)
        }

        //subtraction
        val sub_button = binding.subButton
        sub_button.setOnClickListener {
            calculate_and_display_result(::sub,binding)
        }

        //multiplication
        val multi_button = binding.multiButton
        multi_button.setOnClickListener {
            calculate_and_display_result(::multi,binding)
        }

        //divide
        val divide_button = binding.divideButton
        divide_button.setOnClickListener {
            calculate_and_display_result(::divide,binding)
        }
    }
}

private fun calculate_and_display_result(operation:(Float,Float) -> Float,binding: ActivityMainBinding){
    val input_1 = binding.input1.text.toString().toFloatOrNull()
    val input_2 = binding.input2.text.toString().toFloatOrNull()
    val result_text = binding.resultText

    if (input_1 != null && input_2 != null){
        result_text.setText("Result: ${operation(input_1,input_2)}")
    }else{
        result_text.setText("Lütfen dogru degerleri girdiginizden emin olun!")
    }

}


fun add(input_1 : Float,input_2 : Float): Float {
    return input_1 + input_2
}


fun sub(input_1: Float, input_2: Float): Float{
    return input_1 - input_2
}


fun multi(input_1: Float, input_2: Float): Float{
    return  input_1 * input_2
}


fun divide(input_1: Float, input_2: Float): Float{
    return  input_1 / input_2
}