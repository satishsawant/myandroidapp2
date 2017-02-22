package realizer.com.mysurvey.surveylist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import realizer.com.mysurvey.R;

import realizer.com.mysurvey.surveyquestion.SurveyQuestionActivity;
import realizer.com.mysurvey.surveyreport.SurveyReportActivity;
import realizer.com.mysurvey.utils.Config;

import realizer.com.mysurvey.utils.OnTaskCompleted;
import realizer.com.mysurvey.views.ProgressWheel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by shree on 12/7/2016.
 */
public class SurveyListActivity extends AppCompatActivity implements OnTaskCompleted{

    ProgressWheel loading;
    SharedPreferences sharedpreferences;
    ListView surveyList;
    TextView noData;
    ArrayList<SurveyListModel> mainlist=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey_list_layout);

        loading= (ProgressWheel) findViewById(R.id.loading);
        surveyList= (ListView) findViewById(R.id.surveylistview);
        noData = (TextView) findViewById(R.id.tvNoDataMsg);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String ListgetFrom=sharedpreferences.getString("SurveyListFrom", "");

       if ( Config.isConnectingToInternet(SurveyListActivity.this))
       {
           loading.setVisibility(View.VISIBLE);
           SurveyListAsyncTask surveyListAsyncTask=new SurveyListAsyncTask(SurveyListActivity.this,SurveyListActivity.this);
           surveyListAsyncTask.execute();
       }
        else
       {
           Config.alertDialog(SurveyListActivity.this, "Network Error", "Internet Connection is not available.");
       }


        surveyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Object o = surveyList.getItemAtPosition(position);
                SurveyListModel obj = (SurveyListModel) o;


                Intent i;
                if (ListgetFrom.equals("Survey")) {
                    if (obj.getSurveyStatus().equals("Pending") || obj.getSurveyStatus().equals("") || obj.getSurveyStatus().equals(null) || obj.getSurveyStatus().equals("null")) {
                        finish();
                        i = new Intent(SurveyListActivity.this, SurveyQuestionActivity.class);
                        i.putExtra("SurveyId", obj.getSurveyId());
                        i.putExtra("SurveyName", obj.getSurveyName());
                        startActivity(i);
                    } else if (obj.getSurveyStatus().equals("Complete")) {
                        Config.alertDialog(SurveyListActivity.this, "Survey", "You are complete this Survey.");
                    }
                } else if (ListgetFrom.equals("Report")) {
                    finish();
                    i = new Intent(SurveyListActivity.this, SurveyReportActivity.class);
                    i.putExtra("SurveyId", obj.getSurveyId());
                    i.putExtra("SurveyName", obj.getSurveyName());
                    startActivity(i);
                    }
                /*}
                */
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                /*Intent intent=new Intent(this, UserDashboardActivity.class);
                startActivity(intent);*/
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onTaskCompleted(String s) {
        loading.setVisibility(View.GONE);
        JSONArray rootObj = null;
        if (!s.equals(""))
        {
            try
            {
                rootObj = new JSONArray(s);
                for (int i=0;i<rootObj.length();i++)
                {
                    JSONObject obj1 = rootObj.getJSONObject(i);
                    SurveyListModel obj=new SurveyListModel();
                    obj.setSurveyId(obj1.getString("surveyId"));
                    obj.setSurveyName(obj1.getString("surveyName"));
                    obj.setSurveyDescription(obj1.getString("surveryDesc"));
                    obj.setSurveyStatus(obj1.getString("SurveyStatus"));
                    obj.setListGetFrom(sharedpreferences.getString("SurveyListFrom", ""));
                    mainlist.add(obj);
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        if (mainlist.size()>0)
        {
            surveyList.setAdapter(new SurveyListAdapter(SurveyListActivity.this, mainlist));
        }
        else
        {
            surveyList.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
        }

    }
}
