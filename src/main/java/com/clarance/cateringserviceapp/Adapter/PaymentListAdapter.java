package com.clarance.cateringserviceapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;


import com.clarance.cateringserviceapp.R;

import java.util.ArrayList;

public class PaymentListAdapter extends BaseAdapter {
    private ArrayList<String> nameList;
    private ArrayList<String> priceList;
    private ArrayList<Integer> numberList;
    private LayoutInflater mInflater;//布局装载器对象

    public PaymentListAdapter(Context mContext, ArrayList<String> nameList, ArrayList<String> priceList, ArrayList<Integer> numberList) {
        mInflater = LayoutInflater.from(mContext);
        this.nameList = nameList;
        this.priceList = priceList;
        this.numberList = numberList;
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        //如果view未被实例化过，缓存池中没有对应的缓存
        if (convertView == null) {
            viewHolder = new ViewHolder();
            // 由于我们只需要将XML转化为View，并不涉及到具体的布局，所以第二个参数通常设置为null
            convertView = mInflater.inflate(R.layout.payment_listview, null);

            //对viewHolder的属性进行赋值
            viewHolder.food_name = convertView.findViewById(R.id.food_name);
            viewHolder.food_price = convertView.findViewById(R.id.food_price);
            viewHolder.food_size = convertView.findViewById(R.id.food_size);
            viewHolder.payment_delete_bt = convertView.findViewById(R.id.payment_delete_bt);

            //通过setTag将convertView与viewHolder关联
            convertView.setTag(viewHolder);
        }
        else{//如果缓存池中有对应的view缓存，则直接通过getTag取出viewHolder
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 设置控件的数据
        viewHolder.food_name.setText(nameList.get(position));
        viewHolder.food_price.setText(priceList.get(position));
        viewHolder.food_size.setText("x"+ numberList.get(position));
        viewHolder.payment_delete_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemDeleteListener.onDeleteClick(position);
            }
        });

        return convertView;
    }

    /*删除按钮的监听口*/
    public interface onItemDeleteListener {
        void onDeleteClick(int i);
    }

    private onItemDeleteListener mOnItemDeleteListener;

    public void setOnItemDeleteClickListener(onItemDeleteListener mOnItemDeleteListener) {
        this.mOnItemDeleteListener = mOnItemDeleteListener;
    }

    //view holder
    class ViewHolder{
        public TextView food_name;
        public TextView food_price;
        public TextView food_size;
        public ImageButton payment_delete_bt;
    }
}
