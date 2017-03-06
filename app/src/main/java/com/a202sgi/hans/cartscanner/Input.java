package com.a202sgi.hans.cartscanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Input extends AppCompatActivity {

    SimpleDateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String date = dateTime.format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        Button btnSimulate = (Button) findViewById(R.id.btn_simulate);



        btnSimulate.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                SQLController sql = new SQLController(Input.this);

                try
                {
                    EditText etGoodsName = (EditText) findViewById(R.id.etGoodsName);
                    EditText etPrice = (EditText) findViewById(R.id.etPrice);

                    String name = etGoodsName.getText().toString();
                    String Price = etPrice.getText().toString();

                    if(name.matches(""))
                    {
                        Toast.makeText(Input.this, "Please enter name", Toast.LENGTH_SHORT).show();
                    }
                    else if (Price.matches(""))
                    {
                        Toast.makeText(Input.this, "Please enter price", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        sql.open();
                        sql.createEntry(name, Price, date);
                        sql.close();
                        Toast.makeText(Input.this, "Added. This is Simulating NFC action", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Input.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                }catch(Exception e)
                {
                    Toast.makeText(Input.this, e.toString() , Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}
