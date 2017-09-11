package com.phicomm.remotecontrol.preference;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by yong04.zhou on 2017/9/11.
 */

public class PreferenceRepository {

    private Context mContext;

    public PreferenceRepository(Context context) {
        mContext = context;
    }

    public void put(String preferenceName, String key, Object object) {
        SharedPreferences sp = mContext.getSharedPreferences(preferenceName, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else if (object instanceof Set) {
            editor.putStringSet(key, (Set<String>) object);
        } else {
            editor.putString(key, object.toString());
        }
        editor.commit();
    }

    public Object get(String preferenceName, String key, Object defaultObject) {
        SharedPreferences sp = mContext.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        } else if (defaultObject instanceof Set) {
            return sp.getStringSet(key, (Set<String>) defaultObject);
        }
        return null;
    }
}
