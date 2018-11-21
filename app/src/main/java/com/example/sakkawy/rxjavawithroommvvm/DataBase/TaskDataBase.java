package com.example.sakkawy.rxjavawithroommvvm.DataBase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(entities = {TaskEntry.class},version = 1,exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class TaskDataBase extends RoomDatabase {
    private static final String TAG = "TaskDataBase";
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "todolist";
    private static TaskDataBase sInstance;

    public static TaskDataBase getsInstance(Context context) {
        if(sInstance == null){
            synchronized (LOCK){
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        TaskDataBase.class,
                        DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }

    public abstract TaskDao taskDao();
}
