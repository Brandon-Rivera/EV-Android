package mx.tec.bamxmorelos

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import mx.tec.bamxmorelos.adapter.CustomAdapter
import mx.tec.bamxmorelos.model.Elemento

class FamilyFragment : Fragment(R.layout.fragment_family){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = layoutInflater.inflate(R.layout.fragment_family, container, false)
        val fab = view.findViewById<FloatingActionButton>(R.id.fabAgregarFamilia)

        val datos = arrayListOf(
            Elemento("Elemento 1", R.mipmap.ic_launcher_round), Elemento("Elemento 2", R.mipmap.ic_launcher_round), Elemento("Elemento 3", R.mipmap.ic_launcher_round))

        val rcView = view.findViewById<RecyclerView>(R.id.rcvMiembrosFamilia)
        rcView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false )
            adapter = CustomAdapter(context, R.layout.layout_elemento, datos)

            setHasFixedSize(false)
        }

        fab.setOnClickListener {

            val intent = Intent(context, Agregar::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
            return view
    }
}