package com.leopard4.alcoholrecipe.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.leopard4.alcoholrecipe.DogamInfoActivity;
import com.leopard4.alcoholrecipe.R;
import com.leopard4.alcoholrecipe.model.dogam.Dogam;

import java.io.Serializable;
import java.util.ArrayList;

public class DogamAdapter extends RecyclerView.Adapter<DogamAdapter.ViewHolder>{

    Context context;
    ArrayList<Dogam> dogamList;


    public DogamAdapter(Context context, ArrayList<Dogam> dogamList) {
        this.context = context;
        this.dogamList = dogamList;
    }

    @NonNull
    @Override
    public DogamAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // xml 파일을 연결하는 작업
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dogam_row, parent, false);
        return new DogamAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DogamAdapter.ViewHolder holder, int position) {

        Dogam dogam = dogamList.get(position);

        Glide.with(context).load(dogam.getImgUrl())
                .placeholder(R.drawable.outline_image_24)
                .into(holder.imgAlcohol);

    }

    @Override
    public int getItemCount() {
        return dogamList.size();
    }

    // 3. ViewHolder 클래스를 만든다.
    //      이 클래스는 row.xml 화면에 있는 뷰를 연결시키는 클래스다.
    // RecyclerView.ViewHolder 상속받는다.

    // 4. 생성자를 만든다.
    //    생성자에서, 뷰를 연결시키는 코드를 작성하고,
    //    클릭 이벤트도 작성한다.
    public class ViewHolder extends RecyclerView.ViewHolder{


        ImageView imgAlcohol;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAlcohol = itemView.findViewById(R.id.imgAlcohol);

            imgAlcohol.setOnClickListener(new View.OnClickListener() {
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
