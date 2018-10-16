package yue.shen.kotlin.video.mvp.actions

/**
 * Created by queen on 2018/10/16.
 * Author: Queen
 * Date: 2018/10/16
 * Time: 下午2:02
 * Email: zhuolei.jiang@softlinker.com & jiangzhuolei@126.com
 * Describe: TODO
 */
interface ICallBack5<T1, T2, T3, T4, T5> : Action {

    fun onResult1(t1: T1)

    fun onResult2(t2: T2)

    fun onResult3(t3: T3)

    fun onResult4(t4: T4)

    fun onResult5(t5: T5)

    fun onError1(throwable: RequestException)

    fun onError2(throwable: RequestException)

    fun onError3(throwable: RequestException)

    fun onError4(throwable: RequestException)

    fun onError5(throwable: RequestException)
}