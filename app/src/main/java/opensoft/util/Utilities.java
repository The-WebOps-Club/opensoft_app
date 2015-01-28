package opensoft.util;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

/**
 * Created by stejasvin on 1/28/2015.
 */
public class Utilities {

    public static final String DATA_SERV_ACTION = "opensoft.DOWNLOAD";
    public static String TAG_PRIORITY = "priority";
    public static String TAG_ID = "id";
    public static String SDCARD_LOC = Environment.getExternalStorageDirectory()+"MPower/";

    public static String IsParse(InputStream is) {
        String json = "";

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
                sb.append(line + "\n");

            is.close();

            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // return JSON String
        return json;
    }

    public static void gunzipIt(File input,File output){

        byte[] buffer = new byte[1024];

        try{

            GZIPInputStream gzis =
                    new GZIPInputStream(new FileInputStream(input));

            FileOutputStream out =
                    new FileOutputStream(output);

            int len;
            while ((len = gzis.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }

            gzis.close();
            out.close();

            System.out.println("Done");

        }catch(IOException ex){
            ex.printStackTrace();
        }
    }


}
