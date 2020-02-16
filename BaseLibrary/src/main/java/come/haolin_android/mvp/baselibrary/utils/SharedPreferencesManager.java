package come.haolin_android.mvp.baselibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


public class SharedPreferencesManager {
    private static final int STRING_TYPE = 1;
    private static final int INT_TYPE = 2;
    private static final int BOOLEAN_TYPE = 3;
    private static final int FLOAT_TYPE = 4;
    private static final int LONG_TYPE = 5;
    private static final int OBJ_TYPE = 6;
    private static SharedPreferencesManager instance;
    private SharedPreferences sharedPreferences;


    private SharedPreferencesManager(Context context) {
        initSpManager(context);
    }


    public static SharedPreferencesManager getInstance(Context context) {
        // 单例设计模式中的双检锁，加锁是非常消耗资源的，这样设计理论上仅在需要实例化的时候走加锁代码
        if (instance == null) {
            synchronized (SharedPreferencesManager.class) {
                if (instance == null) {
                    instance = new SharedPreferencesManager(context);
                }
            }
        }
        return instance;
    }

    private void initSpManager(Context context) {
        sharedPreferences = context.getSharedPreferences(this.getClass().getName(), Context.MODE_PRIVATE);
    }

    public void putExtra(String key, Object content) {
        remove(key);//防止数据类型不一致，安全起见，先remove掉
        Editor editor = sharedPreferences.edit();
        if (content instanceof String) {
            insertConfigure(editor, key, STRING_TYPE, content);
        } else if (content instanceof Integer) {
            insertConfigure(editor, key, INT_TYPE, content);
        } else if (content instanceof Boolean) {
            insertConfigure(editor, key, BOOLEAN_TYPE, content);
        } else if (content instanceof Float) {
            insertConfigure(editor, key, FLOAT_TYPE, content);
        } else if (content instanceof Long) {
            insertConfigure(editor, key, LONG_TYPE, content);
        } else {
            insertConfigure(editor, key, OBJ_TYPE, content);
        }
        editor.apply();
    }

    public void putString(String key, String content) {
        Editor editor = sharedPreferences.edit();
        editor.putString(key, content);
        editor.apply();
    }

    public void putInt(String key, int content) {
        Editor editor = sharedPreferences.edit();
        editor.putInt(key, content);
        editor.apply();
    }

    public void putExtraObj(String key, Object object) {
        try {
            Editor editor = sharedPreferences.edit();
            // 创建字节输出流
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(object);
            // 将字节流编码成base64的字符窜
            String oAuth_Base64 = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            editor.putString(key, oAuth_Base64);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getObj(String key) {
        String string = getString(key);
        try {
            if (string == null) {
                return null;
            }
            //读取字节
            byte[] base64 = Base64.decode(string.getBytes(), Base64.DEFAULT);
            //封装到字节流
            ByteArrayInputStream bais = new ByteArrayInputStream(base64);
            //再次封装
            ObjectInputStream bis = new ObjectInputStream(bais);
            //读取对象
            return bis.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void putExtras(HashMap<String, Object> map) {
        Editor editor = sharedPreferences.edit();
        for (Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object content = entry.getValue();
            putExtra(key, content);
        }
        editor.apply();
    }


    private void insertConfigure(Editor editor, String key, int type, Object content) {
        switch (type) {
            case STRING_TYPE:
                editor.putString(key, String.valueOf(content));
                break;
            case BOOLEAN_TYPE:
                editor.putBoolean(key, Boolean.parseBoolean(String.valueOf(content)));
                break;
            case INT_TYPE:
                editor.putInt(key, Integer.parseInt(String.valueOf(content)));
                break;
            case FLOAT_TYPE:
                editor.putFloat(key, Float.parseFloat(String.valueOf(content)));
                break;
            case LONG_TYPE:
                editor.putLong(key, Long.parseLong(String.valueOf(content)));
                break;
            case OBJ_TYPE:
                try {
                    // 创建字节输出流
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    // 创建对象输出流，并封装字节流
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    // 将对象写入字节流
                    oos.writeObject(content);
                    // 将字节流编码成base64的字符窜
                    String oAuth_Base64 = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
                    editor.putString(key, oAuth_Base64);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }


    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }


    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public Float getFloat(String key) {
        return sharedPreferences.getFloat(key, 0);
    }

    public Long getLong(String key) {
        return sharedPreferences.getLong(key, 0);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }


    public int getInt(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }


    public String getString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    public Float getFloat(String key, Float defValue) {
        return sharedPreferences.getFloat(key, defValue);
    }

    public Long getLong(String key, Long defValue) {
        return sharedPreferences.getLong(key, defValue);
    }

    public Map<String, ?> getAllSharedPreferences() {
        return sharedPreferences.getAll();
    }


    public void clearAllSharedPreferences() {
        Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void remove(String key) {
        Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }
}
