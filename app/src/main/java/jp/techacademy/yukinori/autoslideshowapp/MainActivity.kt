package jp.techacademy.yukinori.autoslideshowapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.provider.MediaStore
import android.content.ContentUris
import kotlinx.android.synthetic.main.activity_main.*
import  android.os.Handler
import java.util.*

class MainActivity : AppCompatActivity() {

    private val PERMISSIONS_REQUEST_CODE = 100

    private var mTimer: Timer? = null
    private var mTimerSec = 0.0
    private var mHandler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Android 6.0以降の場合
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                getContentsInfo()
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSIONS_REQUEST_CODE
                )
            }
            // Android 5系以下の場合
        } else {
            getContentsInfo()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo()
                }
        }
    }

    private fun getContentsInfo() {
        // 画像の情報を取得する
        val resolver = contentResolver
        val cursor = resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            null
        )


        if (cursor!!.moveToFirst()) {
            val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
            val id = cursor.getLong(fieldIndex)
            val imageUri =
                ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

            Log.d("ANDROID", "URI :" + imageUri.toString())
            imageView.setImageURI(imageUri)
        }

        next_button.setOnClickListener {
            if (cursor.isLast()) {
                cursor.moveToFirst()
                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor.getLong(fieldIndex)
                val imageUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                Log.d("ANDROID", "URI :" + imageUri.toString())
                imageView.setImageURI(imageUri)
            } else {
                cursor.moveToNext()
                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor.getLong(fieldIndex)
                val imageUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                Log.d("ANDROID", "URI :" + imageUri.toString())
                imageView.setImageURI(imageUri)
            }

        }

        back_button.setOnClickListener {
            if (cursor.isFirst()) {
                cursor.moveToLast()
                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor.getLong(fieldIndex)
                val imageUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                Log.d("ANDROID", "URI :" + imageUri.toString())
                imageView.setImageURI(imageUri)
            } else {
                cursor.moveToPrevious()
                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor.getLong(fieldIndex)
                val imageUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                Log.d("ANDROID", "URI :" + imageUri.toString())
                imageView.setImageURI(imageUri)
            }

        }

        start_stop_button.setOnClickListener{
            if (mTimerSec == 0.0) {
                start_stop_button.text = "停止"
                mTimer = Timer()
                mTimer!!.schedule(object : TimerTask(){
                    override fun run() {
                        mTimerSec += 2.0
                        mHandler.post{
                            if (cursor.isLast()) {
                                cursor.moveToFirst()
                                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                                val id = cursor.getLong(fieldIndex)
                                val imageUri =
                                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                                Log.d("ANDROID", "URI :" + imageUri.toString())
                                imageView.setImageURI(imageUri)
                            } else {
                                cursor.moveToNext()
                                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                                val id = cursor.getLong(fieldIndex)
                                val imageUri =
                                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                                Log.d("ANDROID", "URI :" + imageUri.toString())
                                imageView.setImageURI(imageUri)
                            }
                        }
                    }
                }, 2000, 2000)
                next_button.isClickable = false
                back_button.isClickable = false
            } else {
                start_stop_button.text = "再生"
                mTimer!!.cancel()
                mTimerSec = 0.0
                next_button.isClickable = true
                back_button.isClickable = true
            }


        }


    }
}