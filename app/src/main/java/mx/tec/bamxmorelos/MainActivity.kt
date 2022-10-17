package mx.tec.bamxmorelos

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager.BadTokenException
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONObject

class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var locationManager: LocationManager
    lateinit var mapa: GoogleMap
    //lateinit var nombre:String
    lateinit var queue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        this.requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val clMain = findViewById<ConstraintLayout>(R.id.clMain)



        val sharedPreference = getSharedPreferences("profile", Context.MODE_PRIVATE)
        queue = Volley.newRequestQueue(this@MainActivity)

        requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if(ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){} //Si el permiso se concedió, se activa el GPS

        /*override fun onLocationChanged(location: Location) {
            //Toast.makeText(this@MainActivity, location.latitude.toString(), Toast.LENGTH_SHORT).show()
            var marcador: LatLng = LatLng(location.latitude, location.longitude)
            if(inicio == true){
                mapa.addMarker(MarkerOptions().position(marcador))
            }
        }*/


            LoadingDialog.display(this@MainActivity)
            val password = sharedPreference.getString("password", "#")
            val nombre = sharedPreference.getString("user", "#")

            val body = JSONObject()
            with(body) {
                put("userName", nombre)
                put("userPassword", password)
            }

            val url = "http://api-vacaciones.us-east-1.elasticbeanstalk.com/api/login"

            Log.e("URL", body.toString())

            val listener = Response.Listener<JSONObject> { response ->
                Log.e("RESPONSE", response.toString())
                if (response.get("mensaje") == "Usuario autenticado") {

                    LoadingDialog.dismiss()

                    sharedPreference.edit().putString("token", response.get("token").toString()).apply()

                    val intent = Intent(this@MainActivity, LandingPage::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                }
                LoadingDialog.dismiss()
            }
            val error = Response.ErrorListener { error ->
                Log.e("ERROR", error.message!!)

            }

            val request =
                JsonObjectRequest(Request.Method.POST, url, body, listener, error)
            queue.add(request)



        clMain.setOnClickListener(this)
    }

    val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            isGranted ->
        val message = when {
            isGranted -> "Permiso concedido"
            Build.VERSION.SDK_INT > Build.VERSION_CODES.M &&
                    shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) ->
                "Explicación"
            else -> "Permiso denegado"
        }
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View?){
        val intent = Intent(this@MainActivity, Login::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}