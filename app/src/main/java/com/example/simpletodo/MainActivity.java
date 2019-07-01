package com.example.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public final static int EDIT_REQUEST_CODE = 20;
    public final static String ITEM_TEXT = "itemText";
    public final static String ITEM_POSITION = "itemPosition";

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readItems(); // handles saving the items upon closing
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems = findViewById(R.id.lvitems);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    public void onAddItem(View v){
        /*
        Adding an item to the list view
         */
        EditText etNewItem = findViewById(R.id.et);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
        Toast.makeText(getApplicationContext(), "item added to list", Toast.LENGTH_SHORT).show();
    }

    private void setupListViewListener(){
        /*
        Setup listener for clicking on item and editing it
         */
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.i("MainActivity", "item removed from list");
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent i = new Intent(MainActivity.this, EditItemActivity.class); // create the new activity
                i.putExtra(ITEM_TEXT, items.get(position)); // pass the data being edited
                i.putExtra(ITEM_POSITION, position);
                startActivityForResult(i, 20); // display the activity
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK && requestCode==EDIT_REQUEST_CODE){
            String updatedItem = data.getExtras().getString(ITEM_TEXT); // extract updated item text from result intent extras
            int position = data.getExtras().getInt(ITEM_POSITION); // extract original position of edited item
            items.set(position, updatedItem); // update the model with the new item text at the edited position
            itemsAdapter.notifyDataSetChanged(); // notify the adapter that the model changed
            writeItems(); // persist the changed model
            Toast.makeText(this, "item updated successfully", Toast.LENGTH_SHORT).show(); // notify the user the operation completed okay
        }
    }

    private File getDataFile(){
        return new File(getFilesDir(), "todo.txt");
    }

    private void readItems(){
        try{
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e){
            Log.e("MainActivity", "error reading file",e);
            items = new ArrayList<>();
        }
    }

    private void writeItems(){
        try{
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e){
            Log.e("MainActivity", "error writing file", e);
        }
    }
}