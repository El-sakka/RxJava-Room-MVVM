package com.example.sakkawy.rxjavawithroommvvm;

import com.example.sakkawy.rxjavawithroommvvm.DataBase.TaskEntry;

import java.util.List;

public interface DataCallBack {
    void taskAdded();
    void taskDeleted();
    void taskUpdated();
    void errorAdded();
    void loadAllTasks(List<TaskEntry> taskEntries);
}
