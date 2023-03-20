package com.leopard4.alcoholrecipe.adapter;


// 1. RecyclerView.Adapter 를 상속받는다.

// 2. 상속받은 클래스가 abstract 이므로, unimplemented method 오버라이드!


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leopard4.alcoholrecipe.R;
import com.leopard4.alcoholrecipe.model.alcohol.Alcohol;

import java.util.ArrayList;

// 7. 빨간색 에러가 뜨면, 우리가 만든 ViewHolder 로
// onCreateViewHolder, onBindViewHolder 변경해준다.

// 유저가 레시피 작성때 선택한 알콜의 목록
public class SeletedAlcoholAdapter extends RecyclerView.Adapter<SeletedAlcoholAdapter.ViewHolder>{

    public static Class<?> ViewHolder;
    Context context;
    ArrayList<Alcohol> alcoholSelectedlList;

    public SeletedAlcoholAdapter(Context context, ArrayList<Alcohol> alcoholSelectedlList) {
        this.context = context;
        this.alcoholSelectedlList = alcoholSelectedlList;
    }



    // 8. 오버라이드 함수 3개를 전부 구현 해주면 끝!

    @NonNull
    @Override
    public SeletedAlcoholAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // xml 파일을 연결하는 작업
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.selected_alcohol_row, parent, false);
        return new SeletedAlcoholAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeletedAlcoholAdapter.ViewHolder holder, int position) {






//        Alcohol alcohol = alcoholSelectedlList.get(position);
//
//        holder.txtAlcoholName.setText( alcohol.getName() );

    }

    @Override
    public int getItemCount() {
        return alcoholSelectedlList.size();
    }

    // 3. ViewHolder 클래스를 만든다.
    //      이 클래스는 row.xml 화면에 있는 뷰를 연결시키는 클래스다.
    // RecyclerView.ViewHolder 상속받는다.

    // 4. 생성자를 만든다.
    //    생성자에서, 뷰를 연결시키는 코드를 작성하고,
    //    클릭 이벤트도 작성한다.
    public class ViewHolder extends RecyclerView.ViewHolder{




        TextView txtAlcoholName;
        ImageButton imgDelete;

        public ViewHolder(@NonNull View itemView) {


            super(itemView);



            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });




        }
    }
}
