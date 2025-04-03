package com.example.asm_ad;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.asm_ad.Database.DataBaseUserHelper;
import com.example.asm_ad.Model.User;


public class Register extends AppCompatActivity {

    private EditText editEmail, editPassword, editFirstName, editLastName;
    private Button btnSign,btnback;
    private TextView txtthongbao;
    private DataBaseUserHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        dbHelper = new DataBaseUserHelper(this);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        btnSign = findViewById(R.id.btnSign);
        txtthongbao = findViewById(R.id.txtthongbao);
        btnback = findViewById(R.id.btnback);
        btnSign.setOnClickListener(v -> userRegister());
        btnback.setOnClickListener(v -> navigateToLogin(

        ));
    }

    private void userRegister() {
        String mail = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String firstName = editFirstName.getText().toString().trim();
        String lastName = editLastName.getText().toString().trim();

        if (mail.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            txtthongbao.setText("Vui lòng nhập đầy đủ thông tin");
            txtthongbao.setVisibility(View.VISIBLE);
            return;
        }

        // Kiểm tra xem tài khoản đã tồn tại chưa
        if (dbHelper.isEmailExists(mail)) {
            txtthongbao.setText("Tài khoản đã tồn tại");
            txtthongbao.setVisibility(View.VISIBLE);
            return;
        }
        // Tạo đối tượng User mới
        User newUser = new User(mail, password, firstName, lastName);
        long id = dbHelper.addUser(newUser);

        if (id != -1) {
            txtthongbao.setText("Đăng ký thành công");
            txtthongbao.setVisibility(View.VISIBLE);
            navigateToLogin();
        } else {
            txtthongbao.setText("Đăng ký thất bại, vui lòng thử lại");
            txtthongbao.setVisibility(View.VISIBLE);
        }
    }

    private void navigateToLogin() {
        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);
        finish();
    }
}