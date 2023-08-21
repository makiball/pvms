package kr.co.toplink.pvms.util

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kr.co.toplink.pvms.data.SendKakaoAlrim
import kr.co.toplink.pvms.data.SerializeConvert
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class JsonReceiver {

    private val client = OkHttpClient()

    fun fetchJson(url: String, callback: JsonCallback) {
        val request = createRequest(url)
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                callback.onFailure(e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                if (response.isSuccessful && responseData != null) {
                    try {
                        val json = JSONObject(responseData)
                        callback.onSuccess(json)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        callback.onFailure("JSON 파싱 오류")
                    }
                } else {
                    callback.onFailure("응답 오류 - 상태 코드: ${response.code}")
                }
            }
        })
    }



    private fun createRequest(url: String): Request {

        val data = SerializeConvert("test","test","test")
        val json = Json.encodeToString(data)
        val mediaType = "application/json".toMediaType()
        val requestBody = json.toRequestBody(mediaType)

        return Request.Builder()
            .url(url)
            .post(requestBody)
            .build()
    }
}

interface JsonCallback {
    fun onSuccess(json: JSONObject)
    fun onFailure(errorMessage: String?)
}