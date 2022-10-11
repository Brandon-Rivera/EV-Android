package mx.tec.bamxmorelos

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentTransaction

class InfoFragment : Fragment(){
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = layoutInflater.inflate(R.layout.fragment_info, container, false)

        val btnEncuesta = view.findViewById<Button>(R.id.btnEncuesta)
        val btnRetro = view.findViewById<Button>(R.id.btnRetro)
        val btnContacto = view.findViewById<Button>(R.id.btnContacto)

        btnEncuesta.setOnClickListener {

            val intent = Intent(context, Encuesta::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)

        }

        btnRetro.setOnClickListener {

            val intent = Intent(context, Retroalimentacion::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)

        }

        btnContacto.setOnClickListener {

            val intent = Intent(context, Contacto::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)

        }

        return view
    }

}