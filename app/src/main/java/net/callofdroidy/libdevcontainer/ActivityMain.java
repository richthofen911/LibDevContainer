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

        Utils.importDummyDatabase(this);
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
    }


}
