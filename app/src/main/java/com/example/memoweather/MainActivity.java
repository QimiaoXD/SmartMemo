package com.example.memoweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NotesDao dao;
    private final List<Note> notes = new ArrayList<>();
    private NoteAdapter adapter;

    private TextView tvEmpty;
    private TextInputEditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyDarkMode();

        Log.d("LC", "Main onCreate");
        setContentView(R.layout.activity_main);

        dao = new NotesDao(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvEmpty = findViewById(R.id.tvEmpty);
        etSearch = findViewById(R.id.etSearch);

        RecyclerView rv = findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NoteAdapter(notes, new NoteAdapter.Listener() {
            @Override public void onClick(Note note) {
                Intent it = new Intent(MainActivity.this, EditNoteActivity.class);
                it.putExtra(EditNoteActivity.EXTRA_NOTE_ID, note.id);
                startActivity(it);
            }

            @Override public void onLongClick(Note note) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("删除这条记事？")
                        .setMessage(note.title)
                        .setPositiveButton("删除", (d, w) -> {
                            dao.delete(note.id);
                            loadNotes(getSearchText());
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
        rv.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(v -> startActivity(new Intent(this, EditNoteActivity.class)));

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) { loadNotes(getSearchText()); }
        });
    }

    private void applyDarkMode() {
        SharedPreferences sp = getSharedPreferences(SettingsActivity.SP_NAME, MODE_PRIVATE);
        boolean dark = sp.getBoolean(SettingsActivity.KEY_DARK, false);
        AppCompatDelegate.setDefaultNightMode(dark ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }

    private String getSearchText() {
        return etSearch.getText() == null ? "" : etSearch.getText().toString().trim();
    }

    private void loadNotes(String keyword) {
        notes.clear();
        if (keyword.isEmpty()) notes.addAll(dao.queryAll());
        else notes.addAll(dao.search(keyword));
        adapter.notifyDataSetChanged();
        tvEmpty.setVisibility(notes.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("LC", "Main onResume");
        loadNotes(getSearchText());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override protected void onStart() { super.onStart(); Log.d("LC","Main onStart"); }
    @Override protected void onPause() { super.onPause(); Log.d("LC","Main onPause"); }
    @Override protected void onStop() { super.onStop(); Log.d("LC","Main onStop"); }
    @Override protected void onDestroy() { super.onDestroy(); Log.d("LC","Main onDestroy"); }
}
