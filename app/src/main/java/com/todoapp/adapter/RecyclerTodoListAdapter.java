package com.todoapp.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.todoapp.TaskDialogFragment;
import com.todoapp.R;
import com.todoapp.model.TodoModel;

import java.util.List;

/**
 * Created by Sonu Verma on 2/7/2017.
 */
public class RecyclerTodoListAdapter extends RecyclerView.Adapter<RecyclerTodoListAdapter.RecyclerTodoViewHolder> {

    List<TodoModel> todoModelList;
    Context context;

    public RecyclerTodoListAdapter(Context context, List<TodoModel> todoModelList) {
        this.context = context;
        this.todoModelList = todoModelList;
    }

    @Override
    public RecyclerTodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_todolist_layout, parent, false);
        return new RecyclerTodoViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final RecyclerTodoViewHolder holder, final int position) {

        String name = todoModelList.get(position).getName();
        String description = todoModelList.get(position).getDescription();
        String status = todoModelList.get(position).getStatus();

        holder.name.setText(name);
        holder.description.setText(description);
        holder.status.setText(status);
        if (status.equalsIgnoreCase("Completed")) {
            holder.status.setTextColor(context.getResources().getColor(R.color.textColorGreen));
        } else
            holder.status.setTextColor(context.getResources().getColor(R.color.textColorRed));

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity act = (Activity) context;
                FragmentManager fm = act.getFragmentManager();
                TodoModel model = todoModelList.get(position);
                TaskDialogFragment taskDialogFragment = TaskDialogFragment.newInstance("Edit Task", model);
                taskDialogFragment.show(fm, "tag");
            }
        });

    }

    @Override
    public int getItemCount() {
        return todoModelList.size();
    }

    public void listUpdate(List<TodoModel> list) {
        this.todoModelList = list;
        notifyDataSetChanged();
    }

    public class RecyclerTodoViewHolder extends RecyclerView.ViewHolder {

        public TextView name, description, status;
        CardView card_view;

        public RecyclerTodoViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.taskName);
            description = (TextView) itemView.findViewById(R.id.taskDescription);
            status = (TextView) itemView.findViewById(R.id.taskStaus);
            card_view = (CardView) itemView.findViewById(R.id.card_view);
        }


    }
}
