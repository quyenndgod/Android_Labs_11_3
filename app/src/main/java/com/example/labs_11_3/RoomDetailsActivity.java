package com.example.labs_11_3;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RoomDetailsActivity extends AppCompatActivity {

    private TextView tvRoomName, tvRoomId, tvStatusBadge, tvPrice, tvStatus, tvTenantName, tvPhone;
    private ImageButton btnBack;
    private ImageView ivStatusIcon;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_details);

        tvRoomName = findViewById(R.id.tvRoomName);
        tvRoomId = findViewById(R.id.tvRoomId);
        tvStatusBadge = findViewById(R.id.tvStatusBadge);
        tvPrice = findViewById(R.id.tvPrice);
        tvStatus = findViewById(R.id.tvStatus);
        tvTenantName = findViewById(R.id.tvTenantName);
        tvPhone = findViewById(R.id.tvPhone);
        btnBack = findViewById(R.id.btnBack);
        ivStatusIcon = findViewById(R.id.ivStatusIcon);

        Room room = (Room) getIntent().getSerializableExtra("room");

        if (room != null) {
            tvRoomName.setText(room.getRoomName());
            tvRoomId.setText("ID: " + room.getRoomId());
            tvPrice.setText("$" + (int)room.getPrice() + "/month");
            tvStatus.setText(room.getStatus());
            tvTenantName.setText(room.isOccupied() ? room.getTenantName() : "N/A");
            tvPhone.setText(room.isOccupied() ? room.getPhoneNumber() : "N/A");

            if (room.isOccupied()) {
                tvStatusBadge.setText("Occupied");
                tvStatusBadge.setBackgroundResource(R.drawable.bg_badge_red);
                ivStatusIcon.setBackgroundResource(R.drawable.bg_icon_red_light);
                ivStatusIcon.setColorFilter(Color.parseColor("#E57373"));
            } else {
                tvStatusBadge.setText("Available");
                updateBadgeToGreen(tvStatusBadge);
                ivStatusIcon.setBackgroundResource(R.drawable.bg_icon_green_light);
                ivStatusIcon.setColorFilter(Color.parseColor("#81C784"));
            }
        }

        btnBack.setOnClickListener(v -> finish());
    }

    private void updateBadgeToGreen(TextView tv) {
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(40);
        shape.setColor(Color.parseColor("#81C784"));
        tv.setBackground(shape);
    }
}
