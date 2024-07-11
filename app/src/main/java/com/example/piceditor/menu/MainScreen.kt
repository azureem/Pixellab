package com.example.piceditor.menu

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.piceditor.R
import com.example.piceditor.databinding.ScreenMenuBinding
import com.example.piceditor.edit.EditScreenGal
class MainScreen() : Fragment(), Parcelable {

    private var _binding: ScreenMenuBinding? = null
    private val binding get() = _binding!!

    private val getContent =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {result ->
            if (result != null) {
                val bundil= bundleOf(Pair("image",result.toString()))
                findNavController().navigate(R.id.action_mainScreen_to_editScreenGal2,bundil)
            }
        }

    constructor(parcel: Parcel) : this() {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScreenMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.statusBarColor = Color.parseColor("#3F496D")
        requireActivity().window.navigationBarColor = Color.parseColor("#3F496D")
        binding.gallery.setOnClickListener {
            getContent.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MainScreen> {
        override fun createFromParcel(parcel: Parcel): MainScreen {
            return MainScreen(parcel)
        }

        override fun newArray(size: Int): Array<MainScreen?> {
            return arrayOfNulls(size)
        }
    }
}