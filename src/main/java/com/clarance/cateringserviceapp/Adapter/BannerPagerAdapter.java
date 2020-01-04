package com.clarance.cateringserviceapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class BannerPagerAdapter extends PagerAdapter {
    private Context mContext;
    private int[] bannerSlideRes;

    public BannerPagerAdapter(Context mContext, int[] bannerSlideRes) {
        this.mContext = mContext;
        this.bannerSlideRes = bannerSlideRes;
    }

    @Override
    public int getCount() {
        return bannerSlideRes.length + 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    //add (length + 2) image to banner
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final ImageView slide = new ImageView(mContext);
        slide.setScaleType(ImageView.ScaleType.CENTER_CROP);
        slide.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // first banner image is last image, last banner image is first image
        //in the middle is image 1, 2, 3, 4...
        if (position == 0) {
            slide.setImageResource(bannerSlideRes[bannerSlideRes.length - 1]);
        } else if (position == bannerSlideRes.length+1) {
            slide.setImageResource(bannerSlideRes[0]);
        }else {
            slide.setImageResource(bannerSlideRes[position-1]);
        }
        container.addView(slide);
        return slide;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        return;
    }
}
