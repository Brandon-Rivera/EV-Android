package mx.tec.bamxmorelos

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
        val cb = view.findViewById<FloatingActionButton>(R.id.floatingActionButton2)

        cb.setOnClickListener {
            val miembroFragment = MiembroFragment()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.landingLayout, miembroFragment)
            transaction.commit()
        }
            return view
    }
}