package opensoft.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import opensoft.databases.Content;
import opensoft.databases.ContentDatabaseHandler;
import opensoft.databases.User;
import opensoft.util.Utilities;

/**
 * Created by stejasvin on 1/28/2015.
 */
public class DataService extends IntentService {
    public static final int UPDATE_PROGRESS = 8344;
    private static final String TAG = "DataService";
    private String url_send_note = "";
    DefaultHttpClient mDefaultHttpClient = null;

    public DataService() {
        super("DataService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {

            mDefaultHttpClient = new DefaultHttpClient();
            if(mDefaultHttpClient == null){
                return;
            }

            String priority = extras.getString("com.opensoft.app.PRIORITY");
            String userId = extras.getString("com.dhilcare.app.S_ID");

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(Utilities.TAG_PRIORITY, priority));
            params.add(new BasicNameValuePair(Utilities.TAG_ID, userId));

            try {
                URL url;
                url = new URL(url_send_note);

                HttpParams httpParameters = new BasicHttpParams();
                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 50000);
                HttpConnectionParams.setSoTimeout(httpParameters, 50000);

                HttpPost httppost = new HttpPost(url.toString());
                httppost.setEntity(new UrlEncodedFormEntity(params));
                httppost.setParams(httpParameters);
                HttpResponse response = mDefaultHttpClient
                        .execute(httppost);
                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent();

//                JSONArray jsonArray = Utilities.JSONAParse(is);

                String resp;
                resp = Utilities.IsParse(is);
                Log.i(TAG, resp);
                Log.d("dataService","data: "+resp);
                if(resp!=null && !resp.equals("")) {

                    File file = new File(Utilities.SDCARD_LOC+"temp");
                    File outfile = new File(Utilities.SDCARD_LOC+"tempOut");
                    writeIntoFile(resp,file);
                    Utilities.gunzipIt(file,outfile);
                    String strResp = readFile(outfile);

                    try {
                        JSONArray jsonArray = new JSONArray(strResp);
                        ContentDatabaseHandler contentDatabaseHandler = new ContentDatabaseHandler(DataService.this);

                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            Content content = new Content();
                            content.setiServerId(jsonObject.getInt("id"));
                            content.setsTitle(jsonObject.getString("title"));
                            //content.setsUrl(jsonObject.getString("url"));
                            content.setsPageId(jsonObject.getString("page_id"));
                            content.setsRevId(jsonObject.getString("revision_id"));
                            content.setsContent(jsonObject.getString("content"));
                            content.setsSummary(jsonObject.getString("summary"));
                            content.setSaImagePath(jsonObject.getJSONArray("image"));

                            contentDatabaseHandler.addContent(content);

                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Intent broadcastIntent = new Intent(Utilities.DATA_SERV_ACTION);
            sendBroadcast(broadcastIntent);

        }
    }

    void writeIntoFile(String s, File file){
        FileOutputStream fop = null;

        String content = s;

        try {

//            file = new File("c:/newfile.txt");
            fop = new FileOutputStream(file);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            // get the content in bytes
            byte[] contentInBytes = content.getBytes();

            fop.write(contentInBytes);
            fop.flush();
            fop.close();

            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fop != null) {
                    fop.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String readFile( File file ) throws IOException {
        BufferedReader reader = new BufferedReader( new FileReader(file));
        String         line = null;
        StringBuilder  stringBuilder = new StringBuilder();
        String         ls = System.getProperty("line.separator");

        while( ( line = reader.readLine() ) != null ) {
            stringBuilder.append( line );
            stringBuilder.append( ls );
        }

        return stringBuilder.toString();
    }
}
