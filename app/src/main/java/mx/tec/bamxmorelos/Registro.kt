package mx.tec.bamxmorelos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
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
import com.google.android.material.textfield.TextInputLayout
import mx.tec.bamxmorelos.databinding.ActivityAgregarBinding
import mx.tec.bamxmorelos.databinding.ActivityRegistroBinding
import org.json.JSONObject
import org.w3c.dom.Text
import javax.xml.transform.ErrorListener

class Registro : AppCompatActivity() {
    private lateinit var queue: RequestQueue
    lateinit var binding: ActivityRegistroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = "http://api-vacaciones.us-east-1.elasticbeanstalk.com/api/register"
        //val clRegistro = findViewById<ConstraintLayout>(R.id.clRegistro)

        queue = Volley.newRequestQueue(this@Registro)

        emailFocusListener()
        nameFocusListener()
        pwdFocusListener()
        folioFocusListener()
        phoneFocusListener()

        binding.btnRegistroRegistro.setOnClickListener {
            if(!submitForm()){
                Log.e("ERROR", "Invalid Data")
            val mySnackbar = Snackbar.make(
                findViewById(R.id.clRegistro),
                "Revisar datos de registro",
                Snackbar.LENGTH_SHORT
            ).show()}
            else {


                val jsonBody = JSONObject()
                jsonBody.put("folio", binding.tiedFolioRegistro.text.toString().toInt())
                jsonBody.put("userName", binding.tiedNombreRegistro.text.toString())
                jsonBody.put("userPassword", binding.tiedPasswordRegistro.text.toString())
                jsonBody.put("phoneNumber", binding.tiedTelefonoRegistro.text.toString())
                jsonBody.put("eMail", binding.tiedCorreoRegistro.text.toString())

                val listener = Response.Listener<JSONObject> { response ->
                    Toast.makeText(this@Registro, "Usuario creado exitosamente", Toast.LENGTH_SHORT).show()
                    if (response.get("mensaje").toString() == "Usuario creado") {
                        Log.e("RESPONSE", response.toString())
                        /*val mySnackbar = Snackbar.make(
                            findViewById(R.id.clRegistro),
                            "Usuario creado exitosamente",
                            Snackbar.LENGTH_SHORT
                        ).show()*/
                        val intent = Intent(this@Registro, Login::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or
                                Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }

                }

                val error = Response.ErrorListener { error ->
                    Log.e("ERROR", error.message!!)
                    Toast.makeText(this@Registro, "Failed", Toast.LENGTH_SHORT).show()
                }

                val request = JsonObjectRequest(Request.Method.POST, url, jsonBody, listener, error)

                queue.add(request)
            }

        }
        binding.btnBackRegistro.setOnClickListener {
            val intent = Intent(this@Registro, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    private fun phoneFocusListener() {
        binding.tiedTelefonoRegistro.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus){
                binding.tiledtTelefonoRegistro.helperText = validPhone()
            }
        }
    }

    private fun validPhone(): CharSequence? {
        val text = binding.tiedTelefonoRegistro.text.toString()
        if(text == "")
            return "*Requerido"
        return null
    }

    private fun folioFocusListener() {
        binding.tiedFolioRegistro.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus){
                binding.tiledtFoliopRegistro.helperText = validFolio()
            }
        }
    }

    private fun validFolio(): CharSequence? {
        val text = binding.tiedFolioRegistro.text.toString()
        if(text == "")
            return "*Requerido"
        return null
    }

    private fun pwdFocusListener() {
        binding.tiedPasswordRegistro.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus){
                binding.tiledPasswordRegistro.helperText = validPwd()
            }
        }
    }

    private fun validPwd(): CharSequence? {
        val text = binding.tiedPasswordRegistro.text.toString()
        if(text == "")
            return "*Requerido"
        if(text.length < 3)
            return "Mínimo 3 caracteres"
        return null
    }

    private fun nameFocusListener() {
        binding.tiedNombreRegistro.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus){
                binding.tiledNombreRegistro.helperText = validName()
            }
        }
    }

    private fun validName(): CharSequence? {
        val text = binding.tiedNombreRegistro.text.toString()
        if(text == "")
            return "*Requerido"
        return null
    }

    private fun emailFocusListener() {
        binding.tiedCorreoRegistro.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus){
                binding.tiledCorreoRegistro.helperText = validEmail()
            }
        }
    }

    private fun validEmail(): String? {
        val emailText = binding.tiedCorreoRegistro.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            return "Dirección de correo inválida"
        }
        return null
    }

    private fun submitForm(): Boolean {
        binding.tiledNombreRegistro.helperText = validName()
        binding.tiledPasswordRegistro.helperText = validPwd()
        binding.tiledtFoliopRegistro.helperText = validFolio()
        binding.tiledtTelefonoRegistro.helperText = validPhone()
        binding.tiledCorreoRegistro.helperText = validEmail()

        val validUser = binding.tiledNombreRegistro.helperText == null
        val validPwd = binding.tiledPasswordRegistro.helperText == null
        val validFolio = binding.tiledtFoliopRegistro.helperText == null
        val validPhone = binding.tiledtTelefonoRegistro.helperText == null
        val validEmail = binding.tiledCorreoRegistro.helperText == null

        return validUser && validPwd && validFolio && validPhone && validEmail
    }
}