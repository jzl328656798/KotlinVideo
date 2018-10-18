package yue.shen.kotlin.video.base

import android.app.Activity
import android.support.v4.app.Fragment
import org.jetbrains.anko.startActivity

/**
 * Created by queen on 2018/10/16.
 * Author: Queen
 * Date: 2018/10/16
 * Time: 下午1:58
 * Email: zhuolei.jiang@softlinker.com & jiangzhuolei@126.com
 * Describe: TODO
 */
abstract class BaseFragment :Fragment() {


    inline fun <reified T : Activity> startActivity() {
        context.startActivity<T>()
    }
}