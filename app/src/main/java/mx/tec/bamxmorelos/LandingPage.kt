package mx.tec.bamxmorelos

import android.content.Intent
import android.icu.text.IDNA.Info
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class LandingPage : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)
        supportActionBar?.hide()

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

}