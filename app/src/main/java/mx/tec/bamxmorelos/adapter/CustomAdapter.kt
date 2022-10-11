package mx.tec.bamxmorelos.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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

        init {
            imagen = itemView.findViewById(R.id.imgImagen) as ImageView
            nombre = itemView.findViewById (R.id.txtNombre) as TextView
            edad = itemView.findViewById(R.id.txtEdad) as TextView

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