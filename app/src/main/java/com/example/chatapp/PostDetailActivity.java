package com.example.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PostDetailActivity extends AppCompatActivity {
    private TextView postTitleTextView;
    private Button backButton, chatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        postTitleTextView = findViewById(R.id.postTitleTextView);
        backButton = findViewById(R.id.backButton);
        chatButton = findViewById(R.id.chatButton);

        String postTitle = getIntent().getStringExtra("postTitle");
        postTitleTextView.setText(postTitle);

        backButton.setOnClickListener(v -> finish());

        chatButton.setOnClickListener(v -> {
            Intent intent = new Intent(PostDetailActivity.this, ChatActivity.class);
            startActivity(intent);
        });
    }
}