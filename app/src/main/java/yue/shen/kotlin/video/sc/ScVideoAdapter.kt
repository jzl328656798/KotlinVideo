package yue.shen.kotlin.video.sc

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import yue.shen.kotlin.video.R

class ScVideoAdapter(var context: Context,var list:ArrayList<String> ) : RecyclerView.Adapter<ScVideoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ScVideoViewHolder {
        val itemView = LayoutInflater.from(context)?.inflate(R.layout.item_sc_video, parent, false)
        val holder = ScVideoViewHolder(itemView)
        val controller = TxVideoPlayerController(context)
        holder.setController(controller)
        return holder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ScVideoViewHolder?, position: Int) {
        val url = list[position]
        holder?.bindData(url,"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1587404333870&di=e70e1c1b8ddf9061c84b4d99faf90833&imgtype=0&src=http%3A%2F%2Fa4.att.hudong.com%2F21%2F09%2F01200000026352136359091694357.jpg")
    }
}