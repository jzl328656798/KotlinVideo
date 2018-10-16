package yue.shen.kotlin.video.activity

import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorListener
import android.view.View
import yue.shen.kotlin.video.R
import yue.shen.kotlin.video.base.BaseActivity
import kotlinx.android.synthetic.main.activity_1.*
import yue.shen.kotlin.video.MainActivity

/**
 * Created by queen on 2018/10/16.
 * Author: Queen
 * Date: 2018/10/16
 * Time: 下午2:14
 * Email: zhuolei.jiang@softlinker.com & jiangzhuolei@126.com
 * Describe: TODO
 */
class SplashActivity : BaseActivity(), ViewPropertyAnimatorListener {
    override fun onAnimationEnd(view: View?) {
        startActivityAndFinish<MainActivity>()
    }

    override fun onAnimationCancel(view: View?) {
    }

    override fun onAnimationStart(view: View?) {
    }

    override fun getLayout(): Int {
        return R.layout.activity_1
    }

    override fun initView() {
        super.initView()

        ViewCompat.animate(iv_splash).scaleX(1.0f).scaleY(1.0f).setListener(this).duration = 3000

    }
}