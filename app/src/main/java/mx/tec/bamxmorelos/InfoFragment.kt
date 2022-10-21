package mx.tec.bamxmorelos

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentTransaction

class InfoFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = layoutInflater.inflate(R.layout.fragment_info, container, false)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)
        val contacto = view.findViewById<CardView>(R.id.cvContacto)
        val sugerencias = view.findViewById<CardView>(R.id.cvRecomendaciones)
        val btnEncuesta = view.findViewById<CardView>(R.id.cvEncuesta)

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

        contacto.setOnClickListener {

            //var builder = AlertDialog.Builder(context)
            /*builder.setTitle("Contacto")
                .setMessage("Telefono : 777 317 5516\nDirección: Eugenio J Cañas 11, Acacias y Priv. Compo, Lomas de, La Pradera, 62175 Cuernavaca, Mor.")
                .setNegativeButton("Cerrar") { dialog, _ ->
                    dialog.dismiss()
                }
                .show() */

            //MessageDialog.display(context, R.layout.layout_contacto)

            val myDialog = Dialog(requireContext())
            myDialog.setContentView(R.layout.layout_contacto)
            myDialog.setCancelable(true)
            myDialog.show()
            val button = myDialog.findViewById<Button>(R.id.btnCerrarContacto)
            button.setOnClickListener {
                myDialog.dismiss()
            }

        }

        sugerencias.setOnClickListener {

            var builder = AlertDialog.Builder(context)
            /*builder.setTitle("Recomendaciones")
                .setMessage("*Llevar una dieta variada y equilibrada\n" +
                        "*Disminuir el consumo de ácidos grasos saturados y preferir el consumo de acidos grasos mono y poliinsaturados\n" +
                        "*Llevar una alimentación que cubra las necesidades de cada integrante de la familia\n" +
                        "*Realizar ejercicio físico mínimo 30 minutos al día, combinando ejercicios aeróbicos y anaeróbicos\n" +
                        "*Tomar mínimo 2 litros de agua simple al día para evitar una deshidratación")
                .setNegativeButton("Cerrar") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()*/
            val intent = Intent(context, Retroalimentacion::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }


        btnEncuesta.setOnClickListener {
            val intent = Intent(context, Encuesta::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        return view
    }

}