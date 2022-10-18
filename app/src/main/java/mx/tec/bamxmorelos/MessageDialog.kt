package mx.tec.bamxmorelos

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout

object MessageDialog {

    var dialog: Dialog? = null //obj
    fun display(context: Context?, layout: Int) { // function -- context(parent (reference))
        dialog = Dialog(context!!)
        dialog!!.setContentView(layout)
        dialog!!.setCancelable(true)
        try {
            dialog!!.show()
        } catch (e: Exception) {
        }
    }

    fun dismiss() {
        try {
            if (dialog != null) {
                dialog!!.dismiss()
            }
        } catch (e: Exception) {
        }
    }
}