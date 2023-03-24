package com.leopard4.alcoholrecipe;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.leopard4.alcoholrecipe.api.CreatingApi;
import com.leopard4.alcoholrecipe.api.NetworkClient;
import com.leopard4.alcoholrecipe.config.Config;

import com.leopard4.alcoholrecipe.model.CreateRecipeRes.RecipeRes;
import com.leopard4.alcoholrecipe.model.CreateRecipeRes.Recipe_id;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyRecipeWriteFirstActivity extends AppCompatActivity {

    // 레시피 작성페이지
    // 한글이름, 영어이름
    EditText editTitle, editEngTitle;

    // 라디오버튼 ( 약 / 중 / 강 / ? )
    RadioGroup radioGroup;

    // 설명란
    // 소개 / 레시피
    EditText editIntro, editContent;

    // 이미지 첨부하면 나타나는 부분
    ImageView imageView;

    // 이미지 첨부 버튼, 다음단계 버튼
    Button btnImg, btnNext;

    File photoFile;

    private ProgressDialog dialog;

    int percent = 0;
    ImageButton imgBack;
    TextView textView96; // 주인장의 연구실로 이동
    private String thisRecipeId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe_write_first);

        editTitle = findViewById(R.id.editTitle);
        editEngTitle = findViewById(R.id.editEngTitle);

        radioGroup = findViewById(R.id.radioGroup);


        editIntro = findViewById(R.id.editIntro);
        editContent = findViewById(R.id.editContent);

        imageView = findViewById(R.id.imageView);
        imgBack = findViewById(R.id.imgBack);
        btnImg = findViewById(R.id.btnImg);
        btnNext = findViewById(R.id.btnNext);
        // 이화면에서 수정을 재활용하고싶다면
        // thisRecipeId변수에 recipeId를 할당한다.
        // 그러면 next버튼이 수정하기로 바뀌고, 수정하기를 누르면
        // 수정하는 api가 호출된다.
        if (thisRecipeId != null){
            Log.i("이액티비티로 돌아왔을때 레시피id", thisRecipeId);
            btnNext.setText("수정하기");
        }

        // 레시피 버튼을 눌렀을때 주인장의 연구실로 이동
        textView96 = findViewById(R.id.textView96);
        textView96.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyRecipeWriteFirstActivity.this, RecipeActivity.class);
                startActivity(intent);
            }
        });

        // 뒤로가기
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 이미지 업로드
        btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 버튼을 누르면, 카메라에서 선택인지, 앨범에서 선택인지를
                // 고를 수 있또록 알러트 다이얼로그를 띄운다.
                showDialog();

            }
        });



        // <한글 이름, 도수, 레시피, 사진은 필수입니다.>
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (thisRecipeId == null) {
                    // 한글 이름
                    String title = editTitle.getText().toString().trim();
                    if (title.isEmpty()){
                        Toast.makeText(MyRecipeWriteFirstActivity.this, "한글 이름은 필수입니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 영문 이름
                    String engTitle = editEngTitle.getText().toString().trim();
                    if (engTitle.isEmpty()){
                        engTitle = "";
                    }

                    // 도수 선택
                    int radioBtnId = radioGroup.getCheckedRadioButtonId();

                    if (radioBtnId == R.id.radioOne){
                        percent = 1;
                    } else if (radioBtnId == R.id.radioTwo) {
                        percent = 2;
                    } else if (radioBtnId == R.id.radioThree) {
                        percent = 3;
                    } else if (radioBtnId == R.id.radioFour) {
                        percent = 4;
                    } else {
                        Toast.makeText(MyRecipeWriteFirstActivity.this, "도수를 선택하세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 소개
                    String intro = editIntro.getText().toString().trim();

                    if (intro.isEmpty()){
                        intro = "";
                    }

                    // 레시피
                    String content = editContent.getText().toString().trim();
                    if (content.isEmpty()){
                        Toast.makeText(MyRecipeWriteFirstActivity.this, "레시피 작성은 필수입니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 사진 첨부
                    // 사진이 첨부되었는지 - 안돼있으면 토스트메세지
                    if(photoFile == null) {
                        Toast.makeText(MyRecipeWriteFirstActivity.this, "사진은 필수입니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    showProgress("포스팅 업로드 중...");

                    Retrofit retrofit = NetworkClient.getRetrofitClient(MyRecipeWriteFirstActivity.this);
                    CreatingApi api = retrofit.create(CreatingApi.class);


                    // title, engTitle, intro, percent, content, img
                    // 멀티파트로 파일을 보내는 경우, 파일 파라미터를 만든다.
                    RequestBody fileBody = RequestBody.create(photoFile, MediaType.parse("image/png")); // 파일타입을 png로 지정
                    MultipartBody.Part img = MultipartBody.Part.createFormData("img", photoFile.getName(), fileBody);



                    // 멀티파트로 텍스트를 보내는 경우, 텍스트 파라미터를 만든다.
                    // title
                    RequestBody titleBody = RequestBody.create(title, MediaType.parse("text/plain"));
                    //engTitle
                    RequestBody engTitleBody = RequestBody.create(engTitle, MediaType.parse("text/plain"));
                    //intro
                    RequestBody introBody = RequestBody.create(intro, MediaType.parse("text/plain"));
                    // percent
                    RequestBody percentBody = RequestBody.create(percent + "", MediaType.parse("text/plain"));
                    // content
                    RequestBody contentBody = RequestBody.create(content, MediaType.parse("text/plain"));

                    SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
                    String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

                    // title, engTitle, intro, percent, content, img

                    Call<RecipeRes> call = api.addRecipe(accessToken,
                            titleBody,
                            engTitleBody,
                            introBody,
                            percentBody,
                            contentBody,
                            img);

                    call.enqueue(new Callback<RecipeRes>() {
                        @SuppressLint("LongLogTag") // 로그가 길어도 무시
                        @Override
                        public void onResponse(Call<RecipeRes> call, Response<RecipeRes> response) {
                            dismissProgress();
                            if (response.isSuccessful()){
                                // response의 상세한 내용을 확인하는 과정 (공부용)
                                RecipeRes res = response.body();
                                Recipe_id recipe_id = res.getRecipe_id()[0];
                                Recipe_id[] res2 = response.body().getRecipe_id();
                                Recipe_id recipeId2 = res2[0];
                                String recipeId = recipe_id.getId();
                                String res3 = response.body().getRecipe_id()[0].getId();

                                Log.i("리스폰 res = response.body()", res + "");
                                Log.i("리스폰 recipe_id = res.getRecipe_id()[0]", res.getResult() + "");
                                Log.i("리스폰 res2 = response.body().getRecipe_id();", res.getRecipe_id() + "");
                                Log.i("리스폰 res2 = response.body().getRecipe_id(); ", res2 + "");
                                Log.i("리스폰 recipe_id = res.getRecipe_id()[0];", recipe_id + "");
                                Log.i("리스폰 recipeId2 = res2[0];", recipeId2 + "");
                                Log.i("리스폰 recipeId = recipe_id.getId();", recipeId + "");
                                Log.i("리스폰 res3 = response.body().getRecipe_id()[0].getId();", res3 + "");

                                thisRecipeId = recipeId;
                                // 결론적으로 recipeId를 가져올 수 있었고 이를 다음 액티비티로 넘겨준다.
                                Intent intent = new Intent(MyRecipeWriteFirstActivity.this, MyRecipeWriteSecondActivity.class);
                                intent.putExtra("recipeId", thisRecipeId);
                                startActivity(intent);

                            } else {
                                try {
                                    String errorBody = response.errorBody().string(); // 에러바디를 String으로 변환
                                    JSONObject jsonObject = new JSONObject(errorBody); // String을 JSON으로 변환
                                    String errorMessage = jsonObject.getString("error"); // JSON에서 error 키의 값을 가져옴
                                    Log.i("중복에러", errorMessage);

                                    if(errorMessage.contains("Duplicate")){
                                        editTitle.setError("이미 존재하는 제목입니다.");
                                        Toast.makeText(MyRecipeWriteFirstActivity.this, "이미 존재하는 제목입니다.", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace(); // 에러바디를 String으로 변환하는 과정에서 에러가 발생하면

                                }

                            }

                        }

                        @Override
                        public void onFailure(Call<RecipeRes> call, Throwable t) {
                            dismissProgress();
                            Log.e("통신 실패", t.getMessage());


                        }
                    });
                    // 수정 하기
                }else if (thisRecipeId != null){
                    String title = editTitle.getText().toString().trim();
                    if (title.isEmpty()){
                        Toast.makeText(MyRecipeWriteFirstActivity.this, "한글 이름은 필수입니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 영문 이름
                    String engTitle = editEngTitle.getText().toString().trim();
                    if (engTitle.isEmpty()){
                        engTitle = "";
                    }

                    // 도수 선택
                    int radioBtnId = radioGroup.getCheckedRadioButtonId();

                    if (radioBtnId == R.id.radioOne){
                        percent = 1;
                    } else if (radioBtnId == R.id.radioTwo) {
                        percent = 2;
                    } else if (radioBtnId == R.id.radioThree) {
                        percent = 3;
                    } else if (radioBtnId == R.id.radioFour) {
                        percent = 4;
                    } else {
                        Toast.makeText(MyRecipeWriteFirstActivity.this, "도수를 선택하세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 소개
                    String intro = editIntro.getText().toString().trim();

                    if (intro.isEmpty()){
                        intro = "";
                    }

                    // 레시피
                    String content = editContent.getText().toString().trim();
                    if (content.isEmpty()){
                        Toast.makeText(MyRecipeWriteFirstActivity.this, "레시피 작성은 필수입니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 사진 첨부
                    // 사진이 첨부되었는지 - 안돼있으면 토스트메세지
                    if(photoFile == null) {
                        Toast.makeText(MyRecipeWriteFirstActivity.this, "사진은 필수입니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    showProgress("포스팅 업로드 중...");

                    Retrofit retrofit = NetworkClient.getRetrofitClient(MyRecipeWriteFirstActivity.this);
                    CreatingApi api = retrofit.create(CreatingApi.class);


                    // title, engTitle, intro, percent, content, img
                    // 멀티파트로 파일을 보내는 경우, 파일 파라미터를 만든다.
                    RequestBody fileBody = RequestBody.create(photoFile, MediaType.parse("image/png")); // 파일타입을 png로 지정
                    MultipartBody.Part img = MultipartBody.Part.createFormData("img", photoFile.getName(), fileBody);



                    // 멀티파트로 텍스트를 보내는 경우, 텍스트 파라미터를 만든다.
                    // title
                    RequestBody titleBody = RequestBody.create(title, MediaType.parse("text/plain"));
                    //engTitle
                    RequestBody engTitleBody = RequestBody.create(engTitle, MediaType.parse("text/plain"));
                    //intro
                    RequestBody introBody = RequestBody.create(intro, MediaType.parse("text/plain"));
                    // percent
                    RequestBody percentBody = RequestBody.create(percent + "", MediaType.parse("text/plain"));
                    // content
                    RequestBody contentBody = RequestBody.create(content, MediaType.parse("text/plain"));

                    SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
                    String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

                    // title, engTitle, intro, percent, content, img

                    Call<RecipeRes> call = api.editRecipe(accessToken,
                            titleBody,
                            engTitleBody,
                            introBody,
                            percentBody,
                            contentBody,
                            img,thisRecipeId);

                    call.enqueue(new Callback<RecipeRes>() {
                        @Override
                        public void onResponse(Call<RecipeRes> call, Response<RecipeRes> response) {
                            dismissProgress();
                            if (response.isSuccessful()){
                                RecipeRes res = response.body();
                                Log.i("리스폰2", res + "");

                                Intent intent = new Intent(MyRecipeWriteFirstActivity.this, MyRecipeWriteSecondActivity.class);
                                startActivity(intent);

                            } else {
                                try {
                                    String errorBody = response.errorBody().string(); // 에러바디를 String으로 변환
                                    JSONObject jsonObject = new JSONObject(errorBody); // String을 JSON으로 변환
                                    String errorMessage = jsonObject.getString("error"); // JSON에서 error 키의 값을 가져옴
                                    Log.i("중복에러", errorMessage);

                                    if(errorMessage.contains("Duplicate")){
                                        editTitle.setError("이미 존재하는 제목입니다.");
                                        Toast.makeText(MyRecipeWriteFirstActivity.this, "이미 존재하는 제목입니다.", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace(); // 에러바디를 String으로 변환하는 과정에서 에러가 발생하면

                                }

                            }

                        }

                        @Override
                        public void onFailure(Call<RecipeRes> call, Throwable t) {
                            dismissProgress();
                            Log.e("통신 실패", t.getMessage());


                        }
                    });
                }



            }
        });

    }

    // 카메라 관련 코드
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyRecipeWriteFirstActivity.this);
        builder.setTitle(R.string.alert_title);
        builder.setItems(R.array.alert_photo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0){
                    // 사진 찍는 코드를 실행
                    camera();

                } else if (i == 1) {
                    // 앨범에서 사진 가져오는 코드 실행
                    album();
                }
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void camera(){
        int permissionCheck = ContextCompat.checkSelfPermission(
                MyRecipeWriteFirstActivity.this, android.Manifest.permission.CAMERA);

        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MyRecipeWriteFirstActivity.this,
                    new String[]{Manifest.permission.CAMERA} ,
                    1000);
            Toast.makeText(MyRecipeWriteFirstActivity.this, "카메라 권한 필요합니다.",
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(i.resolveActivity(MyRecipeWriteFirstActivity.this.getPackageManager())  != null  ){

                // 사진의 파일명을 만들기
                String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                photoFile = getPhotoFile(fileName);

                Uri fileProvider = FileProvider.getUriForFile(MyRecipeWriteFirstActivity.this,
                        "com.leopard4.alcoholrecipe.fileprovider", photoFile);
                i.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
                startActivityForResult(i, 100);

            } else{
                Toast.makeText(MyRecipeWriteFirstActivity.this, "이폰에는 카메라 앱이 없습니다.",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void album(){
        if(checkPermission()){
            displayFileChoose();
        }else{
            requestPermission();
        }
    }

    private void displayFileChoose() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "SELECT IMAGE"), 300);
    }

    private File getPhotoFile(String fileName) {
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try{
            return File.createTempFile(fileName, ".jpg", storageDirectory);
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    private void requestPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(MyRecipeWriteFirstActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(MyRecipeWriteFirstActivity.this, "권한 수락이 필요합니다.",
                    Toast.LENGTH_SHORT).show();
        }else{
            ActivityCompat.requestPermissions(MyRecipeWriteFirstActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 500);
        }
    }
    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(MyRecipeWriteFirstActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE); // 외부저장소 쓰기 권한
        if(result == PackageManager.PERMISSION_DENIED){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MyRecipeWriteFirstActivity.this, "권한 허가 되었음",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyRecipeWriteFirstActivity.this, "아직 승인하지 않았음",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case 500: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MyRecipeWriteFirstActivity.this, "권한 허가 되었음",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyRecipeWriteFirstActivity.this, "아직 승인하지 않았음",
                            Toast.LENGTH_SHORT).show();
                }

            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 100 && resultCode == RESULT_OK){

            Bitmap photo = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

            ExifInterface exif = null;
            try {
                exif = new ExifInterface(photoFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            photo = rotateBitmap(photo, orientation);

            // 압축시킨다. 해상도 낮춰서
            OutputStream os;
            try {
                os = new FileOutputStream(photoFile);
                photo.compress(Bitmap.CompressFormat.JPEG, 50, os);
                os.flush();
                os.close();
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
            }

            photo = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

            imageView.setImageBitmap(photo);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            // 네트워크로 데이터 보낸다.



        }else if(requestCode == 300 && resultCode == RESULT_OK && data != null &&
                data.getData() != null){

            Uri albumUri = data.getData( );
            String fileName = getFileName( albumUri );
            try {

                ParcelFileDescriptor parcelFileDescriptor = getContentResolver( ).openFileDescriptor( albumUri, "r" );
                if ( parcelFileDescriptor == null ) return;
                FileInputStream inputStream = new FileInputStream( parcelFileDescriptor.getFileDescriptor( ) );
                photoFile = new File( this.getCacheDir( ), fileName );
                FileOutputStream outputStream = new FileOutputStream( photoFile );
                IOUtils.copy( inputStream, outputStream );

//                //임시파일 생성
//                File file = createImgCacheFile( );
//                String cacheFilePath = file.getAbsolutePath( );


                // 압축시킨다. 해상도 낮춰서
                Bitmap photo = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                OutputStream os;
                try {
                    os = new FileOutputStream(photoFile);
                    photo.compress(Bitmap.CompressFormat.JPEG, 60, os);
                    os.flush();
                    os.close();
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
                }

                imageView.setImageBitmap(photo);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

//                imageView.setImageBitmap( getBitmapAlbum( imageView, albumUri ) );

            } catch ( Exception e ) {
                e.printStackTrace( );
            }

            // 네트워크로 보낸다.
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    //앨범에서 선택한 사진이름 가져오기
    public String getFileName( Uri uri ) {
        Cursor cursor = getContentResolver( ).query( uri, null, null, null, null );
        try {
            if ( cursor == null ) return null;
            cursor.moveToFirst( );
            @SuppressLint("Range") String fileName = cursor.getString( cursor.getColumnIndex( OpenableColumns.DISPLAY_NAME ) );
            cursor.close( );
            return fileName;

        } catch ( Exception e ) {
            e.printStackTrace( );
            cursor.close( );
            return null;
        }
    }

    // 네트워크 로직 처리시에 화면에 보여주는 함수
    void showProgress(String message){
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(message);
        dialog.show();
    }

    // 로직처리가 끝나면 화면에서 사라지는 함수
    void dismissProgress(){
        dialog.dismiss();
    }
    // onResume
    @Override
    protected void onResume() {
        super.onResume();
        // 이화면에서 수정을 재활용하고싶다면
        // thisRecipeId변수에 recipeId를 할당한다.
        // 그러면 수정하는 화면이 된다.
        if (thisRecipeId != null){
            Log.i("이액티비티로 돌아왔을때 레시피id", thisRecipeId);
            btnNext.setText("수정하기");
        }


    }



}