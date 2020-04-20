package yue.shen.kotlin.video.sc

import android.R
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.SurfaceTexture
import android.media.AudioManager
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.Surface
import android.view.TextureView.SurfaceTextureListener
import android.view.ViewGroup
import android.widget.FrameLayout
import tv.danmaku.ijk.media.player.AndroidMediaPlayer
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import java.io.IOException

class ScVideoPlayer : FrameLayout, IScVideoPlayer, SurfaceTextureListener {


    companion object {
        /**
         * 播放错误
         */
        const val STATE_ERROR = -1

        /**
         * 播放未开始
         */
        const val STATE_IDLE = 0

        /**
         * 播放准备中
         */
        const val STATE_PREPARING = 1

        /**
         * 播放准备就绪
         */
        const val STATE_PREPARED = 2

        /**
         * 正在播放
         */
        const val STATE_PLAYING = 3

        /**
         * 暂停播放
         */
        const val STATE_PAUSED = 4

        /**
         * 正在缓冲(播放器正在播放时，缓冲区数据不足，进行缓冲，缓冲区数据足够后恢复播放)
         */
        const val STATE_BUFFERING_PLAYING = 5

        /**
         * 正在缓冲(播放器正在播放时，缓冲区数据不足，进行缓冲，此时暂停播放器，继续缓冲，缓冲区数据足够后恢复暂停
         */
        const val STATE_BUFFERING_PAUSED = 6

        /**
         * 播放完成
         */
        const val STATE_COMPLETED = 7

        /**
         * 普通模式
         */
        const val MODE_NORMAL = 10

        /**
         * 全屏模式
         */
        const val MODE_FULL_SCREEN = 11

        /**
         * 小窗口模式
         */
        const val MODE_TINY_WINDOW = 12

        /**
         * IjkPlayer
         */
        const val TYPE_IJK = 111

        /**
         * MediaPlayer
         */
        const val TYPE_NATIVE = 222
    }


    private var mPlayerType: Int = TYPE_NATIVE
    private var mCurrentState: Int = STATE_IDLE
    private var mCurrentMode: Int = MODE_NORMAL

    private var mAudioManager: AudioManager? = null
    private var mMediaPlayer: IMediaPlayer? = null
    private lateinit var mContainer: FrameLayout
    private var mTextureView: ScTextureView? = null
    private var mController: ScVideoPlayerController? = null
    private var mSurfaceTexture: SurfaceTexture? = null
    private var mSurface: Surface? = null
    private lateinit var mUrl: String
    private var mBufferPercentage = 0
    private var continueFromLastPosition = true
    private var skipToPosition: Long = 0

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }


    private fun initView() {
        mContainer = FrameLayout(context)
        mContainer.setBackgroundColor(Color.BLACK)
        val params = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        this.addView(mContainer, params)
    }

    override fun setUp(url: String) {
        mUrl = url
    }

    fun setController(controller: ScVideoPlayerController) {
        mContainer.removeView(mController)
        mController = controller
        mController?.reset()
        mController?.setNiceVideoPlayer(this)
        val params = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        mContainer.addView(mController, params)
    }

    fun setPlayerType(playerType: Int) {
        mPlayerType = playerType
    }

    override fun continueFromLastPosition(continueFromLastPosition: Boolean) {
        this.continueFromLastPosition = continueFromLastPosition
    }

    private fun log(text: String) {
        Log.i("queen", text)
    }

    override fun setSpeed(speed: Float) {
        if (mMediaPlayer is IjkMediaPlayer) {
            (mMediaPlayer as IjkMediaPlayer).setSpeed(speed)
        } else {
            log("设置播放速度出错")
        }
    }


    override fun start() {
        if (mCurrentState == STATE_IDLE) {
            ScVideoPlayerManager.setCurrentNiceVideoPlayer(this)
            initAudioManager()
            initMediaPlayer()
            initTextureView()
            addTextureView()
        } else {
            log("NiceVideoPlayer只有在mCurrentState == STATE_IDLE时才能调用start方法.")
        }
    }


    override fun start(position: Long) {
        skipToPosition = position
        start()
    }

    override fun restart() {
        if (mCurrentState == STATE_PAUSED) {
            mMediaPlayer?.start()
            mCurrentState = STATE_PLAYING
            mController?.onPlayStateChanged(mCurrentState)
            log("STATE_PLAYING")
        } else if (mCurrentState == STATE_BUFFERING_PAUSED) {
            mMediaPlayer?.start()
            mCurrentState = STATE_BUFFERING_PLAYING
            mController?.onPlayStateChanged(mCurrentState)
            log("STATE_BUFFERING_PLAYING")
        } else if (mCurrentState == STATE_COMPLETED || mCurrentState == STATE_ERROR) {
            mMediaPlayer?.reset()
            openMediaPlayer()
        } else {
            log("NiceVideoPlayer在mCurrentState == " + mCurrentState + "时不能调用restart()方法.")
        }
    }

    override fun pause() {
        if (mCurrentState == STATE_PLAYING) {
            mMediaPlayer?.pause()
            mCurrentState = STATE_PAUSED
            mController?.onPlayStateChanged(mCurrentState)
            log("STATE_PAUSED")
        }
        if (mCurrentState == STATE_BUFFERING_PLAYING) {
            mMediaPlayer?.pause()
            mCurrentState = STATE_BUFFERING_PAUSED
            mController?.onPlayStateChanged(mCurrentState)
            log("STATE_BUFFERING_PAUSED")
        }
    }

    override fun seekTo(pos: Long) {
        mMediaPlayer?.seekTo(pos)

    }

    override fun setVolume(volume: Int) {
        mAudioManager?.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0)
    }

    override fun isIdle(): Boolean {
        return mCurrentState == STATE_IDLE
    }

    override fun isPreparing(): Boolean {
        return mCurrentState == STATE_PREPARING
    }

    override fun isPrepared(): Boolean {
        return mCurrentState == STATE_PREPARED
    }

    override fun isBufferingPlaying(): Boolean {
        return mCurrentState == STATE_BUFFERING_PLAYING
    }

    override fun isBufferingPaused(): Boolean {
        return mCurrentState == STATE_BUFFERING_PAUSED
    }

    override fun isPlaying(): Boolean {
        return mCurrentState == STATE_PLAYING
    }

    override fun isPaused(): Boolean {
        return mCurrentState == STATE_PAUSED
    }

    override fun isError(): Boolean {
        return mCurrentState == STATE_ERROR
    }

    override fun isCompleted(): Boolean {
        return mCurrentState == STATE_COMPLETED
    }

    override fun isFullScreen(): Boolean {
        return mCurrentMode == MODE_FULL_SCREEN
    }

    override fun isTinyWindow(): Boolean {
        return mCurrentMode == MODE_TINY_WINDOW
    }

    override fun isNormal(): Boolean {
        return mCurrentMode == MODE_NORMAL
    }

    override fun getMaxVolume(): Int {
        return mAudioManager?.getStreamMaxVolume(AudioManager.STREAM_MUSIC) ?: 0
    }

    override fun getVolume(): Int {
        return mAudioManager?.getStreamVolume(AudioManager.STREAM_MUSIC) ?: 0
    }

    override fun getDuration(): Long {
        return mMediaPlayer?.duration ?: 0
    }

    override fun getCurrentPosition(): Long {
        return mMediaPlayer?.currentPosition ?: 0
    }

    override fun getBufferPercentage(): Int {
        return mBufferPercentage
    }

    override fun getSpeed(speed: Float): Float {
        return if (mMediaPlayer is IjkMediaPlayer) {
            (mMediaPlayer as IjkMediaPlayer).getSpeed(speed)
        } else {
            0f
        }
    }

    override fun getTcpSpeed(): Long {
        return if (mMediaPlayer is IjkMediaPlayer) {
            (mMediaPlayer as IjkMediaPlayer).tcpSpeed
        } else 0
    }

    private fun initAudioManager() {
        mAudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        mAudioManager?.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
    }

    private fun initMediaPlayer() {
        mMediaPlayer = when (mPlayerType) {
            TYPE_NATIVE -> AndroidMediaPlayer()
            TYPE_IJK -> IjkMediaPlayer()
            else -> IjkMediaPlayer()
        }
        mMediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)

    }

    private fun initTextureView() {
        if (mTextureView == null) {
            mTextureView = ScTextureView(context)
            mTextureView?.surfaceTextureListener = this
        }
    }

    private fun addTextureView() {
        mContainer.removeView(mTextureView)
        val params = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.CENTER)
        mContainer.addView(mTextureView, 0, params)
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
        if (mSurfaceTexture == null) {
            mSurfaceTexture = surface
            openMediaPlayer()
        } else {
            mTextureView?.surfaceTexture = mSurfaceTexture
        }
    }

    private fun openMediaPlayer() {
        // 屏幕常亮
        mContainer.keepScreenOn = true
        // 设置监听
        mMediaPlayer?.setOnPreparedListener(mOnPreparedListener)
        mMediaPlayer?.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener)
        mMediaPlayer?.setOnCompletionListener(mOnCompletionListener)
        mMediaPlayer?.setOnErrorListener(mOnErrorListener)
        mMediaPlayer?.setOnInfoListener(mOnInfoListener)
        mMediaPlayer?.setOnBufferingUpdateListener(mOnBufferingUpdateListener)
        // 设置dataSource
        try {
            mMediaPlayer?.setDataSource(context.applicationContext, Uri.parse(mUrl))
            if (mSurface == null) {
                mSurface = Surface(mSurfaceTexture)
            }
            mMediaPlayer?.setSurface(mSurface)
            mMediaPlayer?.prepareAsync()
            mCurrentState = STATE_PREPARING
            mController?.onPlayStateChanged(mCurrentState)
            log("STATE_PREPARING")
        } catch (e: IOException) {
            e.printStackTrace()
            log("打开播放器发生错误$e")
        }
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {}

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        return mSurfaceTexture == null
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {}

    private val mOnPreparedListener = IMediaPlayer.OnPreparedListener { mp ->
        mCurrentState = STATE_PREPARED
        mController?.onPlayStateChanged(mCurrentState)
        log("onPrepared ——> STATE_PREPARED")
        mp.start()
        // 从上次的保存位置播放
        if (continueFromLastPosition) {
            val savedPlayPosition: Long = ScUtil.getSavedPlayPosition(context, mUrl)
            mp.seekTo(savedPlayPosition)
        }
        // 跳到指定位置播放
        if (skipToPosition != 0L) {
            mp.seekTo(skipToPosition)
        }
    }

    private val mOnVideoSizeChangedListener = IMediaPlayer.OnVideoSizeChangedListener { mp, width, height, sar_num, sar_den ->
        mTextureView?.adaptVideoSize(width, height)
        log("onVideoSizeChanged ——> width：$width， height：$height")
    }

    private val mOnCompletionListener = IMediaPlayer.OnCompletionListener {
        mCurrentState = STATE_COMPLETED
        mController?.onPlayStateChanged(mCurrentState)
        log("onCompletion ——> STATE_COMPLETED")
        // 清除屏幕常亮
        mContainer.keepScreenOn = false
    }

    private val mOnErrorListener = IMediaPlayer.OnErrorListener { mp, what, extra -> // 直播流播放时去调用mediaPlayer.getDuration会导致-38和-2147483648错误，忽略该错误
        if (what != -38 && what != -2147483648 && extra != -38 && extra != -2147483648) {
            mCurrentState = STATE_ERROR
            mController?.onPlayStateChanged(mCurrentState)
            log("onError ——> STATE_ERROR ———— what：$what, extra: $extra")
        }
        true
    }

    private val mOnInfoListener = IMediaPlayer.OnInfoListener { mp, what, extra ->
        if (what == IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
            // 播放器开始渲染
            mCurrentState = STATE_PLAYING
            mController?.onPlayStateChanged(mCurrentState)
            log("onInfo ——> MEDIA_INFO_VIDEO_RENDERING_START：STATE_PLAYING")
        } else if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_START) {
            // MediaPlayer暂时不播放，以缓冲更多的数据
            if (mCurrentState == STATE_PAUSED || mCurrentState == STATE_BUFFERING_PAUSED) {
                mCurrentState = STATE_BUFFERING_PAUSED
                log("onInfo ——> MEDIA_INFO_BUFFERING_START：STATE_BUFFERING_PAUSED")
            } else {
                mCurrentState = STATE_BUFFERING_PLAYING
                log("onInfo ——> MEDIA_INFO_BUFFERING_START：STATE_BUFFERING_PLAYING")
            }
            mController?.onPlayStateChanged(mCurrentState)
        } else if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_END) {
            // 填充缓冲区后，MediaPlayer恢复播放/暂停
            if (mCurrentState == STATE_BUFFERING_PLAYING) {
                mCurrentState = STATE_PLAYING
                mController?.onPlayStateChanged(mCurrentState)
                log("onInfo ——> MEDIA_INFO_BUFFERING_END： STATE_PLAYING")
            }
            if (mCurrentState == STATE_BUFFERING_PAUSED) {
                mCurrentState = STATE_PAUSED
                mController?.onPlayStateChanged(mCurrentState)
                log("onInfo ——> MEDIA_INFO_BUFFERING_END： STATE_PAUSED")
            }
        } else if (what == IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED) {
            // 视频旋转了extra度，需要恢复
            mTextureView?.rotation = extra.toFloat()
            log("视频旋转角度：$extra")
        } else if (what == IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE) {
            log("视频不能seekTo，为直播视频")
        } else {
            log("onInfo ——> what：$what")
        }
        true
    }


    private val mOnBufferingUpdateListener = IMediaPlayer.OnBufferingUpdateListener { mp, percent -> mBufferPercentage = percent }


    override fun enterFullScreen() {
        if (mCurrentMode == MODE_FULL_SCREEN) return

        // 隐藏ActionBar、状态栏，并横屏

        // 隐藏ActionBar、状态栏，并横屏
        ScUtil.hideActionBar(context)
        ScUtil.scanForActivity(context)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        val contentView = ScUtil.scanForActivity(context)?.findViewById<ViewGroup>(R.id.content)
        if (mCurrentMode == MODE_TINY_WINDOW) {
            contentView?.removeView(mContainer)
        } else {
            removeView(mContainer)
        }
        val params = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        contentView?.addView(mContainer, params)

        mCurrentMode = MODE_FULL_SCREEN
        mController?.onPlayModeChanged(mCurrentMode)
        log("MODE_FULL_SCREEN")
    }


    override fun exitFullScreen(): Boolean {
        if (mCurrentMode == MODE_FULL_SCREEN) {
            ScUtil.showActionBar(context)
            ScUtil.scanForActivity(context)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            val contentView = ScUtil.scanForActivity(context)?.findViewById<ViewGroup>(R.id.content)
            contentView?.removeView(mContainer)
            val params = LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
            this.addView(mContainer, params)
            mCurrentMode = MODE_NORMAL
            mController?.onPlayModeChanged(mCurrentMode)
            log("MODE_NORMAL")
            return true
        }
        return false
    }

    override fun enterTinyWindow() {
        if (mCurrentMode == MODE_TINY_WINDOW) return
        removeView(mContainer)

        // 小窗口的宽度为屏幕宽度的60%，长宽比默认为16:9，右边距、下边距为8dp。
        // 小窗口的宽度为屏幕宽度的60%，长宽比默认为16:9，右边距、下边距为8dp。
        val params = LayoutParams(
                (ScUtil.getScreenWidth(context) * 0.6f) as Int,
                (ScUtil.getScreenWidth(context) * 0.6f * 9f / 16f) as Int)
        params.gravity = Gravity.BOTTOM or Gravity.END
        params.rightMargin = ScUtil.dp2px(context, 8f)
        params.bottomMargin = ScUtil.dp2px(context, 8f)

        ScUtil.scanForActivity(context)?.findViewById<ViewGroup>(R.id.content)?.addView(mContainer, params)

        mCurrentMode = MODE_TINY_WINDOW
        mController?.onPlayModeChanged(mCurrentMode)
        log("MODE_TINY_WINDOW")
    }


    override fun exitTinyWindow(): Boolean {
        if (mCurrentMode == MODE_TINY_WINDOW) {
            ScUtil.scanForActivity(context)?.findViewById<ViewGroup>(R.id.content)?.removeView(mContainer)
            val params = LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
            this.addView(mContainer, params)
            mCurrentMode = MODE_NORMAL
            mController?.onPlayModeChanged(mCurrentMode)
            log("MODE_NORMAL")
            return true
        }
        return false
    }


    override fun releasePlayer() {
        if (mAudioManager != null) {
            mAudioManager?.abandonAudioFocus(null)
            mAudioManager = null
        }
        if (mMediaPlayer != null) {
            mMediaPlayer?.release()
            mMediaPlayer = null
        }
        mContainer.removeView(mTextureView)
        if (mSurface != null) {
            mSurface?.release()
            mSurface = null
        }
        if (mSurfaceTexture != null) {
            mSurfaceTexture?.release()
            mSurfaceTexture = null
        }
        mCurrentState = STATE_IDLE
    }

    override fun release() {
        // 保存播放位置

        // 保存播放位置
        if (isPlaying() || isBufferingPlaying() || isBufferingPaused() || isPaused()) {
            ScUtil.savePlayPosition(context, mUrl, getCurrentPosition())
        } else if (isCompleted()) {
            ScUtil.savePlayPosition(context, mUrl, 0)
        }
        // 退出全屏或小窗口
        // 退出全屏或小窗口
        if (isFullScreen()) {
            exitFullScreen()
        }
        if (isTinyWindow()) {
            exitTinyWindow()
        }
        mCurrentMode = MODE_NORMAL

        // 释放播放器

        // 释放播放器
        releasePlayer()

        // 恢复控制器

        // 恢复控制器
        mController?.reset()
        Runtime.getRuntime().gc()
    }


}