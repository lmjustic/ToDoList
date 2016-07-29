package hackingforjustice.todolist;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends ListActivity {
    ArrayList<ToDoItem> todos = new ArrayList<>();

    EditText newName;
    EditText newDate;
    TimePicker newTime;

    EditText editName;
    EditText editDate;
    TimePicker editTime;

    DatabaseHandler db;

    int editPos = 0;
    ToDoItem edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);

        db.addToDoItem(new ToDoItem(this, "abc", "1/2/3", "2:12 AM"));
        todos = (ArrayList<ToDoItem>) db.getAllToDoItems(this);

        todos.add(new ToDoItem(this, "test", "1/2/2019", "3:15 PM"));

        setContentView(R.layout.activity_main);

        final ListAdapter listAdapter = createListAdapter();
        setListAdapter(listAdapter);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void onNewSave(View v) {
        newName = (EditText) findViewById(R.id.newNameInput);
        newDate = (EditText) findViewById(R.id.newDateInput);
        newTime = (TimePicker) findViewById(R.id.newTimePicker);

        ToDoItem newTodo;
        String name = newName.getText().toString().trim();
        String date = newDate.getText().toString().trim();

        if (date.equals("")) {
            newTodo = new ToDoItem(this, name);
        }
        else {
            int hour;
            int minute;
            int currentApiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1){
                hour = newTime.getHour();
                minute = newTime.getMinute();
            } else {
                hour = newTime.getCurrentHour();
                minute = newTime.getCurrentMinute();
            }
            newTodo = new ToDoItem(this, name, date, hour, minute);
        }
        todos.add(newTodo);

        final ListAdapter listAdapter = createListAdapter();
        setListAdapter(listAdapter);

        db.addToDoItem(newTodo);

        setContentView(R.layout.activity_main);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void onEditSave(View v) {
        editName = (EditText) findViewById(R.id.editNameInput);
        editDate = (EditText) findViewById(R.id.editDateInput);
        editTime = (TimePicker) findViewById(R.id.editTimePicker);

        edit.setName(editName.getText().toString().trim());
        edit.setDate(editDate.getText().toString().trim());

        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            edit.setTime(editTime.getHour(), editTime.getMinute());
        }
        else {
            edit.setTime(editTime.getCurrentHour(), editTime.getCurrentMinute());
        }

        final ListAdapter listAdapter = createListAdapter();
        setListAdapter(listAdapter);

        db.updateToDoItem(edit);

        setContentView(R.layout.activity_main);
    }

    public void onDelete(View v) {
        todos.remove(editPos);

        final ListAdapter listAdapter = createListAdapter();
        setListAdapter(listAdapter);

        db.deleteToDoItem(edit);

        setContentView(R.layout.activity_main);
    }

    public void onAddToDo(View v) {
        setContentView(R.layout.make_todo);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Object selectedToDo = getListAdapter().getItem(position);
        Toast.makeText(this, position + selectedToDo.toString(), Toast.LENGTH_SHORT).show();

        editPos = position;
        edit = todos.get(editPos);

        setContentView(R.layout.edit_todo);

        editName = (EditText) findViewById(R.id.editNameInput);
        editDate = (EditText) findViewById(R.id.editDateInput);
        editTime = (TimePicker) findViewById(R.id.editTimePicker);

        editName.setText(edit.getName());
        editDate.setText(edit.getDate());

        int savedHour = edit.getHour();
        if (!edit.getAM()) {
            savedHour += 12;
        }

        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            editTime.setHour(savedHour);
            editTime.setMinute(edit.getMinute());
        } else {
            editTime.setCurrentHour(savedHour);
            editTime.setCurrentMinute(edit.getMinute());
        }
    }



    private ListAdapter createListAdapter() {
        final String[] fromMapKey = new String[] {"text1", "text2"};
        final int[] toLayoutId = new int[] {android.R.id.text1, android.R.id.text2};
        final List<Map<String, String>> list = convertToListItems();

        return new SimpleAdapter(this, list,
                android.R.layout.simple_list_item_2, fromMapKey, toLayoutId);
    }

    private List<Map<String, String>> convertToListItems() {
        final List<Map<String, String>> listItem = new ArrayList<>(todos.size());

        for (final ToDoItem todo: todos) {
            final Map<String, String> listItemMap = new HashMap<>();
            listItemMap.put("text1", todo.getName());
            listItemMap.put("text2", todo.getDateTime());
            listItem.add(Collections.unmodifiableMap(listItemMap));
        }

        return Collections.unmodifiableList(listItem);
    }
}
