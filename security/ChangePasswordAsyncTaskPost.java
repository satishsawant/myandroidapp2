package realizer.com.mysurvey.security;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import realizer.com.mysurvey.exceptionhandler.NetworkException;
import realizer.com.mysurvey.utils.Config;
import realizer.com.mysurvey.utils.OnTaskCompleted;

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

/**
 * Created by Win on 13/12/2016.
 */
public class ChangePasswordAsyncTaskPost extends AsyncTask<Void,Void,StringBuilder> {

    StringBuilder resultLogin;
    Context myContext;
    String UserId,oldPass,NewPass;
    private OnTaskCompleted callback;

    public ChangePasswordAsyncTaskPost(Context myContext, String userId, String oldPass, String newPass, OnTaskCompleted callback) {
        this.myContext = myContext;
        UserId = userId;
        this.oldPass = oldPass;
        NewPass = newPass;
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected StringBuilder doInBackground(Void... params)
    {
        resultLogin = new StringBuilder();
        HttpClient httpclient = new DefaultHttpClient();
        String url = Config.URLSECURITY+"security/changePassword/"+UserId;
        HttpPost httpPost = new HttpPost(url);

        System.out.println(url);

        String json = "";
       /* Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String date = df.format(calendar.getTime());*/
        StringEntity se = null;
        JSONObject jsonobj = new JSONObject();
        try {

            jsonobj.put("oldPassword",oldPass);
            jsonobj.put("newPassword",NewPass);

            json=jsonobj.toString();
            se=new StringEntity(json);
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);
            StatusLine statusLine = httpResponse.getStatusLine();

            int statusCode = statusLine.getStatusCode();
            Log.d("StatusCode", "" + statusCode);
            if(statusCode == 200)
            {
                HttpEntity entity = httpResponse.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while((line=reader.readLine()) != null)
                {
                    resultLogin.append(line);
                }
            }
            else {
                StringBuilder exceptionString = new StringBuilder();
                HttpEntity entity = httpResponse.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                exceptionString.append("URL: "+url.toString()+"\nInput: "+jsonobj.toString()+"\nException: ");
                while((line=reader.readLine()) != null)
                {
                    exceptionString.append(line);
                }

                NetworkException.insertNetworkException(myContext, exceptionString.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultLogin;
    }


    @Override
    protected void onPostExecute(StringBuilder stringBuilder) {
        super.onPostExecute(stringBuilder);
        stringBuilder.append("@@@changePass");
        callback.onTaskCompleted(stringBuilder.toString());
    }
}
