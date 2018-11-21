package com.example.sakkawy.rxjavawithroommvvm.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.sakkawy.rxjavawithroommvvm.DataBase.TaskEntry;
import com.example.sakkawy.rxjavawithroommvvm.DataCallBack;
import com.example.sakkawy.rxjavawithroommvvm.TaskRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    LiveData<List<TaskEntry>> tasks;
    private final TaskRepository mRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        mRepository = TaskRepository.getsInstance(application);
    }
    public void deleteTask(DataCallBack dataCallBack , TaskEntry taskEntry){
        mRepository.deleteTask(dataCallBack,taskEntry);
    }

    public void loadAllData(DataCallBack dataCallBack){
        mRepository.loadDataObserver(dataCallBack);
    }

    public LiveData<List<TaskEntry>> getAllTasks(){
        return mRepository.loadTasks();

    }
}
