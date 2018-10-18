package yue.shen.kotlin.video.activity

import kotlinx.android.synthetic.main.toolbar.*
import yue.shen.kotlin.video.R
import yue.shen.kotlin.video.base.BaseActivity
import yue.shen.kotlin.video.manager.ToolBarManager

/**
 * Created by queen on 2018/10/17.
 * Author: Queen
 * Date: 2018/10/17
 * Time: 下午5:37
 * Email: zhuolei.jiang@softlinker.com & jiangzhuolei@126.com
 * Describe: TODO
 */
class SettingActivity : BaseActivity() {


    override fun getLayout(): Int {
        return R.layout.activity_setting
    }

    override fun initView() {
        super.initView()
        ToolBarManager.initSettingTitle(tool_bar)
    }
}