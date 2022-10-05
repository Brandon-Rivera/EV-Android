package mx.tec.bamxmorelos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentTransaction

class InfoFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = layoutInflater.inflate(R.layout.fragment_info, container, false)

        val cb = view.findViewById<Button>(R.id.btnContactoInfo)

        cb.setOnClickListener {
            val contactFragment = ContactoFragment()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.landingLayout, contactFragment)
            transaction.commit()

        }
        return view
    }

}