package net.callofdroidy.httplighter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by admin on 09/12/15.
 */
public class APICaller {
    private String APIUrlStr;
    private String APIUrlEncoded;

    private int requestMethod; //Request.Method.GET is an int
    private StringRequest requestCallAPI;
    private static RequestQueue myRequestQueue;
    private static APICaller singletonAPICaller;
    private Context context;

    public static synchronized APICaller getInstance(Context cxt){
        if(singletonAPICaller == null)
            singletonAPICaller = new APICaller(cxt);
        return singletonAPICaller;
    }

    public APICaller(Context context){
        this.context = context;
        myRequestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if (myRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            myRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return myRequestQueue;
    }

    public APICaller setAPI(String urlBase, String urlPath, String urlParams, int method){
        if(urlParams == null)
            APIUrlStr = urlBase + urlPath;
        else
            APIUrlStr = urlBase + urlPath + urlParams;
        APIUrlEncoded = Uri.encode(APIUrlStr).replace("%3A", ":");
        APIUrlEncoded = APIUrlEncoded.replace("%2F", "/");
        APIUrlEncoded = APIUrlEncoded.replace("%3F", "?");
        APIUrlEncoded = APIUrlEncoded.replace("%3D", "=");
        APIUrlEncoded = APIUrlEncoded.replace("%26", "&");
        Log.e("url encoded", APIUrlEncoded);
        requestMethod = method;

        return this;
    }

    public interface VolleyCallback{
        void onDelivered(String result);
    }

    public void exec(final VolleyCallback callback){
        if(APIUrlEncoded == null){
            callback.onDelivered("API has not been set yet");
        }else {
            final Timer timerRequestExec = new Timer();
            requestCallAPI = new StringRequest(requestMethod, APIUrlEncoded, new Response.Listener<String>(){
                @Override
                public void onResponse(String response){
                    requestCallAPI.markDelivered();
                    timerRequestExec.cancel();
                    callback.onDelivered(response.replace("\\", ""));
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error){
                    requestCallAPI.markDelivered();
                    timerRequestExec.cancel();
                    callback.onDelivered(error.toString());
                }
            });
            myRequestQueue.add(requestCallAPI);
            timerRequestExec.schedule(new TimerTask() { //if the request doesn't get any response in 5 seconds, cancel it and pop up network issus
                @Override
                public void run() {
                    if(!requestCallAPI.hasHadResponseDelivered()){
                        requestCallAPI.cancel();
                        //Toast.makeText(context, "Bad Network Status", Toast.LENGTH_SHORT).show();  //need to use handler here
                    }
                }
            }, 5000);
        }
    }

    public void cancelRequest(){
        if(requestCallAPI != null)
            requestCallAPI.cancel();
        Log.e("request cancelled", "");
    }
}
