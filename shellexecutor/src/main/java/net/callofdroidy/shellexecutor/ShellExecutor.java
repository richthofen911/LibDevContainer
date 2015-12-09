package net.callofdroidy.shellexecutor;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by admin on 09/12/15.
 */
public class ShellExecutor {
    public static String execCommand(String[] command) throws IOException {
        // start the ls command running
        String result = "blank";

        try{
            Process proc = Runtime.getRuntime().exec(command);
            int resultCode = proc.waitFor();

            InputStreamReader stdinReader = new InputStreamReader(proc.getInputStream());
            InputStreamReader stderrReader = new InputStreamReader(proc.getErrorStream());

            String line = "";
            StringBuilder cmdResult = new StringBuilder();
            BufferedReader bufferedReader;

            if(resultCode == 0)
                bufferedReader = new BufferedReader(stdinReader);
            else
                bufferedReader = new BufferedReader(stderrReader);

            while ((line = bufferedReader.readLine()) != null) {
                cmdResult.append(line);
                cmdResult.append("\n");
            }
            result = cmdResult.toString();
            Log.e("result", result + "\n" + cmdResult.toString());
        }catch (InterruptedException e) {
            Log.e("interrupt err", e.toString());
        }finally {
            return result;
        }
    }
}
