package nah.prayer.infiniteviewpagerjava;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import nah.prayer.loopingvp.ImageHolderView;
import nah.prayer.loopingvp.NahInfiniteview;
import nah.prayer.loopingvp.viewpagertransformers.FadePageTransformer;

public class MainActivity extends AppCompatActivity {

    NahInfiniteview nahInfiniteview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nahInfiniteview = findViewById(R.id.nahInfiniteview);

        nahInfiniteview.getViewPager().setPageTransformer(true, new FadePageTransformer());
        nahInfiniteview.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                //TODO
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        nahInfiniteview.setOnItemClickListener(position -> {
            startActivity(new Intent(MainActivity.this, MainKotlinActivity.class));
        });

        nahInfiniteview.setPages(ImageHolderView::new, dumy());
        int[] indicator = {android.R.drawable.button_onoff_indicator_on, android.R.drawable.button_onoff_indicator_off};
        int[] indicator2 = {R.drawable.indicator_on, R.drawable.indicator_off};
        nahInfiniteview.setPointViewVisible(true)
                //.setPageIndicator(indicator)
                .setPageIndicator(indicator2, 50, 50)
                .setPageIndicatorAlign(NahInfiniteview.PageIndicatorAlign.CENTER_HORIZONTAL);
        nahInfiniteview.startTurning();
    }

    private ArrayList<String> dumy() {
        ArrayList<String> list = new ArrayList<>();
        list.add("https://cdn.pixabay.com/photo/2019/09/13/14/31/elephant-4474027__340.jpg");
        list.add("https://cdn.pixabay.com/photo/2018/11/17/16/33/forest-3821416__340.jpg");
        list.add("https://cdn.pixabay.com/photo/2019/09/16/12/37/cello-4480885__340.jpg");
        list.add("https://cdn.pixabay.com/photo/2019/09/23/07/37/sunrise-4497745__340.jpg");
        list.add("https://cdn.pixabay.com/photo/2019/08/20/10/12/sheep-4418342__340.jpg");

        return list;
    }
}
