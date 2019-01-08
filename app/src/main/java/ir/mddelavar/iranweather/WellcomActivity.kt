package ir.mddelavar.iranweather

import android.animation.ValueAnimator
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation

import java.util.*
import kotlin.concurrent.schedule
import android.view.animation.TranslateAnimation
import kotlinx.android.synthetic.main.activity_wellcom.*


class WellcomActivity : AppCompatActivity() {

    private val context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wellcom)

        val animation7 = TranslateAnimation(Animation.RELATIVE_TO_SELF, //fromXType
                0f, //fromXValue
                Animation.RELATIVE_TO_SELF, //toXType
                0.0f, //toXValue
                Animation.RELATIVE_TO_PARENT, //fromYType
                -1f, //fromYValue
                Animation.RELATIVE_TO_SELF, //toYType
                0f)
        animation7.duration = 1000
        animation7.fillAfter = true
        logo.startAnimation(animation7)


        val animation = AlphaAnimation(0f,1f)
        animation.duration = 1000
        animation.startOffset = 1000
        animation.fillAfter = true

        text.startAnimation(animation)

        Timer("SettingUp", false).schedule(3000) {
            val intent = Intent(context , MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
