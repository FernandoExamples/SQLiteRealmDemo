package com.tecno.udemy.fernando.sqliterealmdemo.activities;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.tecno.udemy.fernando.sqliterealmdemo.adapters.BoardAdapter;
import com.tecno.udemy.fernando.sqliterealmdemo.model.Board;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<Board>>, AdapterView.OnItemClickListener {

    private FloatingActionButton fabButton;
    private Realm realmDB;

    private BoardAdapter adapter;
    private ListView listView;
    private RealmResults<Board> boards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initComponents();
    }

    private void initData(){
        realmDB = Realm.getDefaultInstance();

        boards = realmDB.where(Board.class).findAll();
        boards.addChangeListener(this);

        adapter = new BoardAdapter(this, boards, R.layout.list_view_board_item);
    }

    private void initComponents(){
        fabButton = findViewById(R.id.fabAddButton);
        listView = findViewById(R.id.listViewBoard);

        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertForCreatingBoard("Add new board", "Type a name for your new Board");
            }
        });

        registerForContextMenu(listView);

    }


    /* ------------------------------------------------CRUD ACTIONS-------------------------------------------*/
    private void createNewBoard(String boardName){
        realmDB.beginTransaction();
        Board newBoard = new Board(boardName);
        realmDB.copyToRealm(newBoard);
        realmDB.commitTransaction();
    }

    private void deleteBoard(Board board){
        realmDB.beginTransaction();
        board.deleteFromRealm();
        realmDB.commitTransaction();
    }

    private void editBoard(String newName, Board board){
        realmDB.beginTransaction();
        board.setTitle(newName);
        realmDB.copyToRealmOrUpdate(board);
        realmDB.commitTransaction();
    }

    private void deleteAll(){
        realmDB.beginTransaction();
        realmDB.deleteAll();
        realmDB.commitTransaction();
    }

    /* -------------------- DIALOG ------------------------------*/
    private void showAlertForCreatingBoard(String title, String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(title != null) builder.setTitle(title);
        if(message != null) builder.setMessage(message);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_board, null);
        builder.setView(viewInflated);

        final EditText input = viewInflated.findViewById(R.id.editTextNewBoard);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String boardName = input.getText().toString();
                if(boardName.length() > 0)
                    createNewBoard(boardName);
                else
                    Toast.makeText(getApplicationContext(), "The name is required", Toast.LENGTH_SHORT).show();
            }
        });

       AlertDialog alert = builder.create();
       alert.show();
    }

    private void showAlertForEditingBoard(String title, String message, final Board board){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(title != null) builder.setTitle(title);
        if(message != null) builder.setMessage(message);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_board, null);
        builder.setView(viewInflated);

        final EditText input = viewInflated.findViewById(R.id.editTextNewBoard);
        input.setText(board.getTitle());

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String boardName = input.getText().toString().trim();

                if(boardName.length() == 0)
                    Toast.makeText(getApplicationContext(), "The name is required", Toast.LENGTH_SHORT).show();

                else if (boardName.equals(board.getTitle()))
                    Toast.makeText(getApplicationContext(), "The name is the same than it was before", Toast.LENGTH_SHORT).show();

                else
                    editBoard(boardName, board);
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    /* -------------------------------  listeners -----------------------------------------*/
    @Override
    public void onChange(RealmResults<Board> boards) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(MainActivity.this, NoteActivity.class);
        intent.putExtra("id", boards.get(position).getId());
        startActivity(intent);
    }

    /* --------------------------------------MENUS -----------------------------------------*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_board, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.mnuDeleteAll:
                deleteAll();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(boards.get(info.position).getTitle());
        getMenuInflater().inflate(R.menu.context_menu_board, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()){
            case R.id.mnu_delete_board:
               deleteBoard(boards.get(info.position));
                break;

            case R.id.mnu_edit_board:
                showAlertForEditingBoard("Edit Board", "Change the Board Name", boards.get(info.position));
        }

        return super.onContextItemSelected(item);
    }

}
