package net.callofdroidy.onresume_saver;

import android.app.Application;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Created by admin on 02/12/15.
 */
public class StateSaver {
    private static StateSaver singleStateSaverInstance;
    private static SharedPreferences spStatusSaver;

    public StateSaver(Application application){
        spStatusSaver = application.getSharedPreferences("spStatusSaver", 0);
    }

    public static synchronized StateSaver getInstance(Application application){
        if(spStatusSaver == null)
            singleStateSaverInstance = new StateSaver(application);
        return singleStateSaverInstance;
    }

    public StateSaver addStatus(String keyName, Object value){
        if(value instanceof String)
            spStatusSaver.edit().putString(keyName, (String) value);
        if(value instanceof Integer)
            spStatusSaver.edit().putInt(keyName, (int) value);
        if(value instanceof Boolean)
            spStatusSaver.edit().putBoolean(keyName, (boolean) value);
        if(value instanceof Float)
            spStatusSaver.edit().putFloat(keyName, (float) value);
        if(value instanceof Long)
            spStatusSaver.edit().putLong(keyName, (Long) value);

        return  this;
    }

    public String getStatusString(String keyName){
        return spStatusSaver.getString(keyName, "empty");
    }

    public int getStatusInt(String keyName){
        return spStatusSaver.getInt(keyName, -1);
    }

    public boolean getStatusBoolean(String keyName){
        return spStatusSaver.getBoolean(keyName, false);
    }

    public float getStatusFloat(String keyName){
        return spStatusSaver.getFloat(keyName, -1);
    }

    public long getStatusLong(String keyName){
        return spStatusSaver.getLong(keyName, -1);
    }

    public Map<String, ?> getAll(){
        return spStatusSaver.getAll();
    }

    public void commit(){
        spStatusSaver.edit().commit();
    }
}
