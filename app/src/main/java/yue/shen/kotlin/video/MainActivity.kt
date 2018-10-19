package yue.shen.kotlin.video

import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.roughike.bottombar.OnTabSelectListener
import kotlinx.android.synthetic.main.activity_main.*
import yue.shen.kotlin.video.base.BaseActivity
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.startActivity
import yue.shen.kotlin.video.activity.SettingActivity
import yue.shen.kotlin.video.manager.ToolBarManager
import yue.shen.kotlin.video.utils.MainFragmentUtils

class MainActivity : BaseActivity(), Toolbar.OnMenuItemClickListener, OnTabSelectListener {

    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        super.initView()
        ToolBarManager.initMainTitle(tool_bar, this)
        bottom_bar_main.setOnTabSelectListener(this)
    }


    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.setting -> {
                startActivity<SettingActivity>()
            }
        }
        return true
    }

    override fun onTabSelected(tabId: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_main,MainFragmentUtils.util.getFragment(tabId),tabId.toString())
        transaction.commit()
    }
}
