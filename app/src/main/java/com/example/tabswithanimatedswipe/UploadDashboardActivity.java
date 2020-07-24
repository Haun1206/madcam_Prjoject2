package com.example.tabswithanimatedswipe;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static com.example.tabswithanimatedswipe.TabFragment3.sRf;
import static com.example.tabswithanimatedswipe.TabFragment3.myDataset;

public class UploadDashboardActivity extends AppCompatActivity {

    public static boolean is_dash = false;
    String imgname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploaddashboard);

        Button addbtn = (Button) findViewById(R.id.upload);
        Button imagebtn = (Button) findViewById(R.id.upload_image);
        final EditText title = (EditText) findViewById(R.id.title);
        ImageView picture = (ImageView) findViewById(R.id.dashupload_image);
        final EditText content = (EditText) findViewById(R.id.content);

        if (is_dash){
            Intent intent = getIntent();
            imgname = intent.getStringExtra("imgname");
            Glide.with(UploadDashboardActivity.this)
                    .load("http://192.249.19.243:8680/uploads/" + imgname)
                    .into(picture);
            is_dash = false;
        }



        //upload 버튼 눌렀을때 데이터 베이스에 저장하고 목록으로 돌아감
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> map = new HashMap<>();
                //글 목록 수 추가
                if(myDataset.size() == 0){
                    map.put("number", "1");
                } else{
                    DashData dd = myDataset.get(0);
                    int next_number = Integer.parseInt(dd.getNumber())+1;
                    map.put("number",Integer.toString(next_number));
                }
                map.put("title", title.getText().toString());
                map.put("name", "Haun");
                map.put("click", "0");
                String sc = content.getText().toString();
                map.put("content", "{" + "picture" + ":" + imgname + "," + "writing" + ":" + sc + "}");

                Call<DashResult> call = sRf.executePost(map);
                call.enqueue(new Callback<DashResult>() {
                    @Override
                    public void onResponse(Call<DashResult> call, Response<DashResult> response) {
                        if (response.code() == 200) {
                            Intent intent = new Intent(getApplicationContext(), TabFragment3.class);
                            startActivityForResult(intent, 1001);
                            finish();
                            //Toast.makeText(, "Success", Toast.LENGTH_SHORT).show();
                        } else if (response.code() == 400) {
                            Toast.makeText(UploadDashboardActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<DashResult> call, Throwable t) {
                        Toast.makeText(UploadDashboardActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        imagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is_dash = true;
                Intent intent = new Intent(UploadDashboardActivity.this, UploadActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

