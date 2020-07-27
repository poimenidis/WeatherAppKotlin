package android.example.com.weatherappkotlin

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import org.json.JSONObject
import java.net.URL
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class WeatherList : AppCompatActivity() {

    private var CITY: String = "thessaloniki"
    private val API: String = "b1b15e88fa797225412429c1c50c122a1"
    private var adapter: WeatherAdapter? = null
    private var weatherList = ArrayList<Weather>()
    private var textviewloc: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_list)

        textviewloc = findViewById(R.id.location)

        val listView: ListView = findViewById(R.id.listview)

        val searchView = findViewById(R.id.search_bar) as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query!=null) {
                    CITY = query
                    weatherTask().execute()
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

//        var weatherList = ArrayList<String>()
//        weatherList.add("lk")
//        weatherList.add("lk")
//        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, weatherList)
//        listView.adapter = adapter


        var w1 = Weather("","1","2")
        var w2 = Weather("","1","2")
        var w3 = Weather("","1","4")
        weatherList.add(w1)
        weatherList.add(w2)
        weatherList.add(w3)

        adapter = WeatherAdapter(this,weatherList)
        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            val element = position // The item that was clicked
            val intent = Intent(this, WeatherDetails::class.java)
            intent.putExtra("element", element)
            startActivity(intent)
        }

        weatherTask().execute()
    }


    inner class weatherTask() : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            /* Showing the ProgressBar, Making the main design GONE */
            findViewById<ProgressBar>(R.id.load).visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: String?): String? {
            var response: String?
            try {
                response =
                    URL("https://pro.openweathermap.org/data/2.5/forecast/climate?units=metric&q=$CITY&appid=$API").readText(
                        Charsets.UTF_8
                    )
            } catch (e: Exception) {
                response = null
                Log.i("makis","erro2r")
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                /* Extracting JSON returns from the API */
                val jsonObj = JSONObject(result)
                val list = jsonObj.getJSONArray("list")

                weatherList.clear()
                adapter?.clear();

                for(i in 0.. list.length()-1) {



                    val Noumero: JSONObject = list.getJSONObject(i)

                    val temp = Noumero.getJSONObject("temp")

                    val updatedAt: Long = Noumero.getLong("dt")
                    val updatedAtText =
                        SimpleDateFormat(
                            "dd/MM/yyy",
                            Locale.ENGLISH
                        ).format(
                            Date(updatedAt * 1000)
                        )

                    val format2: DateFormat = SimpleDateFormat("EEEE")
                    val finalDay: String = format2.format(Date(updatedAt * 1000))

                    val eve = temp.getString("eve")
                    //******************************
                    val strs = eve.split(".").toTypedArray()

                    val wea = Noumero.getJSONArray("weather")
                    val icon = wea.getJSONObject(0).getString("icon")

                    var w1 = Weather(icon,finalDay+"    "+ updatedAtText,"Temperature: "+strs[0]+ "°C")
                    weatherList.add(w1)
                }
                /* Views populated, Hiding the loader, Showing the main design */
                findViewById<ProgressBar>(R.id.load).visibility = View.GONE
                adapter?.notifyDataSetChanged()
                textviewloc!!.text ="WEATHER IN $CITY".toUpperCase()

            } catch (e: Exception) {
                findViewById<ProgressBar>(R.id.load).visibility = View.GONE
                Log.i("makis","error")
                textviewloc!!.text = "NO RESULTS"
                adapter?.clear()
            }

        }
    }
}