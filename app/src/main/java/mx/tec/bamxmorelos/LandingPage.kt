package mx.tec.bamxmorelos

import android.Manifest
import android.content.Context
import android.content.Intent
import android.icu.text.IDNA.Info
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.bottomnavigation.BottomNavigationView

class LandingPage : AppCompatActivity(), LocationListener {

    lateinit var locationManager: LocationManager
    lateinit var mapa: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)
        supportActionBar?.hide()

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val landingFragment = LandingFragment()
        val infoFragment = InfoFragment()
        val familyFragment = FamilyFragment()
        replaceFragment(landingFragment)



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
        TODO("Not yet implemented")
    }

}