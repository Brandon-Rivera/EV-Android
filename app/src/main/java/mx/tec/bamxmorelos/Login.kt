package mx.tec.bamxmorelos

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import mx.tec.bamxmorelos.databinding.ActivityLoginBinding
import org.json.JSONObject

class Login : AppCompatActivity() {

    lateinit var queue : RequestQueue
    lateinit var binding : ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)
        binding  = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        queue = Volley.newRequestQueue(this@Login)


        /*val btnRegistro = findViewById<Button>(R.id.btnRegistro)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnPassword = findViewById<Button>(R.id.btnForgotPassword)

        val edtNombreLogin = findViewById<EditText>(R.id.edtNombre)
        val edtPasswordLogin = findViewById<EditText>(R.id.edtPassword)*/



        binding.btnLogin.setOnClickListener {

                val body = JSONObject()
                with(body){
                    put("userName", binding.edtNombre.text.toString())
                    put("userPassword", binding.edtPassword.text.toString())
                }
                val url = "http://api-vacaciones.us-east-1.elasticbeanstalk.com/api/login"

                Log.e("URL", body.toString())

                val listener = Response.Listener<JSONObject> { response ->
                    Log.e("RESPONSE", response.toString())
                    if(response.get("mensaje") == "Usuario autenticado"){

                        val sharedPreference = getSharedPreferences("archivo", Context.MODE_PRIVATE)

                        with(sharedPreference.edit()){
                            putString("user", binding.edtNombre.text.toString())
                            putString("password", binding.edtPassword.text.toString())
                            commit()
                        }

                        val intent = Intent(this@Login, LandingPage::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or
                                Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                    else {
                        Toast.makeText(this@Login, "Usuario o contraseña incorrectos",
                            Toast.LENGTH_SHORT).show()
                    }
                }
                val error = Response.ErrorListener { error ->
                    Log.e("ERROR", error.message!!)
                    Toast.makeText(this@Login, error.message,
                        Toast.LENGTH_SHORT).show()
                }

                val request = JsonObjectRequest(Request.Method.POST, url, body, listener, error)
                queue.add(request)


            /*val intent = Intent(this@Login, LandingPage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)*/
        }

        binding.btnRegistro.setOnClickListener {
            val intent = Intent(this@Login, Registro::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        binding.btnForgotPassword.setOnClickListener {
            val intent = Intent(this@Login, Password::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)

        }
    }
}