package com.example.memoweather;

public class Note {
    public long id;
    public String title;
    public String content;
    public String summary;   // 摘要
    public String keywords;  // 关键词（可选）
    public long time;
    public int pinned;

    public Note(long id, String title, String content, String summary, String keywords, long time, int pinned) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.summary = summary;
        this.keywords = keywords;
        this.time = time;
        this.pinned = pinned;
    }
}
