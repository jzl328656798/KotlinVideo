package yue.shen.kotlin.video.sc

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.ContextThemeWrapper
import android.util.TypedValue
import android.view.WindowManager
import java.util.*

object ScUtil {

    fun scanForActivity(context: Context?): Activity? {
        if (context == null) return null
        if (context is Activity) {
            return context
        } else if (context is ContextWrapper) {
            return scanForActivity(context.baseContext)
        }
        return null
    }

    private fun getAppCompActivity(context: Context?): AppCompatActivity? {
        if (context == null) return null
        if (context is AppCompatActivity) {
            return context
        } else if (context is ContextThemeWrapper) {
            return getAppCompActivity(context.baseContext)
        }
        return null
    }

    @SuppressLint("RestrictedApi")
    fun showActionBar(context: Context?) {
        val ab = getAppCompActivity(context)?.getSupportActionBar()
        if (ab != null) {
            ab.setShowHideAnimationEnabled(false)
            ab.show()
        }
        scanForActivity(context)
                ?.getWindow()
                ?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    @SuppressLint("RestrictedApi")
    fun hideActionBar(context: Context?) {
        val ab =getAppCompActivity(context)?.getSupportActionBar()
        if (ab != null) {
            ab.setShowHideAnimationEnabled(false)
            ab.hide()
        }
        scanForActivity(context)
                ?.getWindow()
                ?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    fun getScreenWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    fun getScreenHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    fun dp2px(context: Context, dpVal: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal,
                context.resources.displayMetrics).toInt()
    }

    fun formatTime(milliseconds: Long): String {
        if (milliseconds <= 0 || milliseconds >= 24 * 60 * 60 * 1000) {
            return "00:00"
        }
        val totalSeconds = milliseconds / 1000
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600
        val stringBuilder = StringBuilder()
        val mFormatter = Formatter(stringBuilder, Locale.getDefault())
        return if (hours > 0) {
            mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString()
        } else {
            mFormatter.format("%02d:%02d", minutes, seconds).toString()
        }
    }

    fun savePlayPosition(context: Context, url: String, position: Long) {
        context.getSharedPreferences("NICE_VIDEO_PALYER_PLAY_POSITION",
                Context.MODE_PRIVATE)
                .edit()
                .putLong(url, position)
                .apply()
    }

    fun getSavedPlayPosition(context: Context, url: String): Long {
        return context.getSharedPreferences("NICE_VIDEO_PALYER_PLAY_POSITION",
                Context.MODE_PRIVATE)
                .getLong(url, 0)
    }
}