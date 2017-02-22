package realizer.com.mysurvey;

import android.content.Context;
import android.os.AsyncTask;



import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import realizer.com.mysurvey.exceptionhandler.NetworkException;
import realizer.com.mysurvey.utils.Config;
import realizer.com.mysurvey.utils.OnTaskCompleted;
import realizer.com.mysurvey.utils.Utility;


/**
 * Created by Win on 08/12/2016.
 */
public class UserProfileAsyncTaskPost extends AsyncTask<Void,Void,StringBuilder> {
    StringBuilder resultbuilder;
    String userID;
    String thumbnail;
    Context mycontext;
    private OnTaskCompleted callback;

    public UserProfileAsyncTaskPost(String userID, String thumbnail, Context mycontext, OnTaskCompleted callback) {
        this.userID = userID;
        this.thumbnail = thumbnail;
        this.mycontext = mycontext;
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected StringBuilder doInBackground(Void... params) {
        resultbuilder =new StringBuilder();
        HttpClient httpClient=new DefaultHttpClient();
        String url= Config.URL+"UpdateProfilePicture/"+userID;
        HttpPost httpPost=new HttpPost(url);
        String json="";
        StringEntity se=null;
        JSONObject jsonObject=new JSONObject();
        try
        {
            String newThumbnail= Utility.getURLImage(thumbnail);
            jsonObject.put("base64Pic", thumbnail.replaceAll("\n",""));
            json=jsonObject.toString();

            se=new StringEntity(json);
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse=httpClient.execute(httpPost);
            StatusLine statusLine=httpResponse.getStatusLine();
            int statuscode=statusLine.getStatusCode();
            if (statuscode==200)
            {
                HttpEntity entity=httpResponse.getEntity();
                InputStream content=entity.getContent();
                BufferedReader reader=new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line=reader.readLine())!=null)
                {
                    resultbuilder.append(line);
                }
            }
            else {
                StringBuilder exceptionString = new StringBuilder();
                HttpEntity entity = httpResponse.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                exceptionString.append("URL: "+url.toString()+"\nInput: "+jsonObject.toString()+"\nException: ");
                while((line=reader.readLine()) != null)
                {
                    exceptionString.append(line);
                }

                NetworkException.insertNetworkException(mycontext, exceptionString.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return resultbuilder;
    }

    @Override
    protected void onPostExecute(StringBuilder stringBuilder) {
        super.onPostExecute(stringBuilder);
        stringBuilder.append("@@@ProfilePic");
        callback.onTaskCompleted(stringBuilder.toString());
    }
}
