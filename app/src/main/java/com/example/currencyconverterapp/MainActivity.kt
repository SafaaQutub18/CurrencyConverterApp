package com.example.currencyconverterapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.currencyconverterapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var currencyList = arrayListOf("inr", "aud" , "ada" , "all")
    var selected = 0
    var currencyData : CurrencyData ? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //attached array adapter to spinner
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencyList)
        binding.spinner.adapter = arrayAdapter

        //spinner setting
        binding.spinner.onItemSelectedListener = object :

            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int,id: Long, ) {
                selected = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {  }

        }
        binding.convertBtn.setOnClickListener {
            var value = 0.0
            try {
                value = binding.inputET.text.toString().toDouble()
            }catch(e: Exception){
                Toast.makeText(this, "Please Enter number!", Toast.LENGTH_LONG).show()
            }
            Log.d("Main1", "$value")
            getCurrencyData(onResult = {
                var currentResult : Double
                currencyData = it
                when(selected){
                    0-> calculate(currencyData?.eur?.inr?.toDouble(), value)
                    1-> calculate(currencyData?.eur?.aud?.toDouble(), value)
                    2-> calculate(currencyData?.eur?.ada?.toDouble(), value)
                    3-> calculate(currencyData?.eur?.all?.toDouble(), value)
                }
            })
        }
    }

    private fun presentResult(result: Double) {
        binding.resultTV.setText("The result is $result")
    }

    private fun calculate(selectedCurrency: Double?, value: Double): Double {
        var result = 0.0
        if(selectedCurrency != null)
            result =  value * selectedCurrency
        presentResult(result)
        
        return result
    }

    fun getCurrencyData(onResult: (CurrencyData?) -> Unit){
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        if(apiInterface != null ){
        apiInterface.getCurrency()?.enqueue(object : Callback<CurrencyData> {
            override fun onResponse(call: Call<CurrencyData>, response: Response<CurrencyData>) {
                Log.d("Main", response.body().toString())
                onResult(response.body())
            }

            override fun onFailure(call: Call<CurrencyData?>, t: Throwable) {

                Toast.makeText(applicationContext, "Something wrong : ${t.message}", Toast.LENGTH_LONG).show()
                onResult(null)
            }
        })
    }
    }
}
