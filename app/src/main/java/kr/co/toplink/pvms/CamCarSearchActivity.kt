package kr.co.toplink.pvms

import android.content.Context
import android.graphics.*
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.OrientationEventListener
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.toplink.pvms.adapter.CamCarSearchAdapter
import kr.co.toplink.pvms.camerax.CameraManager
import kr.co.toplink.pvms.data.regSwitch
import kr.co.toplink.pvms.database.CarInfoToday
import kr.co.toplink.pvms.database.CarInfoTotal
import kr.co.toplink.pvms.database.Report
import kr.co.toplink.pvms.databinding.ActivityCamCarSearchBinding
import kr.co.toplink.pvms.util.*
import kr.co.toplink.pvms.viewmodel.CamCarViewModel
import kr.co.toplink.pvms.viewmodel.ReportCarViewModel
import kr.co.toplink.pvms.viewmodel.ViewModelFactory
import java.util.*

@ExperimentalGetImage
class CamCarSearchActivity: AppCompatActivity(){
    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ActivityCamCarSearchBinding
    private lateinit var cameraManager: CameraManager
    private lateinit var camCarViewModel: CamCarViewModel
    private lateinit var camcarsearchadapter : CamCarSearchAdapter
    private lateinit var reportCarViewModel: ReportCarViewModel

    private var lastid : Int = 0

    private lateinit var CarInfoToday : List<CarInfoToday>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCamCarSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        Log.d(TAG,"카메라 차량 입력 엑티비티 실행!!!")


        // 뒤로가기시 현재 엑티비티 닫기
        val callback = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
        binding.backBt.setOnClickListener{  finish()  }

        binding.imageButtonShutter.setOnClickListener {
            takePicture()
        }

        createCameraManager()
        cameraManager.startCamera()

        binding.alldelte.setOnClickListener {
            val msg = "데이터를 삭제하시면 복구 하실수 없습니다. "
            val dlg = DeleteDialog(this)
            dlg.setOnOKClickedListener{
                /* 모두 삭제 처리 */
                camCarViewModel.carInfoTodayDelete()
            }
            dlg.show(msg)
        }


        /*
        val postCardViewBinder = CarInfoListTodayViewBinder { binding, CarInfoListTodayModel ->
            gotoDetailWithTransition(CarInfoListTodayModel, binding)
            Log.d(TAG,"=====> 클릭")
        }

        listAdapter = SingleViewBinderListAdapter(postCardViewBinder as ItemBinder)
        binding.recyclerView.apply {
            this.adapter = listAdapter
            layoutManager = LinearLayoutManager(this@CamCarSearchActivity)
        }

        init()
         */

        /* 차량 등록 미등록 스위치 액션 */
        binding.isReg.setOnCheckedChangeListener { _, isChecked ->

            //Log.d(TAG, "=====> $isChecked")

            if (isChecked) {
                RegSwitch.setSharedSwitch(regSwitch.ON)
                //camCarViewModel.regSwitch(regSwitch.ON)
            } else {
                //camCarViewModel.regSwitch(regSwitch.OFF)
                RegSwitch.setSharedSwitch(regSwitch.OFF)
                // SwitchMaterial이 OFF 상태일 때 동작
                // 여기에 필요한 코드 작성
            }
        }

        /* 보고서 등록 */
        binding.makeReport.setOnClickListener {

            val msg = "보고서 생성시 주차 검색 내용은 삭제됩니다."
            val dlg = DeleteDialog(this)
            dlg.setOnOKClickedListener{
                /* 모두 삭제 처리 */
                //camCarViewModel.carInfoTodayDelete()

                //미등록 갯수, 등록 갯수

                val datepatterned = Date()
                //Log.d(TAG,"=====> ${CarInfoToday.size}")

                //미등록 갯수, 등록 갯수
                val total = CarInfoToday.size
                var count = 0
                val regSu = CarInfoToday.filter { it.type == 0 }.size
                val regSuNo = CarInfoToday.filter { it.type == 1 }.size
                val lowStop = CarInfoToday.filter { it.lawstop == 1 }.size

                if(total <= 0) {
                    Toast.makeText(this, "차량 조회가 없습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnOKClickedListener
                }

                val report = Report(
                    id = lastid,
                    date = datepatterned,
                    total_type_0 = regSu,
                    total_type_1 = regSuNo,
                    total_lawstop = lowStop
                    )
                reportCarViewModel.reportInsert(report)

                CarInfoToday.forEach{
                    val carInfoTotal = CarInfoTotal(
                        carnumber = it.carnumber,
                        phone = it.phone,
                        date = it.date,
                        etc = it.etc,
                        type = it.type,
                        lawstop = it.lawstop,
                        reportnum = lastid
                    )
                    count += 1
                    reportCarViewModel.carInfoTotalInsert(carInfoTotal)
                }


                Log.d(TAG, "===========> $count -  $total ")
                if(count == total) {
                    camCarViewModel.carInfoTodayDelete()
                }
            }
            dlg.show(msg)
        }
    }

    private fun init() {

        camCarViewModel = ViewModelProvider(this, ViewModelFactory(this)).get(CamCarViewModel::class.java)
        camcarsearchadapter = CamCarSearchAdapter(camCarViewModel)
        camCarViewModel.carInfoTodayList()
        camCarViewModel.camcarinfos.observe(this, EventObserver {

            CarInfoToday = it

            //binding.total.text = format("총 차량 : ${it.size}"

            //미등록 갯수, 등록 갯수
            val regSu = it.filter { it.type == 0 }.size
            val regSuNo = it.filter { it.type == 1 }.size
            val lowStop = it.filter { it.lawstop == 1 }.size

            binding.total.text = getString(R.string.camcarsearch_total_txt).format(it.size)
            binding.totalreg.text = getString(R.string.camcarsearch_totalreg_txt).format(regSu)
            binding.totalnotreg.text = getString(R.string.camcarsearch_totalnotreg_txt).format(regSuNo)
            binding.lawstop.text = getString(R.string.camcarsearch_lawstop_txt).format(lowStop)

            binding.recyclerView.apply {
                adapter = camcarsearchadapter
                layoutManager = GridLayoutManager(this@CamCarSearchActivity, 1)
                adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }

            camcarsearchadapter.apply {
                submitList(it)
            }
        })
        camCarViewModel.regSwitch(regSwitch.OFF)

        reportCarViewModel = ViewModelProvider(this, ViewModelFactory(this)).get(ReportCarViewModel::class.java)
        reportCarViewModel.reportLastId()
        reportCarViewModel.lastid.observe(this, EventObserver {
            lastid = it + 1

            Log.d(TAG, "=====> 라스트 아이디 $lastid")
        })

        /*
        viewModel = ViewModelProvider(this).get(CarNumberSearchViewModel::class.java)
        viewModel.searchCarnumToday(this, "")
        viewModel.carinfoListToday.observe(this, androidx.lifecycle.Observer {
            it.apply {
                binding.totalreg.text = "총 차량 : ${this.size}"
                listAdapter.submitList(generateMockCarinfo(this))

                //미등록 갯수, 등록 갯수
                val regSu = it.filter { it.type == 0 }.size
                val regSuNo = it.filter { it.type == 1 }.size

                binding.totalnotreg.text = "등록차량 : $regSu"
                binding.total.text = "미등록 차량 : $regSuNo"

            }
        })
         */
    }

    /*
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
        binding : CarinfoItemTodayLayoutBinding
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
     */

    private fun createCameraManager() {
        cameraManager = CameraManager(
            this,
            binding.previewViewFinder,
            this,
            binding.graphicOverlayFinder,
            camCarViewModel
        )
    }

    private fun takePicture() {
        // shutter effect
        Toast.makeText(this, "캡처 화면 저장 !!!", Toast.LENGTH_SHORT).show()
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
                /*
            ?.rotateFlipImage(
                cameraManager.rotation,
                cameraManager.isFrontMode()
            )

                 */
            ?.scaleImage(
                binding.previewViewFinder,
                cameraManager.isHorizontalMode()
            )
            ?.let { bitmap ->
                //val originalResizeBmp = Bitmap.createScaledBitmap(bitmap, 800, bitmap.height, false);
                binding.graphicOverlayFinder.processCanvas.drawBitmap(
                    bitmap,
                    0f,
                    bitmap .getBaseYByView(
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

    /* 화면이 다시 돌아올대 닫는다. */
    override fun onResume() {
        super.onResume()
        binding.isReg.isChecked = false
        RegSwitch.setSharedSwitch(regSwitch.OFF)
        RegSwitch.setRegClose()
        init()
    }
}

/* 미등록 스위치와 화면 등록 창 여부를 표시한다. */
object RegSwitch {
    private var regswitch: regSwitch = regSwitch.OFF
    private var regactivytisopen: Int = 0

    fun setSharedSwitch(regswitch: regSwitch) {
        this.regswitch = regswitch
    }
    fun getSharedSwitch(): regSwitch {
        return regswitch
    }

    fun setRegOpen() {
        regactivytisopen = 1
    }

    fun setRegClose() {
        regactivytisopen = 0
    }

    fun getRegIsOPen(): Int {
        return regactivytisopen
    }
}