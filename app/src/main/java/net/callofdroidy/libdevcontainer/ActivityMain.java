package net.callofdroidy.libdevcontainer;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import com.android.volley.Request;

import net.callofdroidy.httplighter.APICaller;


public class ActivityMain extends AppCompatActivity {


    private boolean testValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        String targetBeacon1 = DataStore.UUID_AprilBrother + "," + DataStore.beacon_major + "," + DataStore.beacon_minor;
        Bundle detectionConfig = new Bundle();
        detectionConfig.putStringArray("regionDefinition", new String[]{targetBeacon1});
        detectionConfig.putInt("rssiBorderValue", -65);
        detectionConfig.putBoolean("isGeneralSearchMode", false);
        detectionConfig.putString("UserID", "2"); // userId is got from ActivityLogin

        startService(new Intent(this, ServiceMyBeaconDetector.class).putExtras(detectionConfig));
        */


    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.e("lifecycle", "onSaveInstanceState");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("lifecycle", "onResume");

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e("lifecycle", "onDestroy");
        // stopService(new Intent(this, ServiceMyBeaconDetector.class));
    }

    // This method can be used if some basic database conditions need to be involved when testing a lib
    private void createDatabaseForLibTest(){
        MySQLiteHelper mySQLiteHelper = new MySQLiteHelper(this);
        SQLiteDatabase db = mySQLiteHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("title", "morning news");
        contentValues.put("content", "bad weather today");
        contentValues.put("time", "when I get up");

        db.insert("news", "morning news", contentValues);
    }
}
