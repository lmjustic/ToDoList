package hackingforjustice.todolist;

import android.content.Context;

import java.util.List;

/**
 * Created by Luke on 7/25/2016
 */
public class ToDoItem {
    private Context context;
    private int id = 0;
    private int month;
    private int day;
    private int year;
    private String date = "";
    private int hour;
    private int minute;
    private boolean am;
    private String time = "";
    private String name = "";

    public ToDoItem(Context c) {
        context = c;
        month = 1;
        day = 1;
        year = 2000;
        date = "";
        hour = 12;
        minute = 0;
        am = true;
        time = "";
        name = "";
        setNewId();
    }

    public ToDoItem(Context c, String name) {
        context = c;
        month = 1;
        day = 1;
        year = 2000;
        date = "";
        hour = 12;
        minute = 0;
        am = true;
        time = "";
        this.name = name;
        setNewId();
    }

    public ToDoItem(Context c, int id, String name) {
        context = c;
        this.id = id;
        month = 1;
        day = 1;
        year = 2000;
        date = "";
        hour = 12;
        minute = 0;
        am = true;
        time = "";
        this.name = name;
    }

    public ToDoItem(Context c, String name, String date, String time) {
        context = c;
        this.date = date;
        this.time = time;
        this.name = name;
        setMonthDayYear();
        setHourMinute();
        setNewId();
    }

    public ToDoItem(Context c, int id, String name, String date, String time) {
        context = c;
        this.id = id;
        this.date = date;
        this.time = time;
        this.name = name;
        setMonthDayYear();
        setHourMinute();
    }

    public ToDoItem(Context c, String name, String date, int hour, int minute) {
        context = c;
        this.date = date;
        this.am = true;
        if (hour > 12) {
            hour -= 12;
            this.am = false;
        }
        this.hour = hour;
        this.minute = minute;
        this.name = name;
        setMonthDayYear();
        setTime();
    }

    public ToDoItem(Context c, String name, int month, int day, int year, int hour, int minute, boolean am) {
        context = c;
        this.month = month;
        this.day = day;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
        this.am = am;
        this.name = name;
        setDate();
        setTime();
        setNewId();
    }

    private void setNewId() {
        DatabaseHandler db = new DatabaseHandler(context);
        List<ToDoItem> currentItems = db.getAllToDoItems(context);
        if (!currentItems.isEmpty()) {
            id = currentItems.get(currentItems.size() - 1).getId() + 1;
        }
        else {
            id = 0;
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    private void setMonthDayYear() {
        if (date.equals("")) {
            month = 1;
            day = 1;
            year = 2000;
        }
        else {
            String[] ints = date.split("/");
            month = Integer.parseInt(ints[0]);
            day = Integer.parseInt(ints[1]);
            year = Integer.parseInt(ints[2]);
        }
    }

    private void setDate() {
        date = month + "/";
        date += day + "/" + year;
    }

    public void setDate(String date) {
        this.date = date;
        setMonthDayYear();
    }

    public void setDate(int month, int day, int year) {
        this.month = month;
        this.day = day;
        this.year = year;
        setDate();
    }

    private void setHourMinute() {
        if (time.equals("")) {
            hour = 12;
            minute = 0;
            am = true;
        }
        else {
            String[] colon = time.split(":");
            hour = Integer.parseInt(colon[0]);
            String[] space = colon[1].split(" ");
            minute = Integer.parseInt(space[0]);
            am = space[1].equals("AM");
        }
    }

    private void setTime() {
        time = hour + ":";
        if (minute < 10) {
            time += "0";
        }
        time += minute + " ";
        if (am) {
            time += "AM";
        }
        else {
            time += "PM";
        }
    }

    public void setTime(String time) {
        this.time = time;
        setHourMinute();
    }

    public void setTime(int hour, int minute, boolean am) {
        this.hour = hour;
        this.minute = minute;
        this.am = am;
        setTime();
    }

    public void setTime(int hour, int minute) {
        this.am = true;
        if (hour > 12) {
            hour -= 12;
            this.am = false;
        }
        this.hour = hour;
        this.minute = minute;
        setTime();
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getTimeDB() {
        String dbTime;
        if (hour > 12) {
            dbTime = (hour + 12) + ":";
        }
        else {
            dbTime = hour + ":";
        }
        if (minute < 10) {
            dbTime += "0";
        }
        dbTime += minute;
        return dbTime;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public boolean getAM() {
        return am;
    }

    public String getDateTime() {
        if (date.equals("")) {
            return "";
        }
        return date + " at " + time;
    }
}
