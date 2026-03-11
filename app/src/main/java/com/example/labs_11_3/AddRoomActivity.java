package com.example.labs_11_3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddRoomActivity extends AppCompatActivity {

    private EditText etRoomId, etRoomName, etPrice;
    private Spinner spinnerStatus;
    private Button btnSave, btnCancel;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        etRoomId = findViewById(R.id.etRoomId);
        etRoomName = findViewById(R.id.etRoomName);
        etPrice = findViewById(R.id.etPrice);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        btnBack = findViewById(R.id.btnBack);

        // Thiết lập Spinner
        String[] statusOptions = {"Available", "Occupied"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statusOptions);
        spinnerStatus.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());
        btnCancel.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> {
            String id = etRoomId.getText().toString().trim();
            String name = etRoomName.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();
            boolean isOccupied = spinnerStatus.getSelectedItemPosition() == 1;

            if (id.isEmpty() || name.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(priceStr);

            // Trả kết quả về MainActivity
            Intent intent = new Intent();
            intent.putExtra("roomId", id);
            intent.putExtra("roomName", name);
            intent.putExtra("price", price);
            intent.putExtra("isOccupied", isOccupied);
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}
