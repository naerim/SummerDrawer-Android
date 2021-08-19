package com.example.summerdrawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = null;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    Button googleLoginBtn;
    TextView joinMailBtn;
    View navbar;

    String userName, userMail, password;

    // 데이터 불러오기
    ArrayList<Contents> contentList; // 전체 작품 리스트
    ArrayList<Contents> movieList;
    ArrayList<Contents> bookList;
    ArrayList<Contents> webtoonList;
    ArrayList<Contents> dramaList;
    ArrayList<LikeScrap> likeScrapList; // 좋아요,스크랩 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        contentList = (ArrayList<Contents>) getIntent().getSerializableExtra("allContents");
        movieList = (ArrayList<Contents>) getIntent().getSerializableExtra("movieList");
        bookList = (ArrayList<Contents>) getIntent().getSerializableExtra("bookList");
        webtoonList = (ArrayList<Contents>) getIntent().getSerializableExtra("webtoonList");
        dramaList = (ArrayList<Contents>) getIntent().getSerializableExtra("dramaList");
        likeScrapList = (ArrayList<LikeScrap>) getIntent().getSerializableExtra("likeScrapList");

        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        googleLoginBtn = findViewById(R.id.googleLoginBtn);
        //구글로 시작하기 버튼 클릭 시
        googleLoginBtn.setOnClickListener(view->{
            signIn();
        });

        joinMailBtn = findViewById(R.id.joinMailBtn);
        //이메일로 회원가입 텍스트 클릭 시
        joinMailBtn.setOnClickListener(view->{
            Intent joinMainI = new Intent(this, JoinActivity.class);
            startActivity(joinMainI);
        });
    }
    // [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                handleSignInResult(task);
            } catch (ApiException e) {
            }
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acct = completedTask.getResult(ApiException.class);

            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();

                Log.d("Google Login", "handleSignInResult:personName "+personName);
                Log.d("Google Login", "handleSignInResult:personGivenName "+personGivenName);
                Log.d("Google Login", "handleSignInResult:personEmail "+personEmail);
                Log.d("Google Login", "handleSignInResult:personId "+personId);
                Log.d("Google Login", "handleSignInResult:personFamilyName "+personFamilyName);
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("Google Login", "signInResult:failed code=" + e.getStatusCode());

        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) { //update ui code here
        if (user != null) {
            //로그인 여부 저장
            editor.putBoolean("isLogin", true);

            //사용자 데이터 저장
            userName = "아무개";
            userMail = "@naver.com";
            password = "12341234";
            editor.putString("userName", userName);
            editor.putString("userId", userMail);
            editor.putString("password", password);
            editor.apply();

            toMain();
        }
    }

    //메인 액티비티로 이동
    void toMain(){
        Intent toMainI = new Intent(this, MainActivity.class);
        toMainI.putExtra("allContents", contentList);
        toMainI.putExtra("movieList", movieList);
        toMainI.putExtra("bookList", bookList);
        toMainI.putExtra("webtoonList", webtoonList);
        toMainI.putExtra("dramaList", dramaList);
        toMainI.putExtra("likeScrapList", likeScrapList);
        startActivity(toMainI);
        finish();
    }
}