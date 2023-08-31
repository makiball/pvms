package kr.co.toplink.pvms.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.co.toplink.pvms.dto.User
import kr.co.toplink.pvms.repository.auth.AuthRepository
import kr.co.toplink.pvms.Event

class UserViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user : LiveData<User> = _user

    private val _userPo = MutableLiveData<Event<Int>>()
    val userPo : LiveData<Event<Int>> = _userPo


}