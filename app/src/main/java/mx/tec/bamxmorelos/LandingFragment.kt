package mx.tec.bamxmorelos

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.MediaController
import android.widget.VideoView

class LandingFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var mediaController: MediaController? = null
        val view = layoutInflater.inflate(R.layout.fragment_landing, container, false)
        val btnEncuesta = view.findViewById<Button>(R.id.btnEncuesta)



        btnEncuesta.setOnClickListener {
            val intent = Intent(context, Encuesta::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        return view
    }
}

