package com.newtech.android.androidsqlitepractice;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText edtTodo;
    Button btnSave,btnDelete,btnCancel;
    RecyclerView recycleViewTodo;
    TodoAdapter mTodoAdapter;
    List<Todo> mTodos;
    DBManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = new DBManager(this);
        recycleViewTodo = findViewById(R.id.recyler_todo);
        setupRecyclerTodos();
        edtTodo = findViewById(R.id.input_todo);
        btnSave = findViewById(R.id.button_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todo = edtTodo.getText().toString();
                addTodo(todo);
            }
        });

    }

    private void addTodo(String todo) {
        manager.addNewTodo(new Todo(todo,false));
        updateUI();
        edtTodo.setText("");
    }

    private void updateUI() {
        mTodos = manager.getTodos();
        mTodoAdapter.setTodos(mTodos);
        recycleViewTodo.post(new Runnable() {
            @Override
            public void run() {
                mTodoAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setupRecyclerTodos() {
        recycleViewTodo.setLayoutManager(new LinearLayoutManager(this));
        mTodos = manager.getTodos();
        mTodoAdapter = new TodoAdapter(this,mTodos);
        recycleViewTodo.setAdapter(mTodoAdapter);
        mTodoAdapter.setItemClickListener(new TodoAdapter.ItemClickListener() {
            @Override
            public void onClick(View view, int pos) {
                Todo todoSelected = mTodos.get(pos);
                edtTodo.setText(todoSelected.getName());
                showOperation(todoSelected);
            }
        });
        mTodoAdapter.setItemCheckedChangeListener(new TodoAdapter.ItemCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked, int pos) {
                Todo todoSelected = mTodos.get(pos);
                if(isChecked){
                    todoSelected.setCompleted(true);
                    Toast.makeText(MainActivity.this, "Todo is completed", Toast.LENGTH_SHORT).show();
                }
                updateTodo(todoSelected);
            }
        });
    }

    private void updateTodo(Todo todoSelected) {
        manager.updateTodo(todoSelected);
        updateUI();
    }

    private void showOperation(Todo todo) {
        btnDelete = findViewById(R.id.button_delete);
        btnCancel = findViewById(R.id.button_cancel);

        btnDelete.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTodo(todo);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideOperation();
            }
        });

    }

    private void deleteTodo(Todo todo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Delete todo")
                .setMessage("Are you sure delete todo "+ todo.getName())
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        manager.deleteTodo(todo);
                        updateUI();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    private void hideOperation() {
        btnSave.setVisibility(View.GONE);
        btnCancel.setVisibility(View.GONE);
    }
}