package com.example.asm_ad.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm_ad.Model.Expense;
import com.example.asm_ad.R;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {
    private List<Expense> expenseList;
    private OnExpenseClickListener listener; // Thêm listener


    public interface OnExpenseClickListener {
        void onItemClick(Expense expense); // Trả về expense khi click

        void onDeleteClick(Expense expense); // Click để xóa
    }

    public ExpenseAdapter(List<Expense> expenseList, OnExpenseClickListener listener) {
        this.expenseList = expenseList;
        this.listener = listener;
    }
    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        if (expense == null) return;

        holder.txtCategory.setText(expense.getCategory());
        holder.txtAmount.setText("$" + expense.getAmount());
        holder.txtDate.setText(expense.getDate());

        // Nhấn vào item để chỉnh sửa
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(expense); // Gửi expense để chỉnh sửa
            }
        });

        // Nhấn nút Xóa
        holder.btnDeleteExpense.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(expense); // Gửi expense để xóa
            }
        });

    }


    @Override
    public int getItemCount() {
        return expenseList.size();
    }
    public  class ExpenseViewHolder extends RecyclerView.ViewHolder {
        private Button btnDeleteExpense;
        private TextView txtCategory, txtAmount, txtDate;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            txtAmount = itemView.findViewById(R.id.txtAmout);
            txtDate = itemView.findViewById(R.id.txtDate);
            btnDeleteExpense = itemView.findViewById(R.id.btnExpense);
        }

    }
}