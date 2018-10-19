package yue.shen.kotlin.video.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import yue.shen.kotlin.video.view.HomeItemView

/**
 * Created by queen on 2018/10/18.
 * Author: Queen
 * Date: 2018/10/18
 * Time: 下午3:48
 * Email: zhuolei.jiang@softlinker.com & jiangzhuolei@126.com
 * Describe: TODO
 */
class MainAdapter : RecyclerView.Adapter<MainAdapter.MainHolder>() {
    override fun onBindViewHolder(holder: MainHolder?, position: Int) {
    }

    override fun getItemCount(): Int {
        return 20
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MainHolder {
        return MainHolder(HomeItemView(parent?.context))
    }

    class MainHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

}