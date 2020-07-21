package com.example.tabswithanimatedswipe;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.example.tabswithanimatedswipe.TabFragment1.retrofitInterface;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.CustomViewHolder> {

    private ArrayList<MainData> arrayList;
    private ArrayList<MainData> copyarrayList;

    public MainAdapter(ArrayList<MainData> arrayList, ArrayList<MainData> copyarrayList) {
        this.arrayList = arrayList;
        this.copyarrayList = copyarrayList;
    }

    @NonNull
    @Override
    public MainAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MainAdapter.CustomViewHolder holder, int position) {
        holder.name.setText(arrayList.get(position).getName());
        holder.number.setText(arrayList.get(position).getNumber());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String curName = holder.name.getText().toString();
                Toast.makeText(view.getContext(), curName, Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {
                //데이터베이스에서 삭제
                TextView tv = (TextView)view.findViewById(R.id.second);
                HashMap<String, String> map = new HashMap<>();
                map.put("number", tv.getText().toString());
                Call<ResponseBody> call = retrofitInterface.executeDelete(map);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.code() == 200){
                            //Toast.makeText(, "Success", Toast.LENGTH_SHORT).show();
                        } else if (response.code() == 400){
                            //Toast.makeText(, "Fail", Toast.LENGTH_SHORT).show();
                        }

                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        //Toast.makeText(, "Error", Toast.LENGTH_SHORT).show();
                    }
                });

                //라사이클뷰에서 삭제
                remove(holder.getAdapterPosition());
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != arrayList?arrayList.size() : 0);
    }

    public void remove(int position){
        try{
            arrayList.remove(position);
            copyarrayList.remove(position); // 검색기능을 위해 꼭 복사본에도 제거
            notifyItemRemoved(position);
        } catch(IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        protected TextView number;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = (TextView)itemView.findViewById(R.id.first);
            this.number = (TextView)itemView.findViewById(R.id.second);
        }
    }
}
