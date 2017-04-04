package com.example.vinay.multinotes;

/**
 * Created by vinay on 2/23/2017.
 */

public class Note {
    private String myNoteName;
    private String myLastUpdateTime;
    private String myNoteText;

    public Note()
    {
        myNoteName = null;
        myLastUpdateTime = null;
        myNoteText = null;
    }

    public String getMyNoteName() {
        return myNoteName;
    }

    public void setMyNoteName(String myNoteName) {
        this.myNoteName = myNoteName;
    }

    public String getMyLastUpdateTime() {
        return myLastUpdateTime;
    }

    public void setMyLastUpdateTime(String myLastUpdateTime) {
        this.myLastUpdateTime = myLastUpdateTime;
    }

    public String getMyNoteText() {
        return myNoteText;
    }

    public void setMyNoteText(String myNoteText) {
        this.myNoteText = myNoteText;
    }
}
