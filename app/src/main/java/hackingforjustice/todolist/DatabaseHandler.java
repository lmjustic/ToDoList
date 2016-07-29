package hackingforjustice.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luke on 7/25/2016
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    //private Context context;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "todoList";
    private static final String TABLE_TODO = "todo";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODOS_TABLE = "CREATE TABLE " + TABLE_TODO + "("
                + KEY_ID + " TEXT," + KEY_NAME + " INTEGER PRIMARY KEY,"
                + KEY_DATE + " TEXT," + KEY_TIME + " TEXT" + ")";
        db.execSQL(CREATE_TODOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);

        onCreate(db);
    }

    public void addToDoItem(ToDoItem todo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues(3);
        values.put(KEY_NAME, todo.getName());
        values.put(KEY_DATE, todo.getDate());
        values.put(KEY_TIME, todo.getTimeDB());

        db.insert(TABLE_TODO, null, values);
        db.close();
    }

    public ToDoItem getToDoItem(int id, Context context) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TODO, new String[] { KEY_ID,
                        KEY_NAME, KEY_DATE, KEY_TIME }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ToDoItem todo = new ToDoItem(context, Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));

        return todo;
    }

    public List<ToDoItem> getAllToDoItems(Context context) {
        List<ToDoItem> contactList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_TODO;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ToDoItem toDoItem = new ToDoItem(context);
                toDoItem.setId(Integer.parseInt(cursor.getString(0)));
                toDoItem.setName(cursor.getString(1));
                toDoItem.setDate(cursor.getString(2));
                toDoItem.setTime(cursor.getString(3));
                contactList.add(toDoItem);
            } while (cursor.moveToNext());
        }

        return contactList;
    }

    public int getToDoItemsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TODO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    public int updateToDoItem(ToDoItem todo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, todo.getName());
        values.put(KEY_DATE, todo.getDate());
        values.put(KEY_TIME, todo.getTimeDB());

        return db.update(TABLE_TODO, values, KEY_ID + " = ?",
                new String[] { String.valueOf(todo.getId()) });
    }

    public void deleteToDoItem(ToDoItem todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODO, KEY_ID + " = ?",
                new String[] { String.valueOf(todo.getId()) });
        db.close();
    }
}
