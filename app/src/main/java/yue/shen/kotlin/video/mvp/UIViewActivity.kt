package yue.shen.kotlin.video.mvp

import android.os.Bundle
import yue.shen.kotlin.video.base.BaseActivity
import android.util.SparseArray
import android.view.View


/**
 * Created by queen on 2018/10/16.
 * Author: Queen
 * Date: 2018/10/16
 * Time: 下午1:53
 * Email: zhuolei.jiang@softlinker.com & jiangzhuolei@126.com
 * Describe: TODO
 */
abstract class UIViewActivity<P : IPresenter> : BaseActivity() {
    protected var mPresenter: P? = null

    private val mViews = SparseArray<View>()

    protected abstract val presenter: Class<P>?

    override fun onCreate(savedInstanceState: Bundle?) {
        if (presenter != null) {
            try {
                mPresenter = presenter!!.newInstance()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }

            mPresenter!!.attach(this)
        }
        super.onCreate(savedInstanceState)
    }

    private fun <V : View> bindView(resId: Int): View? {
        var view: View? = mViews.get(resId)
        if (view == null) {
            view = findViewById<View>(resId)
            mViews.put(resId, view)
        }
        return view
    }

    protected operator fun <V : View> get(resId: Int): View? {
        return bindView<View>(resId)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mPresenter != null) {
            mPresenter!!.detach()
            mPresenter = null
        }
        mViews.clear()
    }
}