package yue.shen.kotlin.video.sc

import android.content.Context
import android.support.annotation.DrawableRes
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.FrameLayout
import android.widget.ImageView
import java.util.*

abstract class ScVideoPlayerController : FrameLayout, OnTouchListener {

    protected lateinit var mIScVideoPlayer: IScVideoPlayer
    private var mUpdateProgressTimer: Timer? = null
    private var mUpdateProgressTimerTask: TimerTask? = null

    private var mDownX = 0f
    private var mDownY = 0f
    private var mNeedChangePosition = false
    private var mNeedChangeVolume = false
    private var mNeedChangeBrightness = false
    private val THRESHOLD = 80
    private var mGestureDownPosition: Long = 0
    private var mGestureDownBrightness = 0f
    private var mGestureDownVolume = 0
    private var mNewPosition: Long = 0


    constructor(context: Context) : super(context) {
        this.setOnTouchListener(this)
    }


    open fun setNiceVideoPlayer(videoPlayerI: IScVideoPlayer) {
        mIScVideoPlayer = videoPlayerI
    }

    abstract fun setImage(@DrawableRes resId: Int)

    abstract fun imageView(): ImageView

    /**
     * 设置总时长
     */
    abstract fun setLength(length: Long)

    /**
     * 当播放器的播放状态发生变化，在此方法中国你更新不同的播放状态的UI
     */
    abstract fun onPlayStateChanged(playState: Int)

    /**
     * 当播放器的播放模式发生变化，在此方法中更新不同模式下的控制器界面。
     */
    abstract fun onPlayModeChanged(playMode: Int)

    /**
     * 重置控制器，将控制器恢复到初始状态。
     */
    abstract fun reset()

    /**
     * 更新进度，包括进度条进度，展示的当前播放位置时长，总时长等。
     */
    protected abstract fun updateProgress()

    /**
     * 开启更新进度的计时器。
     */
    protected open fun startUpdateProgressTimer() {
        cancelUpdateProgressTimer()
        if (mUpdateProgressTimer == null) {
            mUpdateProgressTimer = Timer()
        }
        if (mUpdateProgressTimerTask == null) {
            mUpdateProgressTimerTask = object : TimerTask() {
                override fun run() {
                    this@ScVideoPlayerController.post(Runnable { updateProgress() })
                }
            }
        }
        mUpdateProgressTimer?.schedule(mUpdateProgressTimerTask, 0, 1000)
    }

    /**
     * 取消更新进度的计时器。
     */
    protected open fun cancelUpdateProgressTimer() {
        if (mUpdateProgressTimer != null) {
            mUpdateProgressTimer!!.cancel()
            mUpdateProgressTimer = null
        }
        if (mUpdateProgressTimerTask != null) {
            mUpdateProgressTimerTask!!.cancel()
            mUpdateProgressTimerTask = null
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        // 只有全屏的时候才能拖动位置、亮度、声音

        // 只有全屏的时候才能拖动位置、亮度、声音
        if (!mIScVideoPlayer.isFullScreen()) {
            return false
        }
        // 只有在播放、暂停、缓冲的时候能够拖动改变位置、亮度和声音
        // 只有在播放、暂停、缓冲的时候能够拖动改变位置、亮度和声音
        if (mIScVideoPlayer.isIdle()
                || mIScVideoPlayer.isError()
                || mIScVideoPlayer.isPreparing()
                || mIScVideoPlayer.isPrepared()
                || mIScVideoPlayer.isCompleted()) {
            hideChangePosition()
            hideChangeBrightness()
            hideChangeVolume()
            return false
        }
        val x = event!!.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mDownX = x
                mDownY = y
                mNeedChangePosition = false
                mNeedChangeVolume = false
                mNeedChangeBrightness = false
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - mDownX
                var deltaY = y - mDownY
                val absDeltaX = Math.abs(deltaX)
                val absDeltaY = Math.abs(deltaY)
                if (!mNeedChangePosition && !mNeedChangeVolume && !mNeedChangeBrightness) {
                    // 只有在播放、暂停、缓冲的时候能够拖动改变位置、亮度和声音
                    if (absDeltaX >= THRESHOLD) {
                        cancelUpdateProgressTimer()
                        mNeedChangePosition = true
                        mGestureDownPosition = mIScVideoPlayer.getCurrentPosition()
                    } else if (absDeltaY >= THRESHOLD) {
                        if (mDownX < width * 0.5f) {
                            // 左侧改变亮度
                            mNeedChangeBrightness = true
                            mGestureDownBrightness = ScUtil.scanForActivity(context)
                                    ?.window?.attributes?.screenBrightness?:0f
                        } else {
                            // 右侧改变声音
                            mNeedChangeVolume = true
                            mGestureDownVolume = mIScVideoPlayer.getVolume()
                        }
                    }
                }
                if (mNeedChangePosition) {
                    val duration: Long = mIScVideoPlayer.getDuration()
                    val toPosition = (mGestureDownPosition + duration * deltaX / width).toLong()
                    mNewPosition = 0.coerceAtLeast(duration.coerceAtMost(toPosition).toInt()).toLong()
                    val newPositionProgress = (100f * mNewPosition / duration).toInt()
                    showChangePosition(duration, newPositionProgress)
                }
                if (mNeedChangeBrightness) {
                    deltaY = -deltaY
                    val deltaBrightness = deltaY * 3 / height
                    var newBrightness = mGestureDownBrightness + deltaBrightness
                    newBrightness = 0f.coerceAtLeast(newBrightness.coerceAtMost(1f))
                    val newBrightnessPercentage = newBrightness
                    val params = ScUtil.scanForActivity(context)?.window?.attributes
                    params?.screenBrightness = newBrightnessPercentage
                    ScUtil.scanForActivity(context)?.window?.attributes = params
                    val newBrightnessProgress = (100f * newBrightnessPercentage).toInt()
                    showChangeBrightness(newBrightnessProgress)
                }
                if (mNeedChangeVolume) {
                    deltaY = -deltaY
                    val maxVolume: Int = mIScVideoPlayer.getMaxVolume()
                    val deltaVolume = (maxVolume * deltaY * 3 / height).toInt()
                    var newVolume = mGestureDownVolume + deltaVolume
                    newVolume = 0.coerceAtLeast(maxVolume.coerceAtMost(newVolume))
                    mIScVideoPlayer.setVolume(newVolume)
                    val newVolumeProgress = (100f * newVolume / maxVolume).toInt()
                    showChangeVolume(newVolumeProgress)
                }
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                if (mNeedChangePosition) {
                    mIScVideoPlayer.seekTo(mNewPosition)
                    hideChangePosition()
                    startUpdateProgressTimer()
                    return true
                }
                if (mNeedChangeBrightness) {
                    hideChangeBrightness()
                    return true
                }
                if (mNeedChangeVolume) {
                    hideChangeVolume()
                    return true
                }
            }
        }
        return false
    }

    /**
     * 手势左右滑动改变播放位置时，显示控制器中间的播放位置变化视图，
     * 在手势滑动ACTION_MOVE的过程中，会不断调用此方法。
     *
     * @param duration            视频总时长ms
     * @param newPositionProgress 新的位置进度，取值0到100。
     */
    protected abstract fun showChangePosition(duration: Long, newPositionProgress: Int)

    /**
     * 手势左右滑动改变播放位置后，手势up或者cancel时，隐藏控制器中间的播放位置变化视图，
     * 在手势ACTION_UP或ACTION_CANCEL时调用。
     */
    protected abstract fun hideChangePosition()

    /**
     * 手势在右侧上下滑动改变音量时，显示控制器中间的音量变化视图，
     * 在手势滑动ACTION_MOVE的过程中，会不断调用此方法。
     *
     * @param newVolumeProgress 新的音量进度，取值1到100。
     */
    protected abstract fun showChangeVolume(newVolumeProgress: Int)

    /**
     * 手势在左侧上下滑动改变音量后，手势up或者cancel时，隐藏控制器中间的音量变化视图，
     * 在手势ACTION_UP或ACTION_CANCEL时调用。
     */
    protected abstract fun hideChangeVolume()

    /**
     * 手势在左侧上下滑动改变亮度时，显示控制器中间的亮度变化视图，
     * 在手势滑动ACTION_MOVE的过程中，会不断调用此方法。
     *
     * @param newBrightnessProgress 新的亮度进度，取值1到100。
     */
    protected abstract fun showChangeBrightness(newBrightnessProgress: Int)

    /**
     * 手势在左侧上下滑动改变亮度后，手势up或者cancel时，隐藏控制器中间的亮度变化视图，
     * 在手势ACTION_UP或ACTION_CANCEL时调用。
     */
    protected abstract fun hideChangeBrightness()
}