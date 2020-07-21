package com.example.tabswithanimatedswipe;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.util.ProcessUtils;
//myDataset

public class DashboardActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textView;
    TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        imageView = findViewById(R.id.product_image);
        textView = findViewById(R.id.product);
        textView2 = findViewById(R.id.price);

        boolean mark = false;
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String author = intent.getStringExtra("author");
        String contents = intent.getStringExtra("contents");
        Character token = ':';
        Character token2 = ',';
        Character token3 = '}';
        int marker = 0;
        String gettoken = "";
        String getprice = "";
        System.out.println("AAAAAAAAAAA" + contents);
        for(int i = 0; i < contents.length(); i++ ){
            if (contents.charAt(i) == token2){
                mark = false;
            }
            if(contents.charAt(i) == token3){
                marker++;
            }
            if (mark == true && marker == 1){
                gettoken = gettoken + contents.charAt(i);
            }

            if(mark == true && marker == 2){
                getprice = getprice + contents.charAt(i);
            }

            if (contents.charAt(i) == token){
                mark = true;
                marker ++;
            }
        }

        contents = gettoken;
        textView.setText(title);
        textView2.setText(getprice);

        Glide.with(DashboardActivity.this)
                .load("http://192.249.19.243:8680/uploads/" + gettoken)
                .into(imageView);
    }

}
