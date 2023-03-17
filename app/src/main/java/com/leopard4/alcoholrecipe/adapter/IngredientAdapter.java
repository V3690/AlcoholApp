package com.leopard4.alcoholrecipe.adapter;

// 1. RecyclerView.Adapter 를 상속받는다.

// 2. 상속받은 클래스가 abstract 이므로, unimplemented method 오버라이드!

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leopard4.alcoholrecipe.R;
import com.leopard4.alcoholrecipe.model.alcohol.Alcohol;
import com.leopard4.alcoholrecipe.model.ingredient.Ingredient;

import java.util.ArrayList;

// 7. 빨간색 에러가 뜨면, 우리가 만든 ViewHolder 로
// onCreateViewHolder, onBindViewHolder 변경해준다.

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder>{
    // 5. 어댑터 클래스의 멤버변수와 생성자를 만들어 준다.

    Context context;
    ArrayList<Ingredient> ingredientList;
    Ingredient selectedIngredient;

    public IngredientAdapter(Context context, ArrayList<Ingredient> ingredientList) {
        this.context = context;
        this.ingredientList = ingredientList;
    }

    // 8. 오버라이드 함수 3개를 전부 구현 해주면 끝!

    @NonNull
    @Override
    public IngredientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // xml 파일을 연결하는 작업
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_row, parent, false);
        return new IngredientAdapter.ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.ViewHolder holder, int position) {

        Ingredient ingredient = ingredientList.get(position);

        holder.txtIngredientName.setText( ingredient.getName() );

    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    // 3. ViewHolder 클래스를 만든다.
    //      이 클래스는 row.xml 화면에 있는 뷰를 연결시키는 클래스다.
    // RecyclerView.ViewHolder 상속받는다.

    // 4. 생성자를 만든다.
    //    생성자에서, 뷰를 연결시키는 코드를 작성하고,
    //    클릭 이벤트도 작성한다.
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtIngredientName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtIngredientName = itemView.findViewById(R.id.txtIngredientName);

        }
    }
}
