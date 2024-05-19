package br.com.noobs.agronoobs.ui.components

import android.app.Dialog
import android.content.Context
import android.widget.TextView
import br.com.noobs.agronoobs.R

class LoadingDialogBar(
    private val context: Context,
    private var dialog: Dialog? = null
) {

    private lateinit var textView: TextView


    fun showDialog(title: String) {
        dialog = Dialog(context)
        dialog?.setContentView(R.layout.dialog)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        textView = dialog?.findViewById(R.id.text_view)!!

        textView.text = title

        dialog?.create()
        dialog?.show()
    }

    fun dismissDialog() {
        dialog?.dismiss()
    }


}