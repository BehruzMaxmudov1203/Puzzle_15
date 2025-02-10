package com.example.puzle_15_mukammal;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Recort_3 extends AppCompatActivity {
    private TextView score1, score2, score3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recort3);

        score1 = findViewById(R.id.best_time_1);
        score2 = findViewById(R.id.best_time_2);
        score3 = findViewById(R.id.best_time_3);
        findViewById(R.id.btnHome1).setOnClickListener(view -> {
            finish();
            MediaPlayer.create(this, R.raw.click).start();
        });
        loadBestScores();
    }
    @SuppressLint("SetTextI18n")
    private void loadBestScores() {
        SharedPreferences preferences = getSharedPreferences("game_prefs", MODE_PRIVATE);
        int bestScore1 = preferences.getInt("best_score_1", 0);
        int bestScore2 = preferences.getInt("best_score_2", 0);
        int bestScore3 = preferences.getInt("best_score_3", 0);

        // Natijalarni TextViewga chiqarish
        score1.setText("1. " + Records.getRecord(1));
        score2.setText("2. " + Records.getRecord(2));
        score3.setText("3. " + Records.getRecord(3));
    }

}