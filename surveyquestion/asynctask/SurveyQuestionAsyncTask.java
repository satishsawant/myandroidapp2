package realizer.com.mysurvey.surveyquestion.asynctask;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;



import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import realizer.com.mysurvey.utils.Config;
import realizer.com.mysurvey.utils.OnTaskCompleted;

/**
 * Created by shree on 12/8/2016.
 */
public class SurveyQuestionAsyncTask extends AsyncTask<Void, Void,StringBuilder>
{
    StringBuilder resultLogin;
    String surveyID ;
    Context myContext;
    private OnTaskCompleted callback;

    public SurveyQuestionAsyncTask(String surveyId, Context myContext, OnTaskCompleted cb) {
        this.myContext = myContext;
        this.callback = cb;
        surveyID =surveyId;
    }

    @Override
    protected void onPreExecute() {
        // super.onPreExecute();
        //dialog= ProgressDialog.show(myContext, "", "Please wait Classwork is Loading...");
    }


    @Override
    protected StringBuilder doInBackground(Void... params) {
        resultLogin = new StringBuilder();
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(myContext);
        String my= Config.URL+"getServeyDetails/"+sharedpreferences.getString("userId", "")+"/"+surveyID;
        Log.d("URL", my);
        HttpGet httpGet = new HttpGet(my);
        HttpClient client = new DefaultHttpClient();
        try
        {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();

            int statusCode = statusLine.getStatusCode();
            if(statusCode == 200)
            {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while((line=reader.readLine()) != null)
                {
                    resultLogin.append(line);
                }
            }
            else
            {
                // Log.e("Error", "Failed to Login");
            }
        }
        catch(ClientProtocolException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            client.getConnectionManager().closeExpiredConnections();
            client.getConnectionManager().shutdown();
        }
        return resultLogin;
    }

    @Override
    protected void onPostExecute(StringBuilder stringBuilder) {
        super.onPostExecute(stringBuilder);
        //Pass here result of async task
        stringBuilder.append("@@@Question");
        callback.onTaskCompleted(stringBuilder.toString());
    }
}
