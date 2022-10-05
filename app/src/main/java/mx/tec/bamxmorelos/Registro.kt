package mx.tec.bamxmorelos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject
import javax.xml.transform.ErrorListener

class Registro : AppCompatActivity() {
    private lateinit var queue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        val btnBackRegistro = findViewById<Button>(R.id.btnBackRegistro)
        val btnRegistroRegistro = findViewById<Button>(R.id.btnRegistroRegistro)
        val edtNombreRegistro = findViewById<EditText>(R.id.edtNombreRegistro)
        val edtFolioRegistro = findViewById<EditText>(R.id.edtFolioRegistro)
        val edtPasswordRegistro = findViewById<EditText>(R.id.edtPasswordRegistro)
        val edtTelefonoRegistro = findViewById<EditText>(R.id.edtTelefonoRegistro)
        val edtCorreoRegistro = findViewById<EditText>(R.id.edtEmailRegistro)
        val url = "http://api-vacaciones.us-east-1.elasticbeanstalk.com/api/register"
        //val clRegistro = findViewById<ConstraintLayout>(R.id.clRegistro)

        queue = Volley.newRequestQueue(this@Registro)

        btnRegistroRegistro.setOnClickListener {
            val jsonBody = JSONObject()
            jsonBody.put("folio", edtFolioRegistro.text.toString().toInt())
            jsonBody.put("userName", edtNombreRegistro.text.toString())
            jsonBody.put("userPassword", edtPasswordRegistro.text.toString())
            jsonBody.put("phoneNumber", edtTelefonoRegistro.text.toString())
            jsonBody.put("eMail", edtCorreoRegistro.text.toString())

                val listener = Response.Listener<JSONObject> { response ->


                    Toast.makeText(this@Registro, "Success", Toast.LENGTH_SHORT).show()
                    Log.e("RESPONSE", response.toString())
                }

                val error = Response.ErrorListener {error ->
                    Log.e("ERROR", error.message!!)
                    Toast.makeText(this@Registro, "Failed", Toast.LENGTH_SHORT).show()
                }

               val request = JsonObjectRequest(Request.Method.POST, url, jsonBody, listener, error)

               queue.add(request)
            //val mySnackbar = Snackbar.make(clRegistro, "stringId", Snackbar.LENGTH_SHORT).show()
            val intent = Intent(this@Registro, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        btnBackRegistro.setOnClickListener {
            val intent = Intent(this@Registro, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        //registro.put("",)
    }
}