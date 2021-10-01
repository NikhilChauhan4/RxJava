package com.example.rxjava

import android.util.Log
import com.example.rxjava.appconstants.AppConstants
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit


class VideoDownloader(okHttpClient: OkHttpClient) {
    private var okHttpClient: OkHttpClient
    companion object {
        private const val BUFFER_LENGTH_BYTES = 1024 * 500
        private const val HTTP_TIMEOUT = 30
    }
    init {
        val okHttpBuilder = okHttpClient.newBuilder()
            .connectTimeout(HTTP_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(HTTP_TIMEOUT.toLong(), TimeUnit.SECONDS)
        this.okHttpClient = okHttpBuilder.build()
    }

    fun downloadVideo(url: String, file: File): Flowable<Int> {
        return Flowable.create( { emitter ->
            val request = Request.Builder().url(url).build()
            val response = okHttpClient.newCall(request).execute()
            val body = response.body()
            val responseCode = response.code()
            Log.d(AppConstants.TAG_VIDEO_DOWNLOADER, "download: "+responseCode.toString())
            Log.d(AppConstants.TAG_VIDEO_DOWNLOADER, "download: "+body.toString())
            if (responseCode >= HttpURLConnection.HTTP_OK &&
                responseCode < HttpURLConnection.HTTP_MULT_CHOICE &&
                body != null) {
                val length = body.contentLength()
                body.byteStream().apply {
                    file.outputStream().use { fileOut ->
                        var bytesCopied = 0
                        val buffer = ByteArray(BUFFER_LENGTH_BYTES)
                        var bytes = read(buffer)
                        while (bytes >= 0) {
                            fileOut.write(buffer, 0, bytes)
                            bytesCopied += bytes
                            bytes = read(buffer)
                            emitter.onNext(((bytesCopied * 100)/length).toInt())
                        }
                    }
                    emitter.onComplete()
                }
            } else {
                throw IllegalArgumentException("Error occurred when do http get $url")
            }
        },BackpressureStrategy.LATEST)
    }
}
