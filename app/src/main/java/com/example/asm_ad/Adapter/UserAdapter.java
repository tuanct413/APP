package com.example.asm_ad.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm_ad.Model.User;
import com.example.asm_ad.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;  // Danh sách người dùng
    private OnItemClickListener listener;

    // Interface để lắng nghe sự kiện click
    public interface OnItemClickListener {
        void onItemClick(User user);
        void onDeleteClick(User User);
    }

    // Constructor
    public UserAdapter(List<User> userList, OnItemClickListener listener) {
        this.userList = userList;
        this.listener = listener;
    }
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Khởi tạo item layout cho mỗi user
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin, parent, false);
        return new UserViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);

        // Set dữ liệu cho các TextView trong mỗi item
        holder.txtUserName.setText(user.getFirstname() + " " + user.getLastname());
        holder.txtUserEmail.setText(user.getEmail());


        // Set event listener cho item click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(user);
            }
        });
        // Nhấn nút Xóa
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(user); // Gửi expense để xóa
            }
        });
    }
    public void removeUser(int position) {
        if (position >= 0 && position < userList.size()) {
            userList.remove(position);  // Xóa user khỏi danh sách
            notifyItemRemoved(position);  // Cập nhật RecyclerView
        }
    }


    @Override
    public int getItemCount() {
        return userList != null ? userList.size() : 0;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView txtUserName, txtUserEmail;
        Button btnDelete;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ các TextView từ item layout
            txtUserName = itemView.findViewById(R.id.txtName);
            txtUserEmail = itemView.findViewById(R.id.txtEmail);
            btnDelete = itemView.findViewById(R.id.btnDelte);
        }
    }
}
