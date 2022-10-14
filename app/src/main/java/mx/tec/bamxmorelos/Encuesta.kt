package mx.tec.bamxmorelos

import android.R
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.*
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.view.children
import androidx.core.view.isVisible
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import mx.tec.bamxmorelos.adapter.CustomAdapter
import mx.tec.bamxmorelos.databinding.ActivityEncuestaBinding
import mx.tec.bamxmorelos.model.Elemento
import org.json.JSONArray

class Encuesta : AppCompatActivity() {
    lateinit var binding: ActivityEncuestaBinding
    lateinit var queue: RequestQueue

    var  res = JSONArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityEncuestaBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        this.requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        queue = Volley.newRequestQueue(this@Encuesta)

        val sharedPreference = getSharedPreferences("profile", Context.MODE_PRIVATE)

        supportActionBar?.hide()

        binding.btnAnterior.visibility = INVISIBLE
        binding.btnSiguiente.visibility = INVISIBLE
        binding.tvId.visibility = INVISIBLE
        binding.tvQuestion.visibility = INVISIBLE
        binding.lName.visibility = VISIBLE
        binding.tvNameEncuesta.visibility = INVISIBLE

        var count = 0

        val url = "http://api-vacaciones.us-east-1.elasticbeanstalk.com/api/questions"

        val listener = Response.Listener<JSONArray> { response ->
            res = response
            Log.e("RESPONSE", response.toString())

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
                    println(sharedPreference.getString("token", "#").toString())
                    return hashMap
                }
            }
        queue.add(request)


        val datos = mutableListOf<String>()
        val idUser = sharedPreference.getString("idUser", "#")
        val urlFam = "http://api-vacaciones.us-east-1.elasticbeanstalk.com/api/famMemberByIdUser/$idUser"

        val listenerFam = Response.Listener<JSONArray> { response ->
            for (i in 0 until response.length()) {
                datos.add(
                    "${response.getJSONObject(i).getString("names")} ${response.getJSONObject(i).getString("lastNameD")}"
                )
            }
            val adaptador = ArrayAdapter<String>(
                this@Encuesta,
                android.R.layout.simple_spinner_dropdown_item,
                datos
            )

            binding.spNameIntegrante.adapter = adaptador
        }

        val errorFam = Response.ErrorListener { error ->
            Log.e("ERROR", error.message!!)
        }


        val requestFam =
            object : JsonArrayRequest(Request.Method.GET, urlFam, null, listenerFam, errorFam) {
                override fun getHeaders(): MutableMap<String, String> {
                    val hashMap = HashMap<String, String>()
                    hashMap.put(
                        "x-access-token",
                        sharedPreference.getString("token", "#").toString()
                    )
                    return hashMap
                }
            }
        queue.add(requestFam)


        val datoss = listOf("--Seleccionar --", "Mujer", "Hombre")






        binding.iBtnBack.setOnClickListener{
            val intent = Intent(this@Encuesta, LandingPage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)

        }
        //Cambiar de Inicio de Encuesta a primera pregunta
        binding.btnStartEncuesta.setOnClickListener {
            binding.lName.visibility = GONE
            binding.btnSiguiente.visibility = VISIBLE
            binding.btnAnterior.visibility = VISIBLE
            binding.tvId.visibility = VISIBLE
            binding.tvQuestion.visibility = VISIBLE
            binding.tvNameEncuesta.visibility = VISIBLE
            binding.tvNameEncuesta.text = binding.spNameIntegrante.selectedItem.toString()
            viewType(res.getJSONObject(count).getInt("questionType"))
            onIdChanged(count)
        }

        //Avanzar en preguntas de encuesta
        binding.btnSiguiente.setOnClickListener {
            if(count+1 < res.length()) {
                count += 1
                saveSelection()
                viewType(res.getJSONObject(count).getInt("questionType"))
                onIdChanged(count)
            }
        }

        //Regresar en preguntas de encuesta
        binding.btnAnterior.setOnClickListener {
            if (count-1 >= 0) {
                count -= 1
                saveSelection()
                viewType(res.getJSONObject(count).getInt("questionType"))
                onIdChanged(count)
            }
        }
    }

    private fun saveSelection(count: Int) {
        Log.e("SAVESELECTION",":)")
        val sharedPreference = getSharedPreferences("profile", Context.MODE_PRIVATE)
        if(binding.lRadio4.isVisible){
            val sId = binding.rg4.checkedRadioButtonId
            Log.e("R4", sId.toString())
            val answer = findViewById<RadioButton>(sId).text.toString()
            sharedPreference.edit().putString(count.toString(), answer).apply()


        }

        else if(binding.lRadio5.isVisible){
            val sId = binding.rg5.checkedRadioButtonId
            Log.e("R5", sId.toString())
            val answer = findViewById<RadioButton>(sId).text.toString()
            sharedPreference.edit().putString(count.toString(), answer).apply()

        }

        else if(binding.lRadio6.isVisible){
            val sId = binding.rg6.checkedRadioButtonId
            Log.e("R6", sId.toString())
            val answer = findViewById<RadioButton>(sId).text.toString()
            sharedPreference.edit().putString(count.toString(), answer).apply()

        }

        else if(binding.lRadio8.isVisible){
            val sId = binding.rg8.checkedRadioButtonId
            Log.e("R8", sId.toString())
            val answer = findViewById<RadioButton>(sId).text.toString()
            sharedPreference.edit().putString(count.toString(), answer).apply()

        }

       else  if(binding.lTexto.isVisible){
            val answer = binding.tiedResponse.text.toString()
            Log.e("ANSWER", answer)

        }
    }


    private fun onIdChanged(count: Int) {

        val options = mutableListOf<String>()
        val sharedPreference = getSharedPreferences("profile", Context.MODE_PRIVATE)
        println(sharedPreference.all)
        val idQ = res.getJSONObject(count).getString("qOptions")
        val urlOptions = "http://api-vacaciones.us-east-1.elasticbeanstalk.com/api/questionsoptionsByQuesId/$idQ"
        println(urlOptions)

        val listener = Response.Listener<JSONArray> { response ->
            println(response)
            for (i in 0 until response.length()) {
                options.add(
                    response.getJSONObject(i).getString("optionName")
                )
            }
            println(response)
            println(options)

            fillOptions(options)

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

    private fun fillOptions(options: List<String> ) {

        when( options.size) {
            4 -> {
                binding.rb41.text = options[0]
                binding.rb42.text = options[1]
                binding.rb43.text = options[2]
                binding.rb44.text = options[3]
            }
            5 -> {
                binding.rb51.text = options[0]
                binding.rb52.text = options[1]
                binding.rb53.text = options[2]
                binding.rb54.text = options[3]
                binding.rb55.text = options[4]
            }
            6 -> {
                binding.rb61.text = options[0]
                binding.rb62.text = options[1]
                binding.rb63.text = options[2]
                binding.rb64.text = options[3]
                binding.rb65.text = options[4]
                binding.rb66.text = options[5]
            }
            8 -> {
                binding.rb81.text = options[0]
                binding.rb82.text = options[1]
                binding.rb83.text = options[2]
                binding.rb84.text = options[3]
                binding.rb85.text = options[4]
                binding.rb86.text = options[5]
                binding.rb87.text = options[6]
                binding.rb88.text = options[7]
            }
            else ->{
                binding.rb41.text = options[0]
                binding.rb51.text = options[0]
                binding.rb61.text = options[0]
                binding.rb81.text = options[0]
            }

        }
    }


    //Activar layout correspondiente al tipo de pregunta
    private fun viewType(questionType: Int) {
        when (questionType){
            1 -> {
                binding.lTexto.visibility = VISIBLE
                binding.lRadio4.visibility = GONE
                binding.lRadio5.visibility = GONE
                binding.lRadio6.visibility = GONE
                binding.lRadio8.visibility = GONE
                binding.lName.visibility = GONE
            }
            2 -> {
                binding.lTexto.visibility = VISIBLE
                binding.lRadio4.visibility = GONE
                binding.lRadio5.visibility = GONE
                binding.lRadio6.visibility = GONE
                binding.lRadio8.visibility = GONE
                binding.lName.visibility = GONE
            }
            3 -> {
                binding.lTexto.visibility = GONE
                binding.lRadio4.visibility = VISIBLE
                binding.lRadio5.visibility = GONE
                binding.lRadio6.visibility = GONE
                binding.lRadio8.visibility = GONE
                binding.lName.visibility = GONE
            }
            4 -> {
                binding.lTexto.visibility = GONE
                binding.lRadio4.visibility = GONE
                binding.lRadio5.visibility = VISIBLE
                binding.lRadio6.visibility = GONE
                binding.lRadio8.visibility = GONE
                binding.lName.visibility = GONE
            }
            5 -> {
                binding.lTexto.visibility = GONE
                binding.lRadio4.visibility = GONE
                binding.lRadio5.visibility = GONE
                binding.lRadio6.visibility = VISIBLE
                binding.lRadio8.visibility = GONE
                binding.lName.visibility = GONE
            }
            6 -> {
                binding.lTexto.visibility = GONE
                binding.lRadio4.visibility = GONE
                binding.lRadio5.visibility = GONE
                binding.lRadio6.visibility = GONE
                binding.lRadio8.visibility = VISIBLE
                binding.lName.visibility = GONE
            }

            else -> {
                binding.lTexto.visibility = VISIBLE
                binding.lRadio4.visibility = GONE
                binding.lRadio5.visibility = GONE
                binding.lRadio6.visibility = GONE
                binding.lRadio8.visibility = GONE
                binding.lName.visibility = GONE
            }
        }
    }

}