package yue.shen.kotlin.video.sc

import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import yue.shen.kotlin.video.R

class ScVideoViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    var mController: TxVideoPlayerController? = null
    var mVideoPlayer: ScVideoPlayer? = null

    init {
        itemView?.findViewById<ScVideoPlayer>(R.id.sc_video_player)
    }

    fun bindData(url: String, imgUrl: String) {
        Glide.with(itemView.context)
                .load(imgUrl)
                .placeholder(R.mipmap.img_default)
                .crossFade()
                .into(mController?.imageView())
        mVideoPlayer?.setUp(url)
    }

    fun setController(controller: TxVideoPlayerController) {
        mController = controller
        mController?.let { mVideoPlayer?.setController(it) }
    }
}