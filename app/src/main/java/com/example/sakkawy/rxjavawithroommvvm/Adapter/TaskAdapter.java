package com.example.sakkawy.rxjavawithroommvvm.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sakkawy.rxjavawithroommvvm.DataBase.TaskEntry;
import com.example.sakkawy.rxjavawithroommvvm.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private static final String DATE_FORMAT = "dd/mm//yy";

    final private ItemClickListener mItemClickListener;
    private List<TaskEntry> mTaskEntries;
    private Context mContext;
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    public TaskAdapter(ItemClickListener mItemClickListener, Context mContext) {
        this.mItemClickListener = mItemClickListener;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.task_layout,parent,false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        TaskEntry taskEntry = mTaskEntries.get(position);
        String description = taskEntry.getDescription();
        int priority = taskEntry.getPriority();
        String updatedAt = dateFormat.format(taskEntry.getUpdateAt());

        holder.taskDescriptionView.setText(description);
        holder.updatedAtView.setText(updatedAt);

        String priorityString = "" + priority; // converts int to String
        holder.priorityView.setText(priorityString);
    }

    @Override
    public int getItemCount() {
        return (mTaskEntries == null) ? 0 : mTaskEntries.size();
    }


    public List<TaskEntry> getmTaskEntries() {
        return mTaskEntries;
    }

    public void setTasks(List<TaskEntry> taskEntries){
        mTaskEntries = taskEntries;
        notifyDataSetChanged();
    }

    public interface ItemClickListener{
        void onItemClickListener(int id);
    }

    class TaskViewHolder extends RecyclerView.ViewHolder{

        TextView taskDescriptionView;
        TextView updatedAtView;
        TextView priorityView;

        public TaskViewHolder(View itemView) {
            super(itemView);
            taskDescriptionView = itemView.findViewById(R.id.taskDescription);
            updatedAtView = itemView.findViewById(R.id.taskUpdatedAt);
            priorityView = itemView.findViewById(R.id.priorityTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int elementId = mTaskEntries.get(getAdapterPosition()).getId();
                    mItemClickListener.onItemClickListener(elementId);
                }
            });
        }
    }
}
