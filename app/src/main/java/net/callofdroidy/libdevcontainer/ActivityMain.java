package net.callofdroidy.libdevcontainer;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


public class ActivityMain extends AppCompatActivity {


    private boolean testValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean result = Utils.verifyInputValue("a0aa", "\\d+");
        Log.e("verify", String.valueOf(result));

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
