package com.example.sakkawy.rxjavawithroommvvm.View;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.example.sakkawy.rxjavawithroommvvm.Adapter.TaskAdapter;
import com.example.sakkawy.rxjavawithroommvvm.DataBase.TaskEntry;
import com.example.sakkawy.rxjavawithroommvvm.DataCallBack;
import com.example.sakkawy.rxjavawithroommvvm.R;
import com.example.sakkawy.rxjavawithroommvvm.ViewModel.MainViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskAdapter.ItemClickListener,DataCallBack {

    private static final String TAG = "MainActivity";

    private RecyclerView mRecyclerView;
    private TaskAdapter mAdapter;

    MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mRecyclerView = findViewById(R.id.recyclerViewTasks);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new TaskAdapter(this,this);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(decoration);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // implement on delete
                int position = viewHolder.getAdapterPosition();
                List<TaskEntry> taskEntries = mAdapter.getmTaskEntries();
                viewModel.deleteTask(MainActivity.this,taskEntries.get(position));
            }
        }).attachToRecyclerView(mRecyclerView);

        FloatingActionButton fabButton = findViewById(R.id.fab);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddTaskActivity.class);
                startActivity(intent);
            }
        });

        setUpViewMode();
    }

    private void setUpViewMode(){
       /* viewModel.getAllTasks().observe(this, new Observer<List<TaskEntry>>() {
            @Override
            public void onChanged(@Nullable List<TaskEntry> taskEntries) {
                mAdapter.setTasks(taskEntries);
            }
        });*/

       viewModel.loadAllData(MainActivity.this);
    }

    @Override
    public void onItemClickListener(int id) {
        Intent intent = new Intent(MainActivity.this,AddTaskActivity.class);
        intent.putExtra(AddTaskActivity.EXTRA_TASK_ID,id);
        startActivity(intent);
    }

    @Override
    public void taskAdded() {

    }

    @Override
    public void taskDeleted() {
        Toast.makeText(MainActivity.this,"deleted",Toast.LENGTH_LONG).show();
    }

    @Override
    public void taskUpdated() {

    }

    @Override
    public void errorAdded() {

    }

    @Override
    public void loadAllTasks(List<TaskEntry> taskEntries) {
        mAdapter.setTasks(taskEntries);
        Toast.makeText(this,"data retrived",Toast.LENGTH_LONG).show();
    }
}
