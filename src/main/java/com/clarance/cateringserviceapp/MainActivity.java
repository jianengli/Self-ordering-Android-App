package com.clarance.cateringserviceapp;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clarance.cateringserviceapp.Adapter.BannerPagerAdapter;
import com.clarance.cateringserviceapp.Listener.BannerPageChangeListener;
import com.clarance.cateringserviceapp.Listener.BannerTouchListener;
import com.clarance.cateringserviceapp.Listener.DescriptionBtClickListener;
import com.clarance.cateringserviceapp.Listener.MenuTabCheckedChangeListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public Handler myHandler = new MyHandler();
    private int screenWidth;

    private Animation fadeOutAnim;
    private ValueAnimator showDescriptionAnim, hideDescriptionAnim;
    private AnimUpdateListener animUpdateListener;
    private RelativeLayout welcomeAndDescContainer;

    private int[] bannerSlidesRes = {R.raw.banner_1 , R.raw.banner_2, R.raw.banner_3, R.raw.banner_4};
    private RadioGroup bannerDots;
    private TextView bannerFoodTitle;
    private ViewPager banner;
    private BannerPageChangeListener bannerListener;

    private RadioGroup menuTab;
    private FoodInfo foodInfo = new FoodInfo();

    private ImageView descriptionFoodImage;
    private TextView descriptionFoodName,descriptionFoodPrice, descriptionFoodState,descriptionFoodNum;
    private Button descriptionOrderBt,descriptionCancelBt;
    private ImageButton descriptionBackBt,descriptionDecreaseBt,descriptionIncreaseBt;

    private Map<String,FoodOrder> menuOrder = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getScreenWidth();
        initialWelcomeAndDescription();
        initialBanner();
        initialMenuTab();
    }

    //get screen width(px)
    public void getScreenWidth() {
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;
    }

    //get banner image res
    public int[] getBannerSlidesRes(){
        return bannerSlidesRes;
    }

    //initial welcome page and description page
    private void initialWelcomeAndDescription() {
        welcomeAndDescContainer = findViewById(R.id.welcome_and_description_container);

        //initialize animation
        fadeOutAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out);
        animUpdateListener = new AnimUpdateListener();
        showDescriptionAnim = ValueAnimator.ofFloat(screenWidth, 0.0f);
        hideDescriptionAnim = ValueAnimator.ofFloat(0.0f, screenWidth);
        showDescriptionAnim.setDuration(1000);
        hideDescriptionAnim.setDuration(1000);
        showDescriptionAnim.addUpdateListener(animUpdateListener);
        hideDescriptionAnim.addUpdateListener(animUpdateListener);

        //show logo for 2s, than remove logo, and move container;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message welcomePageFadeOut = new Message();
                welcomePageFadeOut.what = 3;
                myHandler.sendMessage(welcomePageFadeOut);
            }
        }).start();

        // find view
        descriptionFoodImage = findViewById(R.id.description_food_img);
        descriptionFoodName  = findViewById(R.id.description_food_name);
        descriptionFoodPrice = findViewById(R.id.description_food_price);
        descriptionFoodState = findViewById(R.id.description_food_state);
        descriptionOrderBt = findViewById(R.id.description_order_bt);
        descriptionCancelBt = findViewById(R.id.description_cancel_bt);
        descriptionBackBt = findViewById(R.id.description_back_bt);
        descriptionFoodNum = findViewById(R.id.description_food_num);
        descriptionDecreaseBt = findViewById(R.id.description_food_num_decrease);
        descriptionIncreaseBt = findViewById(R.id.description_food_num_increase);
    }

    private void welcomeFadeOut(){
        fadeOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                welcomeAndDescContainer.removeViewAt(0);
                welcomeAndDescContainer.setTranslationX(screenWidth);
                welcomeAndDescContainer.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        welcomeAndDescContainer.startAnimation(fadeOutAnim);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void showDescription(){

        welcomeAndDescContainer.setBackgroundColor(Color.WHITE);
        descriptionFoodName.setText(foodInfo.getFoodName());
        descriptionFoodPrice.setText(foodInfo.getFoodPrice());
        descriptionFoodState.setText(foodInfo.getFoodState());
        if(foodInfo.getFoodImg()!=null){
            descriptionFoodImage.setImageBitmap(foodInfo.getFoodImg());
        }
        else {
            descriptionFoodImage.setImageResource(foodInfo.getBannerFoodImage());
        }

        // enable bt when on sale
        if(descriptionFoodState.getText().toString().equals("ON SALE")){
            descriptionFoodState.setBackgroundColor(getResources().getColor(R.color.light_blue,null));
            descriptionOrderBt.setBackgroundColor(getResources().getColor(R.color.light_blue,null));
            descriptionDecreaseBt.setEnabled(true);
            descriptionIncreaseBt.setEnabled(true);
            descriptionCancelBt.setEnabled(true);
            descriptionOrderBt.setEnabled(true);
        }else{ // disable bt
            descriptionFoodState.setBackgroundColor(getResources().getColor(R.color.sold_out,null));
            descriptionOrderBt.setBackgroundColor(getResources().getColor(R.color.gray,null));
            descriptionDecreaseBt.setEnabled(false);
            descriptionIncreaseBt.setEnabled(false);
            descriptionCancelBt.setEnabled(false);
            descriptionOrderBt.setEnabled(false);
        }

        //set on click listener
        DescriptionBtClickListener descriptionBtClickListener = new DescriptionBtClickListener(MainActivity.this);
        descriptionDecreaseBt.setOnClickListener(descriptionBtClickListener);
        descriptionIncreaseBt.setOnClickListener(descriptionBtClickListener);
        descriptionBackBt.setOnClickListener(descriptionBtClickListener);
        descriptionOrderBt.setOnClickListener(descriptionBtClickListener);
        descriptionCancelBt.setOnClickListener(descriptionBtClickListener);

        showDescriptionAnim.start();
    }

    // hide description
    public void hideDescription(){
        hideDescriptionAnim.start();
    }

    // get foodInfo class
    public FoodInfo getFoodInfo() {
        return foodInfo;
    }

    public Map<String,FoodOrder> getMenuOrder(){
        return menuOrder;
    }

    //initialize banner
    @SuppressLint({"ResourceType", "ClickableViewAccessibility"})
    private void initialBanner() {
        //find view
        banner = findViewById(R.id.banner);
        bannerDots = findViewById(R.id.banner_dots);
        bannerFoodTitle = findViewById(R.id.banner_food_title);

        //initial setting for banner dots
        RadioGroup.LayoutParams dotsLayoutParams = new RadioGroup.LayoutParams(42, 42);
        dotsLayoutParams.setMargins(10, 0, 10, 0);
        //add dot for banner
        for (int i = 0; i < bannerSlidesRes.length; i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setEnabled(false);
            radioButton.setBackgroundResource(R.drawable.bg_banner_dots);
            radioButton.setGravity(Gravity.CENTER);
            radioButton.setLayoutParams(dotsLayoutParams);
            radioButton.setButtonDrawable(null);
            bannerDots.addView(radioButton);
        }
        // default checked dot
        bannerDots.check(bannerDots.getChildAt(0).getId());

        //initial setting for banner slides
        banner.setAdapter(new BannerPagerAdapter(this, bannerSlidesRes));
        banner.setCurrentItem(1,false);
        banner.setOffscreenPageLimit(3);

        //set listener for banner
        bannerListener = new BannerPageChangeListener(MainActivity.this, bannerSlidesRes.length);
        banner.addOnPageChangeListener(bannerListener);
        banner.setOnTouchListener(new BannerTouchListener(MainActivity.this,banner));

        //set timer for automatic change banner slide
        TimerTask autoSlides = new TimerTask() {
            @Override
            public void run() {
                if (!bannerListener.isManualScrolling()) {
                    Message changeSlideMsg = new Message();
                    changeSlideMsg.what = 1;
                    myHandler.sendMessage(changeSlideMsg);
                }
            }
        };

        Timer timer = new Timer();
        timer.schedule(autoSlides, 5000, 5000);
    }

    //initialize menu tab
    private void initialMenuTab() {
        //find view
        menuTab = findViewById(R.id.menu_tab);
        //set default checked button
        menuTab.check(menuTab.getChildAt(0).getId());
        //add event listener
        menuTab.setOnCheckedChangeListener(new MenuTabCheckedChangeListener(MainActivity.this));
    }

    // value animator update listener for show/hide description
    private class AnimUpdateListener implements ValueAnimator.AnimatorUpdateListener{
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float curPosition = (float) animation.getAnimatedValue();
            welcomeAndDescContainer.setTranslationX(curPosition);
        }
    }

    // inner handler class
    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                banner.setCurrentItem(banner.getCurrentItem() + 1, true);
            }  else if (msg.what == 3) {
                welcomeFadeOut();
            }
        }
    }

    // food information class
    public class FoodInfo{
        private String foodName;
        private String foodPrice;
        private Bitmap foodImg;
        private String foodState;
        private int bannerFoodImage;

        public String getFoodName() {
            return foodName;
        }

        public Bitmap getFoodImg() {
            return foodImg;
        }

        public String getFoodPrice() {
            return foodPrice;
        }

        public String getFoodState(){
            return foodState;
        }

        public int getBannerFoodImage(){
            return bannerFoodImage;
        }

        public void setFoodName(String foodName) {
            this.foodName = foodName;
        }

        public void setFoodPrice(String foodPrice) {
            this.foodPrice = foodPrice;
        }

        public void setFoodImg(Bitmap foodImg) {
            this.foodImg = foodImg;
        }

        public void setFoodState(String foodState){
            this.foodState = foodState;
        }

        public void setBannerFoodImage(int bannerFoodImage){
            this.bannerFoodImage = bannerFoodImage;
        }

    }

    // food ordered class
    public static class FoodOrder {
        private String foodName;
        private String foodPrice;
        private int foodNum;

        public FoodOrder(String foodName,String foodPrice,int foodNum){
            this.foodName = foodName;
            this.foodPrice = foodPrice;
            this.foodNum = foodNum;
        }

        public String getFoodName() {
            return foodName;
        }

        public String getFoodPrice() {
            return foodPrice;
        }

        public int getFoodNum(){
            return foodNum;
        }

        public void setFoodName(String foodName){
            this.foodName = foodName;
        }

        public void setFoodPrice(String foodPrice){
            this.foodPrice = foodPrice;
        }

        public void setFoodNum(int foodNum){
            this.foodNum = foodNum;
        }
    }
}
