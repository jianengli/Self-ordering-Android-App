package com.clarance.cateringserviceapp.Listener;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.clarance.cateringserviceapp.R;
import com.clarance.cateringserviceapp.ReadFileFromAssets;

import java.util.ArrayList;

public class BannerPageChangeListener implements ViewPager.OnPageChangeListener {
    Activity mActivity;
    private ViewPager mViewPager;
    private int pagerNum;
    private RadioGroup bannerDots;
    private TextView bannerFoodTitle;
    private ArrayList<String> bannerFoodRes;
    private ArrayList<String> bannerFoodName;
    private ArrayList<String> bannerFoodPrice;
    private ArrayList<String> bannerFoodState;
    private boolean isManualScrolling = false;
    private ReadFileFromAssets readFileFromAssets;

    public BannerPageChangeListener(Activity mActivity, int slidesNum) {
        this.mActivity = mActivity;
        this.mViewPager = mActivity.findViewById(R.id.banner);
        this.pagerNum = slidesNum + 2; // the number of image in banner
        this.bannerDots = mActivity.findViewById(R.id.banner_dots); // banner left-bottom corner dots
        this.bannerFoodTitle = mActivity.findViewById(R.id.banner_food_title);

        this.readFileFromAssets = new ReadFileFromAssets(mActivity);
        bannerFoodName = new ArrayList<>();
        bannerFoodPrice = new ArrayList<>();
        bannerFoodState = new ArrayList<>();

        // read food information from assets file
        bannerFoodRes = readFileFromAssets.readTextFile("banner_food.txt");
        for (int i = 0; i < bannerFoodRes.size(); i++) {
            if (i % 3 == 0) {
                bannerFoodName.add(bannerFoodRes.get(i));
            } else if (i % 3 == 1) {
                bannerFoodPrice.add(bannerFoodRes.get(i));
            } else {
                bannerFoodState.add(bannerFoodRes.get(i));
            }
        }
        bannerFoodTitle.setText(bannerFoodName.get(0)+" -- "+bannerFoodPrice.get(0));
    }

    @Override
    /*实现无缝轮播
      第一张图跳转到倒数第二张0->4
      最后一张跳转到第二张5->1
    * */
    public void onPageScrolled(int i, float v, int i1) {
        if (i == 0 && v == 0) {
            mViewPager.setCurrentItem(pagerNum - 2, false);
        } else if (i == pagerNum - 1 && v == 0) {
            mViewPager.setCurrentItem(1, false);
        }
    }

    @Override
    // change dot and description after banner image changed
    public void onPageSelected(int i) {
        if (i >= 1 && i <= bannerDots.getChildCount()) {
            RadioButton dot = (RadioButton) bannerDots.getChildAt(i - 1);
            dot.setChecked(true);
            bannerFoodTitle.setText(bannerFoodName.get(i-1)+" -- "+bannerFoodPrice.get(i-1));
        }
    }

    @Override
    // set scrolling state
    public void onPageScrollStateChanged(int i) {
        switch (i) {
            case 1:
                isManualScrolling = true;
                break;
            case 2:
                isManualScrolling = false;
                break;
            case 0:
                isManualScrolling = false;
                break;
        }
    }

    public boolean isManualScrolling() {
        return isManualScrolling;
    }

}
