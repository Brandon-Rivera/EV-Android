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
import android.view.View
import android.view.WindowManager.BadTokenException
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var locationManager: LocationManager
    lateinit var mapa: GoogleMap
    //lateinit var nombre:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        val clMain = findViewById<ConstraintLayout>(R.id.clMain)


        val sharedPreference = getSharedPreferences("profile", Context.MODE_PRIVATE)


        requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if(ActivityCompat.checkSelfPermission(this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) //Si el permiso se concedió, se activa el GPS

        /*override fun onLocationChanged(location: Location) {
            //Toast.makeText(this@MainActivity, location.latitude.toString(), Toast.LENGTH_SHORT).show()
            var marcador: LatLng = LatLng(location.latitude, location.longitude)
            if(inicio == true){
                mapa.addMarker(MarkerOptions().position(marcador))
            }
        }*/

        with(sharedPreference){
            val nombre = getString("nombre" ,"#" )
            val password = getString("password", "#")
            if (nombre != null) {
                if (nombre != "#"){
                    TODO()
                }
            }
        }

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
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or
                Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}