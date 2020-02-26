package com.example.sqlitecontactes;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private MyDatabaseHelper dbhelp;
    protected int count;
    private MyDB md;
    private SQLiteDatabase db;
    private ListView llista;
    private SimpleCursorAdapter mAdapter;
    private boolean edited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    createContact();
                } catch (Exception e) {
                    Log.d("patata", "La entrada ja existeix");
                }
            }
        });

        this.dbhelp = new MyDatabaseHelper(this);
        this.db = dbhelp.getWritableDatabase();
        this.md = new MyDB(this);
        llista = (ListView) this.findViewById(R.id.list_view);
        try {
            md.createRecords("1", "Agustí", "601593683");
            md.createRecords("2", "Andreu", "601485472");
            md.createRecords("3", "Bernat", "610492048");
            md.createRecords("4", "Carles", "666666666");
        } catch (Exception e) {
            Log.d("patata", "La entrada ja existeix");
        }
        count=md.getCount();

        updateListView();

        llista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                md.deleteContact(md.getId(position+1));
                updateListView();
                return false;
            }
        });

        llista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showContact(position+1);
            }
        });


    }

    public void createContact(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add planet");
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.popup, null);
        final EditText input1 = (EditText) textEntryView.findViewById(R.id.editText);
        final EditText input2 = (EditText) textEntryView.findViewById(R.id.editText2);
        builder.setView(textEntryView);
        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String contact = input1.getText().toString();
                String num = input2.getText().toString();
                if(!contact.equals("") && !num.equals("")) {
                    md.createRecords(String.valueOf(++count), contact, num);
                    updateListView();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void showContact(final int index){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.info, null);
        final EditText text1 = (EditText) textEntryView.findViewById(R.id.textView4);
        Button button = (Button) textEntryView.findViewById(R.id.button);
        text1.setEnabled(false);
        Cursor c = md.selectContacts();
        c.move(index-1);
        builder.setTitle(c.getString(1));
        text1.setText(c.getString(2));
        builder.setView(textEntryView);
        edited=false;
        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (edited){
                    md.updateContact(md.getId(index),text1.getText().toString());
                    updateListView();
                }
                edited=false;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text1.setEnabled(true);
                edited=true;
            }
        });
        builder.show();
    }

    public void updateListView(){
        Cursor c = md.selectContacts();
        String[] from = new String[]{"name"};
        int[] to = new int[]{android.R.id.text1};
        mAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, c, from, to);
        llista.setAdapter(mAdapter);
    }

}
