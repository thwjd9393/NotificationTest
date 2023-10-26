package com.jjgate.notificationtest

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {

    val btn:Button by lazy { findViewById(R.id.btn) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn.setOnClickListener { checkPermision() }

    }

    private fun checkPermision() {

        val checkResult = ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)

        if (checkResult == PackageManager.PERMISSION_DENIED) {
            permissionResultLancher.launch(Manifest.permission.POST_NOTIFICATIONS);
            return
        }

        createNotification()

    }

    private fun createNotification() {

        //알람 관리 객체
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        var builder : NotificationCompat.Builder? = null

        // 오레오 (26버전) 부터 알림 채널 도입
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(NOTICHANNEL,"noti", NotificationManager.IMPORTANCE_LOW)

            notificationManager.createNotificationChannel(channel);

            builder = NotificationCompat.Builder(this, "ch01")

        } else {
            builder = NotificationCompat.Builder(this, "")
        }

        builder.setSmallIcon(R.drawable.small_img)
        builder.setContentTitle("알람 보내기")
        builder.setContentText("notification 알람 연습")

        val intent = Intent(this, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(this, 10 , intent, PendingIntent.FLAG_IMMUTABLE)

        builder.setContentIntent(pendingIntent)

        builder.setAutoCancel(true)

        val notification: Notification = builder.build()

        notificationManager.notify(NOTINUM, notification)

    }

    //퍼미션 받기
    var permissionResultLancher //퍼미션은 글자 덩어리라서 제네릭 String
            : ActivityResultLauncher<String?> =
        registerForActivityResult(ActivityResultContracts.RequestPermission(), object : ActivityResultCallback<Boolean?> {
            override fun onActivityResult(result: Boolean?) {
                if (result == true) Toast.makeText(this@MainActivity, "알림 허용", Toast.LENGTH_SHORT)
                    .show() else Toast.makeText(
                    this@MainActivity,
                    "알림을 보낼 수 없습니다",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    companion object {
        const val NOTICHANNEL = "ch01" //식별 번호
        const val NOTINUM = 1000 //식별 번호
    }
}