package com.example.asm_ad.Frafment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm_ad.Adapter.ExpenseAdapter;
import com.example.asm_ad.Database.DataBaseUserHelper;
import com.example.asm_ad.Model.Expense;
import com.example.asm_ad.R;

import java.util.ArrayList;
import java.util.List;

public class ExpenseFrafment extends Fragment implements ExpenseAdapter.OnExpenseClickListener {
    private EditText etAmount, etCategory, etDate;
    private Button btnAddExpense;
    private DataBaseUserHelper dbHelper;
    private ExpenseAdapter expenseAdapter;
    private RecyclerView rvExpense;
    private List<Expense> expenseList;
    private int userId;
    private TextView textBudgetStatus;

    private double Total;
    private double expense;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense, container, false);

        etAmount = view.findViewById(R.id.editTextAmount);
        etCategory = view.findViewById(R.id.editTextCategory);
        etDate = view.findViewById(R.id.editTextDate);
        btnAddExpense = view.findViewById(R.id.btnAddExpense);
        rvExpense = view.findViewById(R.id.rvExpense);
        textBudgetStatus = view.findViewById(R.id.textBudgetStatus);

        dbHelper = new DataBaseUserHelper(getActivity());
        btnAddExpense.setOnClickListener(v -> addExpense());
        // Khởi tạo danh sách chi tiêu
        expenseList = new ArrayList<>();
        rvExpense.setLayoutManager(new LinearLayoutManager(requireContext()));
        expenseAdapter = new ExpenseAdapter(expenseList, this);
        rvExpense.setAdapter(expenseAdapter);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", requireContext().MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);
        this.userId = userId;
        showExpense();
        setBudgetStatus();
        return view;
    }
    public void showExpense() {
        List<String[]> expensesList = dbHelper.getUserExpense(userId);
        expenseList.clear();
        for (String[] expense : expensesList) {
            double amount = Double.parseDouble(expense[0]);
            String category = expense[1];
            String date = expense[2];
            expenseList.add(new Expense(amount, category, date));
        }
        expenseAdapter.notifyDataSetChanged();
    }
    private void addExpense() {
        String amountStr = etAmount.getText().toString().trim();
        String category = etCategory.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        if (amountStr.isEmpty() || category.isEmpty() || date.isEmpty()) {
            Toast.makeText(getActivity(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }
        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), "Số tiền không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(dbHelper.isBudgetExpense(category, userId,date)){
            Toast.makeText(getActivity(), "Danh mục đã tồn tại!", Toast.LENGTH_SHORT).show();
            return;
        };
        if (amount > Total) {
            Toast.makeText(getActivity(), "Ngân sách sắp hết, không thể thêm chi tiêu!", Toast.LENGTH_SHORT).show();
            return;
        }
        long insertedId = dbHelper.addExpense(userId, amount, category, date);
        if (insertedId != -1) {
            Toast.makeText(getActivity(), "Chi tiêu đã được lưu!", Toast.LENGTH_SHORT).show();
            etAmount.setText("");
            etCategory.setText("");
            etDate.setText("");
            showExpense();
            setBudgetStatus();
        } else {
            Toast.makeText(getActivity(), "Lỗi khi thêm chi tiêu!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(Expense expense) {
        // Hiển thị thông tin cần chỉnh sửa
        etAmount.setText(String.valueOf(expense.getAmount()));
        etCategory.setText(expense.getCategory());
        etDate.setText(expense.getDate());
        // Lưu category cũ để cập nhật
        etCategory.setTag(expense.getCategory());
        // Đổi nút thành "Cập nhật"
        btnAddExpense.setText("Cập nhật");
        btnAddExpense.setOnClickListener(v -> {
            updateExpense(expense);

        });
    }

    // Hàm cập nhật chi tiêu
    private void updateExpense(Expense oldExpense) {
        String amountStr = etAmount.getText().toString().trim();
        String category = etCategory.getText().toString().trim();
        String date = etDate.getText().toString().trim();

        if (amountStr.isEmpty() || category.isEmpty() || date.isEmpty()) {
            Toast.makeText(getActivity(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), "Số tiền không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gọi hàm cập nhật trong database
        boolean success = dbHelper.updateExpense(userId, oldExpense.getCategory(), amount, category, date);
        if (success) {
            Toast.makeText(getActivity(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            etAmount.setText("");
            etCategory.setText("");
            etDate.setText("");
            btnAddExpense.setText("Thêm chi tiêu");
            // Khôi phục sự kiện thêm mới
            btnAddExpense.setOnClickListener(v -> addExpense());
            showExpense();
            setBudgetStatus();
        } else {
            Toast.makeText(getActivity(), "Lỗi khi cập nhật chi tiêu!", Toast.LENGTH_SHORT).show();
        }
    }
    public void setBudgetStatus() {
        double budget = dbHelper.getUserBudget(userId); // Lấy tổng số tiền budget
        expense = dbHelper.getUseExpense(userId); // Lấy tổng số tiền chi tiêu
        Total = budget - expense;
        this.expense = expense;
        this.Total = Total;
        String budgetText = String.valueOf(Total); // Chuyển từ double sang String
        textBudgetStatus.setText("The remaining budget $ "+ Total); // Gán vào TextView
        SharedPreferences.Editor editor = requireActivity().getSharedPreferences("UserPrefs", requireContext().MODE_PRIVATE).edit();
        editor.putFloat("remainingBudget", (float) Total);
        editor.apply();
    }
    @Override
    public void onDeleteClick(Expense expense) {
        if (dbHelper.deleteExpenseByCategory(expense.getCategory(), userId, expense.getDate())) {
            showExpense(); // Cập nhật danh sách
            setBudgetStatus(); // Cập nhật ngân sách
            Toast.makeText(getActivity(), "Xóa thành công!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Lỗi khi xóa chi tiêu!", Toast.LENGTH_SHORT).show();
        }
    }
}