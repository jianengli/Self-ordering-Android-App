package com.clarance.cateringserviceapp.Listener;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import com.clarance.cateringserviceapp.MenuTabFragment.MenuFragment;
import com.clarance.cateringserviceapp.MenuTabFragment.PaymentFragment;
import com.clarance.cateringserviceapp.R;

public class MenuTabCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
    private AppCompatActivity mActivity;
    //Fragment其目的是为了解决不同屏幕分辩率的动态和灵活UI设计。
    private MenuFragment eatingMenuFragment; // eating fragment
    private MenuFragment drinkingMenuFragment; // drinking fragment
    private  MenuFragment setMealMenuFragment; // set meal fragment
    private  PaymentFragment payingFragment; // payment fragment

    public MenuTabCheckedChangeListener(AppCompatActivity mActivity){
        this.mActivity = mActivity;
        eatingMenuFragment = new MenuFragment();

        //initial & default fragment(eating)
        Bundle eatingFolder = new Bundle();
        eatingFolder.putString("folder", "eating_menu");
        eatingMenuFragment.setArguments(eatingFolder);
        this.mActivity.getSupportFragmentManager().beginTransaction().add(R.id.menu_fragment_container, eatingMenuFragment).commitAllowingStateLoss();

        // initialize other fragment(drinking & set meal)
        drinkingMenuFragment = new MenuFragment();
        Bundle drinkingFolder = new Bundle();
        drinkingFolder.putString("folder", "drinking_menu");
        drinkingMenuFragment.setArguments(drinkingFolder);

        setMealMenuFragment = new MenuFragment();
        Bundle setMealFolder = new Bundle();
        setMealFolder.putString("folder", "set_meal_menu");
        setMealMenuFragment.setArguments(setMealFolder);
    }
    @Override
    //change fragment after clicked tab
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group.getChildAt(0).getId() == checkedId){
            //show eating fragment
            mActivity.getSupportFragmentManager().beginTransaction().replace(R.id.menu_fragment_container, eatingMenuFragment).commitAllowingStateLoss();
        }else if (group.getChildAt(1).getId() == checkedId){
            //show drinking fragment
            mActivity.getSupportFragmentManager().beginTransaction().replace(R.id.menu_fragment_container, drinkingMenuFragment).commitAllowingStateLoss();
        }else if (group.getChildAt(2).getId() == checkedId){
            //show set meal fragment
            mActivity.getSupportFragmentManager().beginTransaction().replace(R.id.menu_fragment_container, setMealMenuFragment).commitAllowingStateLoss();
        }else if (group.getChildAt(3).getId() == checkedId){
            // show payment fragment
            payingFragment = new PaymentFragment();
            mActivity.getSupportFragmentManager().beginTransaction().replace(R.id.menu_fragment_container, payingFragment).commitAllowingStateLoss();
        }
    }
}
