package com.clarance.cateringserviceapp.Listener;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.clarance.cateringserviceapp.MainActivity;
import com.clarance.cateringserviceapp.R;

import java.util.Map;

public class DescriptionBtClickListener implements View.OnClickListener {

    MainActivity mActivity;
    private TextView descriptionFoodName, descriptionFoodPrice, descriptionFoodNum, descriptionFoodOrderNum;
    private ImageButton descriptionDecreaseBt;
    private Map<String, MainActivity.FoodOrder> menuOrder;
    private int numOfFood;
    private String nameOfFood;
    private String priceOfFood;


    public DescriptionBtClickListener(MainActivity mActivity) {
        this.mActivity = mActivity;
        // find view
        descriptionFoodName = mActivity.findViewById(R.id.description_food_name);
        descriptionFoodPrice = mActivity.findViewById(R.id.description_food_price);
        descriptionFoodNum = mActivity.findViewById(R.id.description_food_num);
        descriptionDecreaseBt = mActivity.findViewById(R.id.description_food_num_decrease);
        descriptionFoodOrderNum = mActivity.findViewById(R.id.description_num_order);

        initialize();
    }

    //initialize the description layout
    public void initialize() {
        numOfFood = 0;
        menuOrder = mActivity.getMenuOrder();
        nameOfFood = descriptionFoodName.getText().toString();
        priceOfFood = descriptionFoodPrice.getText().toString();
        descriptionDecreaseBt.setVisibility(View.INVISIBLE);
        descriptionFoodNum.setVisibility(View.INVISIBLE);

        //show the num of food ordered
        if (menuOrder.containsKey(nameOfFood)) {
            descriptionFoodOrderNum.setVisibility(View.VISIBLE);
            descriptionFoodOrderNum.setText("exist " + menuOrder.get(nameOfFood).getFoodNum() + " in list");
        } else {
            descriptionFoodOrderNum.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // decrease food number
            case R.id.description_food_num_decrease:
                descriptionFoodNum.setText(String.valueOf(numOfFood -= 1));
                // when number is 0, number text and decrease bt hide
                if (numOfFood <= 0) {
                    descriptionDecreaseBt.setVisibility(View.INVISIBLE);
                    descriptionFoodNum.setVisibility(View.INVISIBLE);
                }
                break;
            // increase food number
            case R.id.description_food_num_increase:
                descriptionFoodNum.setText(String.valueOf(numOfFood += 1));
                // when number is not 0, number text and decrease bt show
                if (numOfFood > 0) {
                    descriptionDecreaseBt.setVisibility(View.VISIBLE);
                    descriptionFoodNum.setVisibility(View.VISIBLE);
                }
                break;
            // add food name, price, number to order list
	    // or update food number	
            case R.id.description_order_bt:
                if (numOfFood > 0) {
                    if (menuOrder.containsKey(nameOfFood)) {
                        menuOrder.get(nameOfFood).setFoodNum(numOfFood);
                        //update the number if the food in the payment list
                        descriptionFoodOrderNum.setVisibility(View.VISIBLE);
                    } else {
			// show the number of food in the payment list
                        menuOrder.put(nameOfFood, new MainActivity.FoodOrder(nameOfFood, priceOfFood, numOfFood));
                        descriptionFoodOrderNum.setVisibility(View.VISIBLE);
                    }
			
		    // update food number in food order num text
                    descriptionFoodOrderNum.setText("exist " + menuOrder.get(nameOfFood).getFoodNum() + " in list");
                    Toast.makeText(mActivity, "Add to list successfully", Toast.LENGTH_SHORT).show();
                }

                break;
            //remove food from order list
            case R.id.description_cancel_bt:
                if (menuOrder.containsKey(nameOfFood)) {
		    //remove food
                    menuOrder.remove(nameOfFood);
                    //hide number text and decrease bt, set number to 0
                    numOfFood = 0;
                    descriptionFoodNum.setText("0");
                    descriptionDecreaseBt.setVisibility(View.INVISIBLE);
                    descriptionFoodNum.setVisibility(View.INVISIBLE);
                    descriptionFoodOrderNum.setVisibility(View.INVISIBLE);
                    Toast.makeText(mActivity, "Remove from list successfully", Toast.LENGTH_SHORT).show();
                }
                break;
            //hide description
            case R.id.description_back_bt:
                mActivity.hideDescription();
                break;
        }
    }
}
