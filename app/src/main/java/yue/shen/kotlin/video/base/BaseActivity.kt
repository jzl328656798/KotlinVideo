package yue.shen.kotlin.video.base

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.startActivity

/**
 * Created by queen on 2018/10/16.
 * Author: Queen
 * Date: 2018/10/16
 * Time: 上午10:50
 * Email: zhuolei.jiang@softlinker.com & jiangzhuolei@126.com
 * Describe: TODO
 */
abstract class BaseActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        initView()
        initListener()
        initData()
    }

    abstract fun getLayout(): Int

    open fun initView() {

    }

    open fun initListener(){

    }

    open fun initData(){
        
    }

    inline fun <reified T : Activity> startActivityAndFinish() {
        startActivity<T>()
        finish()
    }
}