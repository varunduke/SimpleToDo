package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public  static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> lStrItems;

    Button btnAdd;
    EditText edItem;
    RecyclerView rvItems;
    ItemAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        edItem = findViewById(R.id.edItem);
        rvItems = findViewById(R.id.rvItems);

        loadData();

        ItemAdapter.OnLongClickListner objOnLongClickListner = new ItemAdapter.OnLongClickListner() {
            @Override
            public void onItemLongClicked(int iPosition) {
                lStrItems.remove(iPosition);
                itemsAdapter.notifyItemRemoved(iPosition);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveData();
            }
        };

        ItemAdapter.OnClickListner objOnClickListner = new ItemAdapter.OnClickListner() {
            @Override
            public void onItemClicked(int iPosition) {
                Log.d("MainActivity", "Single click");
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                i.putExtra(KEY_ITEM_TEXT, lStrItems.get(iPosition));
                i.putExtra(KEY_ITEM_POSITION, iPosition);
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };

        itemsAdapter = new ItemAdapter(lStrItems, objOnLongClickListner, objOnClickListner);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strItem = edItem.getText().toString();
                 lStrItems.add(strItem);
                itemsAdapter.notifyItemInserted(lStrItems.size() - 1);
                edItem.setText("");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveData();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE)
        {
            String strItemText = data.getStringExtra(KEY_ITEM_TEXT);
            int iPosition = data.getExtras().getInt(KEY_ITEM_POSITION);
            lStrItems.set(iPosition, strItemText);
            itemsAdapter.notifyItemChanged(iPosition);
            saveData();
            Toast.makeText(getApplicationContext(), "Item updated", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Log.w("MainActivity", "unknown call to OnActivityResult");
        }
    }

    private File getFile()
    {
        return new File(getFilesDir(), "data.txt");
    }
    private void loadData()
    {
        try {
            lStrItems = new ArrayList<>(FileUtils.readLines(getFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error in reading items", e);
            lStrItems = new ArrayList<>();
        }
    }
    private void saveData()
    {
        try {
            FileUtils.writeLines(getFile(), lStrItems);
        } catch (IOException e) {
            Log.e("MainActivity", "Error in Writing items", e);
        }
    }
}