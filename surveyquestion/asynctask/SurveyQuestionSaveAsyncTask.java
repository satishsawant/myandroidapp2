package realizer.com.mysurvey.surveyquestion.asynctask;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;



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

/**
 * Created by shree on 12/8/2016.
 */
public class SurveyQuestionSaveAsyncTask extends AsyncTask<Void, Void,StringBuilder>
{
    StringBuilder resultLogin;
    String surveyID,userId,quesId,optId,date,isComplete;
    Context myContext;
    private OnTaskCompleted callback;

    public SurveyQuestionSaveAsyncTask(String surveyID, String userId, String quesId, String optId,String date,String isComplete, Context myContext, OnTaskCompleted callback) {
        this.surveyID = surveyID;
        this.userId = userId;
        this.quesId = quesId;
        this.date=date;
        this.isComplete=isComplete;
        this.optId = optId;
        this.myContext = myContext;
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        // super.onPreExecute();
        //dialog= ProgressDialog.show(myContext, "", "Please wait Classwork is Loading...");
    }

    @Override
    protected StringBuilder doInBackground(Void... params) {
        resultLogin = new StringBuilder();
        HttpClient httpclient = new DefaultHttpClient();
        String url = Config.URL+"subitSurveyAnswer";
        HttpPost httpPost = new HttpPost(url);

        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(myContext);

        System.out.println(url);
        String json = "";
        StringEntity se = null;
        JSONObject jsonobj = new JSONObject();
        try {
            jsonobj.put("userId",userId);
            jsonobj.put("surveyId",surveyID);
            jsonobj.put("quetionId",quesId);
            jsonobj.put("optionNo",optId);
            String datet[] = date.split(" ");
            String date1[] = datet[0].split("/");
            String resdate = date1[1]+"/"+date1[0]+"/"+date1[2]+" "+datet[1];
            jsonobj.put("answerDate",resdate);
            jsonobj.put("isComplete",isComplete);

            json = jsonobj.toString();
            Log.d("RES", json);
            se = new StringEntity(json);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");


            httpPost.setEntity(se);
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return resultLogin;
    }

    @Override
    protected void onPostExecute(StringBuilder stringBuilder) {
        super.onPostExecute(stringBuilder);
        //Pass here result of async task
        stringBuilder.append("@@@SaveAnswer");
        callback.onTaskCompleted(stringBuilder.toString());
    }
}
