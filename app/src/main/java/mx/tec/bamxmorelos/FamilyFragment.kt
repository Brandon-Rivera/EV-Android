package mx.tec.bamxmorelos

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import mx.tec.bamxmorelos.adapter.CustomAdapter
import mx.tec.bamxmorelos.model.Elemento
import org.json.JSONArray
import java.time.LocalDate
import java.time.LocalDateTime

class FamilyFragment : Fragment(R.layout.fragment_family){
    lateinit var queue: RequestQueue

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        queue = Volley.newRequestQueue(context)
        val view = layoutInflater.inflate(R.layout.fragment_family, container, false)
        val fab = view.findViewById<FloatingActionButton>(R.id.fabAgregarFamilia)
        val sharedPreference = this.requireActivity().getSharedPreferences("profile", Context.MODE_PRIVATE)
        val userId = sharedPreference.getString("idUser", "#")
        println(userId)

        val url = "http://api-vacaciones.us-east-1.elasticbeanstalk.com/api/famMemberByIdUser/$userId"
        val listener = Response.Listener<JSONArray> { response ->

            println(response.toString())

            var datos = mutableListOf<Elemento>()
            for (i in 0 until response.length()) {
                if (response.getJSONObject(i).getString("isActive") == "T"){
                val date = response.getJSONObject(i).getString("birthDate").subSequence(0, response.getJSONObject(i).getString("birthDate").length-1)
                println(date)
                val dob = LocalDateTime.parse(date)
                val today = LocalDate.now()
                var age = today.year - dob.year
                if (today.dayOfYear < dob.dayOfYear)
                    age--
                datos.add(
                    Elemento(
                        response.getJSONObject(i).getString("names") + " " +
                                response.getJSONObject(i).getString("lastNameD"),
                                age,
                        R.mipmap.ic_launcher_round
                    )
                )
                }
            }

            val rcView = view.findViewById<RecyclerView>(R.id.rcvMiembrosFamilia)
            rcView.apply {
                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = CustomAdapter(context, R.layout.layout_elemento, datos)

                setHasFixedSize(false)
            }
        }

        val error = Response.ErrorListener { error ->
            if(error != null) {
                Log.e("ERROR", error.message!!)
            }
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

        fab.setOnClickListener {

            val intent = Intent(context, Agregar::class.java)
            Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)

        }
            return view
    }
}