package com.leopard4.alcoholrecipe;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.leopard4.alcoholrecipe.api.NetworkClient;
import com.leopard4.alcoholrecipe.api.UserApi;
import com.leopard4.alcoholrecipe.config.Config;
import com.leopard4.alcoholrecipe.model.Res;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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

    Button btnPassEdit, btnMyRecipe, btnLisence, btnSecession, btnEdit , btnMyDogam;
    ImageView btnBack, btnLogout;
    TextView txtAppVersion;
    EditText editName;
    private String accessToken;

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

        btnPassEdit = view.findViewById(R.id.btnPassEdit);
        btnMyRecipe = view.findViewById(R.id.btnMyRecipe);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnLisence = view.findViewById(R.id.btnLicense);
        btnSecession = view.findViewById(R.id.btnSecession);
        btnEdit = view.findViewById(R.id.btnEdit);
        editName = view.findViewById(R.id.editName);
        btnMyDogam=view.findViewById(R.id.btnMyDogam);

        // 화면이 보여질때 닉네임을 세팅
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, getActivity().MODE_PRIVATE); // mode_private : 해당 앱에서만 사용
        String nickname = sharedPreferences.getString(Config.NICKNAME, ""); // 저장된 닉네임을 가져옴 (없으면 ""을 가져옴)
        editName.setText(nickname);

        btnMyRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyRecipeActivity.class);
                startActivity(intent);
            }
        });

        btnMyDogam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyDogamActivity.class);
                startActivity(intent);
            }
        });

        // 백버튼을 눌르면 firstfragment로 이동
        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MainActivity)getActivity()).loadFragment(new FirstFragment());

               }
        });
        // 로그아웃 버튼을 누르면 "정말 로그아웃 하시겠습니까?" 알러트 다이얼로그를 띄움
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("로그아웃");
                builder.setMessage("정말 로그아웃 하시겠습니까?");
                // 예 버튼을 누르면 로그인 화면으로 이동
                builder.setPositiveButton("예", (dialogInterface, i) -> { // 람다식
                    // 서버에 로그아웃 요청
                    getNetworkData();

                });
                // 아니오 버튼을 누르면 아무일도 일어나지 않음
                builder.setNegativeButton("아니오", null);
                builder.show();

            }
        });
        // 비밀번호 변경을 누르면
        btnPassEdit.setOnClickListener(new View.OnClickListener() {
            String password;
            @Override
            public void onClick(View view) {
                // 비밀번호 입력 알림창을 띄운다
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("비밀번호 변경");
                builder.setMessage("새로운 비밀번호를 입력해주세요.");
                // 비밀번호 입력창
                EditText editPassword = new EditText(getActivity());
                editPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD); // TYPE_TEXT_VARIATION_PASSWORD : 비밀번호 입력시 *로 표시
                builder.setView(editPassword); // 알림창에 비밀번호 입력창을 추가

                builder.setPositiveButton("확인", (dialogInterface, i) -> {
                    // 비밀번호를 가져온다
                    password = editPassword.getText().toString().trim();
                    // 비밀번호 유효성 체크
                    if(password.isEmpty()){
                        Toast.makeText(getActivity(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // 비밀번호 길이는 8~16자리로 제한
                    if(password.length() < 8 || password.length() > 16){
                        Toast.makeText(getActivity(), "비밀번호는 8~16자리로 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // 비밀번호에는 "';@#$%^&*()_+|~=`{}[]:;<>?,./"를 사용할수없다
                    if(password.contains("'") || password.contains(";") || password.contains("@") || password.contains("#") || password.contains("$") || password.contains("%") || password.contains("^") || password.contains("&") || password.contains("*") || password.contains("(") || password.contains(")") || password.contains("_") || password.contains("+") || password.contains("|") || password.contains("~") || password.contains("=") || password.contains("`") || password.contains("{") || password.contains("}") || password.contains("[") || password.contains("]") || password.contains(":") || password.contains(";") || password.contains("<") || password.contains(">") || password.contains("?") || password.contains(",") || password.contains(".") || password.contains("/")){
                        Toast.makeText(getActivity(), "비밀번호에는 ' ; @ # $ % ^ & * ( ) _ + | ~ = ` { } [ ] : ; < > ? , . / 를 사용할수없습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

//                    // 비밀번호 유효성 체크
//                    if(!password.matches("^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,16}$")){ // 정규식
//                        Toast.makeText(getActivity(), "비밀번호는 영문, 숫자, 특수문자를 포함한 8~16자리로 입력해주세요.", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
                    // 비밀번호 확인창
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                    builder2.setTitle("비밀번호 변경확인");
                    builder2.setMessage("비밀번호를 한번 더 입력해주세요.");
                    EditText editPasswordCheck = new EditText(getActivity());
                    editPasswordCheck.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    builder2.setView(editPasswordCheck);
                    // 확인 버튼을 누르면
                    builder2.setPositiveButton("확인", (dialogInterface2, i2) -> {
                        // 비밀번호를 가져온다
                        String passwordCheck = editPasswordCheck.getText().toString().trim();
                        // 비밀번호가 일치하지 않으면
                        if(!password.equals(passwordCheck)){
                            Toast.makeText(getActivity(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // 서버에 비밀번호 변경 요청
                        getNetworkPassword(password);

                    });
                    // 취소 버튼을 누르면 아무일도 일어나지 않음
                    builder2.setNegativeButton("취소", null);
                    builder2.show();

                });

                // 취소 버튼을 누르면 아무일도 일어나지 않음
                builder.setNegativeButton("취소", null);
                builder.show();

            }
        });
        // 닉네임 수정을 누르면
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 입력한 닉네임을 가져온다
                String nickname = editName.getText().toString().trim();
                // 닉네임 유효성 체크
                if(nickname.isEmpty()){
                    Toast.makeText(getActivity(), "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 닉네임 길이는 2~10자리로 제한
                if(nickname.length() < 2 || nickname.length() > 10){
                    Toast.makeText(getActivity(), "닉네임은 2~10자리로 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 닉네임에는 "';@#$%^&*()_+|~=`{}[]:;<>?,./"를 사용할수없다
                if(nickname.contains("'") || nickname.contains(";") || nickname.contains("@") || nickname.contains("#") || nickname.contains("$") || nickname.contains("%") || nickname.contains("^") || nickname.contains("&") || nickname.contains("*") || nickname.contains("(") || nickname.contains(")") || nickname.contains("_") || nickname.contains("+") || nickname.contains("|") || nickname.contains("~") || nickname.contains("=") || nickname.contains("`") || nickname.contains("{") || nickname.contains("}") || nickname.contains("[") || nickname.contains("]") || nickname.contains(":") || nickname.contains(";") || nickname.contains("<") || nickname.contains(">") || nickname.contains("?") || nickname.contains(",") || nickname.contains(".") || nickname.contains("/")){
                    Toast.makeText(getActivity(), "닉네임에는 ' ; @ # $ % ^ & * ( ) _ + | ~ = ` { } [ ] : ; < > ? , . / 를 사용할수없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 서버에 닉네임 변경 요청
                getNetworkNickname(nickname);
            }
        });
        // 회원탈퇴를 누르면
        btnSecession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원탈퇴 확인창
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("회원탈퇴");
                builder.setMessage("정말로 회원탈퇴를 하시겠습니까?");
                // 확인 버튼을 누르면
                builder.setPositiveButton("확인", (dialogInterface, i) -> {
                    // 새로운 쓰레드로 서버에 회원탈퇴 요청
                    // 이것을 쓰는 이유는 그냥 쓰레드 테스트용이다.
                    // 쓰레드를 사용하는 이유는 동기방식 네트워크 작업은 메인쓰레드에서 하면 안되기 때문이다 튕김
                    new Thread(new Runnable() { // 새로운 쓰레드를 생성 // 이쓰레드는 메인쓰레드와는 별개로 동작한다 // 메인쓰레드는 UI를 담당하고 새로운 쓰레드는 네트워크 작업을 담당한다 // 이렇게 하면 메인쓰레드가 튕기지 않는다 // 이쓰레드의 작업이 끝나면 메인쓰레드에게 알려줘야한다 // 이를 위해 Handler를 사용한다 // 이 코드에서 Handler를 사용한 부분은 // getNetworkSecession()함수에서 // getActivity().runOnUiThread()를 사용한 부분이다
                        @Override
                        public void run() {
                            try {
                                getNetworkSecession();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            }
                    }).start();

                });
                // 취소 버튼을 누르면 아무일도 일어나지 않음
                builder.setNegativeButton("취소", null);
                builder.show();
            }
        });
        btnLisence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 이용약관 함수를 호출
                showTermsAndConditions();
            }
        });

        return view;
    }
    // todo : 이용약관을 보여주는 함수 // 실제로 사용할때는 check box를 사용해서 동의를 받아야한다
    private void showTermsAndConditions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("이용 약관");
        builder.setMessage(getString(R.string.txt_conditions));
        builder.setPositiveButton("동의", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle user agreement
            }
        });
        builder.setNegativeButton("거절", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    // 회원탈퇴함수
    private void getNetworkSecession() throws IOException { // IOException은 execute()에서 발생할 수 있는 예외 // 이것을 작성함으로써 try-catch문을 사용하지 않아도 된다
        // 동기식으로 restrofit을 사용하기 위한 retrofit 객체 생성
        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        UserApi api = retrofit.create(UserApi.class);

        SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, getActivity().MODE_PRIVATE);
        accessToken = "Bearer "  + sp.getString(Config.ACCESS_TOKEN, "");// 액세스 토큰이 없으면 "" 리턴

        Call<Void> call = api.delete(accessToken);
        // 비동기방식이 아닌 동기방식으로 요청을 보낸다 이유는 그냥 테스트용
        // 동기 방식은 요청을 보낸 후 응답이 올 때까지 다른 작업을 할 수 없다
        Response<Void> response = call.execute();
        // 응답이 성공적이면
        if (response.isSuccessful())
        {
            // 로그아웃
            SharedPreferences.Editor editor = sp.edit();
            editor.remove(Config.ACCESS_TOKEN);
            editor.remove(Config.NICKNAME);
            editor.apply();
            // 로그인 화면으로 이동
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
            // 메인 쓰레드에게 알려준다
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "회원탈퇴에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            // 응답이 실패하면
            Toast.makeText(getActivity(), "회원탈퇴에 실패하였습니다.", Toast.LENGTH_SHORT).show();
        }

    }

    private void getNetworkNickname(String nickname) {
        Map<String, String> nicknameMap = new HashMap<>(); // 닉네임을 담을 맵
        nicknameMap.put("nickname", nickname);

        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        UserApi api = retrofit.create(UserApi.class);

        SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, getActivity().MODE_PRIVATE);
        accessToken = "Bearer "  + sp.getString(Config.ACCESS_TOKEN, "");// 액세스 토큰이 없으면 "" 리턴


        Call<Res> call = api.editNickname(accessToken, nicknameMap);

        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                if(response.isSuccessful()){

                    // 변경된 닉네임을 SharedPreference에서도 변경
                    SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, getActivity().MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(Config.NICKNAME, nickname);
                    editor.apply();

                    Toast.makeText(getActivity(), "닉네임이 변경되었습니다.", Toast.LENGTH_SHORT).show();
                }else{ // 에러가 깨진 형태로 직렬화해서 오는듯
                       // 예를들면 "error": "\ubcc0\ud654\uc640 \ud601\uc2e0\uc744" 이런식으로
                       // 때문에 아래와 같은 코드를 작성함.
                    try {
                        String errorBody = response.errorBody().string(); // 에러바디를 String으로 변환
                        JSONObject jsonObject = new JSONObject(errorBody); // String을 JSON으로 변환
                        String errorMessage = jsonObject.getString("error"); // JSON에서 error 키의 값을 가져옴
                        Log.i("REGISTER_ACTIVITY1", errorMessage);
                        // else if errorMessage == 이미 존재하는 이메일입니다. 이런식으로
                        if(errorMessage.equals("이미 존재하는 닉네임입니다.")){
                            Toast.makeText(getActivity(), "이미 존재하는 닉네임입니다.", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace(); // 에러바디를 String으로 변환하는 과정에서 에러가 발생하면

                    }
            }
            }
            @Override
            public void onFailure(Call<Res> call, Throwable t) {
                Toast.makeText(getActivity(), "실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getNetworkPassword(String password) {
        Map<String, String> passwordMap = new HashMap<>(); // 비밀번호를 담을 맵
        passwordMap.put("password", password);

        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        UserApi api = retrofit.create(UserApi.class);

        SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, getActivity().MODE_PRIVATE);
        accessToken = "Bearer "  + sp.getString(Config.ACCESS_TOKEN, "");// 액세스 토큰이 없으면 "" 리턴

        Call<Res> call = api.editPassword(accessToken, passwordMap);

        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getActivity(), "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Res> call, Throwable t) {
                Toast.makeText(getActivity(), "비밀번호 변경에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getNetworkData(){

        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        UserApi api = retrofit.create(UserApi.class);

        SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, getActivity().MODE_PRIVATE);
        accessToken = "Bearer "  + sp.getString(Config.ACCESS_TOKEN, "");// 액세스 토큰이 없으면 "" 리턴

        Call<Res> call = api.logout(accessToken);

        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {

                if(response.isSuccessful()){
                    // 로그아웃을 하면 토큰과 닉네임을 삭제
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, getActivity().MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear(); // 모든 데이터 삭제
                    editor.apply(); // 저장

                    // 로그인 화면으로 이동
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish(); // 현재 액티비티 종료

                    Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }
}