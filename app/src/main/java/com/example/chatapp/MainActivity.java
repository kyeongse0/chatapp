package com.example.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_NEW_POST = 100;

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private ArrayList<Post> postList;

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(this, postList);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(postAdapter);

        findViewById(R.id.writePostButton).setOnClickListener(view -> {
            Intent intent = new Intent(this, WritePostActivity.class);
            startActivityForResult(intent, REQUEST_NEW_POST);
        });

        findViewById(R.id.chatListButton).setOnClickListener(view -> {
            Intent intent = new Intent(this, ChatListActivity.class);
            startActivity(intent);
        });

        connectToServer();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_NEW_POST && resultCode == RESULT_OK && data != null) {
            Post newPost = (Post) data.getSerializableExtra("newPost");
            if (newPost != null) {
                sendPostToServer(newPost);
            }
        }
    }

    private void connectToServer() {
        new Thread(() -> {
            try {
                socket = new Socket("172.30.1.23", 6510); // 서버 IP 및 포트
                writer = new PrintWriter(socket.getOutputStream(), true);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // 클라이언트 ID 전송
                writer.println("MainClient");

                // 서버로부터 메시지 수신
                String message;
                while ((message = reader.readLine()) != null) {
                    if (message.startsWith("POST:")) {
                        String[] parts = message.substring(5).split(";", 3);
                        Post newPost = new Post(parts[0], parts[1], parts[2]);
                        runOnUiThread(() -> {
                            postList.add(0, newPost);
                            postAdapter.notifyDataSetChanged();
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void sendPostToServer(Post post) {
        if (writer != null) {
            writer.println("POST:" + post.getTitle() + ";" + post.getContent() + ";" + post.getImageUrl());
        }
    }
}