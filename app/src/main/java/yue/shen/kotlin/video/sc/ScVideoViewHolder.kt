package yue.shen.kotlin.video.sc

import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import yue.shen.kotlin.video.R

class ScVideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    lateinit var mController: TxVideoPlayerController
    var mVideoPlayer: ScVideoPlayer = itemView.findViewById<ScVideoPlayer>(R.id.sc_video_player)

    init {
        // 将列表中的每个视频设置为默认16:9的比例
        val params = mVideoPlayer.layoutParams
        params.width = itemView.resources.displayMetrics.widthPixels // 宽度为屏幕宽度
        params.height = (params.width * 9f / 16f).toInt() // 高度为宽度的9/16
        mVideoPlayer.layoutParams = params
    }

    fun bindData(url: String, imgUrl: String) {
        Glide.with(itemView.context)
                .load(imgUrl)
                .placeholder(R.mipmap.img_default)
                .crossFade()
                .into(mController.imageView())
        mVideoPlayer.setUp(url)
    }

    fun setController(controller: TxVideoPlayerController) {
        mController = controller
        mVideoPlayer.setController(mController)
    }
}