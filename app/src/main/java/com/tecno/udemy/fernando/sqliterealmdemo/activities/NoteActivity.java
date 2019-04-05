package com.tecno.udemy.fernando.sqliterealmdemo.activities;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.tecno.udemy.fernando.sqliterealmdemo.R;
import com.tecno.udemy.fernando.sqliterealmdemo.adapters.NoteAdapter;
import com.tecno.udemy.fernando.sqliterealmdemo.model.Board;
import com.tecno.udemy.fernando.sqliterealmdemo.model.Note;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

public class NoteActivity extends AppCompatActivity implements RealmChangeListener<Board> {

    private ListView listView;
    private FloatingActionButton fabButton;
    private NoteAdapter adapter;

    private RealmList<Note> notes;
    private Realm realmDB;

    private int boardID;
    private Board board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        initData();
        initComponents();
    }

    private void initComponents(){
        listView = findViewById(R.id.listViewNotes);
        fabButton = findViewById(R.id.fabAddNote);

        listView.setAdapter(adapter);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertForCreatingNote("Add New Note", "Tyoe a Note for "+board.getTitle());
            }
        });

        registerForContextMenu(listView);
    }

    private void initData(){
        realmDB = Realm.getDefaultInstance();
        boardID = getIntent().getExtras().getInt("id");

        board = realmDB.where(Board.class).equalTo("id", boardID).findFirst();
        notes = board.getNotes();
        this.setTitle(board.getTitle());

        board.addChangeListener(this);

        adapter = new NoteAdapter(notes, this, R.layout.list_view_note_item);
    }

    /* ------------------------------------------------CRUD ACTIONS-------------------------------------------*/

    private void createNewNote(String description){
        realmDB.beginTransaction();
        Note newNote = new Note(description);
        realmDB.copyToRealm(newNote);
        board.getNotes().add(newNote);
        realmDB.commitTransaction();
    }

    private void deleteAll(){
        realmDB.beginTransaction();
        board.getNotes().deleteAllFromRealm();
        realmDB.commitTransaction();
    }

    private void editNote(String newDescription, Note note){
        realmDB.beginTransaction();
        note.setDescription(newDescription);
        realmDB.copyToRealmOrUpdate(note);
        realmDB.commitTransaction();
    }

    private void deleteNote(Note note){
        realmDB.beginTransaction();
        note.deleteFromRealm();
        realmDB.commitTransaction();
    }

    /* -------------------- DIALOG ------------------------------*/

    private void showAlertForCreatingNote(String title, String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(title != null) builder.setTitle(title);
        if(message != null) builder.setMessage(message);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_note, null);
        builder.setView(viewInflated);

        final EditText input = viewInflated.findViewById(R.id.editTextNewNote);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String noteDescription = input.getText().toString();
                if(noteDescription.length() > 0)
                    createNewNote(noteDescription);
                else
                    Toast.makeText(getApplicationContext(), "The description is required", Toast.LENGTH_LONG).show();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showAlertForEditingNote(String title, String message, final Note note){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(title != null) builder.setTitle(title);
        if(message != null) builder.setMessage(message);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_note, null);
        builder.setView(viewInflated);

        final EditText input = viewInflated.findViewById(R.id.editTextNewNote);
        input.setText(note.getDescription());

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String noteDescription = input.getText().toString();

                if(noteDescription.length() == 0)
                    Toast.makeText(getApplicationContext(), "The description is required", Toast.LENGTH_SHORT).show();

                else if (noteDescription.equals(note.getDescription()))
                    Toast.makeText(getApplicationContext(), "The description is the same than it was before", Toast.LENGTH_SHORT).show();

                else
                    editNote(noteDescription, note);
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    /*-------------------------LISTENERS----------------------------*/
    @Override
    public void onChange(Board board) {
        adapter.notifyDataSetChanged();
    }

    /* --------------------------------------MENUS -----------------------------------------*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.mnuDeleteAlNotes:
                deleteAll();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle("Note #"+notes.get(info.position).getId());
        getMenuInflater().inflate(R.menu.context_menu_note, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()){

            case R.id.mnu_edit_note:
                showAlertForEditingNote("Edit Note", "Change the description", notes.get(info.position));
                break;

            case R.id.mnu_delete_note:
                deleteNote(notes.get(info.position));
                break;
        }

        return super.onContextItemSelected(item);
    }
}
