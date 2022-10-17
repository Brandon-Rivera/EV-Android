package mx.tec.bamxmorelos

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.TextView

object LoadingDialog {
    var dialog: Dialog? = null //obj
    fun display(context: Context?) { // function -- context(parent (reference))
        dialog = Dialog(context!!)
        dialog!!.setContentView(R.layout.layout_loading)
        dialog!!.setCancelable(false)
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