package mx.tec.bamxmorelos

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager.BadTokenException
import android.widget.Button

class MainActivity : AppCompatActivity() {

    //lateinit var nombre:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val btnStartMain = findViewById<Button>(R.id.btnStartMain)

        val sharedPreference = getSharedPreferences("archivo", Context.MODE_PRIVATE)

        with(sharedPreference){
            val nombre = getString("nombre" ,"#" )
            val password = getString("password", "#")
            if (nombre != null) {
                if (nombre != "#"){
                    TODO()
                }
            }
        }


        btnStartMain.setOnClickListener {
            val intent = Intent(this@MainActivity, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }



    }
}