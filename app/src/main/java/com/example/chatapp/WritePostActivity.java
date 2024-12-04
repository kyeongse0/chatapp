package com.example.chatapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class WritePostActivity extends AppCompatActivity {
    private EditText titleInput;
    private EditText contentInput;
    private ImageView imagePreview;
    private String selectedImageUrl = ""; // 예제: 이미지를 선택했다면 URL 또는 경로 저장

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

        titleInput = findViewById(R.id.titleInput);
        contentInput = findViewById(R.id.contentInput);
        imagePreview = findViewById(R.id.imagePreview);
        Button submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(view -> {
            String title = titleInput.getText().toString();
            String content = contentInput.getText().toString();

            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
                titleInput.setError("제목을 입력하세요!");
                contentInput.setError("내용을 입력하세요!");
                return;
            }

            Post newPost = new Post(title, content, selectedImageUrl);
            Intent resultIntent = new Intent();
            resultIntent.putExtra("newPost", newPost);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });

        imagePreview.setOnClickListener(view -> {
            // TODO: 이미지 선택 로직 (갤러리에서 이미지 가져오기)
            selectedImageUrl = "이미지_경로_또는_URL"; // 예제 URL
        });
    }
}