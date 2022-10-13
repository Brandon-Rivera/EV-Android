package mx.tec.bamxmorelos

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)
        val sharedPreference = requireActivity().getSharedPreferences("profile", Context.MODE_PRIVATE)

        btnLogout.setOnClickListener {
            with(sharedPreference.edit()){
                remove("idUser")
                remove("password")
                remove("user")
                remove("token")
                apply()
            }


            val intent = Intent(context, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        return view
    }

}