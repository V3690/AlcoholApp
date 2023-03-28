package com.leopard4.alcoholrecipe.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.leopard4.alcoholrecipe.DogamInfoActivity;
import com.leopard4.alcoholrecipe.R;
import com.leopard4.alcoholrecipe.model.dogam.Dogam;

import java.util.ArrayList;

public class DogamFavoriteAdapter extends RecyclerView.Adapter<DogamFavoriteAdapter.ViewHolder> {

    Context context;
    ArrayList<Dogam> dogamList;

    public DogamFavoriteAdapter(Context context, ArrayList<Dogam> dogamList) {
        this.context = context;
        this.dogamList = dogamList;
    }

    @NonNull
    @Override
    public DogamFavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_favorite_row, parent, false);
        return new DogamFavoriteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Dogam dogam = dogamList.get(position);
        holder.txtTitle.setText(dogam.getName());

    }



    @Override
    public int getItemCount() {
        return dogamList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView txtTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            txtTitle=itemView.findViewById(R.id.txtTitle);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = getAdapterPosition();
                    Dogam dogam = dogamList.get(index);

                    int alcoholId = dogam.getId();

                    Intent intent = new Intent(context, DogamInfoActivity.class);
                    intent.putExtra("alcoholId", alcoholId);
                    context.startActivity(intent);
                }
            });


        }
    }



}
