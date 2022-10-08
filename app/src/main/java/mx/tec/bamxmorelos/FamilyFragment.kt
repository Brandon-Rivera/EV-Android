package mx.tec.bamxmorelos

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FamilyFragment : Fragment(R.layout.fragment_family){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = layoutInflater.inflate(R.layout.fragment_family, container, false)
        val fab = view.findViewById<FloatingActionButton>(R.id.fabAgregarFamilia)
        fab.setOnClickListener {

            val intent = Intent(context, Agregar::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
            return view
    }
}