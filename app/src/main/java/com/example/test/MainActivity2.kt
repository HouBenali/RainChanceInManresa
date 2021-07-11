package com.example.test

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.*

class MainActivity2 : AppCompatActivity() {
    var hourlyChance : List<HoraTiempo> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        println( "-----------------------------------")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        hourlyChance = getList()
    }

    fun initRecycler() {
        val rvhourlychance: RecyclerView = findViewById(R.id.rvhourlychance) as RecyclerView
        rvhourlychance.layoutManager= LinearLayoutManager(this)
        println("--------------------------------------------------$hourlyChance")
        val adapter = ChanceAdapter(hourlyChance)
        rvhourlychance.adapter= adapter
    }

    fun getList(): List<HoraTiempo> {

        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://api.weatherapi.com/v1/forecast.json?key=422c33389ec34450a3b131731210207&q=Manresa&days=1&aqi=no&alerts=no")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    val resStr = response.body()!!.string()
                    val json = JSONObject(resStr)

                    val forecast = json.getJSONObject("forecast").getJSONArray("forecastday")[0]
                    val stringDay = forecast.toString()
                    val hora = JSONObject(stringDay).getJSONArray("hour")

                    this@MainActivity2.runOnUiThread(java.lang.Runnable {

                        val rightNow: Calendar = Calendar.getInstance()
                        val currentHourIn24Format: Int =
                            rightNow.get(Calendar.HOUR_OF_DAY) // return the hour in 24 hrs format (ranging from 0-23)
                        println(currentHourIn24Format)

                        for (i in 0 until hora.length()) {

                            val horaPM = hora.getJSONObject(i)
                            val chance = horaPM.get("chance_of_rain").toString()
                            hourlyChance = hourlyChance + HoraTiempo(i, chance)
                        }
                        println("-----------------------------------$hourlyChance")
                        initRecycler()
                    })
                }

            }

        })

        return hourlyChance
    }




}

