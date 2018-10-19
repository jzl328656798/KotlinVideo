package yue.shen.kotlin.video.fragment

import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import yue.shen.kotlin.video.R
import yue.shen.kotlin.video.adapter.MainAdapter
import yue.shen.kotlin.video.base.BaseFragment

/**
 * Created by queen on 2018/10/18.
 * Author: Queen
 * Date: 2018/10/18
 * Time: 下午1:30
 * Email: zhuolei.jiang@softlinker.com & jiangzhuolei@126.com
 * Describe: TODO
 */
class HomeFragment : BaseFragment() {

    override fun initView(): Int {
        return R.layout.fragment_home
    }


    override fun initData() {
        super.initData()
        rv_home.layoutManager = LinearLayoutManager(context)
        rv_home.adapter = MainAdapter()
    }


}