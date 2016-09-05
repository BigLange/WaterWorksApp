package com.example.think.waterworksapp;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.think.waterworksapp.custom_view.UpperActivity;
import com.example.think.waterworksapp.utils.NfcUtils;

import org.json.JSONObject;

import java.util.ArrayList;

public class SelectPersonnelActivity extends UpperActivity implements AdapterView.OnItemClickListener {

    //    private ListView staffListView;
    private TextView textView;
    private ArrayAdapter<String> staffAdapter;
    private ArrayList<String> staffNames;
    private NfcUtils nfcUtils;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAdapter();
//        nfcUtils = new NfcUtils(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            nfcUtils.processNfcConnectedIntent(intent);
//            setTextViewValue();
            return;
        }
    }

    public void setTextViewValue(String data){
        textView.setText(data);
    }

    @Override
    protected void initView() {
//        staffListView = (ListView) findViewById(R.id.class_staff_list);
        textView = findView(R.id.nfc_data_show_txt);
    }

    @Override
    protected void initEvent() {
//        staffListView.setOnItemClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_personnel;
    }


    private void initAdapter() {
//        initValue();
//        staffAdapter = new ArrayAdapter<String>(this,R.layout.select_staff_time_moban,R.id.mrele_frag2_grid_item_btn,staffNames);
//        staffListView.setAdapter(staffAdapter);
    }

    private void initValue() {
        staffNames = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            staffNames.add("张三" + i);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, SelectEquipmentActivity.class);
        startActivity(intent);
    }
}
