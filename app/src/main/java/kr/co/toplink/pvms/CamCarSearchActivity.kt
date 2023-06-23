package kr.co.toplink.pvms

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.icu.text.SimpleDateFormat
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.OrientationEventListener
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import kr.co.toplink.pvms.adapter.CarInfoAdapter
import kr.co.toplink.pvms.camerax.CameraManager
import kr.co.toplink.pvms.data.CarInfoList
import kr.co.toplink.pvms.databinding.ActivityCamCarSearchBinding
import kr.co.toplink.pvms.model.CamCarSearchViewModel
import kr.co.toplink.pvms.model.CarNumberSearchViewModel
import kr.co.toplink.pvms.util.*
import java.util.*

@ExperimentalGetImage
class CamCarSearchActivity: AppCompatActivity(), CarInfoAdapter.CarInfoAdapterListener {
    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ActivityCamCarSearchBinding
    private lateinit var cameraManager: CameraManager

    private lateinit var viewModel: CarNumberSearchViewModel

    private val carinfoadapter = CarInfoAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCamCarSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.apply {
            adapter = carinfoadapter
        }

        binding.imageButtonShutter.setOnClickListener {
            takePicture()
        }

        createCameraManager()
        cameraManager.startCamera()
    }

    /*
    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(CamCarSearchViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.onShutterButtonEvent.observe(this) {
            it?.let { takePicture() }
        }
    }
     */

    private fun createCameraManager() {
        cameraManager = CameraManager(
            this,
            binding.previewViewFinder,
            this,
            binding.graphicOverlayFinder
        )
    }

    private fun takePicture() {
        // shutter effect
        Toast.makeText(this, "take a picture!", Toast.LENGTH_SHORT).show()
        setOrientationEvent()

        cameraManager.imageCapture.takePicture(
            cameraManager.cameraExecutor,
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    image.image?.let {
                        imageToBitmapSaveGallery(it)
                    }
                    super.onCaptureSuccess(image)
                }
            })
    }

    private fun imageToBitmapSaveGallery(image: Image) {
        image.imageToBitmap()
            ?.rotateFlipImage(
                cameraManager.rotation,
                cameraManager.isFrontMode()
            )
            ?.scaleImage(
                binding.previewViewFinder,
                cameraManager.isHorizontalMode()
            )
            ?.let { bitmap ->
                binding.graphicOverlayFinder.processCanvas.drawBitmap(
                    bitmap,
                    0f,
                    bitmap.getBaseYByView(
                        binding.graphicOverlayFinder,
                        cameraManager.isHorizontalMode()
                    ),
                    Paint().apply {
                        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)
                    })
                binding.graphicOverlayFinder.processBitmap.saveToGallery(this)
            }
    }

    private fun setOrientationEvent() {
        val orientationEventListener = object : OrientationEventListener(this as Context) {
            override fun onOrientationChanged(orientation: Int) {
                val rotation: Float = when (orientation) {
                    in 45..134 -> 270f
                    in 135..224 -> 180f
                    in 225..314 -> 90f
                    else -> 0f
                }
                cameraManager.rotation = rotation
            }
        }
        orientationEventListener.enable()
    }

    override fun onCarInfoListClicked(carInfoView: View, carInfoList: CarInfoList) {
        TODO("Not yet implemented")
    }
}