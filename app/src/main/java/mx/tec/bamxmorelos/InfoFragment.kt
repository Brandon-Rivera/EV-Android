package mx.tec.bamxmorelos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class InfoFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = layoutInflater.inflate(R.layout.fragment_info, container, false)

        val btn = view.findViewById<Button>(R.id.btnLogoutInfo)
        val sharedPreference = this.requireActivity().getSharedPreferences("profile", Context.MODE_PRIVATE)
        btn.setOnClickListener {
            with(sharedPreference.edit()) {
                remove("user")
                remove("password")
                remove("token")
                remove("idUser")
                commit()
            }

            val intent = Intent(context, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        return view
    }

}