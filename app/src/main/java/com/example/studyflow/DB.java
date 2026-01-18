package com.example.studyflow;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "studyflow.db";
    private static final int DATABASE_VERSION = 4;
    public static final String table_lectures = "lectures";
    public static final String col_lec_id = "id";
    public static final String col_subject = "subject";
    public static final String col_start_time = "startTime";
    public static final String col_end_time = "endTime";
    public static final String col_room = "room";
    public static final String col_teacher = "teacher";
    public static final String col_day_of_week = "dayOfWeek";

    public static final String col_lec_notification_time = "notification_time";


    public static final String table_tasks = "tasks";
    public static final String col_task_id = "id";
    public static final String col_task_title = "title";
    public static final String col_task_description = "description";
    public static final String col_task_lecture_name = "lecture_name";
    public static final String col_task_deadline = "deadline";
    public static final String col_task_priority = "priority";
    public static final String col_task_is_completed = "is_completed";
    public static final String col_task_created_at = "created_at";
    public static final String col_task_completed_at = "completed_at";
    public static final String col_task_notification_time = "notification_time";








    public DB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }





    @Override
    public void onCreate(SQLiteDatabase db) {

        String create_lectures_table = "CREATE TABLE " + table_lectures + " (" +
                col_lec_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                col_subject + " TEXT, " +
                col_start_time + " TEXT, " +
                col_end_time + " TEXT, " +
                col_room + " TEXT, " +
                col_teacher + " TEXT, " +
                col_day_of_week + " INTEGER, " +
                col_lec_notification_time + " INTEGER DEFAULT 0" +
                ");";
        db.execSQL(create_lectures_table);



        String create_tasks_table = "create table " + table_tasks + " (" +
                col_task_id + " integer primary key autoincrement, " +
                col_task_title + " text not null, " +
                col_task_description + " text, " +
                col_task_lecture_name + " text, " +
                col_task_deadline + " integer not null, " +
                col_task_priority + " integer default 2, " +
                col_task_is_completed + " integer default 0, " +
                col_task_created_at + " integer not null, " +
                col_task_completed_at + " integer, " +
                col_task_notification_time + " integer default -1" +
                ");";
        db.execSQL(create_tasks_table);
    }







    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + table_lectures);
        db.execSQL("DROP TABLE IF EXISTS " + table_tasks);
        onCreate(db);

    }


    public long insertLecture(LectureItem lecture) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(col_subject, lecture.getSubject());
        cv.put(col_start_time, lecture.getStartTime());
        cv.put(col_end_time, lecture.getEndTime());
        cv.put(col_room, lecture.getRoom());
        cv.put(col_teacher, lecture.getTeacher());
        cv.put(col_day_of_week, lecture.getDayOfWeek());
        cv.put(col_lec_notification_time, lecture.getNotificationTime());

        long rowId = db.insert(table_lectures, null, cv);
        db.close();
        return rowId;
    }

    @SuppressLint("Range")
    public List<LectureItem> getLecturesByDay(int dayOfWeek) {
        List<LectureItem> lectureList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(table_lectures, null, col_day_of_week + " = ?", new String[]{String.valueOf(dayOfWeek)}, null, null, col_start_time + " ASC");

        if (cursor.moveToFirst()) {
            do {
                lectureList.add(new LectureItem(
                        cursor.getInt(cursor.getColumnIndex(col_lec_id)),
                        cursor.getString(cursor.getColumnIndex(col_subject)),
                        cursor.getString(cursor.getColumnIndex(col_start_time)),
                        cursor.getString(cursor.getColumnIndex(col_end_time)),
                        cursor.getString(cursor.getColumnIndex(col_room)),
                        cursor.getString(cursor.getColumnIndex(col_teacher)),
                        cursor.getInt(cursor.getColumnIndex(col_day_of_week)),
                        cursor.getLong(cursor.getColumnIndex(col_lec_notification_time))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lectureList;
    }
    public int updateLecture(LectureItem lecture) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(col_subject, lecture.getSubject());
        cv.put(col_start_time, lecture.getStartTime());
        cv.put(col_end_time, lecture.getEndTime());
        cv.put(col_room, lecture.getRoom());
        cv.put(col_teacher, lecture.getTeacher());
        cv.put(col_day_of_week, lecture.getDayOfWeek());
        cv.put(col_lec_notification_time, lecture.getNotificationTime());

        int updatedRows = db.update(table_lectures, cv, col_lec_id + " = ?",
                new String[]{String.valueOf(lecture.getId())});
        db.close();
        return updatedRows;
    }


    public int deleteLecture(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(table_lectures, col_lec_id + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return deletedRows;
    }

    public  long insertTask(Task task){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(col_task_title, task.getTitle());
        cv.put(col_task_description, task.getDescription());
        cv.put(col_task_lecture_name, task.getLectureName());
        cv.put(col_task_deadline, task.getDeadline());
        cv.put(col_task_priority, task.getPriority());
        cv.put(col_task_is_completed, task.getIsCompleted());
        cv.put(col_task_created_at, task.getCreatedAt());
        cv.put(col_task_notification_time, task.getNotificationTime());
        long rowId= db.insert(table_tasks, null, cv);
         db.close();
         return rowId;
    }

         @SuppressLint("Range")
         public  List<Task> getAllTasks(){
        List<Task> taskList=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();

             Cursor cursor = db.query(table_tasks, null, null, null, null, null, col_task_deadline + " asc");

             if (cursor.moveToFirst()) {
                 do {
                     Long completedAt = null;
                     if (!cursor.isNull(cursor.getColumnIndex(col_task_completed_at))) {
                         completedAt = cursor.getLong(cursor.getColumnIndex(col_task_completed_at));
                     }

                     taskList.add(new Task(
                             cursor.getInt(cursor.getColumnIndex(col_task_id)),
                             cursor.getString(cursor.getColumnIndex(col_task_title)),
                             cursor.getString(cursor.getColumnIndex(col_task_description)),
                             cursor.getString(cursor.getColumnIndex(col_task_lecture_name)),
                             cursor.getLong(cursor.getColumnIndex(col_task_deadline)),
                             cursor.getInt(cursor.getColumnIndex(col_task_priority)),
                             cursor.getInt(cursor.getColumnIndex(col_task_is_completed)),
                             cursor.getLong(cursor.getColumnIndex(col_task_created_at)),
                             completedAt,
                             cursor.getLong(cursor.getColumnIndex(col_task_notification_time))
                     ));
                 } while (cursor.moveToNext());
             }
             cursor.close();
             db.close();
             return taskList;
         }
    @SuppressLint("Range")
    public List<Task> getTasksByStatus(int isCompleted, String orderBy) {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(table_tasks, null, col_task_is_completed + " = ?",
                new String[]{String.valueOf(isCompleted)}, null, null, orderBy);

        if (cursor.moveToFirst()) {
            do {
                Long completedAt = null;
                if (!cursor.isNull(cursor.getColumnIndex(col_task_completed_at))) {
                    completedAt = cursor.getLong(cursor.getColumnIndex(col_task_completed_at));
                }

                taskList.add(new Task(
                        cursor.getInt(cursor.getColumnIndex(col_task_id)),
                        cursor.getString(cursor.getColumnIndex(col_task_title)),
                        cursor.getString(cursor.getColumnIndex(col_task_description)),
                        cursor.getString(cursor.getColumnIndex(col_task_lecture_name)),
                        cursor.getLong(cursor.getColumnIndex(col_task_deadline)),
                        cursor.getInt(cursor.getColumnIndex(col_task_priority)),
                        cursor.getInt(cursor.getColumnIndex(col_task_is_completed)),
                        cursor.getLong(cursor.getColumnIndex(col_task_created_at)),
                        completedAt,
                        cursor.getLong(cursor.getColumnIndex(col_task_notification_time))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return taskList;
    }


    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(col_task_title, task.getTitle());
        cv.put(col_task_description, task.getDescription());
        cv.put(col_task_lecture_name, task.getLectureName());
        cv.put(col_task_deadline, task.getDeadline());
        cv.put(col_task_priority, task.getPriority());
        cv.put(col_task_is_completed, task.getIsCompleted());

        if (task.getCompletedAt() != null) {
            cv.put(col_task_completed_at, task.getCompletedAt());
        }
        cv.put(col_task_notification_time, task.getNotificationTime());

        int updatedRows = db.update(table_tasks, cv, col_task_id + " = ?",
                new String[]{String.valueOf(task.getId())});
        db.close();
        return updatedRows;
    }

    public int updateTaskStatus(int taskId, int isCompleted) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(col_task_is_completed, isCompleted);

        if (isCompleted == 1) {
            cv.put(col_task_completed_at, System.currentTimeMillis());
        } else {
            cv.putNull(col_task_completed_at);
        }

        int updatedRows = db.update(table_tasks, cv, col_task_id + " = ?",
                new String[]{String.valueOf(taskId)});
        db.close();
        return updatedRows;
    }

    @SuppressLint("Range")
    public Task getTaskById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(table_tasks, null, col_task_id + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Long completedAt = cursor.isNull(cursor.getColumnIndex(col_task_completed_at)) ? null : cursor.getLong(cursor.getColumnIndex(col_task_completed_at));
            Task task = new Task(
                    cursor.getInt(cursor.getColumnIndex(col_task_id)),
                    cursor.getString(cursor.getColumnIndex(col_task_title)),
                    cursor.getString(cursor.getColumnIndex(col_task_description)),
                    cursor.getString(cursor.getColumnIndex(col_task_lecture_name)),
                    cursor.getLong(cursor.getColumnIndex(col_task_deadline)),
                    cursor.getInt(cursor.getColumnIndex(col_task_priority)),
                    cursor.getInt(cursor.getColumnIndex(col_task_is_completed)),
                    cursor.getLong(cursor.getColumnIndex(col_task_created_at)),
                    completedAt,
                    cursor.getLong(cursor.getColumnIndex(col_task_notification_time))
            );
            cursor.close();
            return task;
        }
        return null;
    }

    public int deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(table_tasks, col_task_id + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return deletedRows;
    }


    @SuppressLint("Range")
    public List<LectureItem> searchLectures(String query) {
        List<LectureItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // ვიყენებთ LIKE ოპერატორს, რომ ტექსტის ნაწილიც იპოვოს
        Cursor cursor = db.query(table_lectures, null, col_subject + " LIKE ?",
                new String[]{"%" + query + "%"}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                list.add(new LectureItem(
                        cursor.getInt(cursor.getColumnIndex(col_lec_id)),
                        cursor.getString(cursor.getColumnIndex(col_subject)),
                        cursor.getString(cursor.getColumnIndex(col_start_time)),
                        cursor.getString(cursor.getColumnIndex(col_end_time)),
                        cursor.getString(cursor.getColumnIndex(col_room)),
                        cursor.getString(cursor.getColumnIndex(col_teacher)),
                        cursor.getInt(cursor.getColumnIndex(col_day_of_week)),
                        cursor.getLong(cursor.getColumnIndex(col_lec_notification_time))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    @SuppressLint("Range")
    public List<Task> searchTasks(String query) {
        List<Task> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(table_tasks, null, col_task_title + " LIKE ?",
                new String[]{"%" + query + "%"}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Long completedAt = cursor.isNull(cursor.getColumnIndex(col_task_completed_at)) ? null : cursor.getLong(cursor.getColumnIndex(col_task_completed_at));
                list.add(new Task(
                        cursor.getInt(cursor.getColumnIndex(col_task_id)),
                        cursor.getString(cursor.getColumnIndex(col_task_title)),
                        cursor.getString(cursor.getColumnIndex(col_task_description)),
                        cursor.getString(cursor.getColumnIndex(col_task_lecture_name)),
                        cursor.getLong(cursor.getColumnIndex(col_task_deadline)),
                        cursor.getInt(cursor.getColumnIndex(col_task_priority)),
                        cursor.getInt(cursor.getColumnIndex(col_task_is_completed)),
                        cursor.getLong(cursor.getColumnIndex(col_task_created_at)),
                        completedAt,
                        cursor.getLong(cursor.getColumnIndex(col_task_notification_time))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }


    public int getTotalTasksCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + table_tasks, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public int getCompletedTasksCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + table_tasks + " WHERE " + col_task_is_completed + " = 1", null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public int getTasksCountByPriority(int priority) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + table_tasks + " WHERE " + col_task_priority + " = ?", new String[]{String.valueOf(priority)});
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }
    // აარარი სესრულებული და ვადა გავდა
    public int getOverdueTasksCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long currentTime = System.currentTimeMillis();

        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + table_tasks + " WHERE " + col_task_is_completed + " = 0 AND " + col_task_deadline + " < ?", new String[]{String.valueOf(currentTime)});

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    // არარის შესრულებული მარა აქვთ დრო სესრულებისთვის
    public int getOnTimePendingTasksCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long currentTime = System.currentTimeMillis();

        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + table_tasks + " WHERE " + col_task_is_completed + " = 0 AND " + col_task_deadline + " >= ?", new String[]{String.valueOf(currentTime)});

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public int getOnTimeCompletedTasksCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + table_tasks + " WHERE " + col_task_is_completed + " = 1 AND " + col_task_completed_at + " <= " + col_task_deadline, null);

        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

}
