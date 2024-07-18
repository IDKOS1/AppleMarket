package com.example.applemarket

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.DialogInterface
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.example.applemarket.data.ProductController
import com.example.applemarket.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        initFloatingButton()

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                dialog("종료", "정말 종료하시겠습니까?")
            }
        })

        binding.ivBell.setOnClickListener {
            Log.i("Noti", "Notification Clicked")
            notification()
        }

        binding.rcvProductsList.apply {
            val products = ProductController.getProducts().toMutableList()
            val adapter = ProductAdapter(products)
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(this@MainActivity)

            val decoration = DividerItemDecoration(this@MainActivity, VERTICAL)
            this.addItemDecoration(decoration)

            adapter.itemClick = object : ProductAdapter.ItemClick {
                override fun onClick(view: View, position: Int) {
                    val intent = Intent(this@MainActivity, DetailActivity::class.java)
                    intent.putExtra("product", products[position])
                    startActivity(intent)
                }
            }
        }

        binding.rcvProductsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // 스크롤 상태에 따라 FloatingActionButton 제어
                if (!recyclerView.canScrollVertically(-1)) {
                    // 최상단에 있을 때
                    binding.btnFloating.visibility = View.INVISIBLE
                } else {
                    // 스크롤이 시작되었을 때
                    binding.btnFloating.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun initFloatingButton() {
        with(binding) {
            btnFloating.setOnClickListener { rcvProductsList.smoothScrollToPosition(0) }
            rcvProductsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                var isVisible = false
                val fadeOut = AnimationUtils.loadAnimation(this@MainActivity, R.anim.fade_out)
                val fadeIn = AnimationUtils.loadAnimation(this@MainActivity, R.anim.fade_in)

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    btnFloating.apply {
                        if (!recyclerView.canScrollVertically(-1)) {
                            startAnimation(fadeOut)
                            isVisible = false
                        }

                        if (!isVisible && dy > 0) {
                            startAnimation(fadeIn)
                            isVisible = true
                        }
                    }
                }
            })
        }
    }

    fun notification() {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val builder: NotificationCompat.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 26 버전 이상
            val channelId = "one-channel"
            val channelName = "My Channel One"
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                // 채널에 다양한 정보 설정
                description = "My Channel One Description"
                setShowBadge(true)
                val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
                setSound(uri, audioAttributes)
                enableVibration(true)
            }
            // 채널을 NotificationManager에 등록
            manager.createNotificationChannel(channel)

            // 채널을 이용하여 builder 생성
            builder = NotificationCompat.Builder(this, channelId)
        } else {
            // 26 버전 이하
            builder = NotificationCompat.Builder(this)
        }

        // API 33 이상 일때 알림 활성화 검사 및 비활성화 일경우 설정화면으로 이동
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                // 알림 권한이 없다면, 사용자에게 권한 요청
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                }
                startActivity(intent)
            }
        }


        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        // 알림의 기본 정보
        builder.run {
            setSmallIcon(R.mipmap.ic_launcher)
            setWhen(System.currentTimeMillis())
            setContentTitle("키워드 알림.")
            setContentText("설정한 키워드에 대한 알림이 도착했습니다!!")
        }
        manager.notify(11, builder.build())
    }

    fun dialog(title: String, content: String) {

        var builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(content)
        builder.setIcon(R.mipmap.ic_launcher)

        val listener = object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                when (p1) {
                    DialogInterface.BUTTON_POSITIVE ->
                        finish()
                }
            }
        }

        builder.setPositiveButton("예", listener)
        builder.setNegativeButton("아니요", listener)

        builder.show()
    }
}