package nah.prayer.loopingvp.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

import nah.prayer.loopingvp.R;
import nah.prayer.loopingvp.holder.Holder;
import nah.prayer.loopingvp.holder.NViewHolderCreator;
import nah.prayer.loopingvp.view.NLoopViewPager;


/**
 * Created by Sai on 15/7/29.
 */
public class NPageAdapter<T> extends PagerAdapter {
    private List<T> mDatas;
    private NViewHolderCreator holderCreator;
    private boolean canLoop = true;
    private NLoopViewPager viewPager;
    private final int MULTIPLE_COUNT = 300;

    public int toRealPosition(int position) {
        int realCount = getRealCount();
        if (realCount == 0)
            return 0;
        return position % realCount;
    }

    @Override
    public int getCount() {
        return canLoop ? getRealCount()*MULTIPLE_COUNT : getRealCount();
    }

    public int getRealCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int realPosition = toRealPosition(position);

        View view = getView(realPosition, null, container);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    @Override
    public void finishUpdate(@NonNull ViewGroup container) {
        int position = viewPager.getCurrentItem();
        if (position == 0) {
            position = viewPager.getFristItem();
        } else if (position == getCount() - 1) {
            position = viewPager.getLastItem();
        }
        try {
            viewPager.setCurrentItem(position, false);
        }catch (IllegalStateException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void setCanLoop(boolean canLoop) {
        this.canLoop = canLoop;
    }

    public void setViewPager(NLoopViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public NPageAdapter(NViewHolderCreator holderCreator, List<T> datas) {
        this.holderCreator = holderCreator;
        this.mDatas = datas;
    }

    private View getView(int position, View view, ViewGroup container) {
        Holder holder;
        if (view == null) {
            holder = (Holder) holderCreator.createHolder();
            view = holder.createView(container.getContext());
            view.setTag(R.id.nah_item_tag, holder);
        } else {
            holder = (Holder<T>) view.getTag(R.id.nah_item_tag);
        }
        if (mDatas != null && !mDatas.isEmpty())
            holder.UpdateUI(container.getContext(), position, mDatas.get(position));
        return view;
    }

//    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
//        this.onItemClickListener = onItemClickListener;
//    }
}
