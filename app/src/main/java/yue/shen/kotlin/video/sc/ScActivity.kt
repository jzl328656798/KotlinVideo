package yue.shen.kotlin.video.sc

import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_sc.*
import yue.shen.kotlin.video.R
import yue.shen.kotlin.video.base.BaseActivity

class ScActivity : BaseActivity() {

    override fun getLayout(): Int {
        return R.layout.activity_sc
    }

    override fun initView() {
        super.initView()
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

        val adapter = ScVideoAdapter(this, getData())
        recycler_view.adapter = adapter
        recycler_view.setRecyclerListener { holder ->
            run {
                val scVideoPlayer = (holder as ScVideoViewHolder).mVideoPlayer
                if (scVideoPlayer === ScVideoPlayerManager.getCurrentNiceVideoPlayer()) {
                    ScVideoPlayerManager.releaseNiceVideoPlayer()
                }
            }
        }
    }

    private fun getData(): ArrayList<String> {
        val list = ArrayList<String>()
        list.add("http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-10_10-20-26.mp4")
        list.add("https://cmsqa-oss.sgmlink.com/5d47f514e458d/try/1.m3u8")
        list.add("https://cmsqa-oss.sgmlink.com/5d47f514e458d/try/1.m3u8")
        list.add("https://cmsqa-oss.sgmlink.com/5d47f514e458d/try/1.m3u8")
        list.add("https://cmsqa-oss.sgmlink.com/5d47f514e458d/try/1.m3u8")
        list.add("https://cmsqa-oss.sgmlink.com/5d47f514e458d/try/1.m3u8")
        list.add("https://cmsqa-oss.sgmlink.com/5d47f514e458d/try/1.m3u8")
        list.add("https://cmsqa-oss.sgmlink.com/5d47f514e458d/try/1.m3u8")
        return list
    }

    override fun onStop() {
        super.onStop()
        ScVideoPlayerManager.releaseNiceVideoPlayer()
    }

    override fun onBackPressed() {
        if (ScVideoPlayerManager.onBackPressed()) return
        super.onBackPressed()
    }
}