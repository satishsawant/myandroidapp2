package realizer.com.mysurvey.surveyreport;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import realizer.com.mysurvey.R;

import realizer.com.mysurvey.surveylist.SurveyListActivity;
import realizer.com.mysurvey.surveyreport.asynctask.SurveyReportAsynctaskGet;
import realizer.com.mysurvey.surveyreport.chart.BarView;
import realizer.com.mysurvey.surveyreport.model.FilteredReportModel;
import realizer.com.mysurvey.surveyreport.model.SurveyReportModel;
import realizer.com.mysurvey.utils.Config;

import realizer.com.mysurvey.utils.OnTaskCompleted;
import realizer.com.mysurvey.views.ProgressWheel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by shree on 12/9/2016.
 */
public class SurveyReportActivity extends AppCompatActivity implements OnTaskCompleted{
    BarView barView;
    String surveyId;
    Button next,prev;
    ProgressWheel loading;
    int Percentages=0;
    String barText1="";
    int currentlistno=0;
    LinearLayout barchartouter,buttonouter;
    String[] questions;
    String[] optiontxtlist;
    String[] numanswerlist;
    TextView Surveyname,QuestionName,noSurveyReport;
    LinearLayout linearChart;
    ArrayList<SurveyReportModel> SurveyReportList=new ArrayList<>();
    ArrayList<FilteredReportModel> filteredSurveyList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_report);
        Surveyname= (TextView) findViewById(R.id.report_surveyName);
        QuestionName= (TextView) findViewById(R.id.report_questionName);
        getSupportActionBar().setTitle("Report");
        noSurveyReport= (TextView) findViewById(R.id.txt_report_noreport);
        barchartouter= (LinearLayout) findViewById(R.id.bar_chart_outer);
        buttonouter= (LinearLayout) findViewById(R.id.linearLayout_button);
        barchartouter.setVisibility(View.GONE);
        buttonouter.setVisibility(View.GONE);
        Surveyname.setVisibility(View.GONE);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loading= (ProgressWheel) findViewById(R.id.loading);
        QuestionName.setVisibility(View.GONE);
        Intent intent = getIntent();
        surveyId = intent.getStringExtra("SurveyId");
        if (Config.isConnectingToInternet(this))
        {
            loading.setVisibility(View.VISIBLE);
            SurveyReportAsynctaskGet getReportDetails=new SurveyReportAsynctaskGet(surveyId,this,this);
            getReportDetails.execute();
        }

       if (SurveyReportList.size()>0)
       {
           Percentages=Integer.parseInt(SurveyReportList.get(currentlistno).getNumberofAnswers());
       }




        barView = (BarView)findViewById(R.id.bar_view);
        next= (Button) findViewById(R.id.btn_report_next);
        prev= (Button) findViewById(R.id.btn_report_prev);
        prev.setEnabled(false);
        prev.setTextColor(Color.GRAY);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SurveyReportList.size()>0)
                {
                    if (SurveyReportList.size()-1>currentlistno)
                    {
                        if (SurveyReportList.size()-1==currentlistno)
                        {
                            next.setEnabled(false);
                        }
                        prev.setEnabled(true);
                        prev.setTextColor(Color.WHITE);
                        currentlistno++;
                        optiontxtlist=SurveyReportList.get(currentlistno).getOptionText().split("@@");
                        numanswerlist=SurveyReportList.get(currentlistno).getNumberofAnswers().split("@@");
                        Surveyname.setText(SurveyReportList.get(currentlistno).getUserveyName());
                        QuestionName.setText(SurveyReportList.get(currentlistno).getQuestionText());
                        randomSet(barView);
                        randomSet(barView);
                    }
                    else
                    {
                        next.setEnabled(false);
                        next.setTextColor(Color.GRAY);
                       // Config.alertDialog(SurveyReportActivity.this, "Complete", "This is last Question.");
                    }

                }
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SurveyReportList.size() > 0) {
                    if (currentlistno == 0) {
                        prev.setEnabled(false);
                        prev.setTextColor(Color.GRAY);
                    } else {
                        next.setEnabled(true);
                        next.setTextColor(Color.WHITE);
                        currentlistno--;
                        optiontxtlist = SurveyReportList.get(currentlistno).getOptionText().split("@@");
                        numanswerlist = SurveyReportList.get(currentlistno).getNumberofAnswers().split("@@");
                        Surveyname.setText(SurveyReportList.get(currentlistno).getUserveyName());
                        QuestionName.setText(SurveyReportList.get(currentlistno).getQuestionText());
                        randomSet(barView);
                        randomSet(barView);
                    }
                }
            }
        });
        /*randomSet(barView);*/
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
                Intent i=new Intent(this, SurveyListActivity.class);
                startActivity(i);
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
    private void randomSet(BarView barView){
        int random = (int)(Math.random()*20)+6;
        ArrayList<String> test = new ArrayList<String>();
       /* for (int i=0; i<random; i++){*/

        for (int i=0;i<optiontxtlist.length;i++)
        {
            test.add(optiontxtlist[i]);
        }
        barView.setBottomTextList(test);

        ArrayList<Integer> barDataList = new ArrayList<Integer>();
       /* for(int i=0; i<random*2; i++){*/
        int total=0;
        for (int i=0;i<numanswerlist.length;i++)
        {
            barDataList.add((int)(Integer.valueOf(numanswerlist[i])));
            total=total+Integer.valueOf(numanswerlist[i]);
        }
        //}
        barView.setDataList(barDataList, total);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        Intent i=new Intent(this, SurveyListActivity.class);
        startActivity(i);
    }
    @Override
    public void onTaskCompleted(String s)
    {
        String[] optionTxt;
        String optionno="",optiontxt="",numberofanswer="";
        String[] onTask=s.split("@@@");
        if (onTask[1].equals("SurveyReport"))
        {
            loading.setVisibility(View.GONE);
            barchartouter.setVisibility(View.VISIBLE);
            buttonouter.setVisibility(View.VISIBLE);
            Surveyname.setVisibility(View.VISIBLE);
            QuestionName.setVisibility(View.VISIBLE);
            JSONArray rootObj = null;
            try
            {
                rootObj = new JSONArray(s);
                for (int i=0;i<rootObj.length();i++)
                {
                    JSONObject obj1 = rootObj.getJSONObject(i);
                    SurveyReportModel obj=new SurveyReportModel();
                    obj.setSurveyId(obj1.getString("SurveyId"));
                    obj.setUserveyName(obj1.getString("UsrveyName"));
                    obj.setQuestionNo(obj1.getString("questionNo"));
                    obj.setQuestionText(obj1.getString("questionText"));
                    obj.setCreatedTime(obj1.getString("createTS"));



                    JSONArray jary=null;
                    jary=obj1.getJSONArray("Option");
                    for (int j=0;j<jary.length();j++) {
                        JSONObject optObj=jary.getJSONObject(j);
                        if (j==0)
                        {
                            optionno=optObj.getString("optionNo");
                            optiontxt=optObj.getString("optionText");
                            numberofanswer=optObj.getString("numberofAnswers");
                        }
                        else
                        {
                            optionno=optionno+"@@"+optObj.getString("optionNo");
                            optiontxt=optiontxt+"@@"+optObj.getString("optionText");
                            numberofanswer=numberofanswer+"@@"+optObj.getString("numberofAnswers");
                        }
                        /*FilteredReportModel freport = new FilteredReportModel();
                        freport.setQuestionNo(obj1.getString("questionNo"));
                        freport.setOptionNo(optObj.getString("optionNo"));
                        freport.setOptionText(optObj.getString("optionText"));
                        freport.setNumberofAnswers(optObj.getString("numberofAnswers"));
                        filteredSurveyList.add(freport);*/
                    }
                    obj.setOptionNo(optionno);
                    obj.setOptionText(optiontxt);
                    obj.setNumberofAnswers(numberofanswer);

                    SurveyReportList.add(obj);

                }

                optiontxtlist=SurveyReportList.get(currentlistno).getOptionText().split("@@");
                numanswerlist=SurveyReportList.get(currentlistno).getNumberofAnswers().split("@@");
                Surveyname.setText(SurveyReportList.get(currentlistno).getUserveyName());
                QuestionName.setText(SurveyReportList.get(currentlistno).getQuestionText());
                randomSet(barView);

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        else {
            noSurveyReport.setVisibility(View.VISIBLE);
            barchartouter.setVisibility(View.GONE);
            buttonouter.setVisibility(View.GONE);
            Surveyname.setVisibility(View.GONE);
            QuestionName.setVisibility(View.GONE);
        }
    }
}

