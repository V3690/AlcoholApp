//package com.leopard4.alcoholrecipe.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.cardview.widget.CardView;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.leopard4.alcoholrecipe.R;
//import com.leopard4.alcoholrecipe.model.Posting;
//
//import java.util.ArrayList;
//
//
//public class PostingAdapter extends RecyclerView.Adapter<PostingAdapter.ViewHolder>{
//    Context context;
//    ArrayList<Posting> postingList;
//
//
//    public interface OnItemClickListener {
//        // 프레그먼트에서 사용가능토록,
//        // 어댑터의 특정 행이나 버튼 누르면 처리할 함수를 만든다.
//        void likeProcess(int index);
//        void onImageClick(int index);
//    }
//    public OnItemClickListener listener;
//
//    public void setOnItemClickListener(OnItemClickListener listener){
//        this.listener = listener;
//    }
//
//
////    SimpleDateFormat sdf;
////    SimpleDateFormat df;
//
//
//    public PostingAdapter(Context context, ArrayList<Posting> postingList) {
//        this.context = context;
//        this.postingList = postingList;
//
//        // 날짜의 형식을 변환하는 코드이지만 나는 서버단에서 처리했기 때문에 필요가 없다.
//        // 2023-01-17T00:38:36 => 2023-01-17 00:38:36
//        // UTC => 로컬타임으로 변경
////        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
////        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
////        df.setTimeZone(TimeZone.getDefault()); // 내 핸드폰에 셋팅된 타임으로 변경하라는 뜻
//
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.posting_row, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Posting posting = postingList.get(position);
//
//        holder.txtContent.setText(posting.getContent());
//        holder.txtEmail.setText(posting.getEmail());
//        holder.txtCreatedAt.setText(posting.getCreatedAt());
//
////        try {
////            Date date = sdf.parse(posting.getCreatedAt());
////            holder.txtCreatedAt.setText(df.format(date));
////        } catch (ParseException e) {
////            throw new RuntimeException(e);
////        }
//
//
//
//        // 좋아요 이미지 설정
//        if (posting.getIsLike() == 1){
//            holder.imgLike.setImageResource(R.drawable.ic_thumb_up_2);
//        }else {
//            holder.imgLike.setImageResource(R.drawable.ic_thumb_up_1);
//        }
//
//        // http 프로토콜이 안드로이드 9.0 이상에서는 보안상의 이유로 허용되지 않는다.
//        // 때문에 manifest 에서 android:usesCleartextTraffic="true" 를 추가해야 한다.
//        // 하지만 이미 되어있음에도 불구하고 안되는 경우가 있다.
//        // 그래서 임시조치로 posting.getImageUrl() 를 https 프로토콜로 변경해야 한다.
//        posting.setImageUrl(posting.getImageUrl().replace("http://", "https://"));
//
//        Glide.with(context)
//                .load(posting.getImageUrl())
//                .placeholder(R.drawable.ic_outline_add_photo_alternate_24)
//                .into(holder.imgPhoto);
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return postingList.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder{
//        CardView cardView;
//        TextView txtContent, txtCreatedAt, txtEmail;
//        ImageView imgLike, imgPhoto;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            cardView = itemView.findViewById(R.id.cardView);
//            txtContent = itemView.findViewById(R.id.txtContent);
//            txtCreatedAt = itemView.findViewById(R.id.txtCreatedAt);
//            txtEmail = itemView.findViewById(R.id.txtEmail);
//            imgLike = itemView.findViewById(R.id.imgLike);
//            imgPhoto = itemView.findViewById(R.id.imgPhoto);
//
//            imgLike.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // 1. 어느번째의 데이터의 좋아요를 누른것인지 확인
//                    int index = getAdapterPosition();
////                    ((MainActivity)context).likeProcess(index);
////                    ((FirstFragment) ((MainActivity)context).firstFragment).likeProcess(index);
//                    listener.likeProcess(index);
//
//                }
//            });
//
//            imgPhoto.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int index = getAdapterPosition();
//                    listener.onImageClick(index);
//                }
//            });
//
//        }
//    }
//}
