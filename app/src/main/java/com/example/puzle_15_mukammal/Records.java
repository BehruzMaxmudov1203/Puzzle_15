package com.example.puzle_15_mukammal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.widget.Chronometer;

public class Records {
    private static SharedPreferences sharedPreferences;
    private static final String RECORD = "record";
    private static final String RECORD_TIME1 = "time1";
    private static final String RECORD_TIME2 = "time2";
    private static final String RECORD_TIME3 = "time3";

    private static int record_time1;
    private static int record_time2;
    private static int record_time3;

    static void init(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(RECORD, Context.MODE_PRIVATE);
        }
        record_time1 = sharedPreferences.getInt(RECORD_TIME1, Integer.MAX_VALUE);
        record_time2 = sharedPreferences.getInt(RECORD_TIME2, Integer.MAX_VALUE);
        record_time3 = sharedPreferences.getInt(RECORD_TIME3, Integer.MAX_VALUE);
    }

    static void saveRecord(Chronometer chronometer, String moves) {
        int elapsedMillis = (int) (SystemClock.elapsedRealtime() - chronometer.getBase());
        int recordNum = getRecordNum(elapsedMillis);
        if (recordNum == -1) return;

        String key = RECORD + recordNum;
        String value = chronometer.getText().toString() + "  " + moves;

        sharedPreferences.edit().putString(key, value).apply();
    }

    static int getRecordNum(int millis) {
        if (millis < record_time1) {
            record_time3 = record_time2;
            record_time2 = record_time1;
            record_time1 = millis;
            rearrangeRecords(1);
            return 1;
        } else if (millis < record_time2) {
            record_time3 = record_time2;
            record_time2 = millis;
            rearrangeRecords(2);
            return 2;
        } else if (millis < record_time3) {
            record_time3 = millis;
            rearrangeRecords(3);
            return 3;
        }
        return -1;
    }

    private static void rearrangeRecords(int recordNum) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(RECORD_TIME1, record_time1);
        editor.putInt(RECORD_TIME2, record_time2);
        editor.putInt(RECORD_TIME3, record_time3);

        if (recordNum == 1) {
            editor.putString(RECORD + 3, sharedPreferences.getString(RECORD + 2, "no records yet"));
            editor.putString(RECORD + 2, sharedPreferences.getString(RECORD + 1, "no records yet"));
        } else if (recordNum == 2) {
            editor.putString(RECORD + 3, sharedPreferences.getString(RECORD + 2, "no records yet"));
        }

        editor.apply();
    }

    public static String getRecord(int recordNum) {
        return sharedPreferences.getString(RECORD + recordNum, "no record yet");
    }
}
