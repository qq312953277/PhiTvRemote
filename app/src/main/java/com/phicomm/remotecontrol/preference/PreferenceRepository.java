package com.phicomm.remotecontrol.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.phicomm.remotecontrol.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.Map;
import java.util.Set;


/**
 * Created by yong04.zhou on 2017/9/11.
 */

public class PreferenceRepository {

    private Context mContext;

    public PreferenceRepository(Context context) {
        mContext = context;
    }

    public void put(String preferenceName, Map<String, Object> map) {
        SharedPreferences sp = mContext.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object object = entry.getValue();
            if (object instanceof String) {
                editor.putString(key, (String) object);//key - value
            } else if (object instanceof Integer) {
                editor.putInt(key, (Integer) object);
            } else if (object instanceof Boolean) {
                editor.putBoolean(key, (Boolean) object);
            } else if (object instanceof Float) {
                editor.putFloat(key, (Float) object);
            } else if (object instanceof Long) {
                editor.putLong(key, (Long) object);
            } else {
                editor.putString(key, object.toString());
            }
        }
        editor.commit();
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

    /**
     * 存档对象
     *
     * @param key
     * @param obj
     */
    public boolean saveObj(String preferenceName, String key, Object obj) {
        SharedPreferences sp = mContext.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        try {
            SharedPreferences.Editor editor = sp.edit();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bos);
            //将对象序列化写入byte缓存
            os.writeObject(obj);
            //将序列化的数据转为16进制保存
            String bytesToHexString = StringUtils.bytesToHexString(bos.toByteArray());
            //保存该16进制数组
            editor.putString(key, bytesToHexString);
            editor.commit();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * 根据键值获取之前存档的对象
     *
     * @param key
     * @return
     */
    public Object getObj(String preferenceName, String key) {
        SharedPreferences sp = mContext.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        try {
            if (sp.contains(key)) {
                String value = sp.getString(key, "");
                if (TextUtils.isEmpty(value)) {
                    return null;
                } else {
                    //将16进制的数据转为数组，准备反序列化
                    byte[] stringToBytes = StringUtils.stringToBytes(value);
                    ByteArrayInputStream bis = new ByteArrayInputStream(stringToBytes);
                    ObjectInputStream is = new ObjectInputStream(bis);
                    //返回反序列化得到的对象
                    Object readObject = is.readObject();
                    return readObject;
                }
            }
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean contains(String preferenceName, String key) {
        SharedPreferences sp = mContext.getSharedPreferences(preferenceName,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    public void clear(String preferenceName) {
        SharedPreferences sp = mContext.getSharedPreferences(preferenceName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    public void clear(String preferenceName, String key) {
        SharedPreferences sp = mContext.getSharedPreferences(preferenceName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }
}
