package com.example.puzle_15_mukammal;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AppCompatButton[][] items;
    private List<Integer> numbers = new ArrayList<>();
    private int emptyX = 0;
    private int emptyY = 0;
    private TextView bestScoreTV, scoreTV;
    private Chronometer timeView;
    private  MediaPlayer mediaPlayer;
    private int score = 0;
    private static final int LEVEL = 4;

    private LocalStorage localStorage = LocalStorage.getInstance();

    private long timeBase = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mediaPlayer = MediaPlayer.create(this, R.raw.musiqa);
        mediaPlayer.setLooping(true); // Musiqa takrorlanishi uchun

        setContentView(R.layout.activity_main_test3);
        bestScoreTV = findViewById(R.id.best_score_tv_test);
        scoreTV = findViewById(R.id.score_tv_test);
        timeView = findViewById(R.id.time_view);

        SharedPreferences preferences = getSharedPreferences("game_prefs", MODE_PRIVATE);
        long savedTime = preferences.getLong("last_game_time", 0);

        if (savedInstanceState == null) {
            timeView.setBase(SystemClock.elapsedRealtime());
            timeView.start();
        }

        timeView.start();
        getNumber();
        findView();
        loadDataToView();
        bestScoreTV.setText(getBestScore());
    }

    @Override
    protected void onStart() {
        super.onStart();
        startTime();

    }

    private void updateScore() {
        score++;
        scoreTV.setText(String.valueOf(score));
        long elapsedTime = SystemClock.elapsedRealtime() - timeView.getBase();
        updateBestScores(score, elapsedTime);
    }

    private void updateBestScores(int newScore, long elapsedTime) {
        SharedPreferences preferences = getSharedPreferences("game_prefs", MODE_PRIVATE);
        int best1 = preferences.getInt("best_score_1", 0);
        int best2 = preferences.getInt("best_score_2", 0);
        int best3 = preferences.getInt("best_score_3", 0);
        Log.d("TTT", "updateBestScores 1: " + best1);
        Log.d("TTT", "updateBestScores 2: " + best2);
        Log.d("TTT", "updateBestScores 3: " + best3);
        long bestTime1 = preferences.getLong("best_time_1", Long.MAX_VALUE);
        long bestTime2 = preferences.getLong("best_time_2", Long.MAX_VALUE);
        long bestTime3 = preferences.getLong("best_time_3", Long.MAX_VALUE);

        // Yangi score va vaqtni eng yaxshi uchta rekordga qo'shish
        if (newScore < best1 /*|| (newScore == best1 && elapsedTime < bestTime1)*/) {
            preferences.edit()
                    .putInt("best_score_3", best2)
                    .putInt("best_score_2", best1)
                    .putInt("best_score_1", newScore)
//                    .putLong("best_time_3", bestTime2)
//                    .putLong("best_time_2", bestTime1)
//                    .putLong("best_time_1", elapsedTime)
                    .apply();
            Log.d("TAG", "best1 = " + best1);
        } else if (newScore < best2 /*|| (newScore == best2 && elapsedTime < bestTime2)*/) {
            preferences.edit()
                    .putInt("best_score_3", best2)
                    .putInt("best_score_2", newScore)
//                    .putLong("best_time_3", bestTime2)
//                    .putLong("best_time_2", elapsedTime)
                    .apply();
            Log.d("TAG", "best2 = " + best2);
        } else if (newScore < best3 /*|| (newScore == best3 && elapsedTime < bestTime3)*/) {
            preferences.edit()
                    .putInt("best_score_3", newScore)
//                    .putLong("best_time_3", elapsedTime)
                    .apply();
            Log.d("TAG", "best3 = " + best3);
        }
    }

    ///

    private String getBestScore() {
        String[] parts = Records.getRecord(1).split(" ");
        return parts[parts.length - 1]; // Extracts the second part and converts it to an integer
    }

    private void getNumber() {
        numbers = new ArrayList<>();
        for (int i = 1; i < 16; i++) {
            numbers.add(i);
        }
        numbers.add(0);
    }

    private void loadDataToView() {
        do {
            Collections.shuffle(numbers);
        }
        while (UnsolvableCase.isSolvable((ArrayList<Integer>) numbers));

//!check()
        for (int i = 0; i < LEVEL; i++) {
            for (int j = 0; j < LEVEL; j++) {
                if (numbers.get(i * LEVEL + j) == 0) {
                    items[i][j].setVisibility(View.INVISIBLE);
                    emptyX = i;
                    emptyY = j;
                } else {
                    items[i][j].setVisibility(View.VISIBLE);
                    items[i][j].setText(String.valueOf(numbers.get(i * LEVEL + j)));
                }
                items[i][j].setBackgroundResource(R.drawable.puzzle_button);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        long elapsedMillis = SystemClock.elapsedRealtime() - timeView.getBase(); // O‘tgan vaqtni olish
        outState.putLong("time_elapsed", elapsedMillis); // Vaqtni saqlash
        outState.putInt("score", score); // Qadamlarni saqlash

        // Bo‘sh joyning koordinatalarini saqlash
        outState.putInt("emptyX", emptyX);
        outState.putInt("emptyY", emptyY);

        // Puzzle maydonini saqlash
        int[] boardState = new int[LEVEL * LEVEL];
        for (int i = 0; i < LEVEL; i++) {
            for (int j = 0; j < LEVEL; j++) {
                boardState[i * LEVEL + j] = items[i][j].getVisibility() == View.INVISIBLE ? 0 :
                        Integer.parseInt(items[i][j].getText().toString());
            }
        }
        outState.putIntArray("board_state", boardState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            long timeElapsed = savedInstanceState.getLong("time_elapsed");
            score = savedInstanceState.getInt("score");

            // O‘tgan vaqtni qayta o‘rnatish
            timeView.setBase(SystemClock.elapsedRealtime() - timeElapsed);
            timeView.start();

            // Qadamlarni qayta tiklash
            scoreTV.setText(String.valueOf(score));

            // Poytaxtning o'rnini tiklash
            emptyX = savedInstanceState.getInt("emptyX");
            emptyY = savedInstanceState.getInt("emptyY");

            // Maydonni qayta tiklash
            int[] boardState = savedInstanceState.getIntArray("board_state");
            if (boardState != null) {
                for (int i = 0; i < LEVEL; i++) {
                    for (int j = 0; j < LEVEL; j++) {
                        if (boardState[i * LEVEL + j] == 0) {
                            items[i][j].setVisibility(View.INVISIBLE);
                        } else {
                            items[i][j].setText(String.valueOf(boardState[i * LEVEL + j]));
                            items[i][j].setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }
    }


    private void restart() {
        MediaPlayer.create(this, R.raw.click).start();
        score = 0;
        scoreTV.setText(String.valueOf(score));

        timeView.setBase(SystemClock.elapsedRealtime());
        timeView.start();
        loadDataToView();
    }

    private void findView() {
        findViewById(R.id.btnRefresh).setOnClickListener(v -> restart());
        findViewById(R.id.btnHome).setOnClickListener(v -> finish());

        ViewGroup container = findViewById(R.id.container_game);
        items = new AppCompatButton[LEVEL][LEVEL];

        for (int row = 0; row < LEVEL; row++) {
            LinearLayout linear = (LinearLayout) container.getChildAt(row);
            for (int col = 0; col < LEVEL; col++) {
                AppCompatButton button = (AppCompatButton) linear.getChildAt(col);
                button.setTag(new CoordinateData(row, col));
                items[row][col] = button;

                button.setOnClickListener(view -> {
                    AppCompatButton temp = (AppCompatButton) view;
                    CoordinateData data = (CoordinateData) temp.getTag();
                    checkCanMove(data.x, data.y);
                });
            }
        }
    }

    private void checkCanMove(int x, int y) {
        MediaPlayer.create(this, R.raw.click).start();
        if ((Math.abs(emptyX - x) == 1 && emptyY == y) || (Math.abs(emptyY - y) == 1 && emptyX == x)) {
            items[emptyX][emptyY].setVisibility(View.VISIBLE);
            items[emptyX][emptyY].setText(items[x][y].getText());
            items[x][y].setText("0");
            items[x][y].setVisibility(View.INVISIBLE);
            emptyX = x;
            emptyY = y;
            updateScore();

            if (gameOver()) {
                timeView.stop();
                Toast.makeText(MainActivity.this, "Win", Toast.LENGTH_SHORT).show();
                Records.saveRecord(timeView, scoreTV.getText().toString());
            }
        }
    }

    private boolean gameOver() {
        for (int i = 0; i < LEVEL; i++) {
            for (int j = 0; j < LEVEL; j++) {
                if ((i * LEVEL + j) == 15) return true;
                if (Integer.parseInt(items[i][j].getText().toString()) != (i * LEVEL + j + 1)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        long elapsedMillis = SystemClock.elapsedRealtime() - timeView.getBase();
//         Vaqtni saqlash
        SharedPreferences preferences = getSharedPreferences("game_prefs", MODE_PRIVATE);
        preferences.edit().putLong("last_game_time", elapsedMillis).apply();
        stopTime();
    }

/*    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences preferences = getSharedPreferences("game_prefs", MODE_PRIVATE);
        preferences.edit().remove("last_game_time").apply();
    }*/

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }


    ///

    private void startTime() {
        timeBase = localStorage.getTextTime();
        timeView.setBase(SystemClock.elapsedRealtime() + timeBase);
//        timeView.setBase(SystemClock.elapsedRealtime() + viewModel.getTime().getValue());
        timeView.start();
    }

    private void stopTime() {
        localStorage.setTextTime(timeView.getBase() - SystemClock.elapsedRealtime());
        timeView.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer  != null && !mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    ///

    private boolean check() {
        List<Integer> flatNumbers = new ArrayList<>();

        for (int num : numbers) {
            if (num != 0) {
                flatNumbers.add(num);
            }
        }

        int inversions = 0;
        for (int i = 0; i < flatNumbers.size(); i++) {
            for (int j = i + 1; j < flatNumbers.size(); j++) {
                if (flatNumbers.get(i) > flatNumbers.get(j)) {
                    inversions++;
                }
            }
        }

        int emptyRow = numbers.indexOf(0) / LEVEL + 1;

        return (emptyRow % 2 == 0 && inversions % 2 != 0) || (emptyRow % 2 != 0 && inversions % 2 == 0);
    }

    static class CoordinateData {
        int x, y;

        CoordinateData(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}