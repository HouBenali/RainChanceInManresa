package com.example.test

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.test.databinding.ActivityMainBinding
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val fechaView: TextView = findViewById(R.id.fecha) as TextView
        val textView: TextView = findViewById(R.id.textView2) as TextView
        val imageView : ImageView = findViewById(R.id.img1) as ImageView
        val button : Button = findViewById(R.id.button)

        button.setOnClickListener{
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://api.weatherapi.com/v1/forecast.json?key=422c33389ec34450a3b131731210207&q=Manresa&days=1&aqi=no&alerts=no")
            .build()

        //val response : Response = client.newCall(request).execute()

        // Coroutines not supported directly, use the basic Callback way:
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    println("----------------------------------------5------------------------------------")
                    println(response)

                    val resStr = response.body()!!.string()
                    val json = JSONObject(resStr)

                    val forecast = json.getJSONObject("forecast").getJSONArray("forecastday")[0]

                    val StringDay = forecast.toString()
                    val hora = JSONObject(StringDay).getJSONArray("hour")
                    val rain = JSONObject(StringDay).getJSONObject("day").get("daily_chance_of_rain").toString()
                    val hoy = JSONObject(StringDay).get("date").toString()
                    println(rain)
                    println("length " + hora.length())

                    val rightNow: Calendar = Calendar.getInstance()
                    val currentHourIn24Format: Int =
                        rightNow.get(Calendar.HOUR_OF_DAY) // return the hour in 24 hrs format (ranging from 0-23)
                    println(currentHourIn24Format)

                    this@MainActivity.runOnUiThread(java.lang.Runnable {
                        fechaView.text = hoy

                        if (rain.toInt() == 0){
                            textView.text = "Hoy no llueve. \n 0%"
                            imageView.setImageResource(R.drawable.sol)
                        }
                        else if (rain.toInt() in 1..25){
                            textView.text = "Baja probabilidad de lluvia \n $rain%"
                            imageView.setImageResource(R.drawable.nubes)
                        }
                        else if (rain.toInt() in 26..74){
                            imageView.setImageResource(R.drawable.lluvia)
                            textView.text = "Probabilidad mediana de lluvia: \n $rain%"
                        }
                        else {
                            imageView.setImageResource(R.drawable.tormenta)
                            textView.text = "Probabilidad muy alta de lluvia: \n $rain%"

                        }
                    })
                }
            }
        })

    }


}