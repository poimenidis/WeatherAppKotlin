package android.example.com.weatherappkotlin

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import org.json.JSONObject
import java.net.URL
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class WeatherDetails : AppCompatActivity() {

    private val CITY: String = "thessaloniki"
    private val API: String = "b1b15e88fa797225412429c1c50c122a1"
    var position:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_details)
         position = getIntent().getIntExtra("element",0);



            weatherTask().execute()

        }

        inner class weatherTask() : AsyncTask<String, Void, String>() {
            override fun onPreExecute() {
                super.onPreExecute()
                /* Showing the ProgressBar, Making the main design GONE */
                findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
                findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
            }

            override fun doInBackground(vararg params: String?): String? {
                var response:String?
                try{
                    response =
                    URL("https://pro.openweathermap.org/data/2.5/forecast/climate?units=metric&q=$CITY&appid=$API").readText(
                        Charsets.UTF_8
                    )
                }catch (e: Exception){
                    response = null
                }
                return response
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                try {
                    /* Extracting JSON returns from the API */
                    val jsonOb = JSONObject(result)
                    val list = jsonOb.getJSONArray("list")
                    val jsonObj: JSONObject = list.getJSONObject(position)
                    val temp = jsonObj.getJSONObject("temp")

                    val updatedAt: Long = jsonObj.getLong("dt")
                    val updatedAtText =
                        SimpleDateFormat(
                            "dd/MM/yyy",
                            Locale.ENGLISH
                        ).format(
                            Date(updatedAt * 1000)
                        )

                    val format2: DateFormat = SimpleDateFormat("EEEE")
                    val finalDay: String = format2.format(Date(updatedAt * 1000))


                    val tempMin = "Min temp: "+ temp.getString("min") + "°C"
                    val tempMax ="Max temp: "+ temp.getString("max") + "°C"
                    val eve = temp.getString("eve")
                    //******************************
                    val strs = eve.split(".").toTypedArray()

                    val pressure = jsonObj.getString("pressure")
                    val humidity = jsonObj.getString("humidity")

                    val sunrise:Long = jsonObj.getLong("sunrise")
                    val sunset:Long = jsonObj.getLong("sunset")
                    val windSpeed = jsonObj.getString("speed")

                    val wea = jsonObj.getJSONArray("weather")
                    val weatherDescription = wea.getJSONObject(0).getString("description")

                    val city = jsonOb.getJSONObject("city")
                    val address = city.getString("name")+", "+city.getString("country")

                    /* Populating extracted data into our views */
                    findViewById<TextView>(R.id.address).text = address
                    findViewById<TextView>(R.id.day).text =  finalDay
                    findViewById<TextView>(R.id.updated_at).text =  updatedAtText
                    findViewById<TextView>(R.id.status).text = weatherDescription.capitalize()
                    findViewById<TextView>(R.id.temp).text = strs[0] + "°C"
                    findViewById<TextView>(R.id.temp_min).text = tempMin
                    findViewById<TextView>(R.id.temp_max).text = tempMax
                    findViewById<TextView>(R.id.sunrise).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise*1000))
                    findViewById<TextView>(R.id.sunset).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset*1000))
                    findViewById<TextView>(R.id.wind).text = windSpeed
                    findViewById<TextView>(R.id.pressure).text = pressure
                    findViewById<TextView>(R.id.humidity).text = humidity

                    /* Views populated, Hiding the loader, Showing the main design */
                    findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                    findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE

                } catch (e: Exception) {
                    findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                }

            }
        }
    }