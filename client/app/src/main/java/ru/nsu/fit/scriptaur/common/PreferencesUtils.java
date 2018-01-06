package ru.nsu.fit.scriptaur.common;

import android.content.Context;
import android.preference.PreferenceManager;

public class PreferencesUtils {

    private final static String TOKEN_KEY = "TOKEN";

    private PreferencesUtils() {
    }

    public static String getToken(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(TOKEN_KEY, null);
    }

    public static void setToken(Context context, String newToken) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putString(TOKEN_KEY, newToken).apply();
    }
}
