package com.example.sakkawy.rxjavawithroommvvm;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.example.sakkawy.rxjavawithroommvvm.DataBase.TaskDao;
import com.example.sakkawy.rxjavawithroommvvm.DataBase.TaskDataBase;
import com.example.sakkawy.rxjavawithroommvvm.DataBase.TaskEntry;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TaskRepository {
    private static final String TAG = "TaskRepository";
    private static final Object LOCK = new Object();
    private TaskDataBase mDb;
    private final TaskDao taskDao;
    private static TaskRepository sInstance;

    private TaskRepository(Application application) {
        mDb = TaskDataBase.getsInstance(application.getApplicationContext());
        taskDao = mDb.taskDao();
    }

    public synchronized static TaskRepository getsInstance(Application application) {
        if(sInstance == null){
            synchronized (LOCK){
                sInstance = new TaskRepository(application);
                Log.d(TAG, "getsInstance: made new Instance of repo");
            }
        }
        return sInstance;
    }

    public void addTask(final DataCallBack dataCallBack, final TaskEntry taskEntry){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                taskDao.insertTask(taskEntry);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        dataCallBack.taskAdded();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dataCallBack.errorAdded();
                    }
                });
    }

    public void deleteTask(final DataCallBack dataCallBack, final TaskEntry taskEntry){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                taskDao.deleteTask(taskEntry);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        dataCallBack.taskDeleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dataCallBack.errorAdded();
                    }
                });
    }


    public void updateTask(final DataCallBack dataCallBack, final TaskEntry taskEntry){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                taskDao.deleteTask(taskEntry);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        dataCallBack.taskUpdated();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dataCallBack.errorAdded();
                    }
                });
    }

    public void loadDataObserver(final DataCallBack dataCallBack){
        taskDao.loadAllTasksObeservale()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<TaskEntry>>() {
                    @Override
                    public void accept(List<TaskEntry> taskEntries) throws Exception {
                        dataCallBack.loadAllTasks(taskEntries);
                    }
                });
    }

    public LiveData<List<TaskEntry>> loadTasks(){
        return taskDao.loadAllTasks();
    }

    public LiveData<TaskEntry> loadTaskById(int id){
        return taskDao.loadTaskById(id);
    }
}
