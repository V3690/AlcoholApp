package com.leopard4.alcoholrecipe;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.leopard4.alcoholrecipe.api.GameApi;
import com.leopard4.alcoholrecipe.api.NetworkClient;
import com.leopard4.alcoholrecipe.config.Config;
import com.leopard4.alcoholrecipe.model.EmotionRes;

import org.apache.commons.io.IOUtils;

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

public class GameFaceActivity extends AppCompatActivity {

    Button btnResult;
    Button btnFace, btnShare;
    ImageView imgFace ,imgAlchol , imgBack;
    File photoFile;
    TextView txtResult , txtAlchol;


    private long mLastClickTime =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_face);


        btnFace = findViewById(R.id.btnFace);
        btnResult = findViewById(R.id.btnResult);
        imgFace = findViewById(R.id.imgFace);
        txtResult = findViewById(R.id.txtResult);
        txtAlchol = findViewById(R.id.txtAlchol);
        imgAlchol = findViewById(R.id.imgAlchol);
        imgBack = findViewById(R.id.imgBack);
        btnShare = findViewById(R.id.btnShare);


        btnFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 버튼을 누르면, 카메라에서 선택인지, 앨범에서 선택인지를
                // 고를 수 있또록 알러트 다이얼로그를 띄운다.
                showDialog();

            }
        });

        btnResult.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //중복클릭 방지
                if(SystemClock.elapsedRealtime() - mLastClickTime < 100000 ){

                            return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();


                if(photoFile == null) {
                    Toast.makeText(GameFaceActivity.this, "사진은 필수입니다.", Toast.LENGTH_SHORT).show();
                    return;}


                RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), photoFile);
                MultipartBody.Part photo = MultipartBody.Part.createFormData("photo", photoFile.getName(), requestBody);

                Retrofit retrofit = NetworkClient.getRetrofitClient(GameFaceActivity.this);
                GameApi api = retrofit.create(GameApi.class);
                SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
                String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

                Call<EmotionRes> call = api.faceRes(accessToken,photo);
                call.enqueue(new Callback<EmotionRes>() {
                    @Override
                    public void onResponse(Call<EmotionRes> call, Response<EmotionRes> response) {


                        if(response.isSuccessful()){
                            txtResult.setText(response.body().getResult().getContent());
                            txtAlchol.setText(response.body().getResult().getName());
                            String imgurl = response.body().getResult().getImgUrl();
                            Log.i(TAG,"이미지url은"+imgurl);

                            Glide.with(GameFaceActivity.this).load(imgurl.replace("http://", "https://")).override(500,300).into(imgAlchol);

                            btnShare.setVisibility(View.VISIBLE);
                            btnShare.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    View layoutView;
                                    layoutView = findViewById(R.id.layoutView);
                                    // 특정 뷰 캡쳐
                                    layoutView.buildDrawingCache();
                                    Bitmap captureView = layoutView.getDrawingCache();
                                    String path = MediaStore.Images.Media.insertImage(getContentResolver(), captureView, "title", null);
                                    Uri uri = Uri.parse(path);
                                    Intent intent = new Intent(Intent.ACTION_SEND); // 공유하기 위한 인텐트 생성
                                    intent.setType("image/*"); // 이미지를 공유하기 위해 타입을 이미지로 설정 // 이미지를 공유하기 위해 타입을 이미지로 설정
                                    intent.putExtra(Intent.EXTRA_STREAM, uri); // 공유할 이미지를 추가
                                    startActivity(Intent.createChooser(intent, "공유하기")); // 공유하기 창 띄우기


//                                    // 현재화면을 캡쳐해서 공유하기
//
//                                    View view1 = getWindow().getDecorView(); // 현재 화면을 가져옴 // 현재 화면을 가져오는 방법은 여러가지가 있지만, 여기서는 가장 간단한 방법을 사용
//                                    view1.setDrawingCacheEnabled(true); // 캐시를 사용하도록 설정 // 캐시를 사용하면, 화면이 바뀌어도 캐시를 사용하기 때문에 화면이 바뀌지 않는다.
//                                    Bitmap captureView = view1.getDrawingCache(); // 캐시를 비트맵으로 변환 // 비트맵이란 픽셀로 이루어진 이미지를 의미
//                                    String path = MediaStore.Images.Media.insertImage(getContentResolver(), captureView, "title", null); // 갤러리에 저장 // 갤러리에 저장하기 위해서는 Uri가 필요하다.
//                                    Uri uri = Uri.parse(path); // 저장한 경로를 Uri로 변환 // Uri는 경로를 의미
//                                    Intent intent = new Intent(Intent.ACTION_SEND); // 공유하기 위한 인텐트 생성
//                                    intent.setType("image/*"); // 이미지를 공유하기 위해 타입을 이미지로 설정 // 이미지를 공유하기 위해 타입을 이미지로 설정
//                                    intent.putExtra(Intent.EXTRA_STREAM, uri); // 공유할 이미지를 추가
//                                    startActivity(Intent.createChooser(intent, "공유하기")); // 공유하기 창 띄우기
//                텍스트 보내기
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("text/plain");
//                intent.putExtra(Intent.EXTRA_SUBJECT, "알코올 레시피");
//                intent.putExtra(Intent.EXTRA_TEXT, "알코올 레시피 앱에서 "+txtAlchol.getText().toString()+"를 추천합니다.");
//                startActivity(Intent.createChooser(intent, "알코올 레시피"));

                                }
                            });


                        }else{
                            txtResult.setText("실패)");
                        }


                    }

                    @Override
                    public void onFailure(Call<EmotionRes> call, Throwable t) {
                        Log.d("TAG", "onFailure: " + t.getMessage());
                    }
                });



            }
        });


        imgBack.setOnClickListener(v -> {
            finish();
        });


    }





    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GameFaceActivity.this);
        builder.setTitle(R.string.alert_title);
        builder.setItems(R.array.alert_photo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    // todo : 사진찍는 코드 실행
                    camera();

                } else if (i == 1) {
                    // todo : 앨범에서 사진 가져오는 코드 실행
                    album();
                }
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
}

    private void album() {
        if(checkPermission()){
            displayFileChoose();
        }else{
            requestPermission();
        }

    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(GameFaceActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(result == PackageManager.PERMISSION_DENIED){
            return false;
        }else{
            return true;
        }
    }

    private void requestPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(GameFaceActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(GameFaceActivity.this, "권한 수락이 필요합니다.",
                    Toast.LENGTH_SHORT).show();
        }else{
            ActivityCompat.requestPermissions(GameFaceActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 500);
        }
    }

    private void displayFileChoose() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "SELECT IMAGE"), 300);
    }

    private void camera() {

        int permissionCheck = ContextCompat.checkSelfPermission(
                GameFaceActivity.this, android.Manifest.permission.CAMERA);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(GameFaceActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    1000);
            Toast.makeText(GameFaceActivity.this, "카메라 권한이 필요합니다.",
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (i.resolveActivity(GameFaceActivity.this.getPackageManager()) != null) {

                // 사진의 파일명을 만들기
                String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                photoFile = getPhotoFile(fileName);

                Uri fileProvider = FileProvider.getUriForFile(GameFaceActivity.this,
                        "com.leopard4.alcoholrecipe.fileprovider", photoFile);
                i.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
                startActivityForResult(i, 100);

            } else {
                Toast.makeText(GameFaceActivity.this, "카메라 앱이 필요합니다.",
                        Toast.LENGTH_SHORT).show();
            }
        }

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(GameFaceActivity.this, "권한 허가 되었음",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GameFaceActivity.this, "아직 승인하지 않았음",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case 500: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(GameFaceActivity.this, "권한 허가 되었음",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GameFaceActivity.this, "아직 승인하지 않았음",
                            Toast.LENGTH_SHORT).show();
                }

            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
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

            imgFace.setImageBitmap(photo);

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

                imgFace.setImageBitmap(photo);

//                imageView.setImageBitmap( getBitmapAlbum( imageView, albumUri ) );

            } catch ( Exception e ) {
                e.printStackTrace( );
            }

            // 네트워크로 보낸다.
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getFileName(Uri albumUri) {
        Cursor cursor = getContentResolver( ).query( albumUri, null, null, null, null );
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


    }
