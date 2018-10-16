package yue.shen.kotlin.video.mvp

import yue.shen.kotlin.video.mvp.actions.Action

/**
 * Created by queen on 2018/10/16.
 * Author: Queen
 * Date: 2018/10/16
 * Time: 下午1:39
 * Email: zhuolei.jiang@softlinker.com & jiangzhuolei@126.com
 * Describe: TODO
 */
abstract class Model<T : Action> {

    protected lateinit var mCallBack: T

    fun setCallBack(callBack: T) {
        mCallBack = callBack
    }

    abstract fun cancel()

}