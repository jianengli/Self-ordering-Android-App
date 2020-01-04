package com.clarance.cateringserviceapp.Listener;

import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import com.clarance.cateringserviceapp.MainActivity;
import com.clarance.cateringserviceapp.ReadFileFromAssets;

import java.util.ArrayList;


public class BannerTouchListener implements View.OnTouchListener {
    private float startX;
    private float startY;
    private ViewPager banner;
    private MainActivity mActivity;
    private MainActivity.FoodInfo foodInfo;
    private ReadFileFromAssets readFileFromAssets;
    private ArrayList<String> bannerFoodRes;
    private ArrayList<String> bannerFoodName;
    private ArrayList<String> bannerFoodPrice;
    private ArrayList<String> bannerFoodState;
    private int[] bannerFoodImage;

    public BannerTouchListener(MainActivity mActivity,ViewPager banner){
        this.banner = banner;
        this.mActivity = mActivity;
        foodInfo = mActivity.getFoodInfo();
        bannerFoodImage = mActivity.getBannerSlidesRes();

        this.readFileFromAssets = new ReadFileFromAssets(mActivity);
        bannerFoodName = new ArrayList<>();
        bannerFoodPrice = new ArrayList<>();
        bannerFoodState = new ArrayList<>();

        // read information file from assets
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

    }

    @Override
    /*viewpager没有点击事件
    当接触事件起始点和结束点，即按下和松手的点一致，
    则为点击
    * */
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //start touch point
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (startX == event.getX() && startY == event.getY()){
                    //set information, and show description
                    int item = banner.getCurrentItem();
                    foodInfo.setFoodImg(null);
                    foodInfo.setBannerFoodImage(bannerFoodImage[item-1]);
                    foodInfo.setFoodName(bannerFoodName.get(item-1));
                    foodInfo.setFoodPrice(bannerFoodPrice.get(item-1));
                    foodInfo.setFoodState(bannerFoodState.get(item-1));
                    mActivity.showDescription();
                    }
                break;
                }
            return false;
        }
}
