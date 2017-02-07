package com.todoapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.todoapp.model.TodoModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sonu Verma on 2/6/2017.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "todoDB";
    private static final int DATABASE_VERSION = 1;


    private static final String TABLE_NAME = "todoListTable";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_TYPE = "type";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_STATUS = "status";
    private static final String KEY_DUE_DATE = "due_date";
    private static final String KEY_CREATED_DATE = "createdDate";

    private static DatabaseHelper databaseIntance = null;


    public static synchronized DatabaseHelper getInstance(Context context) {
        if (databaseIntance == null) {
            databaseIntance = new DatabaseHelper(context.getApplicationContext());
        }
        return databaseIntance;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                "(" + KEY_ID + " INTEGER PRIMARY KEY ," +
                KEY_NAME + " TEXT," +
                KEY_TYPE + " TEXT," +
                KEY_DESCRIPTION + " TEXT," +
                KEY_STATUS + " TEXT," +
                KEY_DUE_DATE + " TEXT," +
                KEY_CREATED_DATE + " TEXT" + ")";

        Log.d("Create query", CREATE_TABLE);
        db.execSQL(CREATE_TABLE);

    }

    public List<TodoModel> getAllList() {
        List<TodoModel> todoModelList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + TABLE_NAME +" order by createdDate desc ", null);
        try {
            if (cursor.moveToFirst()) {
                do {

                   /* model.setName(cursor.getString(1));
                    model.setType(cursor.getString(2));
                    model.setDescription(cursor.getString(3));
                    model.setStatus(cursor.getString(4));
                    model.setDueDate(cursor.getString(5));
                    model.setCreatedDate(cursor.getString(6));*/
                    TodoModel model = new TodoModel(cursor.getString(0),cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                            cursor.getString(5), cursor.getString(6));
                    todoModelList.add(model);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        cursor.close();
        return todoModelList;
    }


    public long insertTodoList(TodoModel model) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, model.getName());
        contentValues.put(KEY_TYPE, model.getType());
        contentValues.put(KEY_DESCRIPTION, model.getDescription());
        contentValues.put(KEY_STATUS, model.getStatus());
        contentValues.put(KEY_DUE_DATE, model.getDueDate());
        contentValues.put(KEY_CREATED_DATE, model.getCreatedDate());
        return db.insert(TABLE_NAME, null, contentValues);
    }

    public int updateTodoList(TodoModel model) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, model.getName());
        contentValues.put(KEY_TYPE, model.getType());
        contentValues.put(KEY_DESCRIPTION, model.getDescription());
        contentValues.put(KEY_STATUS, model.getStatus());
        contentValues.put(KEY_DUE_DATE, model.getDueDate());
        contentValues.put(KEY_CREATED_DATE, model.getCreatedDate());
        return db.update(TABLE_NAME, contentValues, KEY_ID + "=?", new String[]{String.valueOf(model.getId())});
    }

    public int deleteTodoListItem(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_NAME, KEY_ID + "=?", new String[]{String.valueOf(id)});
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
