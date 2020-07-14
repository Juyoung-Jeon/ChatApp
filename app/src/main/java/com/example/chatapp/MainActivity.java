package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    EditText etEmail, etPassword; // 액티비티 내 다른 메서드에서도 쓸 수 있게함. 상단에 씀으로써. 아니면 메서드 안에서 final 로 설정 가능.
    ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // layout 과 연결

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        progressBar = findViewById(R.id.progressBar);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() { // 클릭 시 실행
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "Login", Toast.LENGTH_SHORT).show(); <- 눌렀을 때 글자 보이게
                final String stEmail = etEmail.getText().toString(); // String 형태로 받아와 저장하겠다.
                String stPassword = etPassword.getText().toString();
                if(stEmail.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please insert Email", Toast.LENGTH_LONG).show();
                    return;
                }
                if(stPassword.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please insert Password", Toast.LENGTH_LONG).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(stEmail, stPassword)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE); // onComplete 에 두어 로그인 완료 후 사라짐
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    // 유저 정보 불러오는 코드
                                    String stUserEmail = user.getEmail();
                                    String stUserName = user.getDisplayName();
                                    Log.d(TAG, "stUserEmail: "+stUserEmail+", stUserName : "+stUserName);

                                    SharedPreferences sharedPref = getSharedPreferences("shared", Context.MODE_PRIVATE); // 다른 데서 불러올 수 있게 shared 로 변경
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("email", stUserEmail);
                                    editor.commit();

                                    Intent in = new Intent(MainActivity.this, TabActivity.class); // TabActivity 로 넘김. 원래는 Main 으로 두는 게 좋음.
                                    in.putExtra("email",stEmail); // chat 액티비티로 이메일 넘겨주기 위함
                                    startActivity(in); // 클릭 시 인텐트 in 실행
//                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
//                                    updateUI(null);
                                }

                                // ...
                            }
                        });

            }
        });

        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stEmail = etEmail.getText().toString(); // String 형태로 받아와 저장하겠다.
                String stPassword = etPassword.getText().toString();
                if(stEmail.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please insert Email", Toast.LENGTH_LONG).show();
                    return;
                }
                if(stPassword.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please insert Password", Toast.LENGTH_LONG).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
//                Toast.makeText(MainActivity.this, stEmail+",", Toast.LENGTH_SHORT).show(); // 제대로 불러왔는지 창으로 확인하기 위함
                mAuth.createUserWithEmailAndPassword(stEmail, stPassword)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
//                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
//                                    updateUI(null);
                                }

                                // ...
                            }
                        });
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }
}