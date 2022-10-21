package mx.tec.bamxmorelos

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import mx.tec.bamxmorelos.adapter.CustomAdapter
import mx.tec.bamxmorelos.databinding.ActivityAgregarBinding
import mx.tec.bamxmorelos.databinding.ActivityEditarBinding
import mx.tec.bamxmorelos.model.Elemento
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalDateTime

class Editar : AppCompatActivity() {

    lateinit var binding: ActivityEditarBinding
    lateinit var queue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityEditarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        this.requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        queue = Volley.newRequestQueue(this@Editar)
        val datos = listOf("No especificado", "Mujer", "Hombre")
        pregnancyFocusListener()


        //1. contexto 2. layout 3. datos
        val adaptador = ArrayAdapter<String>(
            this@Editar,
            android.R.layout.simple_list_item_1,
            datos
        )

        binding.spSexoEditar.adapter = adaptador

        val passedName = intent.getStringExtra("name")
        var idFamMember = 0
        var memberId = 0

        val sharedPreference = getSharedPreferences("profile", Context.MODE_PRIVATE)
        val userId = sharedPreference.getString("idUser", "#")
        println(userId)

        val urlDiseaseMember = "http://api-vacaciones.us-east-1.elasticbeanstalk.com/api/memberdisease/$memberId"
        val listenerDiseaseM = Response.Listener<JSONArray> { response ->
            //cb for diseases
            //get id for url

            for (i in 0 until response.length()){
                if (response.getJSONObject(i).getInt("idDisease") == 1) {
                    binding.cbDiabetesEditar.isChecked = true
                }
                else if (response.getJSONObject(i).getInt("idDisease") == 2) {
                    binding.cbHipertensionEditar.isChecked = true
                }
                else if(response.getJSONObject(i).getInt("idDisease") == 3) {
                    binding.cbObesidadEditar.isChecked = true
                }
            }
        }

        val errorDiseaseM = Response.ErrorListener { error ->
            Log.e("ERROR", error.message!!)
        }


        val urlUser = "http://api-vacaciones.us-east-1.elasticbeanstalk.com/api/famMemberByIdUser/$userId"
        val listener = Response.Listener<JSONArray> { response ->
            var name = "Jane Doe"

            for (i in 0 until response.length()) {
                name = "${
                    response.getJSONObject(i).getString("names")
                } ${response.getJSONObject(i).getString("lastNameD")}"
                println(name)

                if (passedName == name) {
                    //println("Matched")
                    idFamMember = response.getJSONObject(i).getString("id").toInt()
                    binding.edtNombreEditar.setText(response.getJSONObject(i).getString("names"))
                    binding.edtAPaternoEditar.setText(response.getJSONObject(i).getString("lastNameD"))
                    binding.edtAMaterno.setText(response.getJSONObject(i).getString("lastNameM"))
                    binding.edtAlturaEditar.setText(response.getJSONObject(i).getString("height"))
                    binding.edtPesoEditar.setText(response.getJSONObject(i).getString("weightV"))
                    binding.spSexoEditar.setSelection(response.getJSONObject(i).getInt("sex"))
                    binding.spBDEditar.setText(response.getJSONObject(i).getString("birthDate"))
                    binding.cbLiderEditar.isChecked =  response.getJSONObject(i).getString("isLeader") == "1"
                    binding.cbPregnantEditar.isChecked =  response.getJSONObject(i).getString("isPregnant") == "1"
                    binding.cbLiderEditar.isChecked =  response.getJSONObject(i).getString("isLeader") == "1"

                    memberId = response.getJSONObject(i).getString("id").toInt()

                    val requestDiseaseM =
                        object : JsonArrayRequest(Request.Method.GET, urlDiseaseMember, null, listenerDiseaseM, errorDiseaseM) {
                            override fun getHeaders(): MutableMap<String, String> {
                                val hashMap = HashMap<String, String>()
                                hashMap.put(
                                    "x-access-token",
                                    sharedPreference.getString("token", "#").toString()
                                )
                                return hashMap
                            }
                        }

                    queue.add(requestDiseaseM)

                }
            }
        }

        val error = Response.ErrorListener { error ->
            Log.e("ERROR", error.message!!)
        }

        val request =
            object : JsonArrayRequest(Request.Method.GET, urlUser, null, listener, error) {
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




        val url = "http://api-vacaciones.us-east-1.elasticbeanstalk.com/api/famMember"

        queue = Volley.newRequestQueue(this@Editar)

        binding.spBDEditar.setOnClickListener {
            showDatePickerDialog()
        }

        binding.iBtnRegresarEditar.setOnClickListener{
            val intent = Intent(this@Editar, LandingPage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.btnEliminarEditar.setOnClickListener {

            var builder = AlertDialog.Builder(this@Editar)
            builder.setTitle("Eliminar")
                .setMessage("¿Desea eliminar al usuario?")
                .setNegativeButton("Cerrar") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Eliminar") { dialog, _ ->
                    dialog.dismiss()
                    LoadingDialog.display(this@Editar)


                    val body = JSONObject()
                    val isPregnant = binding.cbPregnantEditar.isChecked.compareTo(false)
                    val isLeader = binding.cbLiderEditar.isChecked.compareTo(false)
                    val sexo = binding.spSexoEditar.selectedItemId



                    with(body){
                        put("id", idFamMember)
                        put("idUser", sharedPreference.getString("idUser", "#").toString().toInt())
                        put("isLeader", isLeader.toString())
                        put("names", binding.edtNombreEditar.text.toString())
                        put("lastNameD", binding.edtAPaternoEditar.text.toString())
                        put("lastNameM", binding.edtAMaterno.text.toString())
                        put("sex", sexo)
                        put("birthDate", binding.spBDEditar.text.toString())
                        put("weightV", binding.edtPesoEditar.text.toString().toFloat())
                        put("height", binding.edtAlturaEditar.text.toString().toFloat())
                        put("isPregnant", isPregnant.toString())
                        put("isActive", "F")
                    }


                    val listener = Response.Listener<JSONObject> { response ->

                        LoadingDialog.dismiss()
                        Toast.makeText(this@Editar, "Miembro eliminado Exitosamente", Toast.LENGTH_SHORT).show()

                        Log.e("RESPONSE", response.toString())
                        val intent = Intent(this@Editar, LandingPage::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                    }

                    val error = Response.ErrorListener { error ->
                        Log.e("ERROR", error.message!!)

                        //Toast.makeText(this@Agregar, "No se agregó", Toast.LENGTH_SHORT).show()
                        val mySnackbar = Snackbar.make(
                            findViewById(R.id.clAgregar),
                            "No se elimino el usuario",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }

                    val request = object: JsonObjectRequest(Method.PUT, url, body, listener, error){
                        override fun getHeaders(): MutableMap<String, String> {
                            val hashMap = HashMap<String, String>()
                            hashMap.put("x-access-token", sharedPreference.getString("token", "#").toString())
                            return hashMap
                        }
                    }
                    queue.add(request)
                }
                .show()

        }

        binding.btnConfirmarEditar.setOnClickListener{
           /* LoadingDialog.display(this@Editar)
            val body = JSONObject()
            val isPregnant = binding.cbPregnantEditar.isChecked.compareTo(false)
            val isLeader = binding.cbLiderEditar.isChecked.compareTo(false)
            val sexo = binding.spSexoEditar.selectedItemId



            with(body){
                put("id", idFamMember)
                put("idUser", sharedPreference.getString("idUser", "#").toString().toInt())
                put("isLeader", isLeader.toString())
                put("names", binding.edtNombreEditar.text.toString())
                put("lastNameD", binding.edtAPaternoEditar.text.toString())
                put("lastNameM", binding.edtAMaterno.text.toString())
                put("sex", sexo)
                put("birthDate", binding.spBDEditar.text.toString())
                put("weightV", binding.edtPesoEditar.text.toString().toFloat())
                put("height", binding.edtAlturaEditar.text.toString().toFloat())
                put("isPregnant", isPregnant.toString())
                put("isActive", "T")
            }
            println(body)

            val listener = Response.Listener<JSONObject> { response ->

                LoadingDialog.dismiss()
                Toast.makeText(this@Editar, "Cambios Guardados Exitosamente", Toast.LENGTH_SHORT).show()

                Log.e("RESPONSE", response.toString())
                val intent = Intent(this@Editar, LandingPage::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }

            val error = Response.ErrorListener { error ->
                Log.e("ERROR", error.message!!)

                //Toast.makeText(this@Agregar, "No se agregó", Toast.LENGTH_SHORT).show()
                val mySnackbar = Snackbar.make(
                    findViewById(R.id.clAgregar),
                    "No se guardaron los cambios",
                    Snackbar.LENGTH_SHORT
                ).show()
            }

            val request = object: JsonObjectRequest(Method.PUT, url, body, listener, error){
                override fun getHeaders(): MutableMap<String, String> {
                    val hashMap = HashMap<String, String>()
                    hashMap.put("x-access-token", sharedPreference.getString("token", "#").toString())
                    return hashMap
                }
            }
            queue.add(request) */





            LoadingDialog.display(this@Editar)
            val body = JSONObject()
            val isPregnant = binding.cbPregnantEditar.isChecked.compareTo(false)
            val isLeader = binding.cbLiderEditar.isChecked.compareTo(false)
            val sexo = binding.spSexoEditar.selectedItemId
            val bodyDisease = JSONArray()



            with(body){
                put("idUser", sharedPreference.getString("idUser", "#").toString().toInt())
                put("isLeader", isLeader)
                put("names", binding.edtNombreEditar.text.toString())
                put("lastNameD", binding.edtAPaternoEditar.text.toString())
                put("lastNameM", binding.edtAMaterno.text.toString())
                put("sex", sexo)
                put("birthDate", binding.spBDEditar.text.toString())
                put("weightV", binding.edtPesoEditar.text.toString().toFloat())
                put("height", binding.edtAlturaEditar.text.toString().toFloat())
                put("isPregnant", isPregnant)

            }
            println("Body General")
            println(body)

            val urlDisease = "http://api-vacaciones.us-east-1.elasticbeanstalk.com/api/memberdiseasedos"
            val listenerDisease = Response.Listener<JSONArray> { response ->

                LoadingDialog.dismiss()
                Toast.makeText(this@Editar, "Agregado Exitosamente", Toast.LENGTH_SHORT).show() //verificar

                Log.e("RESPONSE", response.toString())
                val intent = Intent(this@Editar, LandingPage::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }

            val errorDisease = Response.ErrorListener { error ->
                Log.e("ERRORDisease", error.message!!)
                LoadingDialog.dismiss()
                Toast.makeText(this@Editar, "No se modificó", Toast.LENGTH_SHORT).show()
                /*val mySnackbar = Snackbar.make(
                    findViewById(R.id.clAgregar),
                    "No se pudo agregar miembro",
                    Snackbar.LENGTH_SHORT
                ).show()*/
            }


            val userId = sharedPreference.getString("idUser", "#")
            val urlUser = "http://api-vacaciones.us-east-1.elasticbeanstalk.com/api/famMemberByIdUser/$userId"
            val listenerUser = Response.Listener<JSONArray> { response ->
                val idFamMember = response.getJSONObject(response.length()-1).getString("id")

                if (binding.cbDiabetesEditar.isChecked){
                    val dis = JSONObject()
                    dis.put("idMember", idFamMember)
                    dis.put("idDisease", 1)
                    bodyDisease.put(dis)
                }

                if (binding.cbHipertensionEditar.isChecked){
                    val dis = JSONObject()
                    dis.put("idMember", idFamMember)
                    dis.put("idDisease", 2)
                    bodyDisease.put(dis)
                }
                if (binding.cbObesidadEditar.isChecked){
                    val dis = JSONObject()
                    dis.put("idMember", idFamMember)
                    dis.put("idDisease", 3)
                    bodyDisease.put(dis)
                }

                Log.e("Body Disease", bodyDisease.toString())

                val requestDisease = object: JsonArrayRequest(Method.PUT, urlDisease, bodyDisease, listenerDisease, errorDisease){
                    override fun getHeaders(): MutableMap<String, String> {
                        val hashMap = HashMap<String, String>()
                        hashMap.put("x-access-token", sharedPreference.getString("token", "#").toString())
                        return hashMap
                    }
                }
                queue.add(requestDisease)
            }

            val errorUser = Response.ErrorListener { error ->
                Log.e("ERROR", error.message!!)
            }


            val url = "http://api-vacaciones.us-east-1.elasticbeanstalk.com/api/famMember"
            val listener = Response.Listener<JSONObject> { response ->

                val requestUser =
                    object : JsonArrayRequest(Request.Method.GET, urlUser, null, listenerUser, errorUser) {
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

            val error = Response.ErrorListener { error ->
                Log.e("ERROR", error.message!!)
            }

            val request = object: JsonObjectRequest(Method.PUT, url, body, listener, error){
                override fun getHeaders(): MutableMap<String, String> {
                    val hashMap = HashMap<String, String>()
                    hashMap.put("x-access-token", sharedPreference.getString("token", "#").toString())
                    return hashMap
                }
            }
            queue.add(request)
        }

        binding.spSexoEditar.onItemSelectedListener
    }


    private fun pregnancyFocusListener() {
        binding.spSexoEditar.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                if (binding.spSexoEditar.selectedItem == "Mujer") {
                    binding.cbPregnantEditar.visibility = View.VISIBLE
                }
                else {
                    binding.cbPregnantEditar.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun showDatePickerDialog(){
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(supportFragmentManager, "datePicker")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        binding.spBDEditar.setText("$year-${month+1}-$day")
    }
}