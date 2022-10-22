package mx.tec.bamxmorelos

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.icu.text.IDNA.Info
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.bottomnavigation.BottomNavigationView
import mx.tec.bamxmorelos.adapter.CustomAdapter
import mx.tec.bamxmorelos.model.Elemento
import org.json.JSONArray
import java.time.LocalDate
import java.time.LocalDateTime

class LandingPage : AppCompatActivity(), LocationListener {

    lateinit var queue: RequestQueue
    lateinit var locationManager: LocationManager
    lateinit var mapa: GoogleMap
    var lat = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)
        supportActionBar?.hide()


        this.requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        queue = Volley.newRequestQueue(this@LandingPage)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val landingFragment = LandingFragment()
        val infoFragment = InfoFragment()
        val familyFragment = FamilyFragment()
        replaceFragment(landingFragment)


        if (ActivityCompat.checkSelfPermission(this@LandingPage,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000,
                5f,
                this@LandingPage
            )
        }


        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.selectedItemId = R.id.miHome


        bottomNav.setOnItemSelectedListener{
            when(it.itemId){
                R.id.miHome ->{replaceFragment(landingFragment) }
                R.id.miInfo->{replaceFragment(infoFragment) }
                R.id.miFamily->{replaceFragment(familyFragment) }
            }
            true
        }


        val sharedPreference = getSharedPreferences("profile", Context.MODE_PRIVATE)
        val userId = sharedPreference.getString("idUser", "#")

        val url = "http://api-vacaciones.us-east-1.elasticbeanstalk.com/api/famMemberByIdUser/$userId"
        val listener = Response.Listener<JSONArray> { response ->
            if (response.toString() == "Token inválido"){
                val intent = Intent(this@LandingPage, Login::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }

        }

        val error = Response.ErrorListener { error ->
            //Log.e("ERROR", error.message!!)
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

    }
    private fun replaceFragment(fragment: Fragment){
        if(fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.flFragment, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    override fun onLocationChanged(location: Location) {
        lat = location.latitude
    }

}