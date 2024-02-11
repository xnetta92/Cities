package com.example.cities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private Button buttonSave;
    private Button buttonModify;
    private Button buttonBack;
    private Button buttonNewData;
    private ProgressBar progressBar;
    private EditText editTextId;
    private EditText editTextName;
    private EditText editTextAge;
    private LinearLayout linearLayoutForm;
    private ListView listViewData;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}