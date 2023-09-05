package kr.co.toplink.pvms.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.toplink.pvms.dto.User
import kr.co.toplink.pvms.repository.auth.AuthRepository
import kr.co.toplink.pvms.Event
import kr.co.toplink.pvms.data.KakaoAlim
import kr.co.toplink.pvms.dto.UserResponse
import kr.co.toplink.pvms.network.response.KakaoAlrimResponse

class UserViewModel(private val authRepository: AuthRepository) : ViewModel() {

    /*
    private val _user = MutableLiveData<User>()
    val user : LiveData<User> = _user
     */

    private val _userPo = MutableLiveData<Event<Int>>()
    val userPo : LiveData<Event<Int>> = _userPo

    private val _userResponse  = MutableLiveData<Event<UserResponse>>()
    val userResponse : LiveData<Event<UserResponse>> = _userResponse

    private val _kakaoalrimresponse = MutableLiveData<Event<KakaoAlrimResponse>>()
    val kakaoalrimresponse : LiveData<Event<KakaoAlrimResponse>> = _kakaoalrimresponse

    fun signIn(user: User) {
        viewModelScope.launch{
            val userResponse = authRepository.signIn(user)
            _userResponse.value = Event(userResponse)
        }
    }

    fun getPoint(user: User) {
        viewModelScope.launch{
            val userResponse = authRepository.signIn(user)
            _userPo.value = Event(userResponse.smsPoint)
        }
    }

    fun kakaoAlimSend(kakaoalim : KakaoAlim) {
        viewModelScope.launch {
            val kakaoalrimresponse = authRepository.kakaoAlimSend(kakaoalim)
            _kakaoalrimresponse.value = Event(kakaoalrimresponse)
        }
    }

}