package com.example.memoweather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.VH> {

    public interface Listener {
        void onClick(Note note);
        void onLongClick(Note note);
    }

    private final List<Note> data;
    private final Listener listener;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

    public NoteAdapter(List<Note> data, Listener listener) {
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Note n = data.get(position);
        h.tvTitle.setText(n.title);
        String sum = (n.summary == null || n.summary.trim().isEmpty()) ? "（暂无摘要）" : n.summary;
        h.tvSummary.setText(sum);
        h.tvTime.setText("更新时间: " + sdf.format(new Date(n.time)));

        h.tvBadge.setVisibility(n.pinned == 1 ? View.VISIBLE : View.GONE);

        h.itemView.setOnClickListener(v -> listener.onClick(n));
        h.itemView.setOnLongClickListener(v -> {
            listener.onLongClick(n);
            return true;
        });
    }

    @Override
    public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvTitle, tvSummary, tvTime, tvBadge;
        VH(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSummary = itemView.findViewById(R.id.tvSummary);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvBadge = itemView.findViewById(R.id.tvBadge);
        }
    }
}
