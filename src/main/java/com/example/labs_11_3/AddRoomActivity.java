package com.example.labs_11_3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class AddRoomActivity extends AppCompatActivity {

    private EditText etRoomId, etRoomName, etPrice, etTenantName, etPhoneNumber;
    private TextInputLayout layoutRoomId, layoutRoomName, layoutPrice, layoutTenantName, layoutPhone;
    private Spinner spinnerStatus;
    private Button btnSave, btnCancel;
    private ImageButton btnBack;
    private TextView tvTitle;
    private Room editRoom;
    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        // Bind views
        etRoomId = findViewById(R.id.etRoomId);
        etRoomName = findViewById(R.id.etRoomName);
        etPrice = findViewById(R.id.etPrice);
        etTenantName = findViewById(R.id.etTenantName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        
        // Bind TextInputLayout using IDs from XML
        layoutRoomId = findViewById(R.id.layoutRoomId);
        layoutRoomName = findViewById(R.id.layoutRoomName);
        layoutPrice = findViewById(R.id.layoutPrice);
        layoutTenantName = findViewById(R.id.layoutTenantName);
        layoutPhone = findViewById(R.id.layoutPhone);
        
        spinnerStatus = findViewById(R.id.spinnerStatus);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        btnBack = findViewById(R.id.btnBack);
        tvTitle = findViewById(R.id.tvTitle);

        // Thiết lập Spinner
        String[] statusOptions = {"Available", "Occupied"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statusOptions);
        spinnerStatus.setAdapter(adapter);

        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) { // Occupied
                    layoutTenantName.setVisibility(View.VISIBLE);
                    layoutPhone.setVisibility(View.VISIBLE);
                } else { // Available
                    layoutTenantName.setVisibility(View.GONE);
                    layoutPhone.setVisibility(View.GONE);
                    layoutTenantName.setError(null);
                    layoutPhone.setError(null);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        editRoom = (Room) getIntent().getSerializableExtra("room");
        position = getIntent().getIntExtra("position", -1);

        if (editRoom != null) {
            tvTitle.setText("Edit Room");
            btnSave.setText("UPDATE ROOM");
            etRoomId.setText(editRoom.getRoomId());
            etRoomId.setEnabled(false);
            etRoomName.setText(editRoom.getRoomName());
            etPrice.setText(String.valueOf((int)editRoom.getPrice()));
            spinnerStatus.setSelection(editRoom.isOccupied() ? 1 : 0);
            etTenantName.setText(editRoom.getTenantName());
            etPhoneNumber.setText(editRoom.getPhoneNumber());
        }

        btnBack.setOnClickListener(v -> finish());
        btnCancel.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> {
            if (validateData()) {
                sendResultBack();
            }
        });
    }

    private boolean validateData() {
        boolean isValid = true;
        
        String id = etRoomId.getText().toString().trim();
        String name = etRoomName.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        boolean isOccupied = spinnerStatus.getSelectedItemPosition() == 1;
        String tenant = etTenantName.getText().toString().trim();
        String phone = etPhoneNumber.getText().toString().trim();

        // 1. Validate ID phòng > 3 kí tự
        if (id.length() <= 3) {
            layoutRoomId.setError("Room ID must be more than 3 characters");
            isValid = false;
        } else {
            layoutRoomId.setError(null);
        }

        // 2. Validate tên phòng 3-30 kí tự
        if (name.length() < 3 || name.length() > 30) {
            layoutRoomName.setError("Room Name must be between 3 and 30 characters");
            isValid = false;
        } else {
            layoutRoomName.setError(null);
        }

        // 3. Validate giá tiền > 0
        try {
            if (priceStr.isEmpty() || Double.parseDouble(priceStr) <= 0) {
                layoutPrice.setError("Price must be greater than 0");
                isValid = false;
            } else {
                layoutPrice.setError(null);
            }
        } catch (NumberFormatException e) {
            layoutPrice.setError("Invalid price format");
            isValid = false;
        }

        // 4. Validate thông tin người thuê nếu là Occupied
        if (isOccupied) {
            // Tên người 3-30 kí tự
            if (tenant.length() < 3 || tenant.length() > 30) {
                layoutTenantName.setError("Tenant Name must be between 3 and 30 characters");
                isValid = false;
            } else {
                layoutTenantName.setError(null);
            }

            // Validate Số điện thoại (kiểm tra rỗng và độ dài cơ bản)
            if (phone.isEmpty() || phone.length() < 10 || phone.length() > 11) {
                layoutPhone.setError("Phone Number must be 10-11 digits");
                isValid = false;
            } else {
                layoutPhone.setError(null);
            }
        }

        return isValid;
    }

    private void sendResultBack() {
        Intent intent = new Intent();
        intent.putExtra("roomId", etRoomId.getText().toString().trim());
        intent.putExtra("roomName", etRoomName.getText().toString().trim());
        intent.putExtra("price", Double.parseDouble(etPrice.getText().toString().trim()));
        boolean isOccupied = spinnerStatus.getSelectedItemPosition() == 1;
        intent.putExtra("isOccupied", isOccupied);
        intent.putExtra("tenantName", isOccupied ? etTenantName.getText().toString().trim() : "");
        intent.putExtra("phoneNumber", isOccupied ? etPhoneNumber.getText().toString().trim() : "");
        intent.putExtra("position", position);
        setResult(RESULT_OK, intent);
        finish();
    }
}
