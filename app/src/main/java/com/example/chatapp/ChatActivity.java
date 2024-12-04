package com.example.chatapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private ArrayList<Message> messageList;

    private EditText inputMessage;
    private ImageButton sendButton;

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    private String roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.recyclerView);
        inputMessage = findViewById(R.id.inputMessage);
        sendButton = findViewById(R.id.sendButton);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messageList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        roomId = getIntent().getStringExtra("roomId");

        connectToServer();
        sendButton.setOnClickListener(this::sendMessage);
    }

    private void connectToServer() {
        new Thread(() -> {
            try {
                socket = new Socket("172.30.1.23", 6510); // 채팅 서버 IP와 포트
                writer = new PrintWriter(socket.getOutputStream(), true);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                writer.println("JOIN:" + roomId);

                String message;
                while ((message = reader.readLine()) != null) {
                    if (message.startsWith("MSG:")) {
                        String text = message.substring(4);
                        runOnUiThread(() -> {
                            messageList.add(new Message(text, false));
                            messageAdapter.notifyDataSetChanged();
                            recyclerView.scrollToPosition(messageList.size() - 1);
                        });
                    }
                }
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "서버 연결 실패", Toast.LENGTH_SHORT).show());
                e.printStackTrace();
            }
        }).start();
    }

    private void sendMessage(View view) {
        String message = inputMessage.getText().toString().trim();
        if (!message.isEmpty()) {
            writer.println("MSG:" + message);
            messageList.add(new Message(message, true));
            messageAdapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(messageList.size() - 1);
            inputMessage.setText("");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (writer != null) writer.close();
            if (reader != null) reader.close();
            if (socket != null) socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}