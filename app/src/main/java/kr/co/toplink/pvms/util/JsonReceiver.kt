package kr.co.toplink.pvms.util

import okhttp3.*
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
        return Request.Builder()
            .url(url)
            .build()
    }
}

interface JsonCallback {
    fun onSuccess(json: JSONObject)
    fun onFailure(errorMessage: String?)
}