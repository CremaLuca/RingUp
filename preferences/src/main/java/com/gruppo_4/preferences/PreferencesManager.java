package com.gruppo_4.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Class used to write and read configurations in Android
 *
 * @author Luca Crema
 * @version 1.1
 */
public class PreferencesManager {

    /**
     * @param context context of an Activity or Service
     * @return default shared preferences class
     */
    private static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * @param sharedPreferences can be the default or a custom shared preferences class
     * @return the editor for the preferences
     */
    private static SharedPreferences.Editor getEditor(SharedPreferences sharedPreferences) {
        return sharedPreferences.edit();
    }

    /**
     * @param context context of an Activity or Service
     * @return default shared preferences editor
     */
    private static SharedPreferences.Editor getEditor(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).edit();
    }

    /**
     * @param ctx context of an Activity or Service
     * @param key key for the resource
     * @return the value of the resource if present, -1 otherwise
     */
    public static int getInt(Context ctx, String key) {
        return getSharedPreferences(ctx).getInt(key, -1);
    }

    /**
     * @param ctx context of an Activity or Service
     * @param key key for the resource
     * @return the value of the resource if present, null otherwise
     */
    public static String getString(Context ctx, String key) {
        return getSharedPreferences(ctx).getString(key, null);
    }

    /**
     * @param ctx context of an Activity or Service
     * @param key key for the resource
     * @return the value of the resource if present, false otherwise
     */
    public static boolean getBoolean(Context ctx, String key) {
        return getSharedPreferences(ctx).getBoolean(key, false);
    }

    /**
     * @param ctx   context of an Activity or Service
     * @param key   key for the resource
     * @param value value to be put or override
     * @return if the value has been set correctly
     */
    public static boolean setInt(Context ctx, String key, int value) {
        SharedPreferences.Editor editor = getEditor(getSharedPreferences(ctx));
        editor.putInt(key, value);
        return editor.commit();
    }

    /**
     * @param ctx   context of an Activity or Service
     * @param key   key for the resource
     * @param value value to be put or override
     * @return if the value has been set correctly
     */
    public static boolean setString(Context ctx, String key, String value) {
        SharedPreferences.Editor editor = getEditor(getSharedPreferences(ctx));
        editor.putString(key, value);
        return editor.commit();
    }

    /**
     * @param ctx   context of an Activity or Service
     * @param key   key for the resource
     * @param value value to be put or override
     * @return if the value has been set correctly
     */
    public static boolean setBoolean(Context ctx, String key, boolean value) {
        SharedPreferences.Editor editor = getEditor(getSharedPreferences(ctx));
        editor.putBoolean(key, value);
        return editor.commit();
    }

    /**
     * Sums the value to an integer
     *
     * @param ctx   context of an Activity or Service
     * @param key   key for the resource
     * @param value value to be summed to the current value
     * @return if the value has been updated correctly
     */
    public static int updateInt(Context ctx, String key, int value) {
        int currentValue = getInt(ctx, key);
        if (currentValue == -1)
            currentValue = 0;
        SharedPreferences.Editor editor = getEditor(getSharedPreferences(ctx));
        editor.putInt(key, currentValue + value);
        editor.commit();
        return currentValue + value;
    }

    /**
     * Sums the value to 1
     *
     * @param ctx context of an Activity or Service
     * @param key key for the resource
     * @return if the value has been updated correctly
     */
    public static int updateInt(Context ctx, String key) {
        return updateInt(ctx, key, 1);
    }

    /**
     * Sums 1 to the integer saved in memory and goes back to 1 if it exceeds the maxValue
     *
     * @param ctx context of an Activity or Service
     * @param key key for the resource
     * @return result of the shift
     */
    public static int shiftInt(Context ctx, String key, int maxValue) {
        int currentValue = getInt(ctx, key);
        if (currentValue == -1)
            currentValue = 0;
        int nextValue = (currentValue + 1) % maxValue;
        SharedPreferences.Editor editor = getEditor(getSharedPreferences(ctx));
        editor.putInt(key, nextValue);
        editor.commit();
        return nextValue;
    }

}
