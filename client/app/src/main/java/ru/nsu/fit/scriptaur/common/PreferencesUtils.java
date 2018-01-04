package ru.nsu.fit.scriptaur.common;

import android.content.Context;

public class PreferencesUtils {

    private final static String PREFERENCES_FILE_NAME = "user_info";
    private final static String TOKEN_KEY = "TOKEN";

    private PreferencesUtils() {
    }

    public static String getToken(Context context) {
        return context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)
                .getString(TOKEN_KEY, null);
    }

    public static void setToken(Context context, String newToken) {
        context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)
                .edit().putString("token", newToken).apply();
    }
}
