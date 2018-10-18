package yue.shen.kotlin.video

import android.support.v7.widget.Toolbar
import android.view.MenuItem
import yue.shen.kotlin.video.base.BaseActivity
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.startActivity
import yue.shen.kotlin.video.activity.SettingActivity
import yue.shen.kotlin.video.manager.ToolBarManager

class MainActivity : BaseActivity(), Toolbar.OnMenuItemClickListener {

    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        super.initView()
        ToolBarManager.initMainTitle(tool_bar, this)
    }


    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.setting -> {
                startActivity<SettingActivity>()
            }
        }
        return true
    }
}
