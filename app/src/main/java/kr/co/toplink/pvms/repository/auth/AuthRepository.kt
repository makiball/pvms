package kr.co.toplink.pvms.repository.auth

import kr.co.toplink.pvms.dto.User
import kr.co.toplink.pvms.dto.UserResponse

class AuthRepository(private val remoteDataSource: AuthRemoteDataSource) {
    suspend fun signIn(user : User) : UserResponse {
        return remoteDataSource.singIn(user)
    }
}