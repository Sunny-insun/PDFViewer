package com.example.pdfviewer.utils

import android.content.Context
import android.graphics.Color
import android.os.AsyncTask
import android.os.Environment
import android.util.Log.i
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.pdfviewer.Application
import com.example.pdfviewer.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import java.util.logging.Logger

object PDFUtil {
    //get pdf input stream
    fun getPDFStream(fileUrl: String): InputStream {
        //pdf file url
        val url = URL(fileUrl)
        // http connection for loading
        val urlConnection = url.openConnection();
        urlConnection.connect()
        //get input stream
        return urlConnection.getInputStream()
    }

    //파일 다운로드
    class AsyncFileDownLoadFromUrl() : AsyncTask<String, Void, Void>() {
        override fun doInBackground(vararg strings: String): Void? {
            savePDFFile(strings[0], strings[1]);
            return null
        }

    }

    fun savePDFFile(fileUrl: String, fileName: String) {
        //vue에서 받아오기.

        //외부 저장소주소에 빈 pdf 파일 만들기.
        val extStorageDirectory = Environment.getExternalStorageDirectory().absolutePath
        val pdfFile = File("$extStorageDirectory/mydata/$fileName")

        //코루틴 비동기 처리
        GlobalScope.launch {

            try {
                //url로 부터 input stream 얻어오기
                val inputStream = getPDFStream(fileUrl)
                //빈파일을 out stream 으로 지정.
                val fileOutputStream = FileOutputStream(pdfFile);
                //BYTE ARRAY
                val buffer =
                        ByteArray(1024 * 1024)
                val bufferLength = inputStream.read(buffer)
                while (bufferLength > 0) {
                    //빈파일에 byte로 파일 쓰기.
                    fileOutputStream.write(buffer, 0, bufferLength)
                }
                fileOutputStream.close();

            } catch (e: Exception) {

            } finally {

            }

        }
    }


    fun createEmptyPdfFile(pdfFile: File) {
        try {
            //파일 생성
            pdfFile.createNewFile()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

    }

    fun showProgressBar(context: Context, text: String) : AlertDialog {

        val llPadding = 30
        val ll = LinearLayout(context)
        ll.orientation = LinearLayout.HORIZONTAL
        ll.setPadding(llPadding, llPadding, llPadding, llPadding)
        ll.gravity = Gravity.CENTER
        var llParam = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam

        val progressBar = ProgressBar(context)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, llPadding, 0)
        progressBar.layoutParams = llParam
        progressBar.indeterminateDrawable = context.resources.getDrawable(R.drawable.common_progressbar)

        val tvText = TextView(context)
        tvText.text = text
        tvText.setTextColor(Color.parseColor("#000000"))
        tvText.textSize = 15f
        tvText.layoutParams = llParam

        ll.addView(progressBar)
        ll.addView(tvText)

        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setCancelable(true)
        builder.setView(ll)

        val dialog: AlertDialog = builder.create()
        if (context != null) {
            Application.mActivity.runOnUiThread {
                dialog.show()
            }
        }

        val window: Window = dialog.getWindow()!!
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.getWindow()!!.getAttributes())
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog.getWindow()!!.setAttributes(layoutParams)
        }

        //  dialog.setCancelable(false)

        return dialog
    }
}