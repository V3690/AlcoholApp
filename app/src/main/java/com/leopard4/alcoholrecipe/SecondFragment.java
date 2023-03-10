package com.leopard4.alcoholrecipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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

    private Spinner spinner;
    private Spinner spinner3;
    private Spinner spinner4;
    private Spinner spinner5;

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
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        spinner = (Spinner) view.findViewById(R.id.spinner);
        spinner3 = (Spinner) view.findViewById(R.id.spinner3);
        spinner4 = (Spinner) view.findViewById(R.id.spinner4);
        spinner5 = (Spinner) view.findViewById(R.id.spinner5);

        //문자열 배열과 기본 스피너 레이아웃을 사용하여 ArrayAdapter 만들기
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.spinner_item, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_item3, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_item4, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(getContext(), R.array.spinner_item5, android.R.layout.simple_spinner_item);
        //선택 목록이 나타날 때 사용할 레이아웃을 지정
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //스피너에 어댑터 적용
        spinner.setAdapter(adapter);
        spinner3.setAdapter(adapter3);
        spinner4.setAdapter(adapter4);
        spinner5.setAdapter(adapter5);
        // 스피너(콤보박스)에 특정 값으로 초기값으로 고정하고 싶다면 setSelection메소드를 사용하면 된다.
//        spinner.setSelection(0); //position 값을 지정한다.
//        spinner3.setSelection(0);
//        spinner4.setSelection(0);
//        spinner5.setSelection(0);
        // 스피너 선택값 가져오는 방법
        // spinner.getSelectedItem().toString();
        // 스피너 선택값 가져오는 방법
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "선택된 아이템 : " + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(), "선택된 아이템이 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "선택된 아이템 : " + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(), "선택된 아이템이 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "선택된 아이템 : " + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(), "선택된 아이템이 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "선택된 아이템 : " + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(), "선택된 아이템이 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });




        return view;
    }
}