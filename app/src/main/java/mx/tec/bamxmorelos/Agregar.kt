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
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import mx.tec.bamxmorelos.databinding.ActivityAgregarBinding
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

        val datos = listOf("No especificado", "Mujer", "Hombre")


        //1. contexto 2. layout 3. datos
        val adaptador = ArrayAdapter<String>(
            this@Agregar,
            android.R.layout.simple_list_item_1,
            datos
        )

        binding.spSexoAgregar.adapter = adaptador

        val url = "http://api-vacaciones.us-east-1.elasticbeanstalk.com/api/famMember"

        queue = Volley.newRequestQueue(this@Agregar)

        binding.edtDate.setOnClickListener {
            showDatePickerDialog()
        }

        val sharedPreference = getSharedPreferences("profile", Context.MODE_PRIVATE)

        binding.iBtnBackAgregar.setOnClickListener{
            val intent = Intent(this@Agregar, LandingPage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or
            Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        binding.btnAgregar.setOnClickListener{
            val body = JSONObject()
            val isPregnant = binding.cbPregnantAgregar.isChecked.compareTo(false)
            val isLeader = binding.cbLiderAgregar.isChecked.compareTo(false)
            val sexo = binding.spSexoAgregar.selectedItemId



            with(body){
                put("idUser", sharedPreference.getString("idUser", "#").toString().toInt())
                put("isLeader", isLeader)
                put("names", binding.edtNombresAgregar.text.toString())
                put("lastNameD", binding.edtAPaternoAgegar.text.toString())
                put("lastNameM", binding.edtAMaternoAgregar.text.toString())
                put("sex", sexo)
                put("birthDate", binding.edtDate.text.toString())
                put("weightV", binding.edtPesoAgregar.text.toString().toFloat())
                put("height", binding.edtPesoAgregar.text.toString().toFloat())
                put("isPregnant", isPregnant)
            }
            println(body)

            val listener = Response.Listener<JSONObject> { response ->

                Toast.makeText(this@Agregar, "Agregado Exitosamente", Toast.LENGTH_SHORT).show()
                Log.e("RESPONSE", response.toString())
                val intent = Intent(this@Agregar, LandingPage::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }

            val error = Response.ErrorListener { error ->
                Log.e("ERROR", error.message!!)

                Toast.makeText(this@Agregar, "No se agregó", Toast.LENGTH_SHORT).show()
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
        binding.cbPregnantAgregar.isVisible = binding.spSexoAgregar.selectedItem!= "Hombre"
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    private fun showDatePickerDialog(){
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(supportFragmentManager, "datePicker")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        binding.edtDate.setText("$year-${month+1}-$day")
    }
}