package com.example.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.simpletodo.MainActivity.ITEM_POSITION;
import static com.example.simpletodo.MainActivity.ITEM_TEXT;

public class EditItemActivity extends AppCompatActivity {

    EditText etItemText; // content of item
    int position; // position item is in list view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        etItemText = findViewById(R.id.editItem);
        etItemText.setText(getIntent().getStringExtra(ITEM_TEXT));
        position = getIntent().getIntExtra(ITEM_POSITION,0);
        getSupportActionBar().setTitle("Edit Item");
    }

    public void onSaveItem(View v){
        Intent i = new Intent(); // prepare new intent for result
        i.putExtra(ITEM_TEXT,etItemText.getText().toString()); // pass updated item text as extra
        i.putExtra(ITEM_POSITION, position); // pass original position as extra
        setResult(RESULT_OK, i); // set the intent as the result of the activity
        finish(); // close the activity and reflect to main
    }

    public void onClear(View v){
        EditText etNewItem = findViewById(R.id.editItem);
        etNewItem.setText("");
    }
}