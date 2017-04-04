package com.example.vinay.multinotes;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by vinay on 2/23/2017.
 */

public class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView noteName;
        private TextView lastUpdateTime;
        private TextView noteText;

        public MyRecyclerViewHolder(View view) {
            super(view);
            setNoteNameView((TextView) view.findViewById(R.id.cardNoteName));
            setLastUpdateTimeView((TextView) view.findViewById(R.id.updateTime));
            setNoteTextView((TextView) view.findViewById(R.id.noteText));
        }

    public TextView getNoteNameView() {
        return noteName;
    }

    public void setNoteNameView(TextView noteName) {
        this.noteName = noteName;
    }

    public TextView getLastUpdateTimeview() {
        return lastUpdateTime;
    }

    public void setLastUpdateTimeView(TextView lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public TextView getNoteTextView() {
        return noteText;
    }

    public void setNoteTextView(TextView noteText) {
        this.noteText = noteText;
    }
}
