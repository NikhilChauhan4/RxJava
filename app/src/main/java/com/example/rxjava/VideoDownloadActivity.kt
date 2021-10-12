package com.example.rxjava

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rxjava.appconstants.AppConstants
import com.example.rxjava.model.Video
import com.example.rxjava.recyclerview.VideoAdapter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subscribers.DisposableSubscriber
import okhttp3.OkHttpClient
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class VideoDownloadActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    var videoList: ArrayList<Video> = ArrayList()
    var videoAdapter = VideoAdapter(videoList)
    lateinit var compositeDisposable: CompositeDisposable
    private val videoDownloader by lazy {
        VideoDownloader(
            OkHttpClient.Builder().build()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_download)
        recyclerView = findViewById(R.id.recycler_view)
        compositeDisposable = CompositeDisposable()
        videoAdapter.setOnItemClickListener(object : VideoAdapter.ClickListener {
            override fun onItemClick(position: Int, v: View?) {
                Log.d(AppConstants.TAG_VIDEO_DOWNLOAD_ACTIVITY, "onItemClick: " + "item clicked")
                downloadVideoFile(videoList[position].videoDownloadLink)
            }

        })
        addItemsToList()
        setupRecyclerAdapter()

    }

    private fun addItemsToList() {
        for (i in 0..10) {
            videoList.add(
                Video(
                    i,
                    "Bunny_Video",
                    "https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/1080/Big_Buck_Bunny_1080_10s_30MB.mp4"
                )
            )
        }
    }


    private fun setupRecyclerAdapter() {
        recyclerView.adapter = VideoAdapter(videoList)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    fun downloadVideoFile(url: String) {
        val root = applicationContext.getExternalFilesDir(null)?.absolutePath.toString()
        Log.d(AppConstants.TAG_VIDEO_DOWNLOAD_ACTIVITY, "onCreate: root dir " + root)
        val myDir = File("$root/saved_images")
        if (!myDir.exists()) {
            myDir.mkdirs()
        }
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val targetFile = File(myDir, "video_" + timeStamp + ".mp4")


        Log.d(
            AppConstants.TAG_VIDEO_DOWNLOAD_ACTIVITY,
            "downloadVideoFile: " + compositeDisposable.size()
        )
        try {

            compositeDisposable.add(
                videoDownloader.downloadVideo(
                    url,
                    targetFile
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSubscriber<Int>() {
                        override fun onNext(t: Int) {
                            Log.d(AppConstants.TAG_VIDEO_DOWNLOAD_ACTIVITY, "onNext: " + t)
                        }

                        override fun onError(t: Throwable?) {
                            Log.d(
                                AppConstants.TAG_VIDEO_DOWNLOAD_ACTIVITY,
                                "onError: " + t?.message
                            )
                            Toast.makeText(
                                this@VideoDownloadActivity,
                                "Download failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onComplete() {
                            Toast.makeText(
                                this@VideoDownloadActivity,
                                "Downloaded Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d(
                                AppConstants.TAG_VIDEO_DOWNLOAD_ACTIVITY,
                                "onComplete: " + "download successful"
                            )
                        }

                    })
            )
        } catch (e: Exception) {
            Log.d(
                AppConstants.TAG_VIDEO_DOWNLOAD_ACTIVITY,
                "downloadVideoFile: " + "error " + e.message
            )
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d(AppConstants.TAG_VIDEO_DOWNLOAD_ACTIVITY, "onStop: " + "inside onStop")
        compositeDisposable.clear()
    }
}