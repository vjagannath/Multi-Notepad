package com.example.vinay.multinotes;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@TargetApi(21)
public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{

    private List<Note> myNotesList = new ArrayList<>();
    private NotesAdapter myNotesAdapter;
    private RecyclerView myRecyclerView;
    private SimpleDateFormat formatter;
    private static final int B_REQ = 1;
    private static final String TAG_RETAINED_FRAGMENT = "NoteListFragment";
    private NoteListFragment retainedDataFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        formatter = new SimpleDateFormat("EEE MMM dd, HH:mm:ss a");

        // hide the left caret at the top of the activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        myRecyclerView = (RecyclerView)findViewById(R.id.noteListView);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        android.app.FragmentManager manager = getFragmentManager();
        retainedDataFragment = (NoteListFragment) manager.findFragmentByTag(TAG_RETAINED_FRAGMENT);
        if (retainedDataFragment != null) {
            myNotesList = retainedDataFragment.getNoteList();
            myNotesAdapter = new NotesAdapter(myNotesList, MainActivity.this);
            myRecyclerView.setAdapter(myNotesAdapter);
        }else {
            MyAsyncTask task = new MyAsyncTask();
            task.execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionsmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.infoMenu:
                intent = new Intent(MainActivity.this, AboutActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, MainActivity.class.getSimpleName());
                startActivity(intent);
                break;
            case R.id.addNoteMenu:
                intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, MainActivity.class.getSimpleName());
                intent.putExtra(getString(R.string.noteNameConst), "");
                intent.putExtra(getString(R.string.noteTextConst), "");
                intent.putExtra(getString(R.string.noteIdentifierConst), getString(R.string.newNoteIdentifierConst));
                intent.putExtra(getString(R.string.notePositionConst), 0);
                startActivityForResult(intent, B_REQ);
                break;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == B_REQ) {
            if (resultCode == RESULT_OK) {
                String name = data.getStringExtra(getString(R.string.noteNameConst));
                String text = data.getStringExtra(getString(R.string.noteTextConst));
                String noteIdentifier = data.getStringExtra(getString(R.string.noteIdentifierConst));
                int position = data.getIntExtra(getString(R.string.notePositionConst), 0);

                Note note = new Note();
                note.setMyNoteName(name);
                note.setMyNoteText(text);
                note.setMyLastUpdateTime(formatter.format(new Date()));

                if(noteIdentifier.compareTo(getString(R.string.oldNoteIdentifierConst)) == 0)
                {
                    myNotesList.remove(position);
                    position = 0;
                }
                myNotesList.add(position, note);
                myNotesAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int position = myRecyclerView.getChildLayoutPosition(v);
        Note note = myNotesList.get(position);

        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, MainActivity.class.getSimpleName());
        intent.putExtra(getString(R.string.noteNameConst), note.getMyNoteName());
        intent.putExtra(getString(R.string.noteTextConst), note.getMyNoteText());
        intent.putExtra(getString(R.string.notePositionConst), position);
        intent.putExtra(getString(R.string.noteIdentifierConst), getString(R.string.oldNoteIdentifierConst));
        startActivityForResult(intent, B_REQ);
    }

    @Override
    public boolean onLongClick(View v) {
        final int position = myRecyclerView.getChildLayoutPosition(v);
        Note note = myNotesList.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.deleteNoteDialogText)+" '" + note.getMyNoteName()+"'?");

        builder.setPositiveButton(getString(R.string.dialogOptionYes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myNotesList.remove(position);
                myNotesAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(getString(R.string.dialogOptionNo), null);
        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        SaveNoteList();
        Log.d("MainActivity", "On Pause");
    }

    private void SaveNoteList() {
        try
        {
            FileOutputStream stream = getApplicationContext().openFileOutput(getString(R.string.fileName), Context.MODE_PRIVATE);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(stream, getString(R.string.encoding)));
            writer.setIndent(" ");
            WriteNoteList(writer);
            writer.close();
        }
        catch(Exception e)
        {
            e.getStackTrace();
        }
    }

    private void WriteNoteList(JsonWriter writer) throws IOException {
        writer.beginArray();
        for (Note note:myNotesList) {
            WriteNote(writer, note);
        }
        writer.endArray();
    }

    private void WriteNote(JsonWriter writer, Note note) throws IOException {
        writer.beginObject();
        writer.name(getString(R.string.noteNameConst)).value(note.getMyNoteName());
        writer.name(getString(R.string.noteTextConst)).value(note.getMyNoteText());
        writer.name(getString(R.string.noteUpdateTimeConst)).value(note.getMyLastUpdateTime());
        writer.endObject();
    }

    class MyAsyncTask extends AsyncTask<Long, String, List<Note>> {

        @Override
        protected List<Note> doInBackground(Long... params) {
            List<Note> noteList = null;
            // Load the JSON containing the product data - if it exists
            try {
                noteList = loadFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return noteList;
        }

        @Override
        protected void onPostExecute(List<Note> noteList) {
            // execution of result of Long time consuming operation
            if(noteList != null)
            {
                myNotesList = noteList;
            }

            myNotesAdapter = new NotesAdapter(myNotesList, MainActivity.this);
            myRecyclerView.setAdapter(myNotesAdapter);

            android.app.FragmentManager manager = getFragmentManager();
            retainedDataFragment = (NoteListFragment) manager.findFragmentByTag(TAG_RETAINED_FRAGMENT);
            if (retainedDataFragment == null) {
                // add the fragment
                retainedDataFragment = new NoteListFragment();
                manager.beginTransaction().add(retainedDataFragment, TAG_RETAINED_FRAGMENT).commit();
                // load data from a data source or perform any calculation
                retainedDataFragment.setData(myNotesList);
            }
        }

        private List<Note> loadFile() throws IOException {
            InputStream stream = getApplicationContext().openFileInput(getString(R.string.fileName));
            JsonReader reader = new JsonReader(new InputStreamReader(stream, getString(R.string.encoding)));
            try
            {
                return ReadNoteList(reader);
            }
            finally
            {
                reader.close();
            }
        }

        private List<Note> ReadNoteList(JsonReader reader) throws IOException {
            List<Note> noteList = new ArrayList<Note>();
            reader.beginArray();
            while(reader.hasNext())
            {
                noteList.add(ReadNote(reader));
            }
            reader.endArray();
            return noteList;
        }

        private Note ReadNote(JsonReader reader) throws IOException {
            Note note = new Note();
            reader.beginObject();
            while (reader.hasNext())
            {
                String name = reader.nextName();
                if(name.equals(getString(R.string.noteNameConst)))
                {
                    note.setMyNoteName(reader.nextString());
                }
                else if(name.equals(getString(R.string.noteTextConst)))
                {
                    note.setMyNoteText(reader.nextString());
                }
                else if(name.equals(getString(R.string.noteUpdateTimeConst)))
                {
                    note.setMyLastUpdateTime(reader.nextString());
                }
                else
                {
                    reader.skipValue();
                }
            }
            reader.endObject();
            return note;
        }
    }
}
