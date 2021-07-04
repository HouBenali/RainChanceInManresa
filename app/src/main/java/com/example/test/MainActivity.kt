package com.example.test

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.test.databinding.ActivityMainBinding
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val fechaView: TextView = findViewById(R.id.fecha) as TextView
        val textView: TextView = findViewById(R.id.textView2) as TextView
        val imageView : ImageView = findViewById(R.id.img1) as ImageView

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

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    println("----------------------------------------5------------------------------------")
                    println(response)

                    val resStr = response.body()!!.string()
                    val json = JSONObject(resStr)

                    val forecast = json.getJSONObject("forecast").getJSONArray("forecastday")[0]

                    val StringDay = forecast.toString()
                    val rain = JSONObject(StringDay).getJSONObject("day").get("daily_chance_of_rain").toString()
                    val hoy = JSONObject(StringDay).get("date").toString()
                    println(rain)

                    this@MainActivity.runOnUiThread(java.lang.Runnable {
                        fechaView.text = hoy

                        if (rain.toInt() == 0){
                            textView.text = "Hoy no llueve"
                            imageView.setImageResource(R.drawable.sol)
                        }
                        else if (rain.toInt() > 0 && rain.toInt()<= 25){
                            textView.text = "Poco probable"
                            imageView.setImageResource(R.drawable.nubes)
                        }
                        else if (rain.toInt() > 25 && rain.toInt()< 75){
                            imageView.setImageResource(R.drawable.lluvia)
                            textView.text = "Hoy llueve"
                        }
                        else {
                            imageView.setImageResource(R.drawable.tormenta)
                            textView.text = "Mucha lluvia"

                        }
                    })
                }
            }
        })

    }


}