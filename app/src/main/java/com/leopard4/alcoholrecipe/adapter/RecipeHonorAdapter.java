package com.leopard4.alcoholrecipe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.leopard4.alcoholrecipe.R;
import com.leopard4.alcoholrecipe.model.RecipeHonor;

import java.util.ArrayList;

public class RecipeHonorAdapter extends RecyclerView.Adapter<RecipeHonorAdapter.ViewHolder>{
    Context context;
    ArrayList<RecipeHonor> recipeHonorList;

    // 플래그먼트에서 사용가능토록,
    // 어댑터의 특정 행이나 버튼을 누르면 처리할 함수를 만든다.
    public interface onItemClickListener{
        void onItemClick(int index);
    }

    public onItemClickListener listener;

    public void setOnItemClickListener(onItemClickListener listener){
        this.listener = listener;
    }

    public RecipeHonorAdapter(Context context, ArrayList<RecipeHonor> recipeHonorList) {
        this.context = context;
        this.recipeHonorList = recipeHonorList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_honor_row, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecipeHonor recipeHonor = recipeHonorList.get(position);

        holder.txtLikeCnt.setText(recipeHonor.getLikeCount()+"");
        Glide.with(context).load(recipeHonor.getImgUrl()).into(holder.imgHonor);


    }

    @Override
    public int getItemCount() {
        return recipeHonorList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout layoutView;
        CardView cardView2;
        TextView txtLikeCnt;
        ImageView imgHonor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView2 = itemView.findViewById(R.id.cardView2);
            txtLikeCnt = itemView.findViewById(R.id.txtLikeCnt);
            imgHonor = itemView.findViewById(R.id.imgHonor);

            cardView2.setOnClickListener(new View.OnClickListener() {
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
