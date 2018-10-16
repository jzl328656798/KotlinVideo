package yue.shen.kotlin.video.mvp.actions

/**
 * Created by queen on 2018/10/16.
 * Author: Queen
 * Date: 2018/10/16
 * Time: 下午2:02
 * Email: zhuolei.jiang@softlinker.com & jiangzhuolei@126.com
 * Describe: TODO
 */
interface ICallBack0 : Action {

    fun onResult()

    fun onError(throwable: RequestException)
}