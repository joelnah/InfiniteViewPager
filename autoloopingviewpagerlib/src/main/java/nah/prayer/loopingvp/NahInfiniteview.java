package nah.prayer.loopingvp;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.ViewPager;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import nah.prayer.loopingvp.adapter.NPageAdapter;
import nah.prayer.loopingvp.holder.NViewHolderCreator;
import nah.prayer.loopingvp.listener.NPageChangeListener;
import nah.prayer.loopingvp.listener.OnItemClickListener;
import nah.prayer.loopingvp.view.NLoopViewPager;


/**
 * ConvenientBanner2
 */
public class NahInfiniteview<T> extends LinearLayout {
    private List<T> mDatas;
    private int[] page_indicatorId;
    private ArrayList<ImageView> mPointViews = new ArrayList<>();
    private NPageChangeListener pageChangeListener;
    private ViewPager.OnPageChangeListener onPageChangeListener;
    private NPageAdapter pageAdapter;
    private NLoopViewPager viewPager;
    private ViewPagerScroller scroller;
    private ViewGroup loPageTurningPoint;
    private long autoTurningTime;
    private boolean turning;
    private boolean canTurn = false;
    private boolean manualPageable = true;
    private boolean canLoop = true;
    public enum PageIndicatorAlign{
        ALIGN_PARENT_LEFT,ALIGN_PARENT_RIGHT,CENTER_HORIZONTAL
    }
    private AdSwitchTask adSwitchTask ;

    public NahInfiniteview(Context context) {
        super(context);
        init(context,null);
    }

    public NahInfiniteview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public NahInfiniteview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NahInfiniteview(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if(attrs != null) {
            @SuppressLint("CustomViewStyleable") TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NahInfiniteview);
            canLoop = a.getBoolean(R.styleable.NahInfiniteview_canLoop, true);
            autoTurningTime = a.getInt(R.styleable.NahInfiniteview_autoTurningTime, 5000);
            a.recycle();
        }
        View hView = LayoutInflater.from(context).inflate(
                R.layout.include_viewpager, this, true);
        viewPager = hView.findViewById(R.id.cbLoopViewPager);
        loPageTurningPoint = hView.findViewById(R.id.loPageTurningPoint);
        initViewPagerScroll();

        adSwitchTask = new AdSwitchTask(this);
    }

    static class AdSwitchTask implements Runnable {

        private final WeakReference<NahInfiniteview> reference;

        AdSwitchTask(NahInfiniteview nahInfiniteview) {
            this.reference = new WeakReference<>(nahInfiniteview);
        }

        @Override
        public void run() {
            NahInfiniteview nahInfiniteview = reference.get();

            if(nahInfiniteview != null){
                if (nahInfiniteview.viewPager != null && nahInfiniteview.turning) {
                    int page = nahInfiniteview.viewPager.getCurrentItem() + 1;
                    nahInfiniteview.viewPager.setCurrentItem(page);
                    nahInfiniteview.postDelayed(nahInfiniteview.adSwitchTask, nahInfiniteview.autoTurningTime);
                }
            }
        }
    }

    public void setPages(NViewHolderCreator holderCreator, List<T> datas){
        this.mDatas = datas;
        pageAdapter = new NPageAdapter(holderCreator,mDatas);
        viewPager.setAdapter(pageAdapter,canLoop);

        if (page_indicatorId != null)
            setPageIndicator(page_indicatorId);

    }

    /**
     * 알림 데이터 변경
     * 데이터 만 늘리려면 notifyDataSetAdd()를 사용하는 것이 좋습니다.
     */
    public void notifyDataSetChanged(){
        assert viewPager.getAdapter() != null;
        viewPager.getAdapter().notifyDataSetChanged();
        if (page_indicatorId != null)
            setPageIndicator(page_indicatorId);
    }

    /**
     * 하단 포인트 표시 여부 설정
     *
     * @param visible
     */
    public NahInfiniteview setPointViewVisible(boolean visible) {
        loPageTurningPoint.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * 하단 Indicator 이미지
     *
     * @param page_indicatorId
     */
    public NahInfiniteview setPageIndicator(int[] page_indicatorId) {
        initPageIndicator(page_indicatorId, 0, 0);
        return this;
    }
    /**
     * 하단 Indicator xml
     *
     * @param page_indicatorId
     * @param width
     * @param height
     */
    public NahInfiniteview setPageIndicator(int[] page_indicatorId, int width, int height) {
        initPageIndicator(page_indicatorId, width, height);
        return this;
    }

    private void initPageIndicator(int[] page_indicatorId, int width, int height) {
        loPageTurningPoint.removeAllViews();
        mPointViews.clear();
        this.page_indicatorId = page_indicatorId;
        if(mDatas==null)return;
        for (int count = 0; count < mDatas.size(); count++) {
            int padding = 5;
            ImageView pointView = new ImageView(getContext());
            pointView.setPadding(padding, 0, padding, 0);

            if(width!=0 && height!=0 ) {
                LayoutParams layoutParams = new LayoutParams(width+padding, height-padding);
                pointView.setLayoutParams(layoutParams);
            }

            if (mPointViews.isEmpty()) {
                pointView.setImageResource(page_indicatorId[0]);
            }
            else {
                pointView.setImageResource(page_indicatorId[1]);
            }

            mPointViews.add(pointView);
            loPageTurningPoint.addView(pointView);
        }
        pageChangeListener = new NPageChangeListener(mPointViews, page_indicatorId);
        viewPager.setOnPageChangeListener(pageChangeListener);
        pageChangeListener.onPageSelected(viewPager.getRealItem());
        if(onPageChangeListener != null)pageChangeListener.setOnPageChangeListener(onPageChangeListener);
    }

    /**
     * 방향
     * @param align  RelativeLayout.ALIGN_PARENT_LEFT，（RelativeLayout.CENTER_HORIZONTAL），RelativeLayout.ALIGN_PARENT_RIGHT）
     * @return
     */
    public NahInfiniteview setPageIndicatorAlign(PageIndicatorAlign align) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) loPageTurningPoint.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, align == PageIndicatorAlign.ALIGN_PARENT_LEFT ? RelativeLayout.TRUE : 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, align == PageIndicatorAlign.ALIGN_PARENT_RIGHT ? RelativeLayout.TRUE : 0);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, align == PageIndicatorAlign.CENTER_HORIZONTAL ? RelativeLayout.TRUE : 0);
        loPageTurningPoint.setLayoutParams(layoutParams);
        return this;
    }

    /***
     * 페이지 터닝
     * @return
     */
    public boolean isTurning() {
        return turning;
    }

    /***
     * Start
     * @return
     */
    public NahInfiniteview startTurning() {
        if(turning){
            stopTurning();
        }

        canTurn = true;
        turning = true;
        postDelayed(adSwitchTask, autoTurningTime);
        return this;
    }

    public void stopTurning() {
        turning = false;
        removeCallbacks(adSwitchTask);
    }

    /**
     * Custom page animation effects
     *
     * @param transformer
     * @return
     */
    public NahInfiniteview setPageTransformer(ViewPager.PageTransformer transformer) {
        viewPager.setPageTransformer(true, transformer);
        return this;
    }


    /**
     * ViewPager 스크롤 초기화
     * */
    private void initViewPagerScroll() {
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            scroller = new ViewPagerScroller(
                    viewPager.getContext());
            mScroller.set(viewPager, scroller);

        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public boolean isManualPageable() {
        return viewPager.isCanScroll();
    }

    public void setManualPageable(boolean manualPageable) {
        viewPager.setCanScroll(manualPageable);
    }

    //이벤트 다운시 터닝 중지, 그외 재 시작
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        int action = ev.getAction();
        if (action == MotionEvent.ACTION_UP||action == MotionEvent.ACTION_CANCEL||action == MotionEvent.ACTION_OUTSIDE) {
            // 시작
            if (canTurn)startTurning();
        } else if (action == MotionEvent.ACTION_DOWN) {
            // 중지
            if (canTurn)stopTurning();
        }
        return super.dispatchTouchEvent(ev);
    }

    //현재 페이지 index
    public int getCurrentItem(){
        if (viewPager!=null) {
            return viewPager.getRealItem();
        }
        return -1;
    }
    //index 변경
    public void setcurrentitem(int index){
        if (viewPager!=null) {
            viewPager.setCurrentItem(index);
        }
    }

    public ViewPager.OnPageChangeListener getOnPageChangeListener() {
        return onPageChangeListener;
    }

    /**
     * 리스너 설정
     * @param onPageChangeListener
     * @return
     */
    public NahInfiniteview setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
        //기본 리스너가있는 경우 (즉, 기본 페이지 넘김 표시기가 사용됨) 사용자 설정이 기본값에 첨부되며 그렇지 않은 경우 직접 설정됩니다.
        if(pageChangeListener != null)pageChangeListener.setOnPageChangeListener(onPageChangeListener);
        else viewPager.setOnPageChangeListener(onPageChangeListener);
        return this;
    }

    public boolean isCanLoop() {
        return viewPager.isCanLoop();
    }

    /**
     * 아이템 클릭 리스너
     * @param onItemClickListener
     */
    public NahInfiniteview setOnItemClickListener(OnItemClickListener onItemClickListener) {
        if (onItemClickListener == null) {
            viewPager.setOnItemClickListener(null);
            return this;
        }
        viewPager.setOnItemClickListener(onItemClickListener);
        return this;
    }

    /**
     * ViewPager 스크롤 속도 설정
     * @param scrollDuration
     */
    public void setScrollDuration(int scrollDuration){
        scroller.setScrollDuration(scrollDuration);
    }

    public int getScrollDuration() {
        return scroller.getScrollDuration();
    }

    public NLoopViewPager getViewPager() {
        return viewPager;
    }

    public void setCanLoop(boolean canLoop) {
        this.canLoop = canLoop;
        viewPager.setCanLoop(canLoop);
    }

    public void next(){
        int pos = getCurrentItem();
        if(pos != -1){
            if((mDatas.size()>1)){
                removeCallbacks(adSwitchTask);
                adSwitchTask.run();
            }
        }
    }
    public void previous(){
        int pos = getCurrentItem();
        if(pos != -1){
            if((mDatas.size()>1)) {
                if (pos == 0) {
                    setcurrentitem(mDatas.size() - 1);
                } else {
                    setcurrentitem(pos - 1);
                }
            }
        }
    }
    public void setAutoTurningTime(int autoTurningTime) {
        this.autoTurningTime = autoTurningTime;
        startTurning();
    }

}