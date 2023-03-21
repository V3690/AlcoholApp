package com.leopard4.alcoholrecipe.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leopard4.alcoholrecipe.R;
import com.leopard4.alcoholrecipe.RecipeInfoActivity;
import com.leopard4.alcoholrecipe.model.myRecipe.MyRecipe;

import java.util.ArrayList;

public class MyRecipeAdapter extends RecyclerView.Adapter<MyRecipeAdapter.ViewHolder>{

    Context context;
    ArrayList<MyRecipe> myRecipeList;

    public MyRecipeAdapter(Context context, ArrayList<MyRecipe> myRecipeList) {
        this.context = context;
        this.myRecipeList = myRecipeList;
    }

    @NonNull
    @Override
    public MyRecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // xml 파일을 연결하는 작업
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_recipe_row, parent, false);
        return new MyRecipeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecipeAdapter.ViewHolder holder, int position) {

        MyRecipe myRecipe = myRecipeList.get(position);

        holder.txtRecipe.setText(myRecipe.getTitle());
    }

    @Override
    public int getItemCount() {
        return myRecipeList.size();
    }

    // 3. ViewHolder 클래스를 만든다.
    //      이 클래스는 row.xml 화면에 있는 뷰를 연결시키는 클래스다.
    // RecyclerView.ViewHolder 상속받는다.

    // 4. 생성자를 만든다.
    //    생성자에서, 뷰를 연결시키는 코드를 작성하고,
    //    클릭 이벤트도 작성한다.
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtRecipe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtRecipe = itemView.findViewById(R.id.txtRecipe);

            txtRecipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int index = getAdapterPosition();

                    MyRecipe myRecipe = myRecipeList.get(index);

                    int recipeId = myRecipe.getRecipeId();

                    int userId = myRecipe.getUserId();

                    Intent intent = new Intent(context, RecipeInfoActivity.class);
                    intent.putExtra("recipeId", recipeId);
                    intent.putExtra("userId", userId);
                    context.startActivity(intent);

                }
            });


        }
    }

}
