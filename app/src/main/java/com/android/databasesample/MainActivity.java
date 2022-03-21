package com.android.databasesample;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private int _id = -1;
    private String _name = "";

    private DataHelper _helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lv = findViewById(R.id.lvlist);

        lv.setOnItemClickListener(new ListItemClickListener());

        _helper = new DataHelper(MainActivity.this);
    }

    @Override
    protected void onDestroy() {
        _helper.close();
        super.onDestroy();
    }

    public void onSaveButtonClick(View view) {
        EditText eNote = findViewById(R.id.etNote);
        String note = eNote.getText().toString();

        SQLiteDatabase db = _helper.getWritableDatabase();

        String sqlDelete = "Delete FROM memos WHERE _id = ?";

        SQLiteStatement stmt = db.compileStatement(sqlDelete);

        stmt.bindLong(1, _id);

        stmt.executeUpdateDelete();

        String sqlInsert = "INSERT INTO memos(_id,name,note) VALUES(?, ?, ?)";

        stmt = db.compileStatement(sqlInsert);

        stmt.bindLong(1, _id);
        stmt.bindString(2, _name);
        stmt.bindString(3, note);

        stmt.executeInsert();

        eNote.setText("");
        TextView tvName = findViewById(R.id.name);
        tvName.setText(getString(R.string.tv_name));
        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setEnabled(false);
    }

    private class ListItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            _id = position;
            _name = (String) parent.getItemAtPosition(position);
            TextView tvName = findViewById(R.id.name);
            tvName.setText(_name);

            Button btnSave = findViewById(R.id.btnSave);
            btnSave.setEnabled(true);

            SQLiteDatabase db = _helper.getWritableDatabase();
            String sql = "SELECT * FROM memos WHERE _id = " + _id;
            Cursor cursor = db.rawQuery(sql, null);
            String note = "";

            while (cursor.moveToNext()) {
                int idxNote = cursor.getColumnIndex(" note");
                note = cursor.getString(idxNote);
            }

            EditText etNote = findViewById(R.id.etNote);
            etNote.setText(note);

        }
    }
}