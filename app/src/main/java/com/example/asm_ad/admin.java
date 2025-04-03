package com.example.asm_ad;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm_ad.Adapter.UserAdapter;
import com.example.asm_ad.Database.DataBaseUserHelper;
import com.example.asm_ad.Model.User;

import java.util.ArrayList;
import java.util.List;

public class admin extends AppCompatActivity implements UserAdapter.OnItemClickListener {

    private DataBaseUserHelper dbHelper;
    private Button btnAddUser,btnbacklogin;
    private List<User> userList;
    private RecyclerView rvBudgets;
    private UserAdapter userAdapter;
    private EditText editEmail;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);

        // Khởi tạo các view
        dbHelper = new DataBaseUserHelper(this);
        btnAddUser = findViewById(R.id.btnAddUser);
        btnbacklogin = findViewById(R.id.btnbacklogin);
        rvBudgets = findViewById(R.id.recyclerViewUsers); // Đảm bảo ID này tồn tại trong activity_admin.xml
//        btnbacklogin.setOnClickListener(v -> )
        btnAddUser.setOnClickListener(v -> register());
        btnbacklogin.setOnClickListener(v -> navigateToLogin());

        // Khởi tạo các view

        editEmail = findViewById(R.id.editEmail);


        // Khởi tạo danh sách người dùng và Adapter
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList, this);
        rvBudgets.setLayoutManager(new LinearLayoutManager(this));
        rvBudgets.setAdapter(userAdapter);

        // Load danh sách người dùng từ database
        loadUserData();
    }

    private void loadUserData() {
        userList.clear();  // Xóa danh sách cũ

        List<String[]> rawData = dbHelper.getAllUsers(); // Giả sử đây là danh sách từ database
        for (String[] data : rawData) {
            if (data.length >= 3) { // Đảm bảo có đủ dữ liệu
                String email = data[0];
                String firstName = data[1];
                String lastName = data[2];
                userList.add(new User(email, firstName, lastName));
            }
        }

        userAdapter.notifyDataSetChanged(); // Cập nhật giao diện
    }

    private void register() {
        Intent intent = new Intent(admin.this, AddUserActivity.class);
        startActivity(intent);
    }
    private void navigateToLogin() {
        Intent intent = new Intent(admin.this, Login.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemClick(User user) {

    }
    @Override
    public void onDeleteClick(User User) {
        dbHelper.deleteUser(User.getEmail());
        loadUserData();
    }


}
