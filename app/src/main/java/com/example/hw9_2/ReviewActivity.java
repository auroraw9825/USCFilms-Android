package com.example.hw9_2;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReviewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        getIncomingIntent();
    }

    private void getIncomingIntent(){

        if(getIntent().hasExtra("authorCreatedTime") && getIntent().hasExtra("ratingStar") && getIntent().hasExtra("content")){

            String  authorCreatedTime= getIntent().getStringExtra("authorCreatedTime");
            String ratingStar = getIntent().getStringExtra("ratingStar");
            String content = getIntent().getStringExtra("content");

            TextView review_author = findViewById(R.id.review_author);
            TextView review_rating = findViewById(R.id.review_rating);
            TextView review_content = findViewById(R.id.review_content);

            review_author.setText(authorCreatedTime);
            review_rating.setText(ratingStar);
            review_content.setText(content);


        }
    }
}
