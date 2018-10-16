package yue.shen.kotlin.video.mvp

import android.content.Context

/**
 * Created by queen on 2018/10/16.
 * Author: Queen
 * Date: 2018/10/16
 * Time: 下午1:47
 * Email: zhuolei.jiang@softlinker.com & jiangzhuolei@126.com
 * Describe: TODO
 */
abstract class PresenterImpl<V : IView, M : Model<*>> : IPresenter {

    protected var iView: V? = null
    protected var mModel: M? = null
    protected var mContext: Context? = null

    /**
     * @return Class
     */
    protected abstract val model: Class<M>?

    override fun attach(context: Context) {
        mContext = context
        if (model != null) {
            try {
                mModel = model!!.newInstance()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }

        }
    }

    fun addIView(view: V) {
        iView = view
    }

    override fun detach() {
        if (mModel != null) {
            mModel!!.cancel()
        }
        mModel = null
        mContext = null
        iView = null
    }
}