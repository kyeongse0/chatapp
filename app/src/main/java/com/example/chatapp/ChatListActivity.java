package com.example.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ChatListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ChatListAdapter chatListAdapter;
    private ArrayList<ChatRoom> chatRoomList;

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        chatRoomList = new ArrayList<>();
        chatListAdapter = new ChatListAdapter(this, chatRoomList, this::onChatRoomSelected);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatListAdapter);

        connectToServer();
    }

    private void connectToServer() {
        new Thread(() -> {
            try {
                socket = new Socket("172.30.1.23", 6510); // 채팅 서버 IP와 포트
                writer = new PrintWriter(socket.getOutputStream(), true);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                writer.println("ChatListClient");

                String message;
                while ((message = reader.readLine()) != null) {
                    if (message.startsWith("ROOM:")) {
                        String[] parts = message.substring(5).split(";", 2);
                        ChatRoom newRoom = new ChatRoom(parts[0], parts[1]);
                        runOnUiThread(() -> {
                            chatRoomList.add(newRoom);
                            chatListAdapter.notifyDataSetChanged();
                        });
                    }
                }
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "서버 연결 실패", Toast.LENGTH_SHORT).show());
                e.printStackTrace();
            }
        }).start();
    }

    private void onChatRoomSelected(ChatRoom chatRoom) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("roomId", chatRoom.getId());
        startActivity(intent);
    }
}