package mx.tec.bamxmorelos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class Retroalimentacion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retroalimentacion)

        val btn = findViewById<ImageButton>(R.id.iBtnBackReco)


        btn.setOnClickListener {
            val intent = Intent(this@Retroalimentacion, LandingPage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }
}