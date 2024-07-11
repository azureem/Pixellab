package com.example.piceditor.edit

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.piceditor.R
import com.example.piceditor.databinding.DialogBackBinding

class GoHome : DialogFragment(R.layout.dialog_back) {

    private var binding: DialogBackBinding?=null

    private var onClickYes: (() -> Unit)? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogBackBinding.inflate(inflater, container, false)
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //  requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        //requireActivity().window.statusBarColor = Color.parseColor("#66000000")

        binding!!.backNo.setOnClickListener {
            dismiss()
        }

        binding!!.backYes.setOnClickListener {
            onClickYes!!.invoke()
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        val window = dialog!!.window
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    fun onClickYes(block: () -> Unit) {
        this.onClickYes = block
    }

}