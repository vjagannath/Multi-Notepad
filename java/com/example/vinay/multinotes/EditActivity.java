package com.example.vinay.multinotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by vinay on 2/24/2017.
 */

public class EditActivity extends AppCompatActivity {

    EditText noteName;
    EditText noteText;
    String oldNoteNameStr = "";
    String oldNoteTextStr = "";
    int position;

    String noteIdentifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity);

        // hide the left caret at the top of the activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        noteName = (EditText)findViewById(R.id.noteNameId);
        noteText = (EditText)findViewById(R.id.noteTextId);
        noteText.setMovementMethod(new ScrollingMovementMethod());

        Intent intent = getIntent();
        if (intent.hasExtra(getString(R.string.noteNameConst))) {
            String name = intent.getStringExtra(getString(R.string.noteNameConst));
            noteName.setText(name);
            oldNoteNameStr = name;
        }
        if (intent.hasExtra(getString(R.string.noteTextConst))) {
            String text = intent.getStringExtra(getString(R.string.noteTextConst));
            noteText.setText(text);
            oldNoteTextStr = text;
        }
        noteIdentifier = intent.getStringExtra(getString(R.string.noteIdentifierConst));
        position = intent.getIntExtra(getString(R.string.notePositionConst),0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editlayout_optionsmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.saveMenu)
        {
            SaveData();
        }
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        if(noteName.getText().toString().isEmpty() || noteName.getText().toString().trim().isEmpty()){
            Toast.makeText(this, getString(R.string.untitledNoteText), Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }
        else if(noteName.getText().toString().compareTo(oldNoteNameStr)==0 && noteText.getText().toString().compareTo(oldNoteTextStr)==0)
        {
            super.onBackPressed();
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.noteNotSavedText)+"\n"+getString(R.string.noteSaveText)+" '"+noteName.getText().toString()+"'?");

            builder.setPositiveButton(getString(R.string.dialogOptionYes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CreateIntentAndSetResult();
                    EditActivity.super.onBackPressed();
                }
            });
            builder.setNegativeButton(getString(R.string.dialogOptionNo), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditActivity.super.onBackPressed();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    private void SaveData() {
        if(noteName.getText().toString().isEmpty() || noteName.getText().toString().trim().isEmpty()){
            Toast.makeText(this, getString(R.string.untitledNoteText), Toast.LENGTH_SHORT).show();
        }
        else {
            if(noteName.getText().toString().compareTo(oldNoteNameStr)!=0 || noteText.getText().toString().compareTo(oldNoteTextStr)!=0) {
                CreateIntentAndSetResult();
            }
        }
    }

    private void CreateIntentAndSetResult() {
        Intent data = new Intent();
        data.putExtra(getString(R.string.noteNameConst), noteName.getText().toString());
        data.putExtra(getString(R.string.noteTextConst), noteText.getText().toString());
        data.putExtra(getString(R.string.noteIdentifierConst), noteIdentifier);
        data.putExtra(getString(R.string.notePositionConst), position);
        setResult(RESULT_OK, data);
    }
}
