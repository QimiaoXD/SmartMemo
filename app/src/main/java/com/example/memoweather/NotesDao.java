package com.example.memoweather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class NotesDao {
    private final NotesDbHelper helper;

    public NotesDao(Context context) {
        helper = new NotesDbHelper(context);
    }

    public long insert(String title, String content, String summary, String keywords, int pinned) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("title", title);
        cv.put("content", content);
        cv.put("summary", summary);
        cv.put("keywords", keywords);
        cv.put("time", System.currentTimeMillis());
        cv.put("pinned", pinned);
        return db.insert("notes", null, cv);
    }

    public int update(long id, String title, String content, String summary, String keywords, int pinned) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("title", title);
        cv.put("content", content);
        cv.put("summary", summary);
        cv.put("keywords", keywords);
        cv.put("time", System.currentTimeMillis());
        cv.put("pinned", pinned);
        return db.update("notes", cv, "id=?", new String[]{String.valueOf(id)});
    }

    public int delete(long id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.delete("notes", "id=?", new String[]{String.valueOf(id)});
    }

    public Note queryById(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id,title,content,summary,keywords,time,pinned FROM notes WHERE id=?",
                new String[]{String.valueOf(id)});
        Note n = null;
        if (c.moveToFirst()) {
            n = new Note(
                    c.getLong(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4),
                    c.getLong(5),
                    c.getInt(6)
            );
        }
        c.close();
        return n;
    }

    public List<Note> queryAll() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT id,title,content,summary,keywords,time,pinned FROM notes ORDER BY pinned DESC, time DESC",
                null
        );
        List<Note> list = new ArrayList<>();
        while (c.moveToNext()) {
            list.add(new Note(
                    c.getLong(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4),
                    c.getLong(5),
                    c.getInt(6)
            ));
        }
        c.close();
        return list;
    }

    public List<Note> search(String keyword) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String like = "%" + keyword + "%";
        Cursor c = db.rawQuery(
                "SELECT id,title,content,summary,keywords,time,pinned FROM notes " +
                        "WHERE title LIKE ? OR content LIKE ? OR summary LIKE ? " +
                        "ORDER BY pinned DESC, time DESC",
                new String[]{like, like, like}
        );
        List<Note> list = new ArrayList<>();
        while (c.moveToNext()) {
            list.add(new Note(
                    c.getLong(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4),
                    c.getLong(5),
                    c.getInt(6)
            ));
        }
        c.close();
        return list;
    }
}
