package kr.co.toplink.pvms.repository.auth

import kr.co.toplink.pvms.dto.User
import kr.co.toplink.pvms.dto.UserResponse

interface AuthDataSource {
    suspend fun singIn(user: User) : UserResponse
}