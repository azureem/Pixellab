package com.example.piceditor.edit

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.PointF
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.piceditor.EmogeAdapter
import com.example.piceditor.EmojiData
import com.example.piceditor.R
import com.example.piceditor.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.permissionx.guolindev.PermissionX
import java.text.SimpleDateFormat
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.sqrt

@Suppress("CAST_NEVER_SUCCEEDS")
class EditScreenGal : Fragment() {

    private lateinit var binding: ActivityMainBinding
    private var droble = ArrayList<EmojiData>()
    private var hayvon = ArrayList<EmojiData>()
    private var moylov = ArrayList<EmojiData>()
    private var ls = ArrayList<ViewGroup>()
    private var adpter = EmogeAdapter()
    private var isGray = false
    var onclick = false

    @SuppressLint("ClickableViewAccessibility")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = ActivityMainBinding.inflate(layoutInflater, container, false)

        return binding.root

    }


    @SuppressLint("ClickableViewAccessibility", "UseRequireInsteadOfGet", "InlinedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.statusBarColor = Color.parseColor("#3F496D")
        requireActivity().window.navigationBarColor = Color.parseColor("#3F496D")
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetTheme)

        binding.emojing.setOnClickListener {
            showEmojiBottomSheet(droble, bottomSheetDialog)
        }

        binding.effect.setOnClickListener {
            if (isGray) {
                antiMakeGray(binding.imgHolder)
                    binding.effect.setImageResource(R.drawable.effect)
            } else {
                makeGray(binding.imgHolder)
                binding.effect.setImageResource(R.drawable.return_button_svgrepo_com)
            }

            isGray = !isGray
        }

        binding.goHome.setOnClickListener {
            val dlg = GoHome()
            dlg.show(parentFragmentManager, "")
            dlg.onClickYes {
                findNavController().navigateUp()
            }
        }


        super.onCreate(savedInstanceState)

        hayvon.add(EmojiData(0, R.drawable.group_1))
        hayvon.add(EmojiData(1, R.drawable.group_2))
        hayvon.add(EmojiData(2, R.drawable.group_3))
        hayvon.add(EmojiData(3, R.drawable.group_4))
        hayvon.add(EmojiData(4, R.drawable.group_5))
        hayvon.add(EmojiData(6, R.drawable.group_6))
        droble.add(EmojiData(1, R.drawable.img_2))
        droble.add(EmojiData(0, R.drawable.img_3))
        droble.add(EmojiData(0, R.drawable.img_4))
        droble.add(EmojiData(0, R.drawable.img_5))
        droble.add(EmojiData(0, R.drawable.img_7))
        droble.add(EmojiData(0, R.drawable.img_8))
        droble.add(EmojiData(0, R.drawable.img_9))

        moylov.add(EmojiData(0, R.drawable.moylov_1))
        moylov.add(EmojiData(1, R.drawable.moylov_2))
        moylov.add(EmojiData(2, R.drawable.moylov_3))
        moylov.add(EmojiData(4, R.drawable.moylov_4))
        moylov.add(EmojiData(5, R.drawable.moylov_5))
        moylov.add(EmojiData(6, R.drawable.moylov_6))
        moylov.add(EmojiData(7, R.drawable.moylov_7))
        moylov.add(EmojiData(8, R.drawable.moylov_8))

        PermissionX.init(this)
            .permissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.START_VIEW_PERMISSION_USAGE
            )
            .request { allGranted, _, _ ->
                if (allGranted) {
                }
            }

        binding.texting.setOnClickListener {
            addText(binding.root.x / 2, binding.root.y / 2)
        }

        binding!!.texting.setOnClickListener {

            onclick = true
            binding!!.container.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    addText(event.x, event.y)
                }
                return@setOnTouchListener true
            }


        }

        val m = arguments!!.getString("image", "")
        binding.imgHolder.setImageURI(Uri.parse(m))

        binding.save.setOnClickListener {
            val dlg = SaveDialog()
            dlg.show(parentFragmentManager, "")
            dlg.onClickYes {
                val bitmap = catchBitmapFromView(binding.container)
                saveBitmapToExternalStorage(bitmap)
                Toast.makeText(requireContext(), "Successfully, saved!", Toast.LENGTH_SHORT).show()
            }


        }
    }
    private fun addImage(x: Float, y: Float, emoge: EmojiData) {
        clearImage()
        val emojiContainer = LayoutInflater.from(requireActivity())
            .inflate(R.layout.emoji_container, binding.container, false) as ViewGroup
        emojiContainer[0].isSaveEnabled = true
        (emojiContainer[1] as ImageView).setImageResource(emoge.image)
        emojiContainer[2].setOnClickListener {
            ls.remove(emojiContainer)
            binding.container.removeView(emojiContainer)
        }
        emojiContainer.x = x
        emojiContainer.y = y
        binding.container.addView(emojiContainer)
        ls.add(emojiContainer)

        attachTouchListener(emojiContainer)
    }

    private fun clearImage() {
        ls.forEach {
            it[0].isSelected = false
            it[2].isVisible = false
        }
        ls.forEach {
            if (it[1] is EditText) {
                (it[1] as EditText).clearFocus()
            }
        }
    }


    private fun makeGray(view: ImageView) {
        val matrix = ColorMatrix()
        matrix.setSaturation(0f)
        val filter = ColorMatrixColorFilter(matrix)
        view.colorFilter = filter
    }

    private fun antiMakeGray(view: ImageView) {
        view.colorFilter = null
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun attachTouchListener(viewGroup: ViewGroup) {
        var initTouchX = 0f
        var initTouchY = 0f
        var isInitState = false
        var initDistance = 0f
        var initAngle = 0f
        viewGroup.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    viewGroup[0].isSelected = true
                    viewGroup[2].visibility = View.VISIBLE

                    initTouchY = event.y
                    initTouchX = event.x
                }

                MotionEvent.ACTION_MOVE -> {
                    if (event.pointerCount == 1) {
                        viewGroup.x += event.x - initTouchX
                        viewGroup.y += event.y - initTouchY
                    }
                    if (event.pointerCount == 2) {
                        if (!isInitState) {
                            val firstPointF = PointF(event.getX(0), event.getY(0))
                            val secondPointF = PointF(event.getX(1), event.getY(1))

                            initDistance = firstPointF distance secondPointF
                            initAngle = atan(event.getY(1) - event.getY(0)) / (atan(
                                event.getX(1) - event.getX(0)
                            ))
                            isInitState = true
                        }
                        val nextFirstPointF = PointF(event.getX(0), event.getY(0))
                        val nextSecondPointF = PointF(event.getX(1), event.getY(1))

                        val newDistance = nextFirstPointF distance nextSecondPointF

                        viewGroup.scaleX *= newDistance / initDistance
                        viewGroup.scaleY *= newDistance / initDistance
                        val newAngle =
                            atan(event.getY(1) - event.getY(0)) / (atan(event.getX(1) - event.getX(0)))
                        val diffAngle = (newAngle - initAngle) * (180 / PI)
                        viewGroup.rotation += diffAngle.toFloat()
                    } else isInitState = false
                }
            }
            return@setOnTouchListener true
        }
    }

    private infix fun PointF.distance(pointF: PointF): Float =
        sqrt((this.x - pointF.x) * (this.x - pointF.x) + (this.y - pointF.y) * (this.y - pointF.y))



    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("SimpleDateFormat")
    private fun saveBitmapToExternalStorage(bitmap: Bitmap) {
        val name = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())
        val displayName = "$name.jpg"

        val resolver = requireContext().contentResolver
        val imageCollection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val imageDetails = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }

        val imageUri = resolver.insert(imageCollection, imageDetails)

        try {
            imageUri?.let { uri ->
                val outputStream = resolver.openOutputStream(uri)
                outputStream?.use { stream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        imageUri?.let { uri ->
        }
    }

    private fun addText(x: Float, y: Float) {
        clearImage()
        val emojiContainer = LayoutInflater.from(requireContext())
            .inflate(R.layout.text_container, binding!!.container, false) as ViewGroup

        (emojiContainer[1] as EditText).setText("")
        emojiContainer[0].isSelected = true
        emojiContainer[2].setOnClickListener {
            ls.remove(emojiContainer)
            binding!!.container.removeView(emojiContainer)
        }

        emojiContainer.x = x
        emojiContainer.y = y

        val editText = (emojiContainer[1] as EditText)
        editText.showSoftKeyboard()
        if (onclick) {
            binding!!.container.addView(emojiContainer)
            ls.add(emojiContainer)
            attachTouchListener(emojiContainer)
            onclick = false
        }

    }
    private fun catchBitmapFromView(view: View): Bitmap {
        view.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        view.isDrawingCacheEnabled = false
        return bitmap
    }
    private fun showEmojiBottomSheet(emojis: ArrayList<EmojiData>, bottomSheetDialog: BottomSheetDialog) {
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet, null)

        val recyclerView = bottomSheetView.findViewById<RecyclerView>(R.id.emoji_recycler_view)
        val emojiAdapter = EmogeAdapter()

        emojiAdapter.submitList(emojis)
        recyclerView.adapter = emojiAdapter

        val gridLayoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView.layoutManager = gridLayoutManager
        emojiAdapter.clickEmoji = {x, y ,selectedEmoji ->
            addImage(x, y, selectedEmoji)
            bottomSheetDialog.dismiss()
        }

       val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }
}