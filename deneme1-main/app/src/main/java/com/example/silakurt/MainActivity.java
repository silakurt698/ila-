package com.example.silakurt;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private EditText etName, etDose, etTime;
    private ListView lvMedicines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        etName = findViewById(R.id.etName);
        etDose = findViewById(R.id.etDose);
        etTime = findViewById(R.id.etTime);
        lvMedicines = findViewById(R.id.lvMedicines);
        Button btnAddMedicine = findViewById(R.id.btnAddMedicine);

        // Button tıklama olayını tanımlama
        btnAddMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                String dose = etDose.getText().toString().trim();
                String time = etTime.getText().toString().trim();

                // Boş alan kontrolü
                if (name.isEmpty() || dose.isEmpty() || time.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Tüm alanları doldurun", Toast.LENGTH_SHORT).show();
                } else {
                    // Veritabanına ilaç ekleme
                    boolean isInserted = dbHelper.insertMedicine(name, dose, time);
                    if (isInserted) {
                        Toast.makeText(MainActivity.this, "İlaç Eklendi", Toast.LENGTH_SHORT).show();
                        displayMedicines();  // Listemizi güncelle
                    } else {
                        Toast.makeText(MainActivity.this, "Ekleme Başarısız", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Başlangıçta ilaçları listele
        displayMedicines();
    }

    private void displayMedicines() {
        Cursor cursor = dbHelper.getAllMedicines();
        String[] from = {"_id", "name", "dose", "time"}; // "_id" eklendi
        int[] to = {android.R.id.text1, android.R.id.text2};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2, cursor, from, to, 0);
        lvMedicines.setAdapter(adapter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Veritabanı ve cursor'ları doğru şekilde kapatmayı unutmayın
        Cursor cursor = dbHelper.getAllMedicines();
        if (cursor != null) {
            cursor.close();
        }
        dbHelper.close();
    }
}
