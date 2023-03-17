package com.leopard4.alcoholrecipe.adapter;
// 1. RecyclerView.Adapter 를 상속받는다.

// 2. 상속받은 클래스가 abstract 이므로, unimplemented method 오버라이드!
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leopard4.alcoholrecipe.MyRecipeWriteSecondActivity;
import com.leopard4.alcoholrecipe.R;
import com.leopard4.alcoholrecipe.model.alcohol.Alcohol;
import com.leopard4.alcoholrecipe.model.alcohol.AlcoholList;

import java.io.Serializable;
import java.util.ArrayList;

// 7. 빨간색 에러가 뜨면, 우리가 만든 ViewHolder 로
// onCreateViewHolder, onBindViewHolder 변경해준다.
public class AlcoholAdapter extends RecyclerView.Adapter<AlcoholAdapter.ViewHolder>{
    // 5. 어댑터 클래스의 멤버변수와 생성자를 만들어 준다.

    Context context;
    ArrayList<Alcohol> alcoholList;

    ArrayList<String> selectedAlcohol = new ArrayList<>();

    public AlcoholAdapter(Context context, ArrayList<Alcohol> alcoholList) {
        this.context = context;
        this.alcoholList = alcoholList;
    }

// 8. 오버라이드 함수 3개를 전부 구현 해주면 끝!

    @NonNull
    @Override
    public AlcoholAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // xml 파일을 연결하는 작업
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alcohol_row, parent, false);
        return new AlcoholAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlcoholAdapter.ViewHolder holder, int position) {
//        Posting posting = postingList.get(position);
//        Memo memo = memoList.get(position);

        Alcohol alcohol = alcoholList.get(position);

        holder.txtAlcoholName.setText( alcohol.getName() );

    }

    @Override
    public int getItemCount() {
        return alcoholList.size();
    }

    // 3. ViewHolder 클래스를 만든다.
    //      이 클래스는 row.xml 화면에 있는 뷰를 연결시키는 클래스다.
    // RecyclerView.ViewHolder 상속받는다.

    // 4. 생성자를 만든다.
    //    생성자에서, 뷰를 연결시키는 코드를 작성하고,
    //    클릭 이벤트도 작성한다.
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtAlcoholName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtAlcoholName = itemView.findViewById(R.id.txtAlcoholName);

            txtAlcoholName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = getAdapterPosition();

//                    Alcohol alcohol = alcoholList.get(index);
                    alcoholList.get(index);
                    
//                    selectedAlcohol.add(String.valueOf(alcohol));

//                    Intent intent;//인텐트 선언
//                    intent = new Intent(context, MyRecipeWriteSecondActivity.class); //look_memo.class부분에 원하는 화면 연결
//                    intent.putExtra("selectedAlcohol", (Serializable) alcohol);

//                    Log.i("SELETED_INDEX", alcohol.getId() + " " +alcohol.getName());

//                    Intent intent = new Intent(context, SeletedAlcoholAdapter.ViewHolder);




                }
            });

        }
    }

}