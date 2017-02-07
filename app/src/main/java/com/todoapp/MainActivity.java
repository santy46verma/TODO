package com.todoapp;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.todoapp.adapter.RecyclerTodoListAdapter;
import com.todoapp.database.DatabaseHelper;
import com.todoapp.model.TodoModel;

import java.util.ArrayList;
import java.util.List;

import static com.todoapp.database.DatabaseHelper.getInstance;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TaskDialogFragment.OnTaskStatus {

    DatabaseHelper db;
    Context context;
    FloatingActionButton addNewTaskBtn;
    RecyclerView recyclerView;
    TextView defaultTextView;
    RecyclerTodoListAdapter recyclerTodoListAdapter;
    List<TodoModel> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Intitalize starting Parameters
        context = MainActivity.this;
        db = getInstance(context);
        //Intialize UI Elements
        defaultTextView = (TextView) findViewById(R.id.defaultTextView);
        addNewTaskBtn = (FloatingActionButton) findViewById(R.id.addTaskBtn);
        addNewTaskBtn.setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        list = db.getAllList();

        if (list.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            defaultTextView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            defaultTextView.setVisibility(View.VISIBLE);
        }
        recyclerTodoListAdapter = new RecyclerTodoListAdapter(this, list);
        recyclerView.setAdapter(recyclerTodoListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.addTaskBtn:
                showAddDialogFragment();
                break;
            default:
                break;
        }


    }

    private void showAddDialogFragment() {

        FragmentManager fm = this.getFragmentManager();
        TaskDialogFragment taskDialogFragment = TaskDialogFragment.newInstance("Add New Task",null);
        taskDialogFragment.show(fm, "tag");
    }


    @Override
    public void onTaskSuccess() {
        list = db.getAllList();
        if (list.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            defaultTextView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            defaultTextView.setVisibility(View.VISIBLE);
        }
        recyclerTodoListAdapter.listUpdate(list);

    }
}
