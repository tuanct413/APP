package com.example.asm_ad.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.asm_ad.Model.User;

import java.util.ArrayList;
import java.util.List;

public class DataBaseUserHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    // Thông tin database
    private static final String DATABASE_NAME = "UserManager.db";
    private static final int DATABASE_VERSION = 3;

    private static final String TABLE_USER = "users";
    // Tên các cột
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_LASTNAME = "fullname";
    private static final String COLUMN_FIRSTNAME = "firstname";


    // Bảng Expenses

    private static final String TABLE_EXPENSES = "expenses";
    private static final String COLUMN_EXPENSE_ID = "expense_id";
    private static final String COLUMN_EXPENSE_AMOUNT = "amount";
    private static final String COLUMN_EXPENSE_CATEGORY = "category";
    private static final String COLUMN_EXPENSE_DATE = "date";

    // Bảng Budget
    private static final String TABLE_BUDGET = "budget";
    private static final String COLUMN_BUDGET_ID = "budget_id";
    private static final String COLUMN_BUDGET_AMOUNT = "amount";
    private static final String COLUMN_BUDGET_CATEGORY = "category";

    private static final String COLUMN_BUDGET_DATE = "date";

    public DataBaseUserHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "DatabaseHelper: Khởi tạo database");
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Creating database tables...");

        // Tạo bảng Users
        String createTableUsers = "CREATE TABLE " + TABLE_USER + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EMAIL + " TEXT UNIQUE NOT NULL, " +
                COLUMN_PASSWORD + " TEXT NOT NULL, " +
                COLUMN_LASTNAME + " TEXT NOT NULL," +
                COLUMN_FIRSTNAME + " TEXT NOT NULL);";
        db.execSQL(createTableUsers);

        // Tạo bảng Expenses
        String createTableExpenses = "CREATE TABLE " + TABLE_EXPENSES + " (" +
                COLUMN_EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EXPENSE_AMOUNT + " REAL NOT NULL, " +
                COLUMN_EXPENSE_CATEGORY + " TEXT NOT NULL, " +
                COLUMN_EXPENSE_DATE + " TEXT NOT NULL, " +
                COLUMN_ID + " INTEGER, " +
                "FOREIGN KEY (" + COLUMN_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_ID + ") ON DELETE CASCADE ON UPDATE CASCADE);";
        db.execSQL(createTableExpenses);

        String createTableBudget = "CREATE TABLE " + TABLE_BUDGET + " (" +
                COLUMN_BUDGET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BUDGET_AMOUNT + " REAL NOT NULL, " +
                COLUMN_BUDGET_CATEGORY + " TEXT NOT NULL, " +
                COLUMN_BUDGET_DATE + " TEXT NOT NULL, " + // Thêm cột date
                COLUMN_ID + " INTEGER, " +
                "FOREIGN KEY (" + COLUMN_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_ID + ") ON DELETE CASCADE ON UPDATE CASCADE);";
        db.execSQL(createTableBudget);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Xóa bảng cũ nếu tồn tại và tạo bảng mới
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USER + TABLE_EXPENSES + TABLE_BUDGET);
        onCreate(sqLiteDatabase);
        Log.d(TAG, "onUpgrade: Nâng cấp database từ " + oldVersion + " lên " + newVersion);
    }

    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        long userId = -1;

        try {
            // Thêm user vào bảng users
            ContentValues userValues = new ContentValues();
            userValues.put(COLUMN_EMAIL, user.getEmail());
            userValues.put(COLUMN_PASSWORD, user.getPassword());
            userValues.put(COLUMN_LASTNAME, user.getLastname());
            userValues.put(COLUMN_FIRSTNAME, user.getFirstname());

            userId = db.insert(TABLE_USER, null, userValues);

            if (userId == -1) {
                throw new Exception("Lỗi khi thêm user");
            }
            Log.d(TAG, "User đã được thêm thành công với ID: " + userId);

            db.setTransactionSuccessful(); // Đánh dấu transaction thành công
        } catch (Exception e) {
            Log.e(TAG, "Lỗi khi thêm user  ", e);
        } finally {

            db.close();
        }
        return (userId != -1 ) ? userId : -1; // Trả về userId nếu thành công, ngược lại -1
    }
    public long addBudget(double amount, String category, long userId, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_BUDGET_AMOUNT, amount);
        contentValues.put(COLUMN_BUDGET_CATEGORY, category);
        contentValues.put(COLUMN_ID, userId);
        contentValues.put(COLUMN_BUDGET_DATE,date);
        db.insert(TABLE_BUDGET, null, contentValues);
        db.close();
        return userId;
    }
    public long addExpense(int userId, double amount, String category, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_EXPENSE_AMOUNT, amount);
        contentValues.put(COLUMN_EXPENSE_CATEGORY, category);
        contentValues.put(COLUMN_EXPENSE_DATE, date);
        contentValues.put(COLUMN_ID, userId);
        db.insert(TABLE_EXPENSES, null, contentValues);
        db.close();
        return userId;
    }
    public int getIdUserForEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_ID + " FROM " + TABLE_USER + " WHERE " + COLUMN_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        int userId = -1; // Mặc định nếu không tìm thấy
        if (cursor.moveToFirst()) { // Kiểm tra nếu có dữ liệu
            userId = cursor.getInt(0); // Lấy ID từ cột đầu tiên
        }
        cursor.close(); // Đóng Cursor để tránh rò rỉ bộ nhớ
        db.close(); // Đóng database
        return userId;
    }
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {COLUMN_ID};
        String selection = COLUMN_EMAIL + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(
                TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        int count = cursor.getCount();
        cursor.close();
        db.close();

        Log.d(TAG, "checkUser: Kiểm tra user " + username + " - Kết quả: " + (count > 0));
        return count > 0;
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE_USER + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }
    public boolean isBudgetCategoryExists(String category, int userId, String selectedDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT 1 FROM " + TABLE_BUDGET + " WHERE " + COLUMN_BUDGET_CATEGORY + " = ? " +
                        "AND " + COLUMN_ID + " = ? " +
                        "AND " + COLUMN_BUDGET_DATE + " LIKE ?",
                new String[]{category, String.valueOf(userId), selectedDate + "%"}
        );
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public boolean isBudgetExpense(String category, int userId, String selectedDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT 1 FROM " + TABLE_EXPENSES + " WHERE " + COLUMN_EXPENSE_CATEGORY + " = ? " +
                        "AND " + COLUMN_ID + " = ? " +
                        "AND " + COLUMN_EXPENSE_DATE + " LIKE ?",
                new String[]{category, String.valueOf(userId), selectedDate + "%"}
        );
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public boolean deleteBudgetByCategory(String category, String month, int userID) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(
                TABLE_BUDGET,
                COLUMN_BUDGET_CATEGORY + " = ? AND " + COLUMN_BUDGET_DATE + " LIKE ? AND " + COLUMN_ID + " = ?",
                new String[]{category, month + "%", String.valueOf(userID)} // Xóa theo category và tháng
        );
        db.close();
        return rowsDeleted > 0; // Trả về true nếu xóa thành công, ngược lại false
    }

    public boolean deleteExpenseByCategory(String category, int userID , String month) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(
                TABLE_EXPENSES,
                COLUMN_EXPENSE_CATEGORY + " = ? AND " + COLUMN_EXPENSE_DATE + " LIKE ? AND " + COLUMN_ID + " = ?",
                new String[]{category, month + "%", String.valueOf(userID)} // Xóa theo category và tháng
        );
        db.close();
        return rowsDeleted > 0;
    }
    public void deleteUser(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, COLUMN_EMAIL + "=?", new String[]{email});
        db.close();
    }


    public String getUserFullname(int IdUser) {
        SQLiteDatabase db = this.getReadableDatabase();
        String fullname = "";

        try {
            String query = "SELECT " + COLUMN_LASTNAME + ", " + COLUMN_FIRSTNAME +
                    " FROM " + TABLE_USER +
                    " WHERE " + COLUMN_ID + " = ?";
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(IdUser)});

            if (cursor.moveToFirst()) {
                String lastName = cursor.getString(0);
                String firstName = cursor.getString(1);
                fullname = lastName + " " + firstName;
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return fullname;
    }
    public double getUserBudget(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        double budgetAmount = 0.0;

        try {
            String query = "SELECT SUM(" + COLUMN_BUDGET_AMOUNT + ") " +
                    "FROM " + TABLE_BUDGET +
                    " WHERE " + COLUMN_ID + " = ? " +
                    " AND " + COLUMN_BUDGET_DATE + " = strftime('%Y-%m', 'now')"; // COLUMN_ID là khóa ngoại tham chiếu userId

            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

            if (cursor.moveToFirst() && !cursor.isNull(0)) { // Kiểm tra null trước khi lấy giá trị
                budgetAmount = cursor.getDouble(0);
            }

            cursor.close();
            Log.d(TAG, "getUserBudget: Tổng budget của User ID " + userId + " là " + budgetAmount);
        } catch (Exception e) {
            Log.e(TAG, "Lỗi khi lấy tổng budget của user", e);
        } finally {
            db.close();
        }

        return budgetAmount;
    }
    public double getUserBudgetLastMonth(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        double budgetAmount = 0.0;

        try {
            String query = "SELECT SUM(" + COLUMN_BUDGET_AMOUNT + ") " +
                    "FROM " + TABLE_BUDGET +
                    " WHERE " + COLUMN_ID + " = ? " +
                    " AND " + COLUMN_BUDGET_DATE + " = strftime('%Y-%m', 'now', '-1 month')";



            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

            if (cursor.moveToFirst() && !cursor.isNull(0)) {
                budgetAmount = cursor.getDouble(0);
            }

            cursor.close();
            Log.d(TAG, "getUserBudgetLastMonth: Tổng ngân sách tháng trước của User ID " + userId + " là " + budgetAmount);
        } catch (Exception e) {
            Log.e(TAG, "Lỗi khi lấy tổng ngân sách tháng trước", e);
        } finally {
            db.close();
        }

        return budgetAmount;
    }


    public double getUseExpense(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        double expenseAmount = 0.0;
        try{
            String query = "SELECT SUM(" + COLUMN_EXPENSE_AMOUNT + ") " +
                    "FROM " + TABLE_EXPENSES +
                    " WHERE " + COLUMN_ID + " = ? " +
                    " AND " + COLUMN_EXPENSE_DATE + " = strftime('%Y-%m', 'now')"; //
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
            if (cursor.moveToFirst() && !cursor.isNull(0)) {
                expenseAmount = cursor.getDouble(0);
            }
            cursor.close();
        }
        catch (Exception e){
            Log.e(TAG, "Lỗi khi lấy tổng chi tiêu của user", e);
        }
        finally {
            db.close();
        }
        return  expenseAmount ;
    }
    public double getUserExpenseLastMonth(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        double expenseAmount = 0.0;

        try {
            String query = "SELECT SUM(" + COLUMN_EXPENSE_AMOUNT + ") " +
                    "FROM " + TABLE_EXPENSES +
                    " WHERE " + COLUMN_ID + " = ? " +
                    " AND " + COLUMN_EXPENSE_DATE + " = strftime('%Y-%m', 'now', '-1 month')";


            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

            if (cursor.moveToFirst() && !cursor.isNull(0)) {
                expenseAmount = cursor.getDouble(0);
            }

            cursor.close();
            Log.d(TAG, "getUserExpenseLastMonth: Tổng chi tiêu tháng trước của User ID " + userId + " là " + expenseAmount);
        } catch (Exception e) {
            Log.e(TAG, "Lỗi khi lấy tổng chi tiêu tháng trước", e);
        } finally {
            db.close();
        }

        return expenseAmount;
    }


    public List<String[]> getUserBudgets(int userId) {
        List<String[]> budgetList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT " + COLUMN_BUDGET_AMOUNT + ", " + COLUMN_BUDGET_CATEGORY + " ," + COLUMN_BUDGET_DATE +
                    " FROM " + TABLE_BUDGET + " WHERE " + COLUMN_ID + " = ?";

            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

            while (cursor.moveToNext()) {
                String amount = cursor.getString(0);
                String category = cursor.getString(1);
                String date = cursor.getString(2);
                budgetList.add(new String[]{amount, category,date});
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return budgetList;
    }
    public List<String[]> getUserExpense(int userId) {
        List<String[]> expensesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            String query = "SELECT " + COLUMN_EXPENSE_AMOUNT + ", " + COLUMN_EXPENSE_CATEGORY + "," + COLUMN_EXPENSE_DATE +
                    " FROM " + TABLE_EXPENSES + " WHERE " + COLUMN_ID + " = ?";

            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
            while (cursor.moveToNext()) {
                String amount = cursor.getString(0);
                String category = cursor.getString(1);
                String date = cursor.getString(2);
                expensesList.add(new String[]{amount, category, date});
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return expensesList;
    }

    public boolean updateExpense(int userId, String oldCategory, double amount, String category, String newDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_EXPENSE_AMOUNT, amount);
        contentValues.put(COLUMN_EXPENSE_CATEGORY, category);
        contentValues.put(COLUMN_EXPENSE_DATE, newDate);

        int rowsUpdated = db.update(TABLE_EXPENSES, contentValues,
                COLUMN_ID + " = ? AND " + COLUMN_EXPENSE_CATEGORY + " = ?",
                new String[]{String.valueOf(userId), oldCategory});

        db.close();
        return rowsUpdated > 0;
    }
    public List<String[]> getAllUsers() {
        List<String[]> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Truy vấn lấy dữ liệu từ bảng users
        String query = "SELECT " + COLUMN_EMAIL + ", " + COLUMN_FIRSTNAME + ", " + COLUMN_LASTNAME + " FROM " + TABLE_USER;
        Cursor cursor = db.rawQuery(query, null);

        // Kiểm tra và lấy dữ liệu từ cursor
        if (cursor.moveToFirst()) {
            do {
                // Lấy chỉ mục của các cột
                int emailIndex = cursor.getColumnIndex(COLUMN_EMAIL);
                int firstNameIndex = cursor.getColumnIndex(COLUMN_FIRSTNAME);
                int lastNameIndex = cursor.getColumnIndex(COLUMN_LASTNAME);

                // Lấy giá trị từ các cột
                String email = cursor.getString(emailIndex);  // Lấy giá trị email từ cột
                String firstName = cursor.getString(firstNameIndex);  // Lấy giá trị firstname từ cột
                String lastName = cursor.getString(lastNameIndex);  // Lấy giá trị lastname từ cột

                // Tạo một mảng String[] để chứa thông tin người dùng
                String[] user = new String[]{email, firstName, lastName};

                // Thêm mảng vào danh sách
                userList.add(user);
            } while (cursor.moveToNext());
        }

        // Đóng cursor và db
        cursor.close();
        db.close();

        return userList;
    }






}