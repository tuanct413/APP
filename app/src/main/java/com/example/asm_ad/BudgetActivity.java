package com.example.asm_ad;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm_ad.Model.Budget;
import com.example.asm_ad.Database.DataBaseUserHelper;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;




import com.example.asm_ad.Adapter.BudgetAdapter;

public class BudgetActivity extends AppCompatActivity implements BudgetAdapter.OnItemClickListener {
    private EditText edtBudgetAmount, edtBudgetCategory;
    private Button btnSaveBudget, btnBack,btnDeleteBudget,btnUpdateBudget;
    private RecyclerView rvBudgets;
    private BudgetAdapter budgetAdapter;
    private List<Budget> budgetList;
    private DataBaseUserHelper dbHelper;
    private EditText edtBudgetDate;
    // Khởi tạo đối tượng SimpleDateFormat với định dạng bạn mong muốn



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        // Ánh xạ View
        edtBudgetAmount = findViewById(R.id.edtBudgetAmount);
        edtBudgetCategory = findViewById(R.id.edtBudgetCategory);
        btnSaveBudget = findViewById(R.id.btnSaveBudget);
        rvBudgets = findViewById(R.id.rvExpense);
        btnBack = findViewById(R.id.btnBack);
        btnDeleteBudget = findViewById(R.id.btnDeleteBudget);
        btnUpdateBudget = findViewById(R.id.btnUpdateBudget);
        // Khởi tạo SQLite Database Helper
        dbHelper = new DataBaseUserHelper(this);
        // Khởi tạo danh sách ngân sách
        budgetList = new ArrayList<>();
        budgetAdapter = new BudgetAdapter(budgetList, this);
        edtBudgetDate = findViewById(R.id.edtBudgetDate);


        edtBudgetDate.setOnClickListener(v -> showDatePicker());
        // Truyền `this` để lắng nghe sự kiện click
        showViewBudget();

        // Thiết lập RecyclerView
        rvBudgets.setLayoutManager(new LinearLayoutManager(this));
        rvBudgets.setAdapter(budgetAdapter);
        btnBack.setOnClickListener(v -> backToHome());
        //  "Lưu ngân sách"
        btnSaveBudget.setOnClickListener(v -> saveBudget());
        //  "Xóa ngân sách"
        btnDeleteBudget.setOnClickListener(v -> {
            deleteBudget();
        });
        //  "Update ngân sách"
        btnUpdateBudget.setOnClickListener(v -> {
            update();
        });
    }
    @Override
    public void onItemClick(Budget budget) {
        edtBudgetCategory.setText(budget.getCategory());
        edtBudgetCategory.setTag(budget.getCategory()); // Lưu danh mục ban đầu để kiểm tra khi update
        edtBudgetAmount.setText(String.valueOf(budget.getAmount()));
        edtBudgetDate.setText(budget.getDate());
    }
    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, dayOfMonth) -> {
                    // Định dạng thành "yyyy-MM"
                    String selectedDate = String.format(Locale.getDefault(), "%d-%02d", selectedYear, selectedMonth + 1);
                    edtBudgetDate.setText(selectedDate);
                }, year, month, 1); // Chỉ lấy tháng, ngày mặc định là 1

        datePickerDialog.show();
    }

    public void saveBudget() {
        String selectedDate = edtBudgetDate.getText().toString().trim();
        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn tháng!", Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);
        String category = edtBudgetCategory.getText().toString().trim();
        String amountStr = edtBudgetAmount.getText().toString().trim();
        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Số tiền không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (dbHelper.isBudgetCategoryExists(category, userId,  selectedDate)) {
            Toast.makeText(this, "Danh mục ngân sách đã tồn tại trong tháng này!", Toast.LENGTH_SHORT).show();
            return;
        }
        long insertedId = dbHelper.addBudget(amount, category, userId, selectedDate);
        if (insertedId != -1) {
            Toast.makeText(this, "Ngân sách đã được lưu!", Toast.LENGTH_SHORT).show();
            showViewBudget();
        } else {
            Toast.makeText(this, "Lỗi khi lưu ngân sách!", Toast.LENGTH_SHORT).show();
        }
    }

    public void backToHome() {
        Intent intent = new Intent(BudgetActivity.this, Home.class);
        startActivity(intent);
    }
    public void showViewBudget() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);

        List<String[]> budgets = dbHelper.getUserBudgets(userId);
        budgetList.clear();

        for (String[] budget : budgets) {
            String date = budget[2];
            String category = budget[1];
            double amount = Double.parseDouble(budget[0]);
            budgetList.add(new Budget(category, amount,date));
        }

        budgetAdapter.notifyDataSetChanged();
    }
    public void deleteBudget() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);
        String category = edtBudgetCategory.getText().toString().trim();
        String month = edtBudgetDate.getText().toString().trim();
        if (dbHelper.deleteBudgetByCategory(category,month,userId)) {
            showViewBudget();
        }
        else {
            Toast.makeText(this, "Lỗi khi xóa ngân sách!", Toast.LENGTH_SHORT).show();
        }
    }
    public void update(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);
        String oldCategory = edtBudgetCategory.getTag() != null ? edtBudgetCategory.getTag().toString() : "";
        String newCategory = edtBudgetCategory.getText().toString().trim();
        String amountStr = edtBudgetAmount.getText().toString().trim();
        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Số tiền không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!oldCategory.equals(newCategory)) {
            Toast.makeText(this, "Không thể thay đổi danh mục ngân sách!", Toast.LENGTH_SHORT).show();
            return;
        }
        // Nếu danh mục không đổi, cập nhật số tiền
        deleteBudget();
        saveBudget();
    }

}