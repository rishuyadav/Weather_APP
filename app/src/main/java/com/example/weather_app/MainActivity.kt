package com.example.weather_app

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.weather_app.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button1.setOnClickListener{getWeatherData()}
    }
    private fun getWeatherData(){
        val Date= Calendar.getInstance().time.toString()


        val city= binding.input.text.toString()
        val Base_URL = "https://api.openweathermap.org/"
        val q = city
        val AppId = "d315666360ff3996cb8eccd48d777c26"

        val retrofitbuilder= Retrofit.Builder()
                .baseUrl(Base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInterface::class.java)

        val retrofitData= retrofitbuilder.getCurrentWeatherData(q, AppId)

        retrofitData.enqueue(object : Callback<Weather_Data?> {

            override fun onResponse(call: Call<Weather_Data?>, response: Response<Weather_Data?>) {
                val response_body: Weather_Data? = response.body()
                val temp:String = String.format("%.2f", response_body?.main?.temp?.minus(273.15))
                val mintemp:String? = String.format("%.2f", response_body?.main?.temp_min?.minus(273.15))
                val maxtemp:String? = String.format("%.2f", response_body?.main?.temp_max?.minus(273))
                val pressure: String?= response_body?.main?.pressure.toString()+" hPA"
                val humidity: String?= response_body?.main?.humidity.toString()+" %"
                val windspeed : String?= response_body?.wind?.speed.toString()+" m/s\n"+ response_body?.wind?.deg.toString()+" Degrees"
                val coordinates :String?= "Lat: "+ response_body?.coord?.lat.toString()+"\n Lon: "+ response_body?.coord?.lon.toString()


                fun getDateTimeFromEpocLongOfSeconds(epoc: Long): String? {
                    try {
                        val netDate = Date(epoc*1000)
                        return netDate.toString()
                    } catch (e: Exception) {
                        return e.toString()
                    }
                }
                val sunrise:String? = response_body?.sys?.sunrise?.let { getDateTimeFromEpocLongOfSeconds(it.toLong()) }
                val sunset:String?= response_body?.sys?.sunset?.toLong()?.let { getDateTimeFromEpocLongOfSeconds(it) }
                val visibility= response_body?.visibility.toString()
                val tempminmax:String?="Min: "+ response_body?.main?.temp_min.toString()+"\n"+"Max: "+ response_body?.main?.temp_max.toString()

                binding.text1.text = temp
                binding.tempminmax.text=tempminmax
                binding.pressure.text=pressure
                binding.humidity.text=humidity
                binding.wind.text=windspeed
                binding.coordinates.text= coordinates
                binding.visibility.text=visibility
                binding.sunrisetext.text=sunrise
                binding.sunsettext.text=sunset


            }

            override fun onFailure(call: Call<Weather_Data?>, t: Throwable) {
                val text2 = findViewById<TextView>(R.id.text1)
                text2.text = t.toString()

            }

        })
    }

}