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
import com.leopard4.alcoholrecipe.GameActivity;
import com.leopard4.alcoholrecipe.GameFaceActivity;
import com.leopard4.alcoholrecipe.R;
import com.leopard4.alcoholrecipe.RecipeActivity;
import com.leopard4.alcoholrecipe.RecipeInfoActivity;
import com.leopard4.alcoholrecipe.model.dogam.Dogam;
import com.leopard4.alcoholrecipe.model.recipe.RecipeLab;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecipeLabAdapter extends RecyclerView.Adapter<RecipeLabAdapter.ViewHolder> {

    Context context;
    ArrayList<RecipeLab> RecipeLabList;

    public RecipeLabAdapter(Context context, ArrayList<RecipeLab> recipeLabList) {
        this.context = context;
        this.RecipeLabList = recipeLabList;
    }

    @NonNull
    @Override
    public RecipeLabAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_favorite_row, parent, false);
        return new RecipeLabAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeLabAdapter.ViewHolder holder, int position) {

        RecipeLab recipeLab = RecipeLabList.get(position);

        holder.txtTitle.setText(recipeLab.getTitle());

    }

    @Override
    public int getItemCount() {
        return RecipeLabList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {

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
                    RecipeLab recipeLab = RecipeLabList.get(index);

                    int recipeId = recipeLab.getId();

                    Intent intent = new Intent(context, RecipeInfoActivity.class);
                    intent.putExtra("recipeId", recipeId);
                    context.startActivity(intent);

                }
            });


        }
    }


}
