package com.example.asm_ad.Frafment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.asm_ad.BudgetActivity;
import com.example.asm_ad.Database.DataBaseUserHelper;
import com.example.asm_ad.R;


public class HomeFrafment extends Fragment {

    private TextView user;
    private DataBaseUserHelper dbHelper;
    private SharedPreferences sharedPreferences;

    private Button btnaddBudget;

    private TextView txtBudget;

    private int userId;
    private ProgressBar progressBar1,progressBar2;
    private TextView txtExpensePercentage ,txtExpense;


    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        user = view.findViewById(R.id.user);
        txtBudget = view.findViewById(R.id.txtBudget); // Cần khai báo trong XML
        progressBar1 = view.findViewById(R.id.Budget);
        txtExpensePercentage = view.findViewById(R.id.txtExpensePercentage);
        btnaddBudget = view.findViewById(R.id.addBudget);
        progressBar2 = view.findViewById(R.id.Expense);
        txtExpense = view.findViewById(R.id.txtExpense);
        // Khởi tạo database helper

        dbHelper = new DataBaseUserHelper(requireContext());

        btnaddBudget.setOnClickListener(v -> showAddBudget());
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", requireContext().MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);
        this.userId = userId;

        getUserName();
        showBudget();
        showBudget1();
        return view;
    }
    public void getUserName() {
        dbHelper.getUserFullname(userId);
        user.setText("Welcome :" + dbHelper.getUserFullname(userId));
    }
    public void showAddBudget(){
        Intent intent = new Intent(getActivity(), BudgetActivity.class);
        startActivity(intent);
    }
    public void showBudget() {
        double expense = dbHelper.getUseExpense(userId); // Lấy tổng số tiền chi tiêu
        double budget = dbHelper.getUserBudget(userId); // Lấy tổng số tiền budget
        String budgetText = String.valueOf(budget-expense); // Chuyển từ double sang String

        txtBudget.setText("Budget $ "+ budgetText); // Gán vào TextView
    }
    public void showBudget1() {
        double expenseThisMonth = dbHelper.getUseExpense(userId); // Tổng chi tiêu tháng này
        double budgetThisMonth = dbHelper.getUserBudget(userId);  // Ngân sách tháng này

        double expenseLastMonth = dbHelper.getUserExpenseLastMonth(userId); // Tổng chi tiêu tháng trước
        double budgetLastMonth = dbHelper.getUserBudgetLastMonth(userId);  // Ngân sách tháng trước

//       double remainingAmount = budgetThisMonth - expenseThisMonth; // Tính số tiền còn lại
//       double remainingAmountLast = budgetLastMonth - expenseLastMonth ;

        // Hiển thị số tiền còn lại
        txtExpensePercentage.setText("Ngân sách tháng này: " + budgetThisMonth + " | Chi tiêu: " + expenseThisMonth);
        txtExpense.setText("Ngân sách tháng trước: " + budgetLastMonth + " | Chi tiêu: " + expenseLastMonth);


        // Cập nhật progressBar1 (Thể hiện số tiền còn lại)
        progressBar1.setMax((int) budgetThisMonth);  // Ngân sách là giá trị tối đa
        progressBar1.setProgress((int) expenseThisMonth); // Hiển thị tổng chi tiêu

        // Cập nhật progressBar2 (Thể hiện số tiền đã chi tiêu)
        progressBar2.setMax((int) budgetLastMonth);  // Ngân sách là giá trị tối đa
        progressBar2.setProgress((int) expenseLastMonth); // Hiển thị tổng chi tiêu
    }


    @Override
    public void onResume() {
        super.onResume(); // Gọi phương thức onResume()
        // Gọi phương thức cập nhật thông tin ngân sách (có thể là hiển thị dữ liệu mới nhất).
        showBudget();
        showBudget1();
    }


}