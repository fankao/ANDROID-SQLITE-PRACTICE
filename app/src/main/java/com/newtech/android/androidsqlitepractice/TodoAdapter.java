package com.newtech.android.androidsqlitepractice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {
    private Context mContext;
    private List<Todo> mTodos;

    private ItemClickListener mItemClickListener;
    private ItemCheckedChangeListener mItemCheckedChangeListener;

    public interface ItemClickListener {
        void onClick(View view, int pos);
    }

    public interface ItemCheckedChangeListener {
        void onCheckedChanged(CompoundButton buttonView, boolean isChecked, int pos);
    }

    public TodoAdapter(Context context, List<Todo> todos) {
        mContext = context;
        mTodos = todos;
    }

    @NonNull
    @Override
    public TodoAdapter.TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_todo, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoAdapter.TodoViewHolder holder, int position) {
        holder.bind(mTodos.get(position));
    }

    @Override
    public int getItemCount() {
        return mTodos.size();
    }

    public void setTodos(List<Todo> todos) {
        mTodos = todos;
    }

    public class TodoViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        CheckBox chkComplete;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.text_todo);
            chkComplete = itemView.findViewById(R.id.chk_complete);

            chkComplete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (chkComplete.isChecked()) isChecked = true;
                    mItemCheckedChangeListener.onCheckedChanged(buttonView, isChecked, getAdapterPosition());
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onClick(itemView, getAdapterPosition());
                }
            });
        }

        public void bind(Todo todo) {
            txtName.setText(todo.getName());
            if (todo.isCompleted()) {
                chkComplete.setChecked(true);
            } else {
                chkComplete.setChecked(false);
            }
        }
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void setItemCheckedChangeListener(ItemCheckedChangeListener itemCheckedChangeListener) {
        mItemCheckedChangeListener = itemCheckedChangeListener;
    }
}
