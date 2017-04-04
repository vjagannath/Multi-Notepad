package com.example.vinay.multinotes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by vinay on 2/23/2017.
 */

public class NotesAdapter extends RecyclerView.Adapter<MyRecyclerViewHolder>{

    List<Note> myNotesList;
    MainActivity myMainActivity;

    public NotesAdapter(List<Note> notesList, MainActivity mainActivity)
    {
        myNotesList = notesList;
        myMainActivity =mainActivity;
    }

    // create new views invoked by layout manager
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list_row, parent, false);
        myItemView.setOnClickListener(myMainActivity);
        myItemView.setOnLongClickListener(myMainActivity);
        return new MyRecyclerViewHolder(myItemView);
    }

    // replace the contents of the view invoked by the layout manager
    @Override
    public void onBindViewHolder(MyRecyclerViewHolder holder, int position) {
        Note newNote = myNotesList.get(position);
        holder.getNoteNameView().setText(newNote.getMyNoteName());
        holder.getLastUpdateTimeview().setText(newNote.getMyLastUpdateTime());

        String text = newNote.getMyNoteText();
        if(text.length() > 80)
        {
            text = text.substring(0, 79)+"...";
        }
        holder.getNoteTextView().setText(text);
    }

    // return the size ofthe dataset
    @Override
    public int getItemCount() {
        return myNotesList.size();
    }
}
