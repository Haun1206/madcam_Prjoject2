package com.example.tabswithanimatedswipe;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;


import static android.os.Environment.DIRECTORY_PICTURES;

public class TabFragment3 extends Fragment implements View.OnClickListener{
    /* Define Variables */
    private static final int MY_PERMISSION_EXTERNAL_WRITE = 4444;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    FloatingActionButton fab, fab1, fab2;
    View view;
    public static ArrayList<DashData> myDataset;
    DashData dashData;
    //Nodejsd 부분
    public static Retrofit retrofit;
    public static ApiService sRf;
    BoardAdapter boardAdapter;
    public static String BASE_URL = "http://192.249.19.242:7580";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_object3, container, false);
        recyclerView = view.findViewById(R.id.dash_recycler_view);

        fab_open = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_close);
        fab = view.findViewById(R.id.fab);
        fab1 = view.findViewById(R.id.fab1);
        fab2 = view.findViewById(R.id.fab2);

        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);

        //Nodejs 연결
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        sRf = retrofit.create(ApiService.class);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        myDataset = new ArrayList<>();
        mAdapter = new BoardAdapter(myDataset, getContext());
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);



        /** Fetch Data */
        Call<InitData> call = sRf.executeGet();
        call.enqueue(new Callback<InitData>() {
            @Override
            public void onResponse(Call<InitData> call, Response<InitData> response) {
                if(response.code() == 200){
                    JsonArray ja = response.body().getData();
                    for(int i=0; i<ja.size();i++){
                        //배열에있는 오브젝트를 가져와 이름 전화번호 추출
                        JsonObject jo = ja.get(i).getAsJsonObject();
                        String number = jo.get("number").getAsString();
                        String title = jo.get("title").getAsString();
                        String name = jo.get("name").getAsString();
                        String click = jo.get("click").getAsString();
                        String contents = jo.get("content").getAsString();

                        //어댑터에 넘겨줘서 화면에 뷰
                        DashData dashData = new DashData(number, title, name, click, contents);
                        myDataset.add(0, dashData);
                        mAdapter.notifyDataSetChanged();
                    }

                }
                else if (response.code() == 400){
                    //Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<InitData> call, Throwable t) {
                //Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.fab:
                anim();
                break;
            case R.id.fab1:
                anim();
                Intent intent1 = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                startActivityForResult(intent1, 1001);
                break;
            case R.id.fab2:
                anim();
                Intent intent = new Intent(getActivity().getApplicationContext(), UploadDashboardActivity.class);
                startActivityForResult(intent, 1001);
                break;
        }
    }

    public void anim() {
        if (isFabOpen) {
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
        } else {
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
        }
    }


}
