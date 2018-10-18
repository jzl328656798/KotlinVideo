package yue.shen.kotlin.video.activity

import kotlinx.android.synthetic.main.toolbar.*
import yue.shen.kotlin.video.R
import yue.shen.kotlin.video.base.BaseActivity
import yue.shen.kotlin.video.manager.ToolBarManager

/**
 * Created by queen on 2018/10/18.
 * Author: Queen
 * Date: 2018/10/18
 * Time: 上午9:20
 * Email: zhuolei.jiang@softlinker.com & jiangzhuolei@126.com
 * Describe: TODO
 */
class AboutActivity :BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.activity_about
    }

    override fun initView() {
        super.initView()
        ToolBarManager.initAboutTitle(tool_bar)
    }
}