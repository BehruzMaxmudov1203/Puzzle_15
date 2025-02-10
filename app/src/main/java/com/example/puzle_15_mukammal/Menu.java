package com.example.puzle_15_mukammal;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);


        findViewById(R.id.btnExit).setOnClickListener(view -> {
            finishAffinity();
            MediaPlayer.create(this, R.raw.click).start();
        });

    }

    public void clickPlay(View view) {
        MediaPlayer.create(this, R.raw.click).start();
        Intent intent = new Intent(Menu.this, MainActivity.class);
        startActivity(intent);
    }
    public void clickPlay1(View view) {
        MediaPlayer.create(this, R.raw.click).start();
        Intent intent = new Intent(Menu.this, Recort_3.class);
        startActivity(intent);
    }
    public void clickPlay2(View view) {
        MediaPlayer.create(this, R.raw.click).start();
        Intent intent = new Intent(Menu.this,info.class);
        startActivity(intent);
    }
}
