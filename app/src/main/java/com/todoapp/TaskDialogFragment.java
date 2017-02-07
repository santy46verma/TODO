package com.todoapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.todoapp.database.DatabaseHelper;
import com.todoapp.model.TodoModel;
import com.todoapp.other.GlobalMethods;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.todoapp.database.DatabaseHelper.getInstance;

/**
 * Created by Sonu Verma on 2/6/2017.
 */
public class TaskDialogFragment extends DialogFragment {

    private EditText nameView, descriptionView;
    TextInputLayout nameLayout, descriptionLayout;
    TextView dateView;
    Calendar calendar;
    LinearLayout datelayout;
    Button submitBtn, deleteTaskBtn;
    Spinner taskStatusSpinner, taskTypeSpinner;
    ArrayAdapter<String> taskTypeAdapter, taskStatusAdapter;
    DatabaseHelper db;
    OnTaskStatus onTaskStatus;
    TodoModel model;


    boolean ADD_TASK = true;

    public TaskDialogFragment() {
    }

    public interface OnTaskStatus {
        void onTaskSuccess();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.onTaskStatus = (OnTaskStatus) activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }

    public static TaskDialogFragment newInstance(String title, TodoModel model) {
        TaskDialogFragment taskDialogFragment = new TaskDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putSerializable("model", model);
        taskDialogFragment.setArguments(bundle);
        return taskDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_new_item_fragment, container);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String title = getArguments().getString("title", "");
        model = (TodoModel) getArguments().getSerializable("model");

        if (model == null)
            ADD_TASK = true;
        else
            ADD_TASK = false;

        getDialog().setTitle(title);
        nameView = (EditText) view.findViewById(R.id.taskName);
        descriptionView = (EditText) view.findViewById(R.id.description);
        nameLayout = (TextInputLayout) view.findViewById(R.id.name_input_layout);
        descriptionLayout = (TextInputLayout) view.findViewById(R.id.description_input_layout);
        dateView = (TextView) view.findViewById(R.id.dateView);
        datelayout = (LinearLayout) view.findViewById(R.id.datelayout);
        submitBtn = (Button) view.findViewById(R.id.submitBtn);
        deleteTaskBtn = (Button) view.findViewById(R.id.deleteTaskBtn);
        taskStatusSpinner = (Spinner) view.findViewById(R.id.status);
        taskTypeSpinner = (Spinner) view.findViewById(R.id.type);
        calendar = Calendar.getInstance();
        db = getInstance(getActivity());
        taskTypeAdapter = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_textview, getActivity().getResources().getStringArray(R.array.tasktype));
        taskStatusAdapter = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_textview, getActivity().getResources().getStringArray(R.array.taskstatus));

        taskTypeSpinner.setAdapter(taskTypeAdapter);
        taskStatusSpinner.setAdapter(taskStatusAdapter);

        setListenerOnView();
        taskStatusSpinner.setVisibility(View.GONE);
        deleteTaskBtn.setVisibility(View.GONE);

        if (!ADD_TASK && model != null) {

            taskStatusSpinner.setVisibility(View.VISIBLE);
            deleteTaskBtn.setVisibility(View.VISIBLE);
            String name = model.getName();
            String description = model.getDescription();
            String type = model.getType();
            String status = model.getStatus();
            String selectedDate = model.getDueDate();

            nameView.setText(name);
            descriptionView.setText(description);
            String[] typearray = getActivity().getResources().getStringArray(R.array.tasktype);
            List<String> typeList = new ArrayList<String>(Arrays.asList(typearray));
            int taskTypeIndex = typeList.indexOf(type);
            taskTypeSpinner.setSelection(taskTypeIndex);

            String[] statusarray = getActivity().getResources().getStringArray(R.array.taskstatus);
            List<String> stausList = new ArrayList<String>(Arrays.asList(statusarray));
            int taskStatusIndex = stausList.indexOf(status);
            taskStatusSpinner.setSelection(taskStatusIndex);

            dateView.setText(selectedDate);
            submitBtn.setText("Update Task");


        }


    }

    private void setListenerOnView() {

        nameLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 2) {
                    nameLayout.setError("Atleast 2 character are required");
                    nameLayout.setErrorEnabled(true);
                } else
                    nameLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        descriptionLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 10) {
                    descriptionLayout.setError("Atleast 10 character are required");
                    descriptionLayout.setErrorEnabled(true);
                } else
                    descriptionLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });


        datelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateListner, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameView.getEditableText().toString();
                String descriptipn = descriptionView.getEditableText().toString();
                String type = taskTypeSpinner.getSelectedItem().toString();
                String status = taskStatusSpinner.getSelectedItem().toString();
                String selectedDate = dateView.getText().toString();

                if (validateDetails(name, descriptipn, type, selectedDate)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.US);
                    String currentDateandTime = sdf.format(new Date());
                    long id = -1;
                    if (ADD_TASK)
                        id = db.insertTodoList(new TodoModel(name, type, descriptipn, status, selectedDate, currentDateandTime));
                    else
                        id = db.updateTodoList(new TodoModel(model.getId(), name, type, descriptipn, status, selectedDate, currentDateandTime));
                    Log.i("Data", "" + id);
                    if (id > 0) {
                        GlobalMethods.showMessage(getActivity(), "Task Success");
                        dismiss();
                        onTaskStatus.onTaskSuccess();
                    } else
                        GlobalMethods.showMessage(getActivity(), "Some Error Occurs");
                }


            }
        });

        deleteTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (model != null && !ADD_TASK) {
                    long id = db.deleteTodoListItem(Integer.parseInt(model.getId()));
                    if (id > 0) {
                        GlobalMethods.showMessage(getActivity(), "Task Success");
                        dismiss();
                        onTaskStatus.onTaskSuccess();
                    } else
                        GlobalMethods.showMessage(getActivity(), "Some Error Occurs");
                }
            }
        });


    }

    private boolean validateDetails(String name, String descriptipn, String type, String selectedDate) {

        if (name.length() < 2) {
            GlobalMethods.showMessage(getActivity(), "Must be atleast 2 character");
            return false;
        } else if (descriptipn.length() < 2) {
            GlobalMethods.showMessage(getActivity(), "Must be atleast 10 character");
            return false;
        } else if (type.equalsIgnoreCase("Select Task Type")) {
            GlobalMethods.showMessage(getActivity(), "Select Task Type");
            return false;
        } else if (selectedDate.equalsIgnoreCase("")) {
            GlobalMethods.showMessage(getActivity(), "Choose Task Date");
            return false;
        }

        return true;
    }


    DatePickerDialog.OnDateSetListener dateListner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            // Setting in TextView
            String myFormat = "d MMM , yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            dateView.setText(sdf.format(calendar.getTime()));
        }

    };


}
