package ir.mddelavar.iranweather

import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_choose.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.net.URLConnection
import java.util.*
import kotlin.math.ceil



class MainActivity : AppCompatActivity() {
    var context = this
    private var cities = mutableListOf<City>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initCities()
        val cityNames = getCityNames()
        choose.setOnClickListener {
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.dialog_choose)


            dialog.listView.adapter = ArrayAdapter<String>(context, R.layout.item_list, cityNames)
            dialog.listView.setOnItemClickListener { adapterView, view, position, l ->
                dialog.dismiss()

                GetData(context).execute("http://api.openweathermap.org/data/2.5/weather?lat=${cities[position].lat}&lon=${cities[position].lng}&units=metric")
                cityName.text = cities[position].name
            }
            dialog.show()
        }


    }

    private fun initCities() {
        cities.add(City("Tehran", 35.6964895, 51.0696315))
        cities.add(City("Mashhad", 36.2975402, 59.439372))
        cities.add(City("Isfahan", 32.6622111, 51.5469394))
        cities.add(City("Gilan", 37.5253705, 48.4421208))
        cities.add(City("Zanjan", 36.681, 48.4231826))
        cities.add(City("East Azerbaijan", 37.5947685, 45.6447216))
        cities.add(City("Qayen",33.724754, 59.143223))

    }

    private fun getCityNames(): List<String> {
        val list = mutableListOf<String>()
        cities.forEach {
            list.add(it.name)
        }
        return list
    }







    class City(var name: String, var lat: Double, var lng: Double)
    class GetData (val c:MainActivity ): AsyncTask<String, String, String>() {
        override fun doInBackground(vararg p0: String?): String {

            return getResult(p0[0])
        }
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val jsa = JSONObject(result)

            val weather = jsa.getJSONArray("weather")[0] as JSONObject
            c.wg.text = weather.getString("main")
            c.wd.text = weather.getString("description")

            val temp = jsa.getJSONObject("main")
            c.temp.text = "${ceil(temp.getDouble("temp")).toInt()}°"
            c.max.text = "${ceil(temp.getDouble("temp_max")).toInt()}°"
            c.min.text = " / ${temp.getDouble("temp_min").toInt()}°"
            c.pressure.text = "Pressure : ${temp.getDouble("pressure").toInt()}"
            c.humidity.text = "Humidity : ${temp.getDouble("humidity").toInt()}%"

            val wind = jsa.getJSONObject("wind")
            c.wind.text = "Wind : ${wind.getDouble("speed").toInt()} Km/h  ${wind.getDouble("deg").toInt() } deg"

            val sys = jsa.getJSONObject("sys")


            val sunrise = sys.getLong("sunrise")*1000
            val d = Calendar.getInstance()
            d.time = Date(sunrise)

            val time = String.format("%02d:%02d",d.get(Calendar.HOUR_OF_DAY), d.get(Calendar.MINUTE))
            c.sunrise.text = "Sunrise : $time"

            val sunset = sys.getLong("sunset")*1000
            val d2 = Calendar.getInstance()
            d2.time = Date(sunset)
            val time2 = String.format("%02d:%02d",d2.get(Calendar.HOUR_OF_DAY), d2.get(Calendar.MINUTE))
            c.sunset.text = "Sunset : $time2"

            Log.e("s" , result)
        }

        fun getResult(url: String?): String {

            val connection: URLConnection

            var result = ""

            try {


                val u = URL(url)

                connection = u.openConnection()
                connection.setRequestProperty("x-api-key" , "6b9e397b3c89ea3c22a126dfcd156fd0")
                connection.connect()

                val bufferedReader = BufferedReader(InputStreamReader(connection.getInputStream()))

                val s = StringBuilder()

                var ss = ""



                while (true) {
                    try {
                        ss = bufferedReader.readLine()

                        s.append(ss)
                    } catch (e : Exception) {
                        break
                    }


                }

                result = s.toString()


            } catch (e: IOException) {

            }


            return result

        }

    }


}



