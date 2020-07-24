package com.example.tabswithanimatedswipe;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.example.tabswithanimatedswipe.TabFragment3.sRf;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.MyViewHolder>{
    private ArrayList<DashData> mDataset;
    private Context mContext;
    boolean clicked = false;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
        public TextView number;
        public TextView name;
        public TextView author;
        public TextView clicks;
        public CardView card_view;
        public ImageView item_image;

        public MyViewHolder(View view) {
            super(view);
            number = (TextView) view.findViewById(R.id.text_number);
            name = (TextView) view.findViewById(R.id.product_name);
            author = (TextView) view.findViewById(R.id.author);
            clicks = (TextView) itemView.findViewById(R.id.clicks);
            card_view = (CardView) view.findViewById(R.id.card_view);
            item_image = (ImageView) view.findViewById(R.id.item_image);
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public BoardAdapter(ArrayList<DashData> myDataset, Context context) {
        mDataset = myDataset;
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BoardAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dash_textview, parent, false);
        BoardAdapter.MyViewHolder viewHolder = new BoardAdapter.MyViewHolder(v);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        DashData dashData = mDataset.get(position);
        holder.number.setText(dashData.getNumber());
        holder.name.setText(dashData.getTitle());
        holder.author.setText(dashData.getAuthor());
        holder.clicks.setText(dashData.getClicks());

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //조회수 증가 시키기
                int to = Integer.parseInt(holder.clicks.getText().toString());
                holder.clicks.setText(Integer.toString(to+1));
                //데이터 베이스 조회수 수정
                HashMap<String, String> map = new HashMap<>();
                map.put("title", dashData.getTitle());
                map.put("click", Integer.toString(to+1));
                Call<ResponseBody> call = sRf.executePut(map);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.code() == 200){
                            Toast.makeText(mContext ,"Success", Toast.LENGTH_SHORT).show();
                        } else if (response.code() == 400){
                            Toast.makeText(mContext, "Fail", Toast.LENGTH_SHORT).show();
                        }

                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
                    }
                });

                //글 이 있는 액티비티로 전환
                Intent intent = new Intent(view.getContext(), DashboardActivity.class);
                intent.putExtra("title", dashData.getTitle());
                intent.putExtra("author", dashData.getAuthor());
                intent.putExtra("contents", dashData.getContents());
                //intent.putExtra("title", dashData.getClicks();
                clicked = true;
                view.getContext().startActivity(intent);
            }
        });

        boolean mark = false;
        Character token = ':';
        Character token2 = ',';
        int marker = 0;
        String gettoken = "";

        for(int i = 0; i < dashData.getContents().length(); i++ ){
            if (dashData.getContents().charAt(i) == token2){
                mark = false;
            }
            if (mark == true && marker == 1){
                gettoken = gettoken + dashData.getContents().charAt(i);
            }
            if (dashData.getContents().charAt(i) == token){
                mark = true;
                marker ++;
            }
        }

        if (clicked){
            Glide.with(mContext)
                    .load("http://192.249.19.243:8680/uploads/" + gettoken)
                    .into(holder.item_image);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}