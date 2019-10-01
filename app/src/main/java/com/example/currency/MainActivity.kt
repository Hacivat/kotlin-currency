package com.example.currency

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun getRates (view: View) {

        val download = Download()

        try {
            val url = "http://data.fixer.io/api/latest?access_key=a81f435e0cf7d03f571a0466201dee73&format=1"
            download.execute(url)
        } catch (e: Exception) {
            println("Error!")
        }

    }

    inner class Download : AsyncTask<String, Void, String> () {
        override fun doInBackground(vararg params: String?): String {
            var result = ""
            val url : URL
            val httpURLConnection : HttpURLConnection

            try {
                url = URL(params[0])
                httpURLConnection = url.openConnection() as HttpURLConnection

                val inputStream = httpURLConnection.inputStream
                val inputStreamReader = InputStreamReader(inputStream)
                var data = inputStream.read()

                while (data > 0) {
                    val character = data.toChar()
                    result += character

                    data = inputStreamReader.read()
                }

            } catch (e: Exception){
                return "Error!"
            }
            return result
        }

        override fun onPostExecute(result: String?) {
            println(result)
            try {
                val jsonObject = JSONObject(result)
                val base = jsonObject.getString("base")
                println(base)
                val _rates = jsonObject.getString("rates")
                val rates = JSONObject(_rates)

                val CAD = rates.getString("CAD")
                val CHF = rates.getString("CHF")
                val TRY = rates.getString("TRY")

                textView.text = "Kanada Doları: $CAD"
                textView2.text = "İsviçre Frangı: $CHF"
                textView3.text = "Türk Lirası: $TRY"

            } catch (e: Exception) {
                println("error fetching data")
            }

            super.onPostExecute(result)
        }
    }

}
