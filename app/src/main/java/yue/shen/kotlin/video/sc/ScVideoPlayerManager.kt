package yue.shen.kotlin.video.sc

object ScVideoPlayerManager {

    private var mVideoPlayer: ScVideoPlayer? = null


    //private val sInstance: NiceVideoPlayerManager? = null


    fun getCurrentNiceVideoPlayer(): ScVideoPlayer? {
        return mVideoPlayer
    }

    fun setCurrentNiceVideoPlayer(videoPlayer: ScVideoPlayer) {
        if (mVideoPlayer !== videoPlayer) {
            releaseNiceVideoPlayer()
            mVideoPlayer = videoPlayer
        }
    }

    fun suspendNiceVideoPlayer() {
        mVideoPlayer?.let {
            if (it.isPlaying() || it.isBufferingPlaying()) {
                it.pause()
            }
        }
    }

    fun resumeNiceVideoPlayer() {
        mVideoPlayer?.let {
            if (it.isPaused() || it.isBufferingPaused()) {
                it.restart()
            }

        }
    }

    fun releaseNiceVideoPlayer() {
        mVideoPlayer?.let {
            it.release()
            mVideoPlayer = null
        }
    }

    fun onBackPressed(): Boolean {
        mVideoPlayer?.let {

            if (it.isFullScreen()) {
                return it.exitFullScreen()
            } else if (it.isTinyWindow()) {
                it.exitTinyWindow()
            }
        }
        return false
    }
}