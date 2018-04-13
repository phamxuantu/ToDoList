package com.toanutc.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by sev_user on 10-Apr-18.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String db_name = "TODO_Manager",
            tb_todo = "TODO",
            col_idTodo = "id",
            col_checkTodo = "checkbox",
            col_nameTodo = "name",
            col_timeTodo = "time",
            col_categoryTodo = "category",
            col_description = "description",
            col_imgAlert = "imageAlert";


    public SQLiteHelper(Context context, int version) {
        super(context, db_name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + tb_todo + "(" + col_idTodo + " INTEGER PRIMARY KEY AUTOINCREMENT, " + col_checkTodo + " TEXT, " + col_nameTodo + " TEXT, " + col_timeTodo + " TEXT NULL, " + col_categoryTodo + " TEXT, " + col_description + " TEXT NULL, " + col_imgAlert + " INTEGER NULL);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tb_todo);
        onCreate(db);
    }

    public void addToDo(ToDo toDo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

//        values.put(col_idTodo, toDo.getId());
        values.put(col_checkTodo, String.valueOf(toDo.getCheck()));
        values.put(col_nameTodo, toDo.getName());
        values.put(col_timeTodo, toDo.getTime());
        values.put(col_categoryTodo, toDo.getCategories());
        values.put(col_description, toDo.getDescription());
        values.put(col_imgAlert, toDo.getImageAlert());

        db.insert(tb_todo, null, values);
        db.close();
    }

    public ToDo getToDo(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(tb_todo, new String[] { col_idTodo, col_checkTodo,
                        col_nameTodo, col_timeTodo, col_categoryTodo, col_description, col_imgAlert }, col_idTodo + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ToDo toDo = new ToDo(Integer.parseInt(cursor.getString(0)), cursor.getString(1).equals("true") ? true : false, cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), Integer.parseInt(cursor.getString(6)));
        return toDo;
    }

    public List<ToDo> getAllToDos() {

        List<ToDo> todoList = new ArrayList<ToDo>();
        String selectQuery = "SELECT * FROM " + tb_todo;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ToDo toDo = new ToDo();
                toDo.setId(Integer.parseInt(cursor.getString(0)));
                toDo.setCheck(cursor.getString(1).equals("true") ? true : false);
                toDo.setName(cursor.getString(2));
                toDo.setTime(cursor.getString(3));
                toDo.setCategories(cursor.getString(4));
                toDo.setDescription(cursor.getString(5));
                toDo.setImageAlert(Integer.parseInt(cursor.getString(6)));

                todoList.add(toDo);
            } while (cursor.moveToNext());
        }

        return todoList;
    }

    public List<ToDo> getToDosDone() {

        List<ToDo> todoList = new ArrayList<ToDo>();
        String selectQuery = "SELECT * FROM " + tb_todo + " WHERE " + col_checkTodo + " = 'true';";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ToDo toDo = new ToDo();
                toDo.setId(Integer.parseInt(cursor.getString(0)));
                toDo.setCheck(true);
                toDo.setName(cursor.getString(2));
                toDo.setTime(cursor.getString(3));
                toDo.setCategories(cursor.getString(4));
                toDo.setDescription(cursor.getString(5));
                toDo.setImageAlert(Integer.parseInt(cursor.getString(6)));

                todoList.add(toDo);
            } while (cursor.moveToNext());
        }

        return todoList;
    }

    public List<ToDo> getToDosNotDone() {

        List<ToDo> todoList = new ArrayList<ToDo>();
        String selectQuery = "SELECT * FROM " + tb_todo + " WHERE " + col_checkTodo + " = 'false';";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ToDo toDo = new ToDo();
                toDo.setId(Integer.parseInt(cursor.getString(0)));
                toDo.setCheck(false);
                toDo.setName(cursor.getString(2));
                toDo.setTime(cursor.getString(3));
                toDo.setCategories(cursor.getString(4));
                toDo.setDescription(cursor.getString(5));
                toDo.setImageAlert(Integer.parseInt(cursor.getString(6)));

                todoList.add(toDo);
            } while (cursor.moveToNext());
        }

        return todoList;
    }

    public int updateTodo(ToDo todo) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(col_checkTodo, String.valueOf(todo.getCheck()));
        values.put(col_nameTodo, todo.getName());
        values.put(col_timeTodo, todo.getTime());
        values.put(col_categoryTodo, todo.getCategories());
        values.put(col_description, todo.getDescription());
        values.put(col_imgAlert, todo.getImageAlert());

        return db.update(tb_todo, values, col_idTodo + " = ?",
                new String[]{String.valueOf(todo.getId())});
    }

    public void deleteTodo(int id) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tb_todo, col_idTodo + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    public int getTodoCount() {

        String countQuery = "SELECT * FROM " + tb_todo;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        return count;
    }

    public void createDefaultTodoIfNeed()  {
        int count = this.getTodoCount();
        if(count ==0 ) {
            ToDo todo1 = new ToDo(1, false, "Sinh nhật", "10/04/2018 09:30", "Bạn bè", "Sinh nhật bạn thân", R.drawable.clock);
            ToDo todo2 = new ToDo(2, false, "Đám cưới", "10/04/2018 12:00", "Gia đình", "Đám cưới anh", R.drawable.clock);
            ToDo todo3 = new ToDo(3,true, "Đi chơi", "", "Bạn bè", "Hằng ngày",0);
            this.addToDo(todo1);
            this.addToDo(todo2);
            this.addToDo(todo3);
        }
    }

//    public List<ToDo> getTodoSearch(CharSequence s, CharSequence check) {
//
//        List<ToDo> todoList = new ArrayList<ToDo>();
//        String selectQuery;
//        if (check.equals("")) {
//            selectQuery = "SELECT * FROM " + tb_todo + " WHERE " + col_nameTodo + " LIKE '" + s + "%'";
//        } else {
//            selectQuery = "SELECT * FROM " + tb_todo + " WHERE " + col_checkTodo + " = " + check + " AND " + col_nameTodo + " LIKE '" + s + "%'";
//        }
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                ToDo toDo = new ToDo();
//                toDo.setId(Integer.parseInt(cursor.getString(0)));
//                toDo.setCheck(cursor.getString(1).equals("true") ? true : false);
//                toDo.setName(cursor.getString(2));
//                toDo.setTime(cursor.getString(3));
//                toDo.setCategories(cursor.getString(4));
//                toDo.setDescription(cursor.getString(5));
//                toDo.setImageAlert(Integer.parseInt(cursor.getString(6)));
//
//                todoList.add(toDo);
//            } while (cursor.moveToNext());
//        }
//
//        return todoList;
//    }
}
