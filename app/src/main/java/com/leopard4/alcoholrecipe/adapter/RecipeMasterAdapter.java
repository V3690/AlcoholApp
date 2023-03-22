package com.leopard4.alcoholrecipe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.leopard4.alcoholrecipe.R;
import com.leopard4.alcoholrecipe.model.recipe.RecipeMaster;

import java.util.ArrayList;

public class RecipeMasterAdapter extends RecyclerView.Adapter<RecipeMasterAdapter.ViewHolder>{
    Context context;
    ArrayList<RecipeMaster> recipeMasterList;

    // 플래그먼트에서 사용가능토록,
    // 어댑터의 특정 행이나 버튼을 누르면 처리할 함수를 만든다.
    public interface onItemClickListener{
        void onItemClick(int index);
    }

    public onItemClickListener listener;

    public void setOnItemClickListener(onItemClickListener listener){
        this.listener = listener;
    }

    public RecipeMasterAdapter(Context context, ArrayList<RecipeMaster> recipeMasterList) {
        this.context = context;
        this.recipeMasterList = recipeMasterList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_master_row, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecipeMaster recipeMaster = recipeMasterList.get(position);

        holder.txtTitle.setText(recipeMaster.getTitle());
        Glide.with(context).load(recipeMaster.getImgUrl()).into(holder.imgMaster);


    }

    @Override
    public int getItemCount() {
        return recipeMasterList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView1;
        ImageView imgMaster;
        TextView txtTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView1 = itemView.findViewById(R.id.cardView1);
            txtTitle = itemView.findViewById(R.id.txtRecipeTitle);
            imgMaster = itemView.findViewById(R.id.imgMaster);

            cardView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int index = getAdapterPosition(); // 클릭한 위치를 가져온다.
                        if(index != RecyclerView.NO_POSITION){ // 클릭한 위치가 존재한다면
                            listener.onItemClick(index); // 리스너에게 클릭한 위치를 알려준다.
                        }
                    }
                }
            });
        }
    }
}
