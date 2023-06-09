package com.example.test_android2.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.test_android2.MainActivity;
import com.example.test_android2.R;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import java.io.IOException;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class KakaoLoginActivity extends AppCompatActivity {
    private static final String TAG = "KakaoLogin";
    private ImageView loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakaologin);

        loginButton = findViewById(R.id.kakaologin);

        // 카카오톡이 설치되어 있는지 확인하는 메서드 , 카카오에서 제공함. 콜백 객체를 이용합.
        Function2<OAuthToken,Throwable, Unit> callback =new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            // 콜백 메서드 ,
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                Log.e(TAG,"CallBack Method");
                //oAuthToken != null 이라면 로그인 성공
                if(oAuthToken!=null){
                    // 토큰이 전달된다면 로그인이 성공한 것이고 토큰이 전달되지 않으면 로그인 실패한다.
                    updateKakaoLoginUi();
                    // sendAccessTokenToServer(String.valueOf(oAuthToken));

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                }else {
                    //로그인 실패
                    Log.e(TAG, "invoke: login fail" );
                }

                return null;
            }
        };

        // 로그인 버튼 클릭 리스너
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 해당 기기에 카카오톡이 설치되어 있는 확인
                if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(KakaoLoginActivity.this)){
                    UserApiClient.getInstance().loginWithKakaoTalk(KakaoLoginActivity.this, callback);
                }else{
                    // 카카오톡이 설치되어 있지 않다면
                    UserApiClient.getInstance().loginWithKakaoAccount(KakaoLoginActivity.this, callback);
                }
            }
        });

    }
    private void updateKakaoLoginUi() {

        // 로그인 여부에 따른 UI 설정
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {

                if (user != null) {

                    // 유저의 아이디
                    Log.d(TAG, "invoke: id =" + user.getId());
                    // 유저의 이메일
                    Log.d(TAG, "invoke: email =" + user.getKakaoAccount().getEmail());

                }
                return null;
            }
        });
    }

    // 로그인 성공 시, 접근 토큰을 서버로 전송하는 메소드
    private void sendAccessTokenToServer(String accessToken) {
        // 서버로 접근 토큰 전송을 위한 HTTP 요청
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        String requestBody = "{\"accessToken\":\"" + accessToken + "\"}";

        RequestBody body = RequestBody.create(mediaType, requestBody);
        Request request = new Request.Builder()
                .url("http://your-server-url.com/login")
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            // 서버 응답 처리
        } catch (IOException e) {
            e.printStackTrace();
            // 요청 실패 처리
        }
    }

}