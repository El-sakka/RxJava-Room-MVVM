package com.example.sakkawy.rxjavawithroommvvm.ViewModel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.sakkawy.rxjavawithroommvvm.DataBase.TaskEntry;
import com.example.sakkawy.rxjavawithroommvvm.DataCallBack;
import com.example.sakkawy.rxjavawithroommvvm.TaskRepository;

public class AddTaskViewModel extends ViewModel {
    private TaskRepository mRepository;
    int taskId;

    public AddTaskViewModel(Application application,int taskId){
        mRepository = TaskRepository.getsInstance(application);
        this.taskId = taskId;
    }

    public LiveData<TaskEntry> getTaskById(){
       return mRepository.loadTaskById(taskId);
    }

    public void insertTask(DataCallBack dataCallBack, TaskEntry taskEntry){
        mRepository.addTask(dataCallBack,taskEntry);
    }

    public void updateTask(DataCallBack dataCallBack, TaskEntry taskEntry){
        mRepository.updateTask(dataCallBack,taskEntry);
    }

}
