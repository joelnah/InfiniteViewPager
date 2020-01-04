package nah.prayer.infiniteviewpagerjava

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main_kotlin.*
import nah.prayer.loopingvp.ImageHolderView
import nah.prayer.loopingvp.NahInfiniteview
import nah.prayer.loopingvp.viewpagertransformers.FadePageTransformer

class MainKotlinActivity : AppCompatActivity() {
    private val nahInfinities by lazy {
        NahInfiniteview<String>(this)
    }
    @SuppressLint("RtlHardcoded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_kotlin)
        btnOnDrawer.setOnClickListener {
            menuDrawer.openDrawer(Gravity.RIGHT)
            nahInfinities.startTurning()
        }
        btnMenu.setOnClickListener {
            menuDrawer.closeDrawer(Gravity.RIGHT)
            nahInfinities.stopTurning()
        }
        menuDrawer.openDrawer(Gravity.RIGHT)

        val list= arrayListOf(
                "https://cdn.pixabay.com/photo/2019/09/13/14/31/elephant-4474027__340.jpg",
                "https://cdn.pixabay.com/photo/2019/09/13/14/31/elephant-4474027__340.jpg",
                "https://cdn.pixabay.com/photo/2018/11/17/16/33/forest-3821416__340.jpg",
                "https://cdn.pixabay.com/photo/2019/09/16/12/37/cello-4480885__340.jpg",
                "https://cdn.pixabay.com/photo/2019/09/23/07/37/sunrise-4497745__340.jpg",
                "https://cdn.pixabay.com/photo/2019/08/20/10/12/sheep-4418342__340.jpg"
        )

        layoutTT.post {
            //val layoutParams = LinearLayout.LayoutParams(layoutTT.width, ViewGroup.LayoutParams.WRAP_CONTENT)
            val layoutParams = ViewGroup.LayoutParams(layoutTT.width, layoutTT.width/2)
            nahInfinities.layoutParams = layoutParams
            nahInfinities.viewPager.setPageTransformer(true, FadePageTransformer())
            nahInfinities.setAutoTurningTime(3000)
            nahInfinities.isCanLoop = true
            nahInfinities.setPages({ ImageHolderView() }, list)

            layoutTT.addView(nahInfinities)
        }



    }

}
