package com.example.pdfviewer

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.pdfviewer.Application.Companion.mActivity
import com.example.pdfviewer.utils.PDFUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    var fileUrl: String = ""
    lateinit var dialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);
        init()
    }

    fun init() {
        println("pdfviewer 실행")
        mActivity = this;
        dialog = PDFUtil.showProgressBar(this, "데이터를 불러오는 중 입니다.")
        makeFullScreen()

        //get stream from url
        GlobalScope.launch {
            val inputStream = PDFUtil.getPDFStream(fileUrl)
            pdfView.fromStream(inputStream)
                    .onLoad { dialog.hide() }
                    .load()
            //PDFUtils.AsyncFileDownLoadFromUrl().execute(fileUrl, "sample.pdf")
        }
        pdfView.visibility = View.VISIBLE
    }

    private fun makeFullScreen() {

        val decorView = window.decorView
        var uiOption = window.decorView.systemUiVisibility
        uiOption = uiOption or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        uiOption = uiOption or View.SYSTEM_UI_FLAG_FULLSCREEN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) uiOption =
                uiOption or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        decorView.systemUiVisibility = uiOption
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onDestroy() {
        dialog.dismiss()
        super.onDestroy()
    }
}