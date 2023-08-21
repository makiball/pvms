package kr.co.toplink.pvms.repository.auth

import kr.co.toplink.pvms.dto.User
import kr.co.toplink.pvms.dto.UserResponse
import kr.co.toplink.pvms.network.api.AuthApi

class AuthRemoteDataSource(private val apiClient : AuthApi) : AuthDataSource {

    override suspend fun singIn(user: User): UserResponse {
        return apiClient.signIn(user)
    }
}