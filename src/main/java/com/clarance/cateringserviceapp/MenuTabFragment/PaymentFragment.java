package com.clarance.cateringserviceapp.MenuTabFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clarance.cateringserviceapp.Adapter.PaymentListAdapter;
import com.clarance.cateringserviceapp.MainActivity;
import com.clarance.cateringserviceapp.PaymentActivity;
import com.clarance.cateringserviceapp.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class PaymentFragment extends Fragment {
    private MainActivity mainActivity;
    private Map<String, MainActivity.FoodOrder> menuOrder;
    private ArrayList<String> foodName;
    private ArrayList<String> foodPrice;
    private ArrayList<Integer> foodNum;
    private TextView totalPayment;
    private ListView paymentList;
    private PaymentListAdapter paymentListAdapter;
    private Button btPay;
    private final int totalMoney = 200;
    private int totalPrice;

    String password = "123";

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View menu = inflater.inflate(R.layout.payment_fragment, container, false); // get view
        mainActivity = (MainActivity) getActivity();
        menuOrder = mainActivity.getMenuOrder();
        return menu;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // initialize total payment
        totalPayment = view.findViewById(R.id.total_money);

        foodName = new ArrayList<>();
        foodPrice = new ArrayList<>();
        foodNum = new ArrayList<>();

        // get list from map
        //迭代器是一种设计模式，它是一个对象，它可以遍历并选择序列中的对象，而开发人员不需要了解该序列的底层结构。
        // 迭代器通常被称为“轻量级”对象，因为创建它的代价小。
        Iterator<Map.Entry<String, MainActivity.FoodOrder>> entries = menuOrder.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, MainActivity.FoodOrder> entry = entries.next();
            foodName.add(entry.getValue().getFoodName());
            foodPrice.add(entry.getValue().getFoodPrice());
            foodNum.add(entry.getValue().getFoodNum());
        }

        showTotalPrice();

        paymentList = view.findViewById(R.id.payment_list);

        // show list
        paymentListAdapter = new PaymentListAdapter(this.getContext(), foodName, foodPrice, foodNum);
        paymentList.setAdapter(paymentListAdapter);
        // add remove food from list listener
        paymentListAdapter.setOnItemDeleteClickListener(new PaymentListAdapter.onItemDeleteListener() {
            @Override
            public void onDeleteClick(int i) {
                menuOrder.remove(foodName.get(i));
                Toast.makeText(mainActivity, "Remove successfully", Toast.LENGTH_SHORT).show();
                foodName.remove(i);
                foodPrice.remove(i);
                foodNum.remove(i);
                paymentListAdapter.notifyDataSetChanged();//notifyDataSetChanged方法通过一个外部的方法控制如果适配器的内容改变时需要强制调用getView来刷新每个Item的内容。
                showTotalPrice();
            }
        });

        //initialize button for password checking
        btPay = view.findViewById(R.id.bt_pay);
        // ButtonListener b = new ButtonListener();
        btPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuOrder.size() == 0){
                    Toast.makeText(mainActivity, "List is empty", Toast.LENGTH_LONG).show();
                }else {
                    showCustomizeDialog();
                }
            }
        });
    }

    // compute total price
    public void showTotalPrice() {
        totalPrice = 0;
        for (int i = 0; i < foodNum.size(); i++) {
            totalPrice = totalPrice + Integer.parseInt(foodPrice.get(i).substring(0, foodPrice.get(i).length() - 1)) * foodNum.get(i);
        }
        SpannableString spannableString = new SpannableString("Total: " + totalPrice + "$");
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#CC0000")), 7, spannableString.length()-1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        totalPayment.setText(spannableString);
    }

    // enter password
    private void showCustomizeDialog() {
        /* @setView 装入自定义View ==> R.layout.dialog_customize
         * 由于dialog_customize.xml只放置了一个EditView
         * dialog_customize.xml可自定义更复杂的View
         */
        AlertDialog.Builder customizeDialog = new AlertDialog.Builder(mainActivity);
        final View dialogView = LayoutInflater.from(mainActivity).inflate(R.layout.password_checking, null);
        customizeDialog.setTitle("Please enter your password");
        customizeDialog.setView(dialogView);
        customizeDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取EditView中的输入内容
                        EditText edit_text = dialogView.findViewById(R.id.edit_text);
                        if (edit_text.getText().toString().equals(password)) {
                            Intent intent = new Intent(mainActivity, PaymentActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            //transfer account balance
                            Bundle bundle = new Bundle();
                            int accountBalance=(totalMoney -totalPrice);
                            bundle.putInt("account balance",accountBalance);
                            //put bundle into intent
                            intent.putExtra("Message",bundle);
                            //change to payment page
                            startActivity(intent);

                        } else {
                            showCustomizeDialog();
                            Toast.makeText(mainActivity, "In correct Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        customizeDialog.show();
    }


}

