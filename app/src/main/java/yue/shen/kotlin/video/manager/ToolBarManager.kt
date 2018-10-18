package yue.shen.kotlin.video.manager

import android.support.v7.widget.Toolbar
import yue.shen.kotlin.video.R

/**
 * Created by queen on 2018/10/17.
 * Author: Queen
 * Date: 2018/10/17
 * Time: 下午4:08
 * Email: zhuolei.jiang@softlinker.com & jiangzhuolei@126.com
 * Describe: TODO
 */
object ToolBarManager {

    fun initMainTitle(toolBar: Toolbar,click:Toolbar.OnMenuItemClickListener) {
        toolBar.title = "超级影音"
        toolBar.inflateMenu(R.menu.menu_main)
        toolBar.setOnMenuItemClickListener(click)
    }

    fun initSettingTitle(toolBar: Toolbar){
        toolBar.title = "设置界面"
    }

    fun initAboutTitle(toolBar: Toolbar){
        toolBar.title = "关于App"
    }
}