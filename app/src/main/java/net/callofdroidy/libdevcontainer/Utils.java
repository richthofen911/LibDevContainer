package net.callofdroidy.libdevcontainer;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

/**
 * Created by admin on 21/12/15.
 */
public class Utils {

    /**
     * This can be used if some basic database conditions need to be involved when testing a lib,
     * it import a existing dummy database from res/raw folder
     */

    public static void importDummyDatabase(Context context) {
        String filePath = context.getFilesDir().toString();
        String databasesPath = filePath.substring(0, filePath.length() - 6) + "/databases/";

        File dir = new File(databasesPath);
        if (!dir.exists())
            dir.mkdir();

        File file = new File(dir, "dummy_data.db");
        try {
            if (!file.exists())
                file.createNewFile();
            InputStream is = context.getApplicationContext().getResources().openRawResource(R.raw.lib_dev_dummy_data);
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            fos.write(buffer);
            is.close();
            fos.close();

        } catch (FileNotFoundException e) {
            Log.e("file not found", e.toString());
        } catch (IOException e) {
            Log.e("io excp", e.toString());
        }
    }

    /**
     * this function can be used to verify input value on sign in/sign up or other situations
     */
    public static boolean verifyInputValue(String inputValue, String regex){
        return Pattern.compile(regex).matcher(inputValue).matches();
    }
}
