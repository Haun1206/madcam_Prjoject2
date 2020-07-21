package com.example.tabswithanimatedswipe;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TabFragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabFragment1 extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<MainData> arrayList;
    private MainAdapter mainAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private EditText editSearch;
    private ArrayList<MainData> copyarrayList;


    //Nodejsd 부분
    public static Retrofit retrofit;
    public static RetrofitInterface retrofitInterface;
    public static String BASE_URL = "http://192.249.19.242:7580";

    public TabFragment1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabFragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static TabFragment1 newInstance(String param1, String param2) {
        TabFragment1 fragment = new TabFragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        FrameLayout layout = (FrameLayout)inflater.inflate(R.layout.fragment_object1, container, false);

        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        arrayList = new ArrayList<>();
        copyarrayList = new ArrayList<>();
        mainAdapter = new MainAdapter(arrayList, copyarrayList);
        recyclerView.setAdapter(mainAdapter);

        //Nodejs 연결
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        //데이터 베이스에서 데이터 가져오기
        Call<InitData> call = retrofitInterface.executeGet();
        call.enqueue(new Callback<InitData>() {
            @Override
            public void onResponse(Call<InitData> call, Response<InitData> response) {
                if(response.code() == 200){
                    JsonArray ja = response.body().getData();
                    for(int i=0; i<ja.size();i++){
                        //배열에있는 오브젝트를 가져와 이름 전화번호 추출
                        JsonObject jo = ja.get(i).getAsJsonObject();
                        String name = jo.get("name").getAsString();
                        String number = jo.get("number").getAsString();

                        //어댑터에 넘겨줘서 화면에 뷰
                        MainData mainData = new MainData(name, number);
                        arrayList.add(mainData);
                        mainAdapter.notifyDataSetChanged();
                    }
                    //검색할때를 위해 복사본에도 꼭 추가해주기
                    copyarrayList.addAll(arrayList);

                } else if (response.code() == 400){
                    //Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<InitData> call, Throwable t) {
                //Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        //검색 기능 구현
        editSearch = (EditText)layout.findViewById(R.id.search);

        editSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            //input창에 문자를 입력할때마다 호출됨.
            public void afterTextChanged(Editable editable) {
                String text = editSearch.getText().toString();
                search(text);
            }
        });

        //전화번호 목록 추가버튼
        Button btn_add = (Button)layout.findViewById(R.id.add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View v = LayoutInflater.from(getActivity()).inflate(R.layout.edit_box, null, false);
                builder.setView(v);
                final Button ButtonSubmit = (Button) v.findViewById(R.id.addbutton);
                final EditText editName = (EditText) v.findViewById(R.id.name);
                final EditText editNumber = (EditText) v.findViewById(R.id.number);


                final AlertDialog dialog = builder.create();

                ButtonSubmit.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {

                        // 1. 사용자가 입력한 내용을 가져와서
                        String strName = editName.getText().toString();
                        String strNumber = editNumber.getText().toString();


                        // 2. ArrayList에 추가해서 어댑터로 넘겨 화면에 뷰
                        MainData mainData = new MainData(strName, strNumber);
                        arrayList.add(mainData);
                        copyarrayList.add(mainData);    // 검색기능을 위해 꼭 복사본에도 추가해주기
                        mainAdapter.notifyDataSetChanged();

                        //3.데이터배이스에 추가
                        HashMap<String, String> map = new HashMap<>();
                        map.put("name", strName);
                        map.put("number", strNumber);

                        Call<Result_p> call = retrofitInterface.executePost(map);
                        call.enqueue(new Callback<Result_p>() {
                            @Override
                            public void onResponse(Call<Result_p> call, Response<Result_p> response) {
                                if(response.code() == 200){
                                    Toast.makeText(getContext() ,"Success", Toast.LENGTH_SHORT).show();
                                } else if (response.code() == 400){
                                    Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                                }

                            }
                            @Override
                            public void onFailure(Call<Result_p> call, Throwable t) {
                                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        });

                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        return layout;
    }

    //검색기능
    public void search(String text){
        arrayList.clear();
        if(text.length() == 0){
            arrayList.addAll(copyarrayList);
        } else{
            for(int i = 0; i<copyarrayList.size(); i++){
                if(copyarrayList.get(i).getName().toLowerCase().contains(text)) {
                    arrayList.add(copyarrayList.get(i));
                }
            }
        }
        mainAdapter.notifyDataSetChanged();
    }
}