package com.leopard4.alcoholrecipe;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

/**
 * 간단한 {@link Fragment} 하위 클래스입니다.
 * Use the {@link ThirdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThirdFragment extends Fragment {

    // TODO: 매개변수 인수 이름 바꾸기, 일치하는 이름 선택
    // 프래그먼트 초기화 매개변수, 예: ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: 매개변수 이름 바꾸기 및 유형 변경
    private String mParam1;
    private String mParam2;

    Button btnMyToast, btnMyRecipe, btnLogout, btnLisence, btnSecession, btnEdit;
    EditText editName;

    public ThirdFragment() {
        // 필수 빈 공개 생성자
    }

    /**
     * 이 팩토리 메서드를 사용하여 새 인스턴스를 만듭니다.
     * 제공된 매개변수를 사용하여 이 단편.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return 프래그먼트 ThirdFragment의 새 인스턴스입니다.
     */
    // TODO: 이름 변경 및 유형 및 매개변수 수 변경
    public static ThirdFragment newInstance(String param1, String param2) {
        ThirdFragment fragment = new ThirdFragment();
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
        View view = inflater.inflate(R.layout.fragment_third, container, false);

        btnMyToast = view.findViewById(R.id.btnMyToast);
        btnMyRecipe = view.findViewById(R.id.btnMyRecipe);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnLisence = view.findViewById(R.id.btnLisence);
        btnSecession = view.findViewById(R.id.btnSecession);
        btnEdit = view.findViewById(R.id.btnEdit);
        editName = view.findViewById(R.id.editName);

        btnMyRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyRecipeActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}