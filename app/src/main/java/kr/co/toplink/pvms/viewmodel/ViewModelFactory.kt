package kr.co.toplink.pvms.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.co.toplink.pvms.config.ApplicationClass

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CarInfoViewModel::class.java) -> {
                CarInfoViewModel(ApplicationClass().provideCarRepository(context)) as T
                //CarInfoViewModel(car)
            }

            modelClass.isAssignableFrom(SmsMngViewModel::class.java) -> {
                SmsMngViewModel(ApplicationClass().provideSmsRepository(context)) as T
            }

            modelClass.isAssignableFrom(ExcellViewModel::class.java) -> {
                ExcellViewModel() as T
            }

            else -> {
                throw IllegalArgumentException("Failed to create ViewModel: ${modelClass.name}")
            }
        }
    }

}