package com.clarance.cateringserviceapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.clarance.cateringserviceapp.R;

import java.util.ArrayList;

public class MenuListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<String> nameList;
    private ArrayList<String> priceList;
    private ArrayList<Bitmap> imgList;

    private MenuListAdapter.onItemAddListener mOnItemAddListener;
    @SuppressLint("ResourceType")
    public MenuListAdapter(Context mContext, ArrayList<String> nameList, ArrayList<String> priceList, ArrayList<Bitmap> imgList) {
        mInflater = LayoutInflater.from(mContext);
        this.nameList = nameList;
        this.priceList = priceList;
        this.imgList = imgList;
    }

    @Override
    public int getCount() {
        return nameList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    //view holder
    class ViewHolder {
        ImageView foodImg;
        TextView foodName;
        TextView foodPrice;
        ImageButton btAddToList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        //add view to list
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.menu_list_cell, null);

            //find view
            holder.foodImg = convertView.findViewById(R.id.food_img);
            holder.foodName = convertView.findViewById(R.id.food_name);
            holder.foodPrice = convertView.findViewById(R.id.food_price);
            holder.btAddToList = convertView.findViewById(R.id.bt_add_to_list);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // set attribute
        holder.foodName.setText(nameList.get(position));
        holder.foodPrice.setText(priceList.get(position));
        holder.foodImg.setImageBitmap(imgList.get(position));
        holder.btAddToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemAddListener.onAddClick(position);
            }
        });

        return convertView;
    }

    /*添加按钮的监听口*/
    public interface onItemAddListener {
        void onAddClick(int i);
    }

    public void setOnItemAddClickListener(MenuListAdapter.onItemAddListener mOnItemAddListener) {
        this.mOnItemAddListener = mOnItemAddListener;
    }
}
