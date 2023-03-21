package com.leopard4.alcoholrecipe;


//import static com.leopard4.alcoholrecipe.RecipeInfoActivity.state;

import static com.leopard4.alcoholrecipe.RecipeInfoActivity.state;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leopard4.alcoholrecipe.adapter.RecipeHonorAdapter;
import com.leopard4.alcoholrecipe.adapter.RecipeMasterAdapter;
import com.leopard4.alcoholrecipe.api.GameApi;
import com.leopard4.alcoholrecipe.api.NetworkClient;
import com.leopard4.alcoholrecipe.api.RecipeApi;
import com.leopard4.alcoholrecipe.config.Config;
import com.leopard4.alcoholrecipe.model.ResMessage;
import com.leopard4.alcoholrecipe.model.cheers.CheersMent;
import com.leopard4.alcoholrecipe.model.cheers.CheersMentRes;
import com.leopard4.alcoholrecipe.model.cheers.Ment;
import com.leopard4.alcoholrecipe.model.recipe.RecipeHonor;
import com.leopard4.alcoholrecipe.model.recipe.RecipeHonorList;
import com.leopard4.alcoholrecipe.model.recipe.RecipeMaster;
import com.leopard4.alcoholrecipe.model.recipe.RecipeMasterList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment {

    // TODO: 매개변수 인수 이름 바꾸기, 프래그먼트 초기화 매개변수와 일치하는 이름 선택, 예: ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: 매개변수 이름 바꾸기 및 유형 변경
    private String mParam1;
    private String mParam2;
    // 멘트를 불러오기위한 단어를 저장하기 위한변수
    Button btnMyRecipe, btnGame, btnDogam, btnToast;
    TextView txtFirst, txtLast;
    int count1 = 0;
    int offset1 = 0;
    int limit1 = 5;

    int count2 = 0;
    int offset2 = 0;
    int limit2 = 5;
    String accessToken;
    ProgressBar progressBar3;

    RecyclerView recyclerView1, recyclerView2;
    RecipeMasterAdapter adapter1;
    RecipeHonorAdapter adapter2;
    ArrayList<RecipeMaster> recipeMasterList = new ArrayList<>();
    ArrayList<RecipeHonor> recipeHonorList = new ArrayList<>();
    ArrayList<CheersMent> cheersMentList = new ArrayList<>();

    private boolean isLoading1 = false;
    private boolean isLoading2 = false;

    public FirstFragment() {
        // Required empty public constructor
    }

    /**
     * 이 팩터리 메서드를 사용하여 제공된 매개 변수를 사용하여 이 조각의 새 인스턴스를 만듭니다.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return 프래그먼트 FirstFragment의 새 인스턴스입니다.
     */
    // TODO: 이름 변경 및 유형 및 매개변수 수 변경
    public static FirstFragment newInstance(String param1, String param2) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_first, container, false);

        progressBar3 = rootView.findViewById(R.id.progressBar3);
        btnMyRecipe = rootView.findViewById(R.id.btnMyRecipe);
        btnGame = rootView.findViewById(R.id.btnGame);
        btnDogam = rootView.findViewById(R.id.btnDogam);
        btnToast = rootView.findViewById(R.id.btnToast);
        txtFirst = rootView.findViewById(R.id.txtFirst);
        txtLast = rootView.findViewById(R.id.txtLast);

        recyclerView1 = rootView.findViewById(R.id.recyclerView1);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        recyclerView2 = rootView.findViewById(R.id.recyclerView2);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        getNetworkData1();
        getNetworkData2();
        getCheersMentResNetworkData();

        recyclerView1.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView1, int newState) { // 스크롤이 멈추면
                super.onScrollStateChanged(recyclerView1, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView1, int dx, int dy) { // 스크롤이 되면
                super.onScrolled(recyclerView1, dx, dy);

                int lastPosition1 = ((LinearLayoutManager)recyclerView1.getLayoutManager()).findLastCompletelyVisibleItemPosition(); // 마지막으로 보여지는 아이템의 위치
                int totalCount1 = recyclerView1.getAdapter().getItemCount(); // 아이템의 총 개수
                if(lastPosition1+1 == totalCount1 && !isLoading1){ // 마지막 아이템이 보여지고 && 데이터를 불러오고 있지 않다면

                    // 네트워크 통해서 데이터를 더 불러온다.
                    if(count1 == limit1 ){
                        // 데이터를 불러오고 있다.
                        isLoading1 = true;
                        addNetworkData1();

                    }

                }

            }
        });

        recyclerView2.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView2, int newState) { // 스크롤이 멈추면
                super.onScrollStateChanged(recyclerView2, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView2, int dx, int dy) { // 스크롤이 되면
                super.onScrolled(recyclerView2, dx, dy);

                int lastPosition2 = ((LinearLayoutManager)recyclerView2.getLayoutManager()).findLastCompletelyVisibleItemPosition(); // 마지막으로 보여지는 아이템의 위치
                int totalCount2 = recyclerView2.getAdapter().getItemCount(); // 아이템의 총 개수
                if(lastPosition2+1 == totalCount2 && !isLoading2){ // 마지막 아이템이 보여지고 && 데이터를 불러오고 있지 않다면

                    // 네트워크 통해서 데이터를 더 불러온다.
                    if(count2 == limit2 ){
                        // 데이터를 불러오고 있다.
                        isLoading2 = true;
                        addNetworkData2();

                    }

                }

            }
        });


        btnMyRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecipeActivity.class);
                startActivity(intent);
            }
        });
        btnGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GameActivity.class);
                startActivity(intent);
            }
        });
        btnDogam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DogamActivity.class);
                startActivity(intent);
            }
        });
        // 건배사공장으로 이동
        btnToast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GameToastActivity.class);
                startActivity(intent);
            }
        });


        return rootView;
    }
    private void addNetworkData1(){
        progressBar3.setVisibility(View.VISIBLE);

        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        RecipeApi api = retrofit.create(RecipeApi.class);

        Call<RecipeMasterList> call = api.getRecipeMaster(accessToken,offset1,limit1);
        call.enqueue(new Callback<RecipeMasterList>() {

            @Override
            public void onResponse(Call<RecipeMasterList> call, Response<RecipeMasterList> response) {
                progressBar3.setVisibility(View.GONE);

                if(response.isSuccessful()){
                    count1 = response.body().getCount();
                    recipeMasterList.addAll(response.body().getItems());

                    offset1 = offset1+count1;

                    adapter1.setOnItemClickListener(new RecipeMasterAdapter.onItemClickListener() {
                        @Override
                        public void onItemClick(int index) {
                            Intent intent = new Intent(getActivity(), RecipeInfoActivity.class);
                            intent.putExtra("recipeId", recipeMasterList.get(index).getId());
                            startActivity(intent);
                        }
                    });

                    adapter1.notifyDataSetChanged();

                    isLoading1 = false; // 데이터를 다불러왔다
                    // 데이터를 불러오는 동안에는 스크롤을 못하게 막는것이다.
                    // 자꾸 데이터를 두번씩 가져오므로...
                }
            }

            @Override
            public void onFailure(Call<RecipeMasterList> call, Throwable t) {
                progressBar3.setVisibility(View.GONE);
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }
    private void getNetworkData1(){
        progressBar3.setVisibility(View.VISIBLE);
        // 네트워크 통신을 위한 라이브러리인 Retrofit을 사용한다.
        // 1. 레트로핏 객체 생성
        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        RecipeApi api = retrofit.create(RecipeApi.class);

        SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, getActivity().MODE_PRIVATE);
        accessToken = "Bearer "  + sp.getString(Config.ACCESS_TOKEN, "");// 액세스 토큰이 없으면 "" 리턴

        // 오프셋 초기화는, 함수 호출하기 전에!!
        offset1 = 0;
        count1 = 0;

        Call<RecipeMasterList> call = api.getRecipeMaster(accessToken, offset1,limit1);

        call.enqueue(new Callback<RecipeMasterList>() {
            @Override
            public void onResponse(Call<RecipeMasterList> call, Response<RecipeMasterList> response) {
                progressBar3.setVisibility(View.GONE);
                recipeMasterList.clear(); // 초기화

                if(response.isSuccessful()){
                    count1 = response.body().getCount();
                    recipeMasterList.addAll(response.body().getItems());

                    offset1 = offset1+count1;
                    adapter1 = new RecipeMasterAdapter(getActivity(),recipeMasterList);
                    adapter1.setOnItemClickListener(new RecipeMasterAdapter.onItemClickListener() {
                        @Override
                        public void onItemClick(int index) {
                            Intent intent = new Intent(getActivity(), RecipeInfoActivity.class);
                            intent.putExtra("recipeId", recipeMasterList.get(index).getId());
                            startActivity(intent);
                        }
                    });

                    recyclerView1.setAdapter(adapter1);
                }
            }

            @Override
            public void onFailure(Call<RecipeMasterList> call, Throwable t) {
                progressBar3.setVisibility(View.GONE);
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void addNetworkData2(){
        progressBar3.setVisibility(View.VISIBLE);

        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        RecipeApi api = retrofit.create(RecipeApi.class);

        Call<RecipeHonorList> call = api.getRecipeHonor(accessToken,offset2,limit2);
        call.enqueue(new Callback<RecipeHonorList>() {

            @Override
            public void onResponse(Call<RecipeHonorList> call, Response<RecipeHonorList> response) {
                progressBar3.setVisibility(View.GONE);

                if(response.isSuccessful()){
                    count2 = response.body().getCount();
                    recipeHonorList.addAll(response.body().getItems());

                    offset2 = offset2+count2;

                    adapter2.setOnItemClickListener(new RecipeHonorAdapter.onItemClickListener() {
                        @Override
                        public void onItemClick(int index) {
                            Intent intent = new Intent(getActivity(), RecipeInfoActivity.class);
                            intent.putExtra("recipeId", recipeHonorList.get(index).getId());
                            startActivity(intent);
                        }
                    });

                    adapter2.notifyDataSetChanged();

                    isLoading2 = false; // 데이터를 다불러왔다
                    // 데이터를 불러오는 동안에는 스크롤을 못하게 막는것이다.
                    // 자꾸 데이터를 두번씩 가져오므로...
                }
            }

            @Override
            public void onFailure(Call<RecipeHonorList> call, Throwable t) {
                progressBar3.setVisibility(View.GONE);
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }
    private void getNetworkData2(){

        // 네트워크 통신을 위한 라이브러리인 Retrofit을 사용한다.
        // 1. 레트로핏 객체 생성
        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        RecipeApi api = retrofit.create(RecipeApi.class);

        // 오프셋 초기화는, 함수 호출하기 전에!!
        offset2 = 0;
        count2 = 0;

        Call<RecipeHonorList> call = api.getRecipeHonor(accessToken,offset2,limit2);

        call.enqueue(new Callback<RecipeHonorList>() {
            @Override
            public void onResponse(Call<RecipeHonorList> call, Response<RecipeHonorList> response) {

                recipeHonorList.clear(); // 초기화

                if(response.isSuccessful()){
                    count2 = response.body().getCount();
                    recipeHonorList.addAll(response.body().getItems());

                    offset2 = offset2+count2;
                    adapter2 = new RecipeHonorAdapter(getActivity(),recipeHonorList );
                    adapter2.setOnItemClickListener(new RecipeHonorAdapter.onItemClickListener() {
                        @Override
                        public void onItemClick(int index) {
                            Intent intent = new Intent(getActivity(), RecipeInfoActivity.class);
                            intent.putExtra("recipeId", recipeHonorList.get(index).getId());
                            startActivity(intent);
                        }
                    });

                    recyclerView2.setAdapter(adapter2);
                }
            }

            @Override
            public void onFailure(Call<RecipeHonorList> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void getCheersMentResNetworkData(){

        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        GameApi api = retrofit.create(GameApi.class);

        Ment ment = new Ment(); // 이 객체는 이 함수가 끝나면 사라진다.
        // 랜덤으로 창조말 가져오기
        ment.LandomMent();

        Call<CheersMentRes> call = api.getCheers(accessToken,2, ment ); // 2는 선창 후창임

        call.enqueue(new Callback<CheersMentRes>() {
            @Override
            public void onResponse(Call<CheersMentRes> call, Response<CheersMentRes> response) {

                if(response.isSuccessful()){
                    txtFirst.setText( response.body().getItem().getFirst() );
                    txtLast.setText( response.body().getItem().getLast() );


                }else{
                    // response가 에러를 가져왔다면 여기서 처리
                    txtFirst.setText("이미 집에 가기는");
                    txtLast.setText("너무 늦었어요");

                 }
            }

            @Override
            public void onFailure(Call<CheersMentRes> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());

            }
        });
    }
    // 액티비티에서 현재프래그먼트로 돌아왔을때
    @Override
    public void onResume() {
        super.onResume();

        // 액티비티에서 현재프래그먼트로 돌아왔을때
        // 상태변화를 스탯으로 감지해서 1이면 명예 레시피를 다시불러온다. (좋아요표시때문에)
        int s = state;
        Log.d("플래그스탯", String.valueOf(s));
        if (state == 1) {
            getNetworkData2();
            state = 0;
            Log.d("플래그스탯2", String.valueOf(state));
        }
    }


    public void likeProcess(int index){

        selectedPosting = postingList.get(index);

        // 2. 해당행의 좋아요가 이미 좋아요인지 아닌지 파악
        if (selectedPosting.getIsLike() == 0) {
            // 3. 해당 좋아요에 맞는 좋아요 API를 호출
            Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
            PostingApi api = retrofit.create(PostingApi.class);
            SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, Context.MODE_PRIVATE);
            accessToken ="Bearer " +  sp.getString(Config.ACCESS_TOKEN, "");
            Call<ResMessage> call = api.setLike(accessToken, selectedPosting.getPostingId());

            call.enqueue(new Callback<ResMessage>() {
                @Override
                public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                    if (response.isSuccessful()){
                        // 4. 화면에 결과를 표시
                        selectedPosting.setIsLike(1);
                        adapter.notifyDataSetChanged();

                    }else {
                        Toast.makeText(getActivity(), "좋아요 처리에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResMessage> call, Throwable t) {
                    Toast.makeText(getActivity(), "서버와 통신이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            });

        }else {
            // 3. 좋아요 해제 API를 호출
            // 3. 해당 좋아요에 맞는 좋아요 API를 호출
            Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
            PostingApi api = retrofit.create(PostingApi.class);
            SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, Context.MODE_PRIVATE);
            accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");
            Call<ResMessage> call = api.deleteLike(accessToken, selectedPosting.getPostingId());

            call.enqueue(new Callback<ResMessage>() {
                @Override
                public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                    if (response.isSuccessful()) {
                        // 4. 화면에 결과를 표시
                        selectedPosting.setIsLike(0);
                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getActivity(), "좋아요 해제에 실패했습니다.", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<ResMessage> call, Throwable t) {
                    Toast.makeText(getActivity(), "서버와 통신이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }

}