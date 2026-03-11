package com.example.labs_11_3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
#
public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_ROOM = 1;
    private static final int REQUEST_CODE_EDIT_ROOM = 2;
    private List<Room> roomList;
    private RoomAdapter adapter;
    private RecyclerView rvRooms;
    private ImageButton btnAddRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo dữ liệu mẫu
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
                Intent intent = new Intent(MainActivity.this, AddRoomActivity.class);
                intent.putExtra("room", room);
                intent.putExtra("position", position);
                startActivityForResult(intent, REQUEST_CODE_EDIT_ROOM);
            }

            @Override
            public void onDelete(Room room, int position) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete Confirmation")
                        .setMessage("Are you sure you want to delete this room?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            roomList.remove(position);
                            adapter.notifyItemRemoved(position);
                            adapter.notifyItemRangeChanged(position, roomList.size());
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }

            @Override
            public void onView(Room room, int position) {
                Intent intent = new Intent(MainActivity.this, RoomDetailsActivity.class);
                intent.putExtra("room", room);
                startActivity(intent);
            }
        });

        rvRooms.setLayoutManager(new LinearLayoutManager(this));
        rvRooms.setAdapter(adapter);

        // Mở AddRoomActivity khi nhấn nút +
        btnAddRoom.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddRoomActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_ROOM);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            String id = data.getStringExtra("roomId");
            String name = data.getStringExtra("roomName");
            double price = data.getDoubleExtra("price", 0);
            boolean isOccupied = data.getBooleanExtra("isOccupied", false);
            String tenantName = data.getStringExtra("tenantName");
            String phoneNumber = data.getStringExtra("phoneNumber");
            int position = data.getIntExtra("position", -1);

            if (requestCode == REQUEST_CODE_ADD_ROOM) {
                Room newRoom = new Room(id, name, price, isOccupied, tenantName, phoneNumber);
                roomList.add(newRoom);
                adapter.notifyItemInserted(roomList.size() - 1);
                rvRooms.scrollToPosition(roomList.size() - 1);
            } else if (requestCode == REQUEST_CODE_EDIT_ROOM && position != -1) {
                Room room = roomList.get(position);
                room.setRoomName(name);
                room.setPrice(price);
                room.setOccupied(isOccupied);
                room.setTenantName(tenantName);
                room.setPhoneNumber(phoneNumber);
                adapter.notifyItemChanged(position);
            }
        }
    }
}
