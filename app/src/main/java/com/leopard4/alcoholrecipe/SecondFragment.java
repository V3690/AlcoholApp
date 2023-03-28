package com.leopard4.alcoholrecipe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leopard4.alcoholrecipe.adapter.RecipeFavoriteAdapter;
import com.leopard4.alcoholrecipe.api.NetworkClient;
import com.leopard4.alcoholrecipe.api.RecipeFavoriteApi;
import com.leopard4.alcoholrecipe.config.Config;
import com.leopard4.alcoholrecipe.model.recipe.RecipeFavorite;
import com.leopard4.alcoholrecipe.model.recipe.RecipeFavoriteList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecondFragment extends Fragment {

    // TODO: 매개변수 인수 이름 바꾸기, 일치하는 이름 선택
    // 프래그먼트 초기화 매개변수, 예: ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: 매개변수 이름 바꾸기 및 유형 변경
    private String mParam1;
    private String mParam2;

    private Spinner spinner1;
    private Spinner spinner2;
    private Spinner spinner3;

    String accessToken;
    ProgressBar progressBar2;

    RecyclerView recyclerView;
    RecipeFavoriteAdapter adapter;
    ArrayList<RecipeFavorite> recipeFavoriteList = new ArrayList<>();

    // 쿼리 스트링
    int order  = 1;
    int type = 1;
    int strength = 0;
    int count = 0;
    int offset = 0;
    int limit = 14;

    // 첫화면시 스피너의 함수가 실행되서 문제를 일으키므로
    // 첫화면시 스피너의 함수가 실행되지 않게 하기 위한 변수
    int first1 = 0;
    int first2 = 0;
    int first3 = 0;
    // 스크롤이 자꾸 데이터를 두번씩 가져오므로 중복을 막기 위한 변수
    private boolean isLoading = false;


    public SecondFragment() {
        // 필수 빈 공개 생성자
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SecondFragment.
     */
    // TODO: 이름 변경 및 유형 및 매개변수 수 변경
    public static SecondFragment newInstance(String param1, String param2) {
        SecondFragment fragment = new SecondFragment();
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
        // 이 조각의 레이아웃 확장
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_second, container, false); // attachToRoot = false 란 뷰를 붙이지 않는다는 의미 // 뷰를 붙인다는것은 // 뷰를 레이아웃에 붙이는것이다.

        spinner1 = (Spinner) rootView.findViewById(R.id.spinner1);
        spinner2 = (Spinner) rootView.findViewById(R.id.spinner2);
        spinner3 = (Spinner) rootView.findViewById(R.id.spinner3);

        String accessToken;
        progressBar2 = rootView.findViewById(R.id.progressBar2);

        // 리사이클러뷰 연결
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL, false)); // 가로로 스크롤 // reverseLayout : 역순으로 정렬

        // 이 플래그먼트를 띄웠을때 데이터를 불러온다.
        getNetworkData();

        // 스크롤 리스너를 설정한다.
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) { // 스크롤이 멈추면
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) { // 스크롤이 되면
                super.onScrolled(recyclerView, dx, dy);

                int lastPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition(); // 마지막으로 보여지는 아이템의 위치
                int totalCount = recyclerView.getAdapter().getItemCount(); // 아이템의 총 개수
                if(lastPosition+1 == totalCount && !isLoading){ // 마지막 아이템이 보여지고 && 데이터를 불러오고 있지 않다면

                    // 네트워크 통해서 데이터를 더 불러온다.
                    if(count == limit ){
                         // 데이터를 불러오고 있다.
                        isLoading = true;
                        addNetworkData();

                    }

                }

            }
        });

        //문자열 배열과 기본 스피너 레이아웃을 사용하여 ArrayAdapter 만들기
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_item1, R.layout.layout_spinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_item2, R.layout.layout_spinner);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_item3, R.layout.layout_spinner);
        //선택 목록이 나타날 때 사용할 레이아웃을 지정
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //스피너에 어댑터 적용
        spinner1.setAdapter(adapter1);
        spinner2.setAdapter(adapter2);
        spinner3.setAdapter(adapter3);
        // 스피너(콤보박스)에 특정 값으로 초기값으로 고정하고 싶다면 setSelection메소드를 사용하면 된다.
//        spinner.setSelection(0); //position 값을 지정한다.
//        spinner3.setSelection(0);
//        spinner4.setSelection(0);
//        spinner5.setSelection(0);
        // 스피너 선택값 가져오는 방법
        // spinner.getSelectedItem().toString();
        // 스피너 선택값 가져오는 방법
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 처음에는 실행하지 않고 두번째부터
                // 스피너의 선택값의 인덱스를 가져와서 type에 저장
                if (first1 == 0) {
                    first1++;
                } else {
                    type = position+1;
                    getNetworkData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { // 아무것도 선택하지 않았을때
                Toast.makeText(getContext(), "선택된 아이템이 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (first2 == 0) {
                    first2++;
                } else {
                    strength = position;
                    getNetworkData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(), "선택된 아이템이 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (first3 == 0) {
                    first3++;
                } else {
                    order = position+1;
                    getNetworkData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(), "선택된 아이템이 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
    private void addNetworkData(){
        progressBar2.setVisibility(View.VISIBLE);

        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        RecipeFavoriteApi api = retrofit.create(RecipeFavoriteApi.class);

        Call<RecipeFavoriteList> call = api.getRecipeFavorite(accessToken, order,type,strength,offset,limit);
        call.enqueue(new Callback<RecipeFavoriteList>() {

            @Override
            public void onResponse(Call<RecipeFavoriteList> call, Response<RecipeFavoriteList> response) {
                progressBar2.setVisibility(View.GONE);

                if(response.isSuccessful()){
                    count = response.body().getCount();
                    recipeFavoriteList.addAll(response.body().getResult());

                    offset = offset+count;

                    adapter.setOnItemClickListener(new RecipeFavoriteAdapter.onItemClickListener() {
                        @Override
                        public void onItemClick(int index) {
                            Intent intent = new Intent(getActivity(), RecipeInfoActivity.class);
                            intent.putExtra("recipeId", recipeFavoriteList.get(index).getId());
                            startActivity(intent);
                        }
                    });

                    adapter.notifyDataSetChanged();

                    isLoading = false; // 데이터를 다불러왔다
                    // 데이터를 불러오는 동안에는 스크롤을 못하게 막는것이다.
                    // 자꾸 데이터를 두번씩 가져오므로...
                }
            }

            @Override
            public void onFailure(Call<RecipeFavoriteList> call, Throwable t) {
                progressBar2.setVisibility(View.GONE);
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }
    private void getNetworkData(){
        progressBar2.setVisibility(View.VISIBLE);
        // 네트워크 통신을 위한 라이브러리인 Retrofit을 사용한다.
        // 1. 레트로핏 객체 생성
        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        RecipeFavoriteApi api = retrofit.create(RecipeFavoriteApi.class);

        SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, getActivity().MODE_PRIVATE);
        accessToken = "Bearer "  + sp.getString(Config.ACCESS_TOKEN, "");// 액세스 토큰이 없으면 "" 리턴

        // 오프셋 초기화는, 함수 호출하기 전에!!
        offset = 0;
        count = 0;

        Call<RecipeFavoriteList> call = api.getRecipeFavorite(accessToken, order,type,strength,offset,limit);

        call.enqueue(new Callback<RecipeFavoriteList>() {
            @Override
            public void onResponse(Call<RecipeFavoriteList> call, Response<RecipeFavoriteList> response) {
                progressBar2.setVisibility(View.GONE);
                recipeFavoriteList.clear(); // 초기화

                if(response.isSuccessful()){
                    count = response.body().getCount();
                    recipeFavoriteList.addAll(response.body().getResult());

                    offset = offset+count;
                    adapter = new RecipeFavoriteAdapter(getActivity(),recipeFavoriteList );
                    adapter.setOnItemClickListener(new RecipeFavoriteAdapter.onItemClickListener() {
                        @Override
                        public void onItemClick(int index) {
                            Intent intent = new Intent(getActivity(), RecipeInfoActivity.class);
                            intent.putExtra("recipeId", recipeFavoriteList.get(index).getId());
                            startActivity(intent);
                        }
                    });

                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<RecipeFavoriteList> call, Throwable t) {
                progressBar2.setVisibility(View.GONE);
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }
}