package com.a202sgi.hans.cartscanner;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import static com.a202sgi.hans.cartscanner.SQLController.KEY_NAME;
import static com.a202sgi.hans.cartscanner.SQLController.KEY_PRICE;
import static com.a202sgi.hans.cartscanner.SQLController.KEY_TIMESTAMP;

/**
 * Created by Hanny on 4/3/2017.
 */

public class GoodListAdapter extends CursorAdapter{


    private Context mContext;
    private List<Goods> mGoodsList;

    public GoodListAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.layout_goodlist, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvName = (TextView)view.findViewById(R.id.tv_goodsname);
        TextView tvPrice = (TextView)view.findViewById(R.id.tv_total);
        TextView tvDate = (TextView)view.findViewById(R.id.tv_date);

        //int id = cursor.getInt(0);
        String name = cursor.getString(cursor.getColumnIndexOrThrow("goods_name"));
        String price = cursor.getString(cursor.getColumnIndexOrThrow("price"));
        String date = cursor.getString(cursor.getColumnIndexOrThrow("timeStamp"));

        tvName.setText(name);
        tvPrice.setText("RM " + price);
        tvDate.setText(date);

    }
}
