package yue.shen.kotlin.video.mvp

import android.content.Context

/**
 * Created by queen on 2018/10/16.
 * Author: Queen
 * Date: 2018/10/16
 * Time: 下午1:37
 * Email: zhuolei.jiang@softlinker.com & jiangzhuolei@126.com
 * Describe: TODO
 */
interface IPresenter {

    fun attach(context: Context)

    fun detach()
}