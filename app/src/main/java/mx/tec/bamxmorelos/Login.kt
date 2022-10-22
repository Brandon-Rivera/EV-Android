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
        this.requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        userFocusListener()
        pwdFocusListener()

        queue = Volley.newRequestQueue(this@Login)

        /*val btnRegistro = findViewById<Button>(R.id.btnRegistro)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnPassword = findViewById<Button>(R.id.btnForgotPassword)

        val edtNombreLogin = findViewById<EditText>(R.id.edtNombre)
        val edtPasswordLogin = findViewById<EditText>(R.id.edtPassword)*/



        binding.btnLogin.setOnClickListener {
            if (!submitForm()){
                Log.e("FName", binding.tiedtNombreLogin.text.toString())
                Log.e("FPassword", binding.tiedtPasswordLogin.text.toString())
            }
            else {
                Log.e("FFName", binding.tiedtNombreLogin.text.toString())
                Log.e("FFPassword", binding.tiedtPasswordLogin.text.toString())
                val body = JSONObject()
                with(body) {
                    put("userName", binding.tiedtNombreLogin.text.toString())
                    put("userPassword", binding.tiedtPasswordLogin.text.toString())
                }

                LoadingDialog.display(this@Login)

                val url = "http://api-vacaciones.us-east-1.elasticbeanstalk.com/api/login"

                Log.e("URL", body.toString())

                val listener = Response.Listener<JSONObject> { response ->
                    Log.e("RESPONSE", response.toString())
                    if (response.get("mensaje") == "Usuario autenticado") {

                        val sharedPreference = getSharedPreferences("profile", Context.MODE_PRIVATE)

                        with(sharedPreference.edit()) {
                            putString("user", binding.tiedtNombreLogin.text.toString())
                            putString("password", binding.tiedtPasswordLogin.text.toString())
                            putString("token", response.get("token").toString())
                            putString("idUser", response.get("id").toString())
                            commit()
                        }

                        val intent = Intent(this@Login, LandingPage::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or
                                Intent.FLAG_ACTIVITY_NEW_TASK

                        LoadingDialog.dismiss()
                        startActivity(intent)
                    } else {
                        LoadingDialog.dismiss()
                        Toast.makeText(
                            this@Login, "Usuario o contraseña incorrectos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                val error = Response.ErrorListener { error ->
                    //Log.e("ERROR", error.message!!)
                    if(error != null) {
                        Log.e("ERROR", error.message!!)
                    }
                    Toast.makeText(
                        this@Login, error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                val request = JsonObjectRequest(Request.Method.POST, url, body, listener, error)
                queue.add(request)

            }

        }

        binding.btnRegistro.setOnClickListener {
            val intent = Intent(this@Login, Registro::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

    }

    private fun userFocusListener() {
        binding.tiedtNombreLogin.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                binding.tilNombreLogin.helperText = validUser()
            }
        }
    }

    private fun pwdFocusListener() {
        binding.tiedtPasswordLogin.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                binding.tilPasswordLogin.helperText = validPwd()
            }
        }
    }

    private fun submitForm(): Boolean {
        binding.tilNombreLogin.helperText = validUser()
        binding.tilPasswordLogin.helperText = validPwd()

        val validUser = binding.tilNombreLogin.helperText == null
        val validPwd = binding.tilPasswordLogin.helperText == null

        return validUser && validPwd
    }

    private fun validPwd(): String? {
        val pwdText = binding.tiedtPasswordLogin.text.toString()
        if(pwdText == "")
            return "*Requerido"
        return null
    }

    private fun validUser(): String? {
        val userText = binding.tiedtNombreLogin.text.toString()
        if(userText == "")
            return "*Requerido"
        return null
    }


}