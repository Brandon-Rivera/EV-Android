package mx.tec.bamxmorelos.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import mx.tec.bamxmorelos.Editar
import mx.tec.bamxmorelos.R
import mx.tec.bamxmorelos.model.Elemento

class CustomAdapter(private val context: Context, private val layout: Int,
                    private val dataSource: List<Elemento>)
    : RecyclerView.Adapter<CustomAdapter.ElementoViewHolder>()
{
    class ElementoViewHolder(inflater: LayoutInflater, parent: ViewGroup, layout: Int) :
        RecyclerView.ViewHolder(inflater.inflate(layout, parent, false)) {
        var imagen: ImageView? = null
        var nombre: TextView? = null
        var edad: TextView? = null
        var boton: ImageButton? = null

        init {
            imagen = itemView.findViewById(R.id.imgImagen) as ImageView
            nombre = itemView.findViewById (R.id.txtNombreElemento) as TextView
            edad = itemView.findViewById(R.id.txtEdad) as TextView
            boton = itemView.findViewById(R.id.iBtnEditarMiembro) as ImageButton
            boton!!.setOnClickListener {
                //Log.e("ERROR", nombre!!.text.toString())
                val intent = Intent(parent.context, Editar::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("name", nombre!!.text.toString())
                startActivity(parent.context, intent, null)
            }

        }

        fun bindData(elemento: Elemento) {
            imagen!!.setImageResource(elemento.imagen)
            nombre!!.text = elemento.nombre
            edad!!.text = elemento.edad.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElementoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ElementoViewHolder(inflater, parent, layout)
    }

    override fun onBindViewHolder(holder: ElementoViewHolder, position: Int) {
        val elemento = dataSource[position]
        holder.bindData(elemento)
        val animation = AnimationUtils.loadAnimation(context, R.anim.bounce)
        holder.itemView.startAnimation(animation)
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }
}