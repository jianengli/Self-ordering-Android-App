package com.clarance.cateringserviceapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ReadFileFromAssets {
    private Activity mActivity = null;
    private Fragment mFragment = null;

    public ReadFileFromAssets(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public ReadFileFromAssets(Fragment mFragment) {
        this.mFragment = mFragment;
    }

    // read text file
    public ArrayList<String> readTextFile(String fileName) {
        ArrayList<String> readResult = new ArrayList<>();
        try {
            InputStreamReader inputReader;
            if (mActivity != null) {
                inputReader = new InputStreamReader(mActivity.getResources().getAssets().open(fileName),"GBK");
            } else {
                inputReader = new InputStreamReader(mFragment.getResources().getAssets().open(fileName), "GBK");
            }
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            while ((line = bufReader.readLine()) != null) {
                readResult.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return readResult;
    }

    // read image file
    public Bitmap readImgFile(String fileName) {
        Bitmap img = null;
        try {
            InputStream inputStream;
            if (mActivity != null) {
                inputStream = mActivity.getResources().getAssets().open(fileName);
            } else {
                inputStream = mFragment.getResources().getAssets().open(fileName);
            }
            img = BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return img;
    }
}