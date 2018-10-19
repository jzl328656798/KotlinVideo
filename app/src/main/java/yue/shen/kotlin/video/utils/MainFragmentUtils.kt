package yue.shen.kotlin.video.utils

import yue.shen.kotlin.video.R
import yue.shen.kotlin.video.base.BaseFragment
import yue.shen.kotlin.video.fragment.HomeFragment
import yue.shen.kotlin.video.fragment.MvFragment
import yue.shen.kotlin.video.fragment.VBangFragment
import yue.shen.kotlin.video.fragment.YueDanFragment

/**
 * Created by queen on 2018/10/18.
 * Author: Queen
 * Date: 2018/10/18
 * Time: 下午1:44
 * Email: zhuolei.jiang@softlinker.com & jiangzhuolei@126.com
 * Describe: TODO
 */
class MainFragmentUtils private constructor() {

    private val home by lazy { HomeFragment() }
    private val mv by lazy { MvFragment() }
    private val vb by lazy { VBangFragment() }
    private val yueDan by lazy { YueDanFragment() }

    companion object {
        val util by lazy { MainFragmentUtils() }
    }

    fun getFragment(tabId: Int): BaseFragment {

        when (tabId) {
            R.id.tab_home -> {
                return home
            }
            R.id.tab_mv -> {
                return mv
            }
            R.id.tab_v_bang -> {
                return vb
            }
            R.id.tab_yue_dan -> {
                return yueDan
            }
        }
        return home
    }
}