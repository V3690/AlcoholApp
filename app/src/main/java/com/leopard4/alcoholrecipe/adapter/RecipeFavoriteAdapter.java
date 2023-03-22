package com.leopard4.alcoholrecipe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.leopard4.alcoholrecipe.R;
import com.leopard4.alcoholrecipe.model.recipe.RecipeFavorite;

import java.util.ArrayList;

public class RecipeFavoriteAdapter extends RecyclerView.Adapter<RecipeFavoriteAdapter.ViewHolder>{
    Context context;
    ArrayList<RecipeFavorite> recipeFavoriteList;

    // 플래그먼트에서 사용가능토록,
    // 어댑터의 특정 행이나 버튼을 누르면 처리할 함수를 만든다.
    public interface onItemClickListener{
        void onItemClick(int index);
    }

    public onItemClickListener listener;

    public void setOnItemClickListener(onItemClickListener listener){
        this.listener = listener;
    }

    public RecipeFavoriteAdapter(Context context, ArrayList<RecipeFavorite> recipeFavoriteList) {
        this.context = context;
        this.recipeFavoriteList = recipeFavoriteList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_favorite_row, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecipeFavorite recipeFavorite = recipeFavoriteList.get(position);

        holder.txtTitle.setText(recipeFavorite.getTitle());


    }

    @Override
    public int getItemCount() {
        return recipeFavoriteList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView txtTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView1);
            txtTitle = itemView.findViewById(R.id.txtRecipeTitle);

            cardView.setOnClickListener(new View.OnClickListener() {
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
