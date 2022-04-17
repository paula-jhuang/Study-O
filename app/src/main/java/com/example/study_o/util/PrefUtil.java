package com.example.study_o.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.study_o.SecondFragment;

public class PrefUtil {
    private static final String TIMER_LENGTH = "com.paulahuang.study_o.timer_length";

    private static final String  PREV_TIMER_LENGTH_SECONDS = "com.paulahuang.study_o.previous_timer_length";

    public static int getTimerLength(Context context){
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//        return preferences.getInt(TIMER_LENGTH, 10);
        return 1;
    }

    public static Long getPrevTimerLengthSeconds(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong(PREV_TIMER_LENGTH_SECONDS, 0);
    }

    public static void setPrevTimerLengthSeconds(Long seconds, Context context){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putLong(PREV_TIMER_LENGTH_SECONDS, seconds);
        editor.apply();
    }

    private static final String TIMER_STATE = "com.paulahuang.study_o.timer_state";

    public static SecondFragment.TimerState getTimerState(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int ordinal = preferences.getInt(TIMER_STATE, 0);
        return SecondFragment.TimerState.values()[ordinal];
    }

    public static void setTimerState(SecondFragment.TimerState state, Context context){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        int ordinal = state.ordinal();
        editor.putInt(TIMER_STATE, ordinal);
        editor.apply();
    }

    private static final String  SECONDS_REMAINING = "com.example.cycle.seconds_remaining";

    public static Long getSecondsRemaining(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong(SECONDS_REMAINING, 0);
    }

    public static void setSecondsRemaining(Long seconds, Context context){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putLong(SECONDS_REMAINING, seconds);
        editor.apply();
    }
}
