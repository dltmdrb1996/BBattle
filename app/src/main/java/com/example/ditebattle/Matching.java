package com.example.ditebattle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Matching extends AppCompatActivity {
    Button matchingPlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);
        ActionBar ac = getSupportActionBar();
        ac.hide();

        matchingPlay = (Button)findViewById(R.id.matchingPlay);
        matchingPlay.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent intent = new Intent(Matching.this, MatchingList.class);
                startActivity(intent);
            }
        });

    }
}
