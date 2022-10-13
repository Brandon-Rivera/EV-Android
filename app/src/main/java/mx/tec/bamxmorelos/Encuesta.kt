package mx.tec.bamxmorelos

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.InputQueue
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.view.isVisible
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import mx.tec.bamxmorelos.databinding.ActivityEncuestaBinding
import mx.tec.bamxmorelos.databinding.ActivityRegistroBinding
import org.json.JSONArray
import org.json.JSONObject

class Encuesta : AppCompatActivity() {
    lateinit var binding: ActivityEncuestaBinding
    lateinit var queue: RequestQueue

    var  res = JSONArray()

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityEncuestaBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        queue = Volley.newRequestQueue(this@Encuesta)
        val sharedPreference = getSharedPreferences("profile", Context.MODE_PRIVATE)
        supportActionBar?.hide()
        binding.btnAnterior.visibility = View.INVISIBLE
        binding.btnSiguiente.visibility = View.INVISIBLE
        var count = 0

        val url = "http://api-vacaciones.us-east-1.elasticbeanstalk.com/api/questions"

        binding.lName.visibility = View.VISIBLE

        val listener = Response.Listener<JSONArray> { response ->
            res = response
            println(response)
        }

        val error = Response.ErrorListener { error ->
            Log.e("RequestError", error.toString()!!)
        }

        val request =
            object : JsonArrayRequest(Request.Method.GET, url, null, listener, error) {
                override fun getHeaders(): MutableMap<String, String> {
                    val hashMap = HashMap<String, String>()
                    hashMap.put(
                        "x-access-token",
                        sharedPreference.getString("token", "#").toString()
                    )
                    return hashMap
                }
            }
        queue.add(request)

        binding.btnStartEncuesta.setOnClickListener {
            binding.lName.visibility = View.GONE
            binding.btnSiguiente.visibility = View.VISIBLE
            binding.btnAnterior.visibility = View.VISIBLE
            onIdChanged(count)
        }

        binding.btnSiguiente.setOnClickListener {
            count += 1
            onIdChanged(count)
        }

        binding.btnAnterior.setOnClickListener {
            count -= 1
            onIdChanged(count)
        }
    }

    private fun onIdChanged(count: Int) {

        val idQ = res.getJSONObject(count).getString("id")
        val urlOptions = "http://api-vacaciones.us-east-1.elasticbeanstalk.com/api/famMemberByIdUser/$idQ"
        println(urlOptions)
        val options = mutableListOf<String>()
        val sharedPreference = getSharedPreferences("profile", Context.MODE_PRIVATE)
        val listener = Response.Listener<JSONArray> { response ->
            for (i in 0 until response.length()) {
                options.add(
                    response.getJSONObject(i).getString("names")
                )
            }
            println(response)
            println(options)

            val rg = RadioGroup(this)
            rg.orientation = RadioGroup.VERTICAL
            for (i in options.indices) {
            // create a radio button
            val rb = RadioButton(this)
            // set text for the radio button
            rb.text = options[i]
            // assign an automatically generated id to the radio button
            rb.id = View.generateViewId()
            // add radio button to the radio group
            rg.addView(rb)
                println("AAAA")
            }
            binding.lRadio.addView(rg)
        }

        val error = Response.ErrorListener { error ->
            Log.e("RequestError", error.toString()!!)
        }

        val requestOptions =
            object : JsonArrayRequest(Request.Method.GET, urlOptions, null, listener, error) {
                override fun getHeaders(): MutableMap<String, String> {
                    val hashMap = HashMap<String, String>()
                    hashMap.put(
                        "x-access-token",
                        sharedPreference.getString("token", "#").toString()
                    )
                    return hashMap
                }
            }
        queue.add(requestOptions)

        binding.tvId.text = res.getJSONObject(count).getString("id")
        binding.tvQuestion.text = res.getJSONObject(count).getString("question")
        binding.tvNameIntegrante.text = count.toString()
        //binding.tvRow.text = resp.getJSONObject(idQ.toInt()).getString("optionValue")



    }

}