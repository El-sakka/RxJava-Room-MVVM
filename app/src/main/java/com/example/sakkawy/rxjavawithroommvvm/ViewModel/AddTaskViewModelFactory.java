package com.example.sakkawy.rxjavawithroommvvm.ViewModel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class AddTaskViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final Application application;
    private int mTaskId;


    public AddTaskViewModelFactory(Application application, int mTaskId) {
        this.application = application;
        this.mTaskId = mTaskId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddTaskViewModel(application,mTaskId);
    }
}
