package net.callofdroidy.beacondetector;

import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.perples.recosdk.RECOBeacon;
import com.perples.recosdk.RECOBeaconManager;
import com.perples.recosdk.RECOBeaconRegion;
import com.perples.recosdk.RECOErrorCode;
import com.perples.recosdk.RECORangingListener;
import com.perples.recosdk.RECOServiceConnectListener;

import java.util.ArrayList;
import java.util.Collection;

abstract public class ServiceBeaconDetector extends Service implements RECOServiceConnectListener, RECORangingListener{

    protected final boolean DISCONTINUOUS_SCAN = false;

    protected boolean entered = false;
    protected int exitCount = 0;
    protected boolean exited = false;
    protected int rssiBorder = 0;
    protected int currentMinor = 0;
    protected boolean generalSearchMode = false;
    public static final int NOTIFICATION_ID = 001;

    protected RECOBeaconManager mRecoManager;
    protected ArrayList<RECOBeaconRegion> definedRegions;

    private OnActionEnterListener onActionEnterListener;
    private OnActionExitListener onActionExitListener;

    private static NotificationManager mNotificationManager;

    public ServiceBeaconDetector() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new BinderBeaconDetection();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter != null){
            bluetoothAdapter.enable(); // if Bluetooth Adapter exists, force enabling it.
            Log.e("Bluetooth enabled", "binding BeaconManager to detection listener...");
            mRecoManager = RECOBeaconManager.getInstance(this, false, false);
            mRecoManager.setRangingListener(this);
            mRecoManager.bind(this);
            Log.e("Applying ", "detection configs...");
            definedRegions = new ArrayList<>();
            applyDetectionConfig(intent.getStringArrayExtra("regionDefinition"), intent.getIntExtra("rssiBorderValue", 0), intent.getBooleanExtra("isGeneralSearchMode", false));
        }else
            Log.e("Bluetooth adapter", "not available");

        return START_STICKY;
    }

    protected void applyDetectionConfig(String[] regionDefinition, int rssiBorderValue, boolean useGeneralSearchMode){
        for(String region: regionDefinition){
            String[] regionInfo = region.split(",");
            assignRegions(regionInfo[0], Integer.valueOf(regionInfo[1]), Integer.valueOf(regionInfo[2]));
        }
        rssiBorder = rssiBorderValue;
        generalSearchMode = useGeneralSearchMode;
    }

    protected void assignRegions(String uuid, int major, int minor){
        if(major > 0 && minor > 0)
            definedRegions.add(generateBeaconRegion(uuid, major, minor));
        else if(major > 0)
            definedRegions.add(generateBeaconRegion(uuid, major));
        else
            definedRegions.add(generateBeaconRegion(uuid));
    }

    // start scanning
    private void start(ArrayList<RECOBeaconRegion> regions) {
        for(RECOBeaconRegion region : regions) {
            try {
                mRecoManager.startRangingBeaconsInRegion(region);
                Log.e("start detecting", region.describeContents() + "");
            } catch (RemoteException e) {
                Log.i("RECORangingActivity", "Remote Exception");
                e.printStackTrace();
            } catch (NullPointerException e) {
                Log.i("RECORangingActivity", "Null Pointer Exception");
                e.printStackTrace();
            }
        }
    }

    public void startScanning(){
        start(definedRegions);
    }

    // stop scanning
    private void stop(ArrayList<RECOBeaconRegion> regions) {
        Log.e("stop detecting", "...");
        for(RECOBeaconRegion region : regions) {
            try {
                mRecoManager.stopRangingBeaconsInRegion(region);
                entered = false;
            } catch (RemoteException e) {
                Log.i("RECORangingActivity", "Remote Exception");
                e.printStackTrace();
            } catch (NullPointerException e) {
                Log.i("RECORangingActivity", "Null Pointer Exception");
                e.printStackTrace();
            }
        }
    }

    public void stopScanning(){
        stop(definedRegions);
    }

    private RECOBeaconRegion generateBeaconRegion(String uuid) {
        return new RECOBeaconRegion(uuid, "Defined Region");
    }

    private RECOBeaconRegion generateBeaconRegion(String uuid, int major) {
        return new RECOBeaconRegion(uuid, major, "Defined Region");
    }

    private RECOBeaconRegion generateBeaconRegion(String uuid, int major, int minior){
        return new RECOBeaconRegion(uuid, major, minior, "Defined Region");
    }

    @Override
    public void onServiceConnect() {
        Log.e("RangingActivity", "onServiceConnect()");
        mRecoManager.setDiscontinuousScan(DISCONTINUOUS_SCAN);
        //start(definedRegions);
    }

    @Override
    public void onServiceFail(RECOErrorCode recoErrorCode) {
        Log.e("RECO service error:", recoErrorCode.toString());
    }

    protected void onEnterRegion(RECOBeacon recoBeacon){
        if(onActionEnterListener != null)
            onActionEnterListener.onActionEnter(recoBeacon);
        else
            Log.e("null listener", "should set the listener in Activity");
    }


    protected void onExitRegion(RECOBeacon recoBeacon){
        if(onActionExitListener != null)
            onActionExitListener.onActionExit(recoBeacon);
        else
            Log.e("null listener", "should set the listener in Activity");
    }

    private void inOut(int theRssi, RECOBeacon recoBeacon){
        if(!generalSearchMode){
            if(theRssi > rssiBorder){ // if the beacon is detected and its rssi is stronger enough, which means it is the beacon for the specific location, not a random one
                if(!entered){ //if haven't entered, do it
                    exitCount = 0;
                    entered = true;
                    exited = false;
                    Log.e("put a checkin", " with beacon " + recoBeacon.getProximityUuid() + " :: " + recoBeacon.getMajor() + " :: " + recoBeacon.getMinor());
                    currentMinor = recoBeacon.getMinor();
                    onEnterRegion(recoBeacon);
                }else{
                    Log.e("entered already", ")");
                }
            }else{
                if(recoBeacon.getMinor() == currentMinor){
                    if(exitCount < 3){
                        exitCount++;
                    }else {
                        if(!exited){ // if haven't exited, do it
                            entered = false;
                            exited = true;
                            currentMinor = 0;
                            onExitRegion(recoBeacon);
                        }else {
                            Log.e("exited already", ")");
                        }
                    }
                }else{
                    Log.e("not this beacon", String.valueOf(recoBeacon.getMajor()) + "-" +String.valueOf(recoBeacon.getMinor()));
                }
            }
        }else {
            Log.e("warning", "this app doesn't have general search mode");
        }
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<RECOBeacon> recoBeacons, RECOBeaconRegion recoBeaconRegion) {
        synchronized (recoBeacons){
            for(RECOBeacon recoBeacon: recoBeacons){
                Log.e("beacon detected, rssi", String.valueOf(recoBeacon.getRssi()));
                inOut(recoBeacon.getRssi(), recoBeacon);
            }
        }
    }

    @Override
    public void rangingBeaconsDidFailForRegion(RECOBeaconRegion recoBeaconRegion, RECOErrorCode recoErrorCode) {
        Log.e("RECO ranging error:", recoErrorCode.toString());
    }

    public void setOnActionEnterListener(OnActionEnterListener listenerForActivity){
        this.onActionEnterListener = listenerForActivity;
    }

    public void setOnActionExitListener(OnActionExitListener listenerForActivity){
        this.onActionExitListener = listenerForActivity;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.e("BeaconDetector", "lifecycle: onDestroyed");
        try{
            mRecoManager.unbind();
            Log.e("recon manager", "unbound");
        }catch (RemoteException e){
            Log.e("on destroy error", e.toString());
        }
    }

    protected class BinderBeaconDetection extends Binder{
        public ServiceBeaconDetector getService(){
            return ServiceBeaconDetector.this;
        }
    }
}
