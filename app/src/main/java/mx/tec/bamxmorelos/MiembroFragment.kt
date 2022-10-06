package mx.tec.bamxmorelos

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONObject

class MiembroFragment: Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = layoutInflater.inflate(R.layout.fragment_miembro, container, false)
        val btn = view.findViewById<Button>(R.id.btnAgregarMiembro)

        val nombre = view.findViewById<EditText>(R.id.edtNameMiembro)
        val edad = view.findViewById<EditText>(R.id.edtEdadMiembro)
        val altura = view.findViewById<EditText>(R.id.edtAlturaMiembro)
        val peso = view.findViewById<EditText>(R.id.edtPesoMiembro)
        val ocupacion = view.findViewById<EditText>(R.id.edtOcupacionMiembro)

        btn.setOnClickListener {
            val body = JSONObject()
            with(body){
                put("name", nombre)
                put("edad", edad)
                put("altura", altura)
                put("peso", peso)
                put("ocupacion", ocupacion)
            }

        }
        return view
    }
}
