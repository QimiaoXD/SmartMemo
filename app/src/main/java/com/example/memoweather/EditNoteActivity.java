package com.example.memoweather;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

public class EditNoteActivity extends AppCompatActivity {

    public static final String EXTRA_NOTE_ID = "note_id";

    private NotesDao dao;
    private long noteId = -1;

    private TextInputEditText etTitle, etContent;
    private SwitchMaterial swPinned;
    private TextView tvSummary;

    private String currentSummary = "";
    private String currentKeywords = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LC", "Edit onCreate");
        setContentView(R.layout.activity_edit_note);

        dao = new NotesDao(this);

        Toolbar tb = findViewById(R.id.toolbarEdit);
        setSupportActionBar(tb);
        tb.setNavigationIcon(android.R.drawable.ic_menu_revert);
        tb.setNavigationOnClickListener(v -> finish());

        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        swPinned = findViewById(R.id.swPinned);
        tvSummary = findViewById(R.id.tvSummary);

        Button btnSummarize = findViewById(R.id.btnSummarize);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnShare = findViewById(R.id.btnShare);

        noteId = getIntent().getLongExtra(EXTRA_NOTE_ID, -1);

        if (noteId != -1) {
            tb.setTitle(getString(R.string.edit_note));
            Note n = dao.queryById(noteId);
            if (n != null) {
                etTitle.setText(n.title);
                etContent.setText(n.content);
                swPinned.setChecked(n.pinned == 1);
                currentSummary = n.summary == null ? "" : n.summary;
                currentKeywords = n.keywords == null ? "" : n.keywords;
                tvSummary.setText(renderSummaryText());
            }
        } else {
            tb.setTitle(getString(R.string.new_note));
            tvSummary.setText("摘要：尚未生成");
        }

        btnSummarize.setOnClickListener(v -> summarizeNetwork());
        btnSave.setOnClickListener(v -> save());
        btnShare.setOnClickListener(v -> share());
    }

    private String renderSummaryText() {
        String s = (currentSummary == null || currentSummary.trim().isEmpty()) ? "（暂无摘要）" : currentSummary;
        String k = (currentKeywords == null || currentKeywords.trim().isEmpty()) ? "（无）" : currentKeywords;
        return "摘要：\n" + s + "\n\n关键词：\n" + k;
    }

    private void summarizeNetwork() {
        String content = etContent.getText() == null ? "" : etContent.getText().toString().trim();
        if (content.isEmpty()) {
            Snackbar.make(etContent, "请先输入内容", Snackbar.LENGTH_SHORT).show();
            return;
        }

        tvSummary.setText("摘要：生成中（DeepSeek）…");

        new Thread(() -> {
            try {
                DeepSeekClient client = new DeepSeekClient();
                DeepSeekClient.Result r = client.summarize(content);

                currentSummary = r.summary;
                currentKeywords = r.keywords;

                runOnUiThread(() -> tvSummary.setText(renderSummaryText()));
            } catch (Exception e) {
                runOnUiThread(() -> tvSummary.setText("摘要生成失败：\n" + e.getMessage()));
            }
        }).start();
    }

    private void save() {
        String title = etTitle.getText() == null ? "" : etTitle.getText().toString().trim();
        String content = etContent.getText() == null ? "" : etContent.getText().toString().trim();
        int pinned = swPinned.isChecked() ? 1 : 0;

        if (title.isEmpty() || content.isEmpty()) {
            Snackbar.make(etTitle, "标题和内容不能为空", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (noteId == -1) {
            dao.insert(title, content, currentSummary, currentKeywords, pinned);
        } else {
            dao.update(noteId, title, content, currentSummary, currentKeywords, pinned);
        }
        finish();
    }

    private void share() {
        String title = etTitle.getText() == null ? "" : etTitle.getText().toString().trim();
        String content = etContent.getText() == null ? "" : etContent.getText().toString().trim();

        String shareText = "【" + title + "】\n\n"
                + "摘要：\n" + (currentSummary == null ? "" : currentSummary) + "\n\n"
                + "关键词：" + (currentKeywords == null ? "" : currentKeywords) + "\n\n"
                + "原文：\n" + content;

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(intent, "分享到"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (noteId != -1) {
            getMenuInflater().inflate(R.menu.menu_edit, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            new AlertDialog.Builder(this)
                    .setTitle("删除这条记事？")
                    .setMessage("删除后不可恢复")
                    .setPositiveButton("删除", (d, w) -> {
                        dao.delete(noteId);
                        finish();
                    })
                    .setNegativeButton("取消", null)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
