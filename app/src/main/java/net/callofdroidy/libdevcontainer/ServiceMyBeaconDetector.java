package net.callofdroidy.libdevcontainer;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.perples.recosdk.RECOBeacon;

import net.callofdroidy.beacondetector.ServiceBeaconDetector;
import net.callofdroidy.httplighter.APICaller;

public class ServiceMyBeaconDetector extends ServiceBeaconDetector {
    private IOnActionOnEnterBackendWorkFinishListener onActionOnEnterBackendWorkFinishListener;
    private String userId;

    public ServiceMyBeaconDetector() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new BinderMyBeaconDetection();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        userId = intent.getExtras().getString("UserID");
        return START_STICKY;
    }

    public void setOnActionOnEnterBackendWorkFinishListener(IOnActionOnEnterBackendWorkFinishListener onActionOnEnterBackendWorkFinishListener){
        this.onActionOnEnterBackendWorkFinishListener = onActionOnEnterBackendWorkFinishListener;
    }

    @Override
    protected void onEnterRegion(RECOBeacon recoBeacon) { //Called when the phone checks in with the assigned beacon region.
        Toast.makeText(getApplicationContext(), "beacon detected: " + recoBeacon.getMajor() + "::" + recoBeacon.getMinor(), Toast.LENGTH_SHORT).show();
        //check in with AprilBrother beacon's UUID
        Log.e("enter", "enter region");
    }

    @Override
    protected void onExitRegion(RECOBeacon recoBeacon){}

    protected class BinderMyBeaconDetection extends Binder {
        public ServiceMyBeaconDetector getService(){
            return ServiceMyBeaconDetector.this;
        }
    }
}
