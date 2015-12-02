package net.callofdroidy.libdevcontainer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import net.callofdroidy.onresume_saver.StateSaver;

public class ActivityMain extends AppCompatActivity {

    private StateSaver myStateSaver;
    private boolean testValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myStateSaver = StateSaver.getInstance(getApplication());

        testValue = myStateSaver.getStatusBoolean("testValue");
        Log.e("testValue", testValue + "");
        testValue = true;
        Log.e("testValue", testValue + "");

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.e("lifecycle", "onSaveInstanceState");

        myStateSaver.addStatus("value", false);
        Log.e("add value", testValue + "");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.e("lifecycle", "onResume");

        Log.e("testValue", testValue + "");

    }

}
