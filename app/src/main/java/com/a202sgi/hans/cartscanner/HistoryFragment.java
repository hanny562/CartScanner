
package com.a202sgi.hans.cartscanner;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.jar.Attributes;

import static com.a202sgi.hans.cartscanner.SQLController.KEY_NAME;
import static com.a202sgi.hans.cartscanner.SQLController.KEY_PRICE;
import static com.a202sgi.hans.cartscanner.SQLController.KEY_ROWID;
import static com.a202sgi.hans.cartscanner.SQLController.KEY_TIMESTAMP;

//* A simple {@link Fragment} subclass.

public class HistoryFragment extends Fragment {

    private SQLController dbread;
    private ListView lvGoods;
    SimpleDateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        lvGoods = (ListView) rootView.findViewById(R.id.lv_goods);
        Button btnDeleteAll = (Button) rootView.findViewById(R.id.btn_DeleteAll);
        btnDeleteAll.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(getContext());
                alertDialogBuilder2.setTitle("Remove attendance records");
                alertDialogBuilder2
                        .setMessage("Do you want to remove all the records?")
                        .setCancelable(false)
                        .setPositiveButton("Confirm",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface d1, int id) {
                                        dbread = new SQLController(getContext());
                                        dbread.open();
                                        dbread.deleteAll();

                                        Cursor c = dbread.QueryData("SELECT * FROM CartTable");

                                        GoodListAdapter adapter = new GoodListAdapter(getContext(), c);

                                        lvGoods.setAdapter(adapter);
                                        dbread.close();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });
                AlertDialog alertDialog2 = alertDialogBuilder2.create();
                alertDialog2.show();
            }
        });

        try {
            dbread = new SQLController(getContext());
            dbread.open();
            Cursor c = dbread.QueryData("SELECT * FROM CartTable");

            GoodListAdapter adapter = new GoodListAdapter(getContext(), c);

            lvGoods.setAdapter(adapter);
            dbread.close();
        } catch (Exception e) {
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }

    public void reload()
    {
        Cursor c = dbread.QueryData("SELECT * FROM CartTable");

        GoodListAdapter adapter = new GoodListAdapter(getContext(), c);

        lvGoods.setAdapter(adapter);
        if(adapter != null)
            adapter.notifyDataSetChanged();
    }


}

