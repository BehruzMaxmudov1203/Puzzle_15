package com.example.puzle_15_mukammal;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class LocalStorage {
    private static LocalStorage instance = new LocalStorage();
    private static SharedPreferences sharedPreferences;
    private static final String MUSIC = "music1";

    // continue
    private static final String TEXT_MOVES = "moves";
    private static final String TEXT_TIME = "time";
    private static final String BUTTON = "Button";
    private static final ArrayList<String> list = new ArrayList<>();
    private static final String IN_GAME = "in_game";

    private LocalStorage() {}

    static void init(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(MUSIC, Context.MODE_PRIVATE);
        }
    }

    public static LocalStorage getInstance() {
        return instance;
    }


    public void setTextMoves(String textMoves) {
        sharedPreferences.edit().putString(TEXT_MOVES, textMoves).apply();
    }

    public String getTextMoves() {
        return sharedPreferences.getString(TEXT_MOVES, "0");
    }

    void setContinue(boolean b){
        sharedPreferences.edit().putBoolean(IN_GAME, b).apply();
    }

    boolean getContinue(){
        return sharedPreferences.getBoolean(IN_GAME, false);
    }

    public void setTextTime(long time) {
        sharedPreferences.edit().putLong(TEXT_TIME, time).apply();
    }

    public long getTextTime() {
        return sharedPreferences.getLong(TEXT_TIME, 0);
    }

    void setMusic(boolean val) {
        sharedPreferences.edit().putBoolean(MUSIC, val).apply();
    }

    boolean getMusic() {
        return sharedPreferences.getBoolean(MUSIC, false);
    }

    public void setDate(String  date){
        sharedPreferences.edit().putString("date",date).apply();
    }
    public String  getDate(){
        return sharedPreferences.getString("date","");

    }
    public boolean getRestart(String keyRestart){
        return sharedPreferences.getBoolean(keyRestart,false);
    }
    public void setRestart(String keyRestart, boolean bool){
         sharedPreferences.edit().putBoolean(keyRestart,bool).apply();
    }
}
