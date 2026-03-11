package com.example.labs_11_3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Room> roomList;
    private RoomAdapter adapter;
    private RecyclerView rvRooms;
    private ImageButton btnAddRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo dữ liệu mẫu theo hình ảnh
        roomList = new ArrayList<>();
        roomList.add(new Room("R001", "Deluxe Room A", 1200, true, "John Smith", "0912345678"));
        roomList.add(new Room("R002", "Standard Room B", 800, false, "", ""));
        roomList.add(new Room("R003", "Premium Suite C", 1500, true, "Sarah Johnson", "0987654321"));
        roomList.add(new Room("R004", "Standard Room D", 850, false, "", ""));

        rvRooms = findViewById(R.id.rvRooms);
        btnAddRoom = findViewById(R.id.btnAddRoom);

        // Thiết lập RecyclerView
        adapter = new RoomAdapter(roomList, new RoomAdapter.OnRoomActionListener() {
            @Override
            public void onEdit(Room room, int position) {
                showRoomDialog(room, position);
            }

            @Override
            public void onDelete(Room room, int position) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa phòng này?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            roomList.remove(position);
                            adapter.notifyItemRemoved(position);
                            adapter.notifyItemRangeChanged(position, roomList.size());
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }

            @Override
            public void onView(Room room, int position) {
                Toast.makeText(MainActivity.this, "Xem chi tiết: " + room.getRoomName(), Toast.LENGTH_SHORT).show();
            }
        });

        rvRooms.setLayoutManager(new LinearLayoutManager(this));
        rvRooms.setAdapter(adapter);

        btnAddRoom.setOnClickListener(v -> showRoomDialog(null, -1));
    }

    private void showRoomDialog(Room room, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_room, null);
        builder.setView(dialogView);

        EditText etRoomId = dialogView.findViewById(R.id.etRoomId);
        EditText etRoomName = dialogView.findViewById(R.id.etRoomName);
        EditText etPrice = dialogView.findViewById(R.id.etPrice);
        CheckBox cbIsOccupied = dialogView.findViewById(R.id.cbIsOccupied);
        EditText etTenantName = dialogView.findViewById(R.id.etTenantName);
        EditText etPhoneNumber = dialogView.findViewById(R.id.etPhoneNumber);

        if (room != null) {
            builder.setTitle("Sửa thông tin phòng");
            etRoomId.setText(room.getRoomId());
            etRoomId.setEnabled(false);
            etRoomName.setText(room.getRoomName());
            etPrice.setText(String.valueOf(room.getPrice()));
            cbIsOccupied.setChecked(room.isOccupied());
            etTenantName.setText(room.getTenantName());
            etPhoneNumber.setText(room.getPhoneNumber());
        } else {
            builder.setTitle("Thêm phòng mới");
        }

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String id = etRoomId.getText().toString().trim();
            String name = etRoomName.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();
            
            if (id.isEmpty() || name.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(priceStr);
            boolean occupied = cbIsOccupied.isChecked();
            String tenant = etTenantName.getText().toString().trim();
            String phone = etPhoneNumber.getText().toString().trim();

            if (room == null) {
                roomList.add(new Room(id, name, price, occupied, tenant, phone));
                adapter.notifyItemInserted(roomList.size() - 1);
            } else {
                room.setRoomName(name);
                room.setPrice(price);
                room.setOccupied(occupied);
                room.setTenantName(tenant);
                room.setPhoneNumber(phone);
                adapter.notifyItemChanged(position);
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}
