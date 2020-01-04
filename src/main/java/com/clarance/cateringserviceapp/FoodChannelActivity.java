package com.clarance.cateringserviceapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class FoodChannelActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_channel);
        // get window num
        int windowNum = getIntent().getIntExtra("windowNum", -1);
        TextView window_num_container = findViewById(R.id.window_num);
        // show window num
        window_num_container.setText(windowNum + " window");
    }
}
