package com.example.sakkawy.rxjavawithroommvvm.View;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.sakkawy.rxjavawithroommvvm.DataBase.TaskEntry;
import com.example.sakkawy.rxjavawithroommvvm.DataCallBack;
import com.example.sakkawy.rxjavawithroommvvm.R;
import com.example.sakkawy.rxjavawithroommvvm.ViewModel.AddTaskViewModel;
import com.example.sakkawy.rxjavawithroommvvm.ViewModel.AddTaskViewModelFactory;

import java.util.Date;
import java.util.List;

public class AddTaskActivity extends AppCompatActivity implements DataCallBack {

    public static final String EXTRA_TASK_ID = "extraTaskId";
    // Extra for the task ID to be received after rotation
    public static final String INSTANCE_TASK_ID = "instanceTaskId";
    // Constants for priority
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_LOW = 3;
    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_TASK_ID = -1;
    // Constant for logging
    private static final String TAG = AddTaskActivity.class.getSimpleName();
    // Fields for views
    EditText mEditText;
    RadioGroup mRadioGroup;
    Button mButton;

    private int mTaskId = DEFAULT_TASK_ID;

    AddTaskViewModelFactory factory;
    AddTaskViewModel viewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        initViews();

        factory = new AddTaskViewModelFactory(getApplication(),mTaskId);
        viewModel = ViewModelProviders.of(this,factory).get(AddTaskViewModel.class);
        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)) {
            mTaskId = savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TASK_ID);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_TASK_ID)) {
            mButton.setText(R.string.update_button);
            if (mTaskId == DEFAULT_TASK_ID) {
                // populate the UI
                mTaskId = intent.getIntExtra(EXTRA_TASK_ID,DEFAULT_TASK_ID);
              viewModel.getTaskById().observe(this, new Observer<TaskEntry>() {
                  @Override
                  public void onChanged(@Nullable TaskEntry taskEntry) {
                      viewModel.getTaskById().removeObserver(this);
                      populateUI(taskEntry);
                  }
              });

            }
        }

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_TASK_ID, mTaskId);
        super.onSaveInstanceState(outState);
    }

    private void initViews() {
        mEditText = findViewById(R.id.editTextTaskDescription);
        mRadioGroup = findViewById(R.id.radioGroup);

        mButton = findViewById(R.id.saveButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });
    }
    private void populateUI(TaskEntry task) {
        if(task == null){
            return;
        }
        mEditText.setText(task.getDescription());
        setPriorityInViews(task.getPriority());

    }
    public void onSaveButtonClicked() {
        // Not yet implemented
        String description = mEditText.getText().toString();
        int priority = getPriorityFromViews();
        Date date = new Date();

        final TaskEntry taskEntry = new TaskEntry(description,priority,date);
        if(mTaskId == DEFAULT_TASK_ID) {
            viewModel.insertTask(AddTaskActivity.this,taskEntry);
            // mDb.taskDao().insertTask(taskEntry);
        }
        else {
            taskEntry.setId(mTaskId);
            //mDb.taskDao().updataTask(taskEntry);
            viewModel.updateTask(AddTaskActivity.this,taskEntry);
        }
        finish();
    }

    public int getPriorityFromViews() {
        int priority = 1;
        int checkedId = ((RadioGroup) findViewById(R.id.radioGroup)).getCheckedRadioButtonId();
        switch (checkedId) {
            case R.id.radButton1:
                priority = PRIORITY_HIGH;
                break;
            case R.id.radButton2:
                priority = PRIORITY_MEDIUM;
                break;
            case R.id.radButton3:
                priority = PRIORITY_LOW;
        }
        return priority;
    }
    public void setPriorityInViews(int priority) {
        switch (priority) {
            case PRIORITY_HIGH:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton1);
                break;
            case PRIORITY_MEDIUM:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton2);
                break;
            case PRIORITY_LOW:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton3);
        }
    }

    @Override
    public void taskAdded() {
        Toast.makeText(AddTaskActivity.this,"Task Added",Toast.LENGTH_LONG).show();
    }

    @Override
    public void taskDeleted() {

    }

    @Override
    public void taskUpdated() {
        Toast.makeText(AddTaskActivity.this,"Task updated",Toast.LENGTH_LONG).show();
    }

    @Override
    public void errorAdded() {
        Toast.makeText(AddTaskActivity.this,"add TaskError",Toast.LENGTH_LONG).show();
    }

    @Override
    public void loadAllTasks(List<TaskEntry> taskEntries) {

    }
}
