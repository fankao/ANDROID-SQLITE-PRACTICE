package com.newtech.android.androidsqlitepractice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DBManager extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "todo_list";
    private static final String TABLE_TODO = "todo";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String COMPLETED = "isCompleted";

    private Context context;


    public DBManager(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "CREATE TABLE " + TABLE_TODO + " (" +
                ID + " TEXT primary key, " +
                NAME + " TEXT, " +
                COMPLETED + " NUMERIC)";
        db.execSQL(sqlQuery);
        Toast.makeText(context, "Create successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        onCreate(db);
        Toast.makeText(context, "Drop successfylly", Toast.LENGTH_SHORT).show();
    }

    public void addNewTodo(Todo todo) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID, todo.getId().toString());
        values.put(NAME, todo.getName());
        values.put(COMPLETED, false);

        database.insert(TABLE_TODO, null, values);
        database.close();
    }

    public List<Todo> getTodos() {
        List<Todo> todos = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_TODO;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(sql, null);
        try {
            cursor.moveToNext();
            while (!cursor.isAfterLast()) {
                Todo todo = new Todo();
                todo.setId(UUID.fromString(cursor.getString(0)));
                todo.setName(cursor.getString(1));
                todo.setCompleted(cursor.getInt(2) > 0);
                todos.add(todo);

                cursor.moveToNext();
            }
        } finally {
            database.close();
        }
        return todos;

    }

    public Todo getTodoById(String id) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_TODO,
                new String[]{ID, NAME, COMPLETED},
                ID + "=?",
                new String[]{id},
                null, null, null, null);
        if(cursor!=null){
            cursor.moveToFirst();
            Todo todo = new Todo();
            todo.setId(UUID.fromString(cursor.getString(0)));
            todo.setName(cursor.getString(1));
            todo.setCompleted(cursor.getInt(2) > 0);
            return todo;

        }
        return null;
    }

    public boolean updateTodo(Todo todo) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, todo.getName());
        values.put(COMPLETED, todo.isCompleted() ? 1 : 0);
        return database.update(TABLE_TODO, values, ID + "=?", new String[]{todo.getId().toString()}) > 0;
    }

    public void deleteTodo(Todo todo) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_TODO, ID + "=?", new String[]{todo.getId().toString()});
        database.close();
    }


}
