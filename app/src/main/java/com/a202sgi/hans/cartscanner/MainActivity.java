package com.a202sgi.hans.cartscanner;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar_top;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    NfcAdapter nfcAdapter;
    String nfcData, stringName, stringPrice;
    SimpleDateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar_top = (Toolbar) findViewById(R.id.toolBar_top);
        setSupportActionBar(toolbar_top);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new HomeFragment(), "");
        viewPagerAdapter.addFragment(new HistoryFragment(), "");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_nfc);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_cart);
        String date = dateTime.format(new Date());

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter != null && nfcAdapter.isEnabled()) {
            Intent intent = getIntent();

            if (intent.getType() != null && intent.getType().equals("application/" + getPackageName())) {
                if (nfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
                    // Read the first record which contains the NFC data
                    Parcelable[] rawMsgs = intent
                            .getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
                    NdefRecord Record = ((NdefMessage) rawMsgs[0]).getRecords()[0];
                    nfcData = new String(Record.getPayload());

                    // split with ","
                    String[] stringParts = nfcData.split(",");
                    stringName = stringParts[0];
                    stringPrice = stringParts[1];

                    try {
                        SQLController newEntry = new SQLController(MainActivity.this);
                        newEntry.open();
                        newEntry.createEntry(stringName, stringPrice, date);

                        newEntry.close();
                    } catch (Exception e) {
                        Toast.makeText(this,
                                "Error: Unable to add new entry into database.",
                                Toast.LENGTH_SHORT).show();
                    }
                    // Display the data on the tag
                    if (nfcData.isEmpty()) {
                        Toast.makeText(this, "Not data available", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(
                                this,
                                "Goods: " + stringName
                                        + "\n" + "Price: " + stringPrice, Toast.LENGTH_LONG).show();
                    }
                    Toast.makeText(this, "NDEF Found", Toast.LENGTH_SHORT).show();

                }

            }
            Toast.makeText(this, "NFC available", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "NFC not available", Toast.LENGTH_SHORT).show();
        }

    }
    public void onResume()
    {
        super.onResume();
        HistoryFragment fragment = (HistoryFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_history); // Replace "layout" with your layout where you are loading the fragment
        if (fragment != null) {
            fragment.reload();
        }
    }
}
