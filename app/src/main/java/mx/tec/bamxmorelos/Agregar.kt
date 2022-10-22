package mx.tec.bamxmorelos

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import mx.tec.bamxmorelos.databinding.ActivityAgregarBinding
import org.json.JSONArray
import org.json.JSONObject
import java.sql.Date

class Agregar : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    lateinit var binding: ActivityAgregarBinding
    lateinit var queue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityAgregarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        this.requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        queue = Volley.newRequestQueue(this@Agregar)
        val sharedPreference = getSharedPreferences("profile", Context.MODE_PRIVATE)

        val datos = listOf("No especificado", "Mujer", "Hombre")
        pregnancyFocusListener()


        //1. contexto 2. layout 3. datos
        val adaptador = ArrayAdapter<String>(
            this@Agregar,
            android.R.layout.simple_list_item_1,
            datos
        )

        binding.spSexoAgregar.adapter = adaptador

        binding.edtDate.setOnClickListener {
            showDatePickerDialog()
        }


        binding.iBtnBackAgregar.setOnClickListener{
            val intent = Intent(this@Agregar, LandingPage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.btnAgregar.setOnClickListener{
            LoadingDialog.display(this@Agregar)
            val body = JSONObject()
            val isPregnant = binding.cbPregnantAgregar.isChecked.compareTo(false)
            val isLeader = binding.cbLiderAgregar.isChecked.compareTo(false)
            val sexo = binding.spSexoAgregar.selectedItemId
            val bodyDisease = JSONArray()



            with(body){
                put("idUser", sharedPreference.getString("idUser", "#").toString().toInt())
                put("isLeader", isLeader)
                put("names", binding.edtNombresAgregar.text.toString())
                put("lastNameD", binding.edtAPaternoAgegar.text.toString())
                put("lastNameM", binding.edtAMaternoAgregar.text.toString())
                put("sex", sexo)
                put("birthDate", binding.edtDate.text.toString())
                put("weightV", binding.edtPesoAgregar.text.toString().toFloat())
                put("height", binding.edtAlturaAgregar.text.toString().toFloat())
                put("isPregnant", isPregnant)

            }
            println("Body General")
            println(body)

            val urlDisease = "http://api-vacaciones.us-east-1.elasticbeanstalk.com/api/memberdiseasedos"
            val listenerDisease = Response.Listener<JSONArray> { response ->

                LoadingDialog.dismiss()
                Toast.makeText(this@Agregar, "Agregado Exitosamente", Toast.LENGTH_SHORT).show() //verificar

                Log.e("RESPONSE", response.toString())
                val intent = Intent(this@Agregar, LandingPage::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }

            val errorDisease = Response.ErrorListener { error ->
                //Log.e("ERRORDisease", error.message!!)
                if(error != null) {
                    Log.e("ERROR", error.message!!)
                }
                LoadingDialog.dismiss()
                //Toast.makeText(this@Agregar, "No se agregó", Toast.LENGTH_SHORT).show()
                val mySnackbar = Snackbar.make(
                    findViewById(R.id.clAgregar),
                    "No se pudo agregar miembro",
                    Snackbar.LENGTH_SHORT
                ).show()
            }


            val userId = sharedPreference.getString("idUser", "#")
            val urlUser = "http://api-vacaciones.us-east-1.elasticbeanstalk.com/api/famMemberByIdUser/$userId"
            val listenerUser = Response.Listener<JSONArray> { response ->
                val idFamMember = response.getJSONObject(response.length()-1).getString("id")

                if (binding.cbDiabetes.isChecked){
                    val dis = JSONObject()
                    dis.put("idMember", idFamMember)
                    dis.put("idDisease", 1)
                    bodyDisease.put(dis)
                }

                if (binding.cbHipertension.isChecked){
                    val dis = JSONObject()
                    dis.put("idMember", idFamMember)
                    dis.put("idDisease", 2)
                    bodyDisease.put(dis)
                }
                if (binding.cbObesidad.isChecked){
                    val dis = JSONObject()
                    dis.put("idMember", idFamMember)
                    dis.put("idDisease", 3)
                    bodyDisease.put(dis)
                }

                Log.e("Body Disease", bodyDisease.toString())

                val requestDisease = object: JsonArrayRequest(Method.POST, urlDisease, bodyDisease, listenerDisease, errorDisease){
                    override fun getHeaders(): MutableMap<String, String> {
                        val hashMap = HashMap<String, String>()
                        hashMap.put("x-access-token", sharedPreference.getString("token", "#").toString())
                        return hashMap
                    }
                }
                queue.add(requestDisease)
            }

            val errorUser = Response.ErrorListener { error ->
                //Log.e("ERROR", error.message!!)
                if(error != null) {
                    Log.e("ERROR", error.message!!)
                }
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
                //Log.e("ERROR", error.message!!)
                if(error != null) {
                    Log.e("ERROR", error.message!!)
                }
            }

            val request = object: JsonObjectRequest(Method.POST, url, body, listener, error){
                override fun getHeaders(): MutableMap<String, String> {
                    val hashMap = HashMap<String, String>()
                    hashMap.put("x-access-token", sharedPreference.getString("token", "#").toString())
                    return hashMap
                }
            }
            queue.add(request)
        }

        binding.spSexoAgregar.onItemSelectedListener
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        binding.cbPregnantAgregar.visibility = View.VISIBLE
    }

    private fun pregnancyFocusListener() {
        binding.spSexoAgregar.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                if (binding.spSexoAgregar.selectedItem == "Mujer") {
                    binding.cbPregnantAgregar.visibility = View.VISIBLE
                }
                else {
                    binding.cbPregnantAgregar.visibility = View.INVISIBLE
                }
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        binding.cbPregnantAgregar.visibility = View.VISIBLE

    }

    private fun showDatePickerDialog(){
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(supportFragmentManager, "datePicker")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        binding.edtDate.setText("$year-${month+1}-$day")
    }
}