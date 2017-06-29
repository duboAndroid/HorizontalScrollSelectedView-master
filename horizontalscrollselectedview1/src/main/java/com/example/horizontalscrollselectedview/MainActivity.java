package com.example.horizontalscrollselectedview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private View leftImageView;
    private View rightImageView;
    private HorizontalSelectedView hsMain;
    private List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initdates();
    }

    private void initdates() {
        for (int i = 0; i < 6; i++) {
            list.add(i + "0000");
        }
        hsMain.dates(list);
        hsMain.OnSelectList(new HorizontalSelectedView.SelectList() {
            @Override
            public void select(String text) {
                Toast.makeText(MainActivity.this,text,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        hsMain = (HorizontalSelectedView) findViewById(R.id.hd_main);
        leftImageView = findViewById(R.id.iv_left);
        rightImageView = findViewById(R.id.iv_right);
    }
}
