package com.example.asm_ad;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asm_ad.Database.DataBaseUserHelper;
import com.example.asm_ad.Model.User;

public class AddUserActivity extends AppCompatActivity {

    private EditText editEmail, editPassword, editFirstName, editLastName;
    private Button btnAddUser, btnBack;
    private TextView txtNotification;
    private DataBaseUserHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        dbHelper = new DataBaseUserHelper(this);

        // Lấy các thành phần UI
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        btnAddUser = findViewById(R.id.btnAddUser);
        txtNotification = findViewById(R.id.txtNotification);
        btnBack = findViewById(R.id.btnback);

        // Cài đặt sự kiện cho các nút
        btnAddUser.setOnClickListener(v -> addUser());
        btnBack.setOnClickListener(v -> navigateToAdmin());
    }

    private void addUser() {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String firstName = editFirstName.getText().toString().trim();
        String lastName = editLastName.getText().toString().trim();

        // Kiểm tra nếu có trường nào bị bỏ trống
        if (email.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            txtNotification.setText("Vui lòng nhập đầy đủ thông tin");
            txtNotification.setVisibility(View.VISIBLE);
            return;
        }

        // Kiểm tra xem email đã tồn tại trong cơ sở dữ liệu chưa
        if (dbHelper.isEmailExists(email)) {
            txtNotification.setText("Tài khoản đã tồn tại");
            txtNotification.setVisibility(View.VISIBLE);
            return;
        }

        // Tạo đối tượng User mới
        User newUser = new User(email, password, firstName, lastName);

        // Thêm người dùng vào cơ sở dữ liệu
        long id = dbHelper.addUser(newUser);

        if (id != -1) {
            txtNotification.setText("Thêm người dùng thành công");
            txtNotification.setVisibility(View.VISIBLE);
            navigateToAdmin();  // Quay lại trang quản trị
        } else {
            txtNotification.setText("Thêm người dùng thất bại, vui lòng thử lại");
            txtNotification.setVisibility(View.VISIBLE);
        }
    }

    private void navigateToAdmin() {
        Intent intent = new Intent(AddUserActivity.this, admin.class); // Thay đổi tên Activity nếu cần
        startActivity(intent);
        finish();
    }

}
