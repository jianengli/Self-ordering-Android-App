package com.clarance.cateringserviceapp.MenuTabFragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.clarance.cateringserviceapp.Adapter.MenuListAdapter;
import com.clarance.cateringserviceapp.MainActivity;
import com.clarance.cateringserviceapp.R;
import com.clarance.cateringserviceapp.ReadFileFromAssets;

import java.util.ArrayList;
import java.util.Map;

public class MenuFragment extends Fragment {
    private MainActivity mainActivity;
    private MainActivity.FoodInfo foodInfo;
    private ListView menuList;
    private String fromFolder = null;

    private Map<String, MainActivity.FoodOrder> menuOrder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View menu = inflater.inflate(R.layout.menu_fragment, container, false); // get layout
        mainActivity = (MainActivity)getActivity();
        foodInfo = mainActivity.getFoodInfo(); // get food info class
        menuOrder = mainActivity.getMenuOrder(); // get order list
        return menu;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        fromFolder = getArguments().getString("folder");

        ReadFileFromAssets reader = new ReadFileFromAssets(this);
        final ArrayList<String> foodName = new ArrayList<>();
        final ArrayList<String> foodPrice = new ArrayList<>();
        final ArrayList<Bitmap> foodImg = new ArrayList<>();
        final ArrayList<String> foodSate = new ArrayList<>();

        // read file information from assets
        ArrayList<String> NameAndPriceAndState = reader.readTextFile(fromFolder + "/Name&Price&State.txt");
        for (int i = 0; i<NameAndPriceAndState.size(); i++){
            if (i%3==0){
                foodName.add(NameAndPriceAndState.get(i));
            }
            else if(i%3==1){
                foodPrice.add(NameAndPriceAndState.get(i));
            }
            else{
                foodSate.add(NameAndPriceAndState.get(i));
            }
        }

        for (int i = 0; i < foodName.size(); i++){
            foodImg.add(reader.readImgFile(fromFolder + "/" + foodName.get(i) + ".png"));
        }

        menuList = view.findViewById(R.id.eating_menu_list);

        // set adapter
        MenuListAdapter menuListAdapter = new MenuListAdapter(this.getContext(),foodName, foodPrice, foodImg);
        menuList.setAdapter(menuListAdapter);
        // set listener, show description when click
        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                foodInfo.setFoodName(foodName.get(position));
                foodInfo.setFoodPrice(foodPrice.get(position));
                foodInfo.setFoodImg(foodImg.get(position));
                foodInfo.setFoodState(foodSate.get(position));
                mainActivity.showDescription();
            }
        });

        // set add to list bt click listener
        menuListAdapter.setOnItemAddClickListener(new MenuListAdapter.onItemAddListener() {
            @Override
            public void onAddClick(int i) {
                String nameOfFood = foodName.get(i);
                String priceOfFood = foodPrice.get(i);
                String stateOfFood = foodSate.get(i);

                // add to list when is on sale
                if(stateOfFood.equals("ON SALE")){
                    if(menuOrder.containsKey(nameOfFood)){
                        menuOrder.get(nameOfFood).setFoodNum(menuOrder.get(nameOfFood).getFoodNum()+1);
                        Toast.makeText(mainActivity,"Add to list successfully",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        menuOrder.put(nameOfFood,new MainActivity.FoodOrder(nameOfFood,priceOfFood,1));
                        Toast.makeText(mainActivity,"Add to list successfully",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(mainActivity,"Add to list unsuccessful",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
