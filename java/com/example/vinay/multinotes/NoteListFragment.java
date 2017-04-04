package com.example.vinay.multinotes;

import android.app.Fragment;
import android.os.Bundle;

import java.util.List;

/**
 * Created by vinay on 2/26/2017.
 */

public class NoteListFragment extends Fragment {
    // data object we want to retain
    private List<Note> myNotesList;

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    public void setData(List<Note> noteList) {
        this.myNotesList = noteList;
    }

    public List<Note> getNoteList() {
        return myNotesList;
    }
}
