package mx.tec.bamxmorelos

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.*
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import mx.tec.bamxmorelos.databinding.ActivityEncuestaBinding
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDateTime

class Encuesta : AppCompatActivity(), LocationListener {
    lateinit var binding: ActivityEncuestaBinding
    lateinit var queue: RequestQueue
    lateinit var locationManager: LocationManager
    var res = JSONArray()
    var lat = 0.0
    var lon = 0.0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityEncuestaBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        this.requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager


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

        //Get de preguntas de encuesta
        val listener = Response.Listener<JSONArray> { response ->
            res = response
            Log.e("RESPONSE", response.toString())
        }

        val error = Response.ErrorListener { error ->
            Log.e("RequestError", error.toString()!!)
        }

        val request =
            object : JsonArrayRequest(Method.GET, url, null, listener, error) {
                override fun getHeaders(): MutableMap<String, String> {
                    val hashMap = HashMap<String, String>()
                    hashMap.put(
                        "x-access-token",
                        sharedPreference.getString("token", "#").toString()
                    )
                    //println(sharedPreference.getString("token", "#").toString())
                    return hashMap
                }
            }
        queue.add(request)


        val datos = mutableListOf<String>()
        val idUser = sharedPreference.getString("idUser", "#")
        val urlFam =
            "http://api-vacaciones.us-east-1.elasticbeanstalk.com/api/famMemberByIdUser/$idUser"

        //Get de famMembers por id
        //generar la lista para el spinner de miembros
        val listenerFam = Response.Listener<JSONArray> { response ->
            for (i in 0 until response.length()) {
                if (response.getJSONObject(i).getString("isActive") == "T") {
                    datos.add(
                        "${response.getJSONObject(i).getString("names")} ${
                            response.getJSONObject(i).getString("lastNameD")
                        }"
                    )
                }
            }
            val adaptador = ArrayAdapter<String>(
                this@Encuesta,
                android.R.layout.simple_spinner_dropdown_item,
                datos
            )

            binding.spNameIntegrante.adapter = adaptador
        }

        val errorFam = Response.ErrorListener { errorFam ->
            Log.e("ERRORFam", errorFam.message!!)
        }


        val requestFam =
            object : JsonArrayRequest(Method.GET, urlFam, null, listenerFam, errorFam) {
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

        binding.btnBackEncuesta.setOnClickListener {
            val intent = Intent(this@Encuesta, LandingPage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)

        }

        //Cambiar de Inicio de Encuesta a primera pregunta
        //post de ubicacion de inicio encuesta
        binding.btnStartEncuesta.setOnClickListener {

            if (ActivityCompat.checkSelfPermission(
                    this@Encuesta,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                //saveSelection(count)

                val body = JSONObject()
                with(body) {
                    put("idUser", sharedPreference.getString("idUser", "#")?.toInt())
                    put("placeName", "")
                    put("lat", lat.toFloat())
                    put("lon", lon.toFloat())
                    put("street", "")
                    put("extNum", "")
                    put("intNum", "")
                    put("suburb", "")
                    put("postalCode", "")
                    put("city", "")
                    put("stateN", "")
                }

                print("Ubicacion ")
                println(body)

                val listenerUbi = Response.Listener<JSONObject> { response ->

                    Toast.makeText(this@Encuesta, "Ubicación registrada", Toast.LENGTH_SHORT).show()
                    //Toast.makeText(this@Encuesta, response.toString(), Toast.LENGTH_SHORT).show()
                    Log.e("RESPONSE", response.toString())

                }

                val errorUbi = Response.ErrorListener { error ->
                    Log.e("ERROR", error.message!!)

                    Toast.makeText(this@Encuesta, "Ubicacion no registrada",
                        Toast.LENGTH_SHORT).show()
                }

                //post ubicacion de usuario
                val url = "http://api-vacaciones.us-east-1.elasticbeanstalk.com/api/slocation"
                val requestUbi =
                    object : JsonObjectRequest(Method.POST, url, body, listenerUbi, errorUbi) {
                        override fun getHeaders(): MutableMap<String, String> {
                            val hashMap = HashMap<String, String>()
                            hashMap.put(
                                "x-access-token",
                                sharedPreference.getString("token", "#").toString()
                            )
                            return hashMap
                        }
                    }

                //binding.lName.visibility = GONE
                binding.btnSiguiente.visibility = VISIBLE
                binding.btnAnterior.visibility = VISIBLE
                binding.tvId.visibility = VISIBLE
                binding.tvQuestion.visibility = VISIBLE
                binding.tvNameEncuesta.visibility = VISIBLE
                binding.tvNameEncuesta.text = binding.spNameIntegrante.selectedItem.toString()
                queue.add(requestUbi)
                viewType(res.getJSONObject(count).getInt("questionType"))
                onIdChanged(count)

            } else {
                Toast.makeText(this@Encuesta, "Se necesita acceso a GPS", Toast.LENGTH_SHORT).show()
            }
        }

        //Avanzar en preguntas de encuesta
        binding.btnSiguiente.setOnClickListener {

            if (count + 1 < res.length()) { // <-- regresar a res.lenght()
                saveSelection(count)
                count += 1

                //verificar si es fin de encuesta para submit
                if (count + 1 == 34) { // <-- regresar a res.length()
                    binding.btnSiguiente.visibility = INVISIBLE
                    binding.btnSubmit.visibility = VISIBLE
                } else {
                    binding.btnSiguiente.visibility = VISIBLE
                    binding.btnSubmit.visibility = GONE
                }

                viewType(res.getJSONObject(count).getInt("questionType"))
                onIdChanged(count)
            }
        }

        //Regresar en preguntas de encuesta
        binding.btnAnterior.setOnClickListener {

            if (count - 1 > 0) {
                saveSelection(count)
                count -= 1

                viewType(res.getJSONObject(count).getInt("questionType"))
                onIdChanged(count)
            }
        }

        binding.btnSubmit.setOnClickListener {
            saveSelection(count)
            println("Submiting " + count.toString())

            val answers = JSONArray()
            val timeAnswered = LocalDateTime.now()
            val userId = sharedPreference.getString("idUser", "#")?.toInt()
            var idTemp =
                (userId.toString() + timeAnswered.year.toString() + timeAnswered.monthValue.toString() + timeAnswered.dayOfMonth.toString()).toInt()


            val urlSubmit =
                "http://api-vacaciones.us-east-1.elasticbeanstalk.com/api/subquestionanswer"
            val listenerSubmit = Response.Listener<JSONArray> { response ->
                Log.e("RESPONSE", response.toString())
                /*val mySnackbar = Snackbar.make(
                    findViewById(mx.tec.bamxmorelos.R.id.clEncuesta),
                    "Enviada exitosamente",
                    Snackbar.LENGTH_SHORT
                ).show() */
                Toast.makeText(this@Encuesta, "Enviada exitosamente", Toast.LENGTH_SHORT).show()

                for (i in 0 until res.length()) {
                    with(sharedPreference.edit()) {
                        remove(i.toString())
                        apply()
                    }
                }

                val intent = Intent(this@Encuesta, LandingPage::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }

            val errorSubmit = Response.ErrorListener { error ->
                Log.e("ERRORSubmit", error.message!!.toString())
                val mySnackbar = Snackbar.make(
                    findViewById(mx.tec.bamxmorelos.R.id.clEncuesta),
                    "Envío fallido",
                    Snackbar.LENGTH_SHORT
                ).show()
            }


            val urlUser =
                "http://api-vacaciones.us-east-1.elasticbeanstalk.com/api/famMemberByIdUser/$userId"
            //println(urlUser)
            val listenerUser = Response.Listener<JSONArray> { response ->
                println("users List")
                println(response.toString())
                var name = "Jane Doe"
                for (i in 0 until response.length()) {
                    name = "${
                        response.getJSONObject(i).getString("names")
                    } ${response.getJSONObject(i).getString("lastNameD")}"
                    println(name)
                    println(count)
                    if (binding.tvNameEncuesta.text.toString() == name) {

                        for (j in 1 until count + 2) {
                            val answer = JSONObject()
                            val opt = sharedPreference.getString(j.toString(), "#")

                            answer.put("id", idTemp)
                            answer.put("idQuestion", j)
                            answer.put("idUser", userId)
                            answer.put("idMember", response.getJSONObject(i).getInt("id"))
                            answer.put("timeAnswered", timeAnswered)
                            answer.put("idRow", 0)
                            answer.put("answer", opt)
                            println("answer")
                            println(answer)
                            answers.put(answer)

                            //sharedPreference.edit().remove(i.toString()).apply()
                        }
                        println("en" + answers)

                    }
                }

                val requestSubmit =
                    object : JsonArrayRequest(
                        Method.POST,
                        urlSubmit,
                        answers,
                        listenerSubmit,
                        errorSubmit
                    ) {
                        override fun getHeaders(): MutableMap<String, String> {
                            val hashMap = HashMap<String, String>()
                            hashMap.put(
                                "x-access-token",
                                sharedPreference.getString("token", "#").toString()
                            )
                            return hashMap
                        }
                    }

                queue.add(requestSubmit)

            }

            val errorUser = Response.ErrorListener { error ->
                Log.e("ERRORUser", error.message!!)

            }

            val requestUser =
                object : JsonArrayRequest(
                    Method.GET,
                    urlUser,
                    null,
                    listenerUser,
                    errorUser
                ) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val hashMap = HashMap<String, String>()
                        hashMap.put(
                            "x-access-token",
                            sharedPreference.getString("token", "#").toString()
                        )
                        return hashMap
                    }
                }
            queue.add(requestUser)
        }
    }


    private fun saveSelection(count: Int) {
        Log.e("SAVESELECTION", ":)")
        val sharedPreference = getSharedPreferences("profile", Context.MODE_PRIVATE)
        if (binding.lRadio4.isVisible) {
            val sId = binding.rg4.checkedRadioButtonId
            Log.e("R4", sId.toString())
            Log.e("Count", count.toString())
            val answer = findViewById<RadioButton>(sId).text.toString()
            sharedPreference.edit().putString((count + 1).toString(), answer).apply()
            binding.rg4.clearCheck()
        } else if (binding.lRadio5.isVisible) {
            val sId = binding.rg5.checkedRadioButtonId
            Log.e("R5", sId.toString())
            Log.e("Count", count.toString())
            val answer = findViewById<RadioButton>(sId).text.toString()
            sharedPreference.edit().putString((count + 1).toString(), answer).apply()
            binding.rg4.clearCheck()

        } else if (binding.lRadio6.isVisible) {
            val sId = binding.rg6.checkedRadioButtonId
            Log.e("R6", sId.toString())
            Log.e("Count", count.toString())
            val answer = findViewById<RadioButton>(sId).text.toString()
            sharedPreference.edit().putString((count + 1).toString(), answer).apply()
            binding.rg4.clearCheck()

        } else if (binding.lRadio8.isVisible) {
            val sId = binding.rg8.checkedRadioButtonId
            Log.e("R8", sId.toString())
            Log.e("Count", count.toString())
            val answer = findViewById<RadioButton>(sId).text.toString()
            sharedPreference.edit().putString((count + 1).toString(), answer).apply()
            binding.rg4.clearCheck()

        } else if (binding.lTexto.isVisible) {
            val answer = binding.tiedResponse.text.toString()
            sharedPreference.edit().putString((count + 1).toString(), answer).apply()
            Log.e("ANSWER", answer)
            Log.e("Count", count.toString())
            binding.tiedResponse.text?.clear()

        } else if (binding.lDisease.isVisible) {
            val answer = binding.tiedDisease.text.toString()
            sharedPreference.edit().putString((count + 1).toString(), answer).apply()
            Log.e("ANSWER", answer)
            Log.e("Count", count.toString())
            binding.tiedDisease.text?.clear()
        }
    }


    private fun onIdChanged(count: Int) {

        val options = mutableListOf<String>()
        val sharedPreference = getSharedPreferences("profile", Context.MODE_PRIVATE)
        //println(sharedPreference.all)
        val idQ = res.getJSONObject(count).getString("qOptions")
        val urlOptions =
            "http://api-vacaciones.us-east-1.elasticbeanstalk.com/api/questionsoptionsByQuesId/$idQ"
        //println(urlOptions)

        val listener = Response.Listener<JSONArray> { response ->
            println("response question options by id")
            println(response)
            for (i in 0 until response.length()) {
                options.add(
                    response.getJSONObject(i).getString("optionName")
                )
            }
            fillOptions(options)

        }

        val error = Response.ErrorListener { error ->
            Log.e("RequestError", error.toString()!!)
        }

        val requestOptions =
            object : JsonArrayRequest(Method.GET, urlOptions, null, listener, error) {
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

    private fun fillOptions(options: List<String>) {

        when (options.size) {
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
            else -> {
                binding.rb41.text = options[0]
                binding.rb51.text = options[0]
                binding.rb61.text = options[0]
                binding.rb81.text = options[0]
            }

        }
    }

    //Activar layout correspondiente al tipo de pregunta
    private fun viewType(questionType: Int) {
        when (questionType) {
            1 -> {
                binding.lTexto.visibility = VISIBLE
                binding.lRadio4.visibility = GONE
                binding.lRadio5.visibility = GONE
                binding.lRadio6.visibility = GONE
                binding.lRadio8.visibility = GONE
                binding.lName.visibility = GONE
                binding.lDisease.visibility = GONE
            }
            2 -> {
                binding.lTexto.visibility = GONE
                binding.lRadio4.visibility = GONE
                binding.lRadio5.visibility = GONE
                binding.lRadio6.visibility = GONE
                binding.lRadio8.visibility = GONE
                binding.lName.visibility = GONE
                binding.lDisease.visibility = VISIBLE
            }
            3 -> {
                binding.lTexto.visibility = GONE
                binding.lRadio4.visibility = VISIBLE
                binding.lRadio5.visibility = GONE
                binding.lRadio6.visibility = GONE
                binding.lRadio8.visibility = GONE
                binding.lName.visibility = GONE
                binding.lDisease.visibility = GONE
            }
            4 -> {
                binding.lTexto.visibility = GONE
                binding.lRadio4.visibility = GONE
                binding.lRadio5.visibility = VISIBLE
                binding.lRadio6.visibility = GONE
                binding.lRadio8.visibility = GONE
                binding.lName.visibility = GONE
                binding.lDisease.visibility = GONE
            }
            5 -> {
                binding.lTexto.visibility = GONE
                binding.lRadio4.visibility = GONE
                binding.lRadio5.visibility = GONE
                binding.lRadio6.visibility = VISIBLE
                binding.lRadio8.visibility = GONE
                binding.lName.visibility = GONE
                binding.lDisease.visibility = GONE
            }
            6 -> {
                binding.lTexto.visibility = GONE
                binding.lRadio4.visibility = GONE
                binding.lRadio5.visibility = GONE
                binding.lRadio6.visibility = GONE
                binding.lRadio8.visibility = VISIBLE
                binding.lName.visibility = GONE
                binding.lDisease.visibility = GONE
            }
            8 -> {
                binding.lTexto.visibility = GONE
                binding.lRadio4.visibility = GONE
                binding.lRadio5.visibility = GONE
                binding.lRadio6.visibility = GONE
                binding.lRadio8.visibility = GONE
                binding.lName.visibility = GONE
                binding.lDisease.visibility = VISIBLE
            }

            else -> {
                binding.lTexto.visibility = VISIBLE
                binding.lRadio4.visibility = GONE
                binding.lRadio5.visibility = GONE
                binding.lRadio6.visibility = GONE
                binding.lRadio8.visibility = GONE
                binding.lName.visibility = GONE
                binding.lDisease.visibility = GONE
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        //Toast.makeText(this@Encuesta, location.latitude.toString(), Toast.LENGTH_SHORT).show()
        lat = location.latitude
        lon = location.longitude
    }
}