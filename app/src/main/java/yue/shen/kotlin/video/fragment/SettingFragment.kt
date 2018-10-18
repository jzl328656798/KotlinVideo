package yue.shen.kotlin.video.fragment

import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceScreen
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.startActivity
import yue.shen.kotlin.video.MainActivity
import yue.shen.kotlin.video.R

/**
 * Created by queen on 2018/10/17.
 * Author: Queen
 * Date: 2018/10/17
 * Time: 下午5:45
 * Email: zhuolei.jiang@softlinker.com & jiangzhuolei@126.com
 * Describe: TODO
 */
class SettingFragment :PreferenceFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        addPreferencesFromResource(R.xml.setting)
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onPreferenceTreeClick(preferenceScreen: PreferenceScreen?, preference: Preference?): Boolean {

        when(preference?.key){
            "clear_cache"->{
                Log.i("Queen","clear_cache")
            }
            "about"->{
                Log.i("Queen","about")
                startActivity<MainActivity>()
            }
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference)
    }
}