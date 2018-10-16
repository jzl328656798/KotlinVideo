package yue.shen.kotlin.video.mvp

import android.app.Activity
import yue.shen.kotlin.video.base.BaseFragment


/**
 * Created by queen on 2018/10/16.
 * Author: Queen
 * Date: 2018/10/16
 * Time: 下午1:58
 * Email: zhuolei.jiang@softlinker.com & jiangzhuolei@126.com
 * Describe: TODO
 */
abstract class UIViewFragment<P : IPresenter> : BaseFragment() {

    protected var mPresenter: P? = null

    protected abstract val presenter: Class<P>?

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        if (presenter != null) {
            try {
                mPresenter = presenter!!.newInstance()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: java.lang.InstantiationException) {
                e.printStackTrace()
            }

            mPresenter!!.attach(activity!!)
        }
    }

    override fun onDetach() {
        super.onDetach()
        if (mPresenter != null) {
            mPresenter!!.detach()
            mPresenter = null
        }
    }
}