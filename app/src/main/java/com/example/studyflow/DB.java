package com.example.studyflow;

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
    private static final int DATABASE_VERSION = 1;
    public static final String table_lectures = "lectures";
    public static final String col_lec_id = "id";
    public static final String col_subject = "subject";
    public static final String col_start_time = "startTime";
    public static final String col_end_time = "endTime";
    public static final String col_room = "room";
    public static final String col_teacher = "teacher";
    public static final String col_day_of_week = "dayOfWeek";
    public DB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
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
                col_day_of_week + " INTEGER" +
                ");";
        db.execSQL(create_lectures_table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + table_lectures);
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

        long rowId = db.insert(table_lectures, null, cv);
        db.close();
        return rowId;
    }

    public List<LectureItem> getLecturesByDay(int dayOfWeek) {
        List<LectureItem> lectureList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(table_lectures,
                null,
                col_day_of_week + " = ?",
                new String[]{String.valueOf(dayOfWeek)},
                null, null,
                col_start_time + " ASC");

        if (cursor.moveToFirst()) {
            do {
                lectureList.add(new LectureItem(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getInt(6)
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

        int updatedRows = db.update(table_lectures, cv, col_lec_id + " = ?",
                new String[]{String.valueOf(lecture.getId())});
        db.close();
        return updatedRows;
    }

    // Delete lecture
    public int deleteLecture(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(table_lectures, col_lec_id + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return deletedRows;
    }


}
