package com.example.asm_ad;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.asm_ad.Database.DataBaseUserHelper;

public class Login extends AppCompatActivity {

    private EditText editEmail;
    private EditText editPassword;
    private Button btnLogin;
    private TextView txtthongbao;
    private Button btnSign;
    private SharedPreferences sharedPreferences;
    private DataBaseUserHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);


        // Khởi tạo SQLite Database Helper
        dbHelper = new DataBaseUserHelper(this);

        // Khởi tạo SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtthongbao = findViewById(R.id.txtthongbao);
        btnSign = findViewById(R.id.btnSign);

        btnLogin.setOnClickListener(v -> loginUser());
        btnSign.setOnClickListener(v -> goToRegister());
    }

    private void loginUser() {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            txtthongbao.setText("Vui lòng nhập đầy đủ thông tin");
            txtthongbao.setVisibility(View.VISIBLE);
            return;
        }
        if (email.equals("admin") && password.equals("admin")) {
            admin();
            return;
        }

        // Kiểm tra tài khoản trong SQLite
        boolean isUserValid = dbHelper.checkUser(email, password);

        if (isUserValid) {

            // Lưu trạng thái đăng nhập
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", true);
            int userId = dbHelper.getIdUserForEmail(email);
            editor.putInt("userId", userId);

            editor.apply();
            Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

            navigateToHome();
        } else {
            txtthongbao.setText("Thông Tin Tài Khoản Không Chính Xác");
            txtthongbao.setVisibility(View.VISIBLE);
        }
    }

    private void navigateToHome() {
        Intent intent = new Intent(Login.this, Home.class);
        startActivity(intent);
        finish();
    }
    private  void  admin(){
        Intent intent = new Intent(Login.this, admin.class);
        startActivity(intent);
        finish();
    }
    private void goToRegister() {
        Intent intent = new Intent(Login.this, Register.class);
        startActivity(intent);
        finish();
    }
}