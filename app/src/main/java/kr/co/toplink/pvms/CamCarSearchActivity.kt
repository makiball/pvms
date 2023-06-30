package kr.co.toplink.pvms

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.OrientationEventListener
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.toplink.pvms.adapter.SingleViewBinderListAdapter
import kr.co.toplink.pvms.camerax.CameraManager
import kr.co.toplink.pvms.data.CarInfoListToday
import kr.co.toplink.pvms.data.CarInfoListTodayModel
import kr.co.toplink.pvms.database.CarInfoToday
import kr.co.toplink.pvms.databinding.ActivityCamCarSearchBinding
import kr.co.toplink.pvms.databinding.CarinfoItemLayoutBinding
import kr.co.toplink.pvms.model.CamCarSearchViewModel
import kr.co.toplink.pvms.model.CarNumberSearchViewModel
import kr.co.toplink.pvms.util.*
import kr.co.toplink.pvms.viewholder.CarInfoListTodayViewBinder
import kr.co.toplink.pvms.viewholder.ItemBinder
import java.util.*

@ExperimentalGetImage
class CamCarSearchActivity: AppCompatActivity(){
    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ActivityCamCarSearchBinding
    private lateinit var cameraManager: CameraManager
    private lateinit var listAdapter: SingleViewBinderListAdapter
    private lateinit var viewModel: CarNumberSearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCamCarSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageButtonShutter.setOnClickListener {
            takePicture()
        }

        createCameraManager()
        cameraManager.startCamera()

        val postCardViewBinder = CarInfoListTodayViewBinder { binding, CarInfoListTodayModel ->
            gotoDetailWithTransition(CarInfoListTodayModel, binding)
        }
        listAdapter = SingleViewBinderListAdapter(postCardViewBinder as ItemBinder)
        binding.recyclerView.apply {
            this.adapter = listAdapter
            layoutManager = LinearLayoutManager(this@CamCarSearchActivity)
        }

        init()

        binding.alldelte.setOnClickListener {
            val msg = "데이터를 삭제하시면 복구 하실수 없습니다. "
            val dlg = DeleteDialog(this)
            dlg.setOnOKClickedListener{
                /* 모두 삭제 처리 */
                viewModel.allTodayDeteData(this)
            }
            dlg.show(msg)
        }
    }

    private fun init() {
        viewModel = ViewModelProvider(this).get(CarNumberSearchViewModel::class.java)
        viewModel.searchCarnumToday(this, "")
        viewModel.carinfoListToday.observe(this, androidx.lifecycle.Observer {

            Log.d(TAG,"=====> ")

            it.apply {
                binding.totalreg.text = "총 수량 : ${this.size}대"
                listAdapter.submitList(generateMockCarinfo(this))
            }
        })
    }

    private fun generateMockCarinfo(carInfo: MutableList<CarInfoToday>): List<CarInfoListTodayModel> {
        val carInfoList = ArrayList<CarInfoListTodayModel>()
        carInfo.forEach{
            val carinfolisttoday = CarInfoListToday(it.id, it.carnumber, it.phone, it.date, it.etc, it.type)
            carInfoList.add(CarInfoListTodayModel(carinfolisttoday))
        }
        return carInfoList
    }


    private fun gotoDetailWithTransition (
        carInfoListTodayModel: CarInfoListTodayModel,
        binding : CarinfoItemLayoutBinding
    ) {
        val intent =
            Intent(this, CarNumberSearchDetailActivity::class.java)
        intent.putExtra(KEY_CARINFOLIST_MODEL, carInfoListTodayModel)

        val pairIvCarnum = Pair<View, String>(binding.carnumberTxt, binding.carnumberTxt.transitionName)

        val options = ActivityOptions
            .makeSceneTransitionAnimation(
                this,
//                pairTvTitle,
                pairIvCarnum
            )

        // start the new activity
        startActivity(intent, options.toBundle())
    }

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
}