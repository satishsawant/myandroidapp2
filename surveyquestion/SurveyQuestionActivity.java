package realizer.com.mysurvey.surveyquestion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import realizer.com.mysurvey.R;
import realizer.com.mysurvey.surveylist.SurveyListActivity;
import realizer.com.mysurvey.surveyquestion.asynctask.SurveyQuestionAsyncTask;
import realizer.com.mysurvey.surveyquestion.asynctask.SurveyQuestionSaveAsyncTask;
import realizer.com.mysurvey.surveyquestion.model.SurveyOptionsModel;
import realizer.com.mysurvey.surveyquestion.model.SurveyQuestionModel;
import realizer.com.mysurvey.surveyquestion.model.SurveyQuestionsModel;
import realizer.com.mysurvey.surveyreport.SurveyReportActivity;
import realizer.com.mysurvey.utils.Config;
import realizer.com.mysurvey.utils.OnTaskCompleted;
import realizer.com.mysurvey.views.ProgressWheel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by shree on 12/8/2016.
 */
public class SurveyQuestionActivity extends AppCompatActivity implements OnTaskCompleted{

    //DatabaseQueries db;
    TextView queNo,question,surveyName,noQuestions;
    SharedPreferences sharedpreferences;
    Button back,submit,next;
    RadioButton optionA,optionB,optionC,optionD,optionE;
    RadioGroup radioGroup;
    private MenuItem home;
    LinearLayout questionOuter;
    ProgressWheel loading;
    String selectedAns,surveyId,SurveyStatus="false";
    String surveyname;
    int questionlength;
    ArrayList<SurveyQuestionsModel> questionlistnew=new ArrayList<>();
    ArrayList<SurveyOptionsModel> optionsList=new ArrayList<>();

    ArrayList<SurveyQuestionModel> questionlist=new ArrayList<>();
    ArrayList<SurveyQuestionModel> questionFilterlist=new ArrayList<>();
    ArrayList<String> options=new ArrayList<>();
    //ArrayList<Integer> optionCount=new ArrayList<>();
    public static int currentQuestionNo=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey_question_layout);
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(SurveyQuestionActivity.this);
        surveyName = (TextView) findViewById(R.id.surveyName);
        loading= (ProgressWheel) findViewById(R.id.loading);
        queNo = (TextView) findViewById(R.id.questionno);
        question = (TextView) findViewById(R.id.question);
        back = (Button) findViewById(R.id.btn_back);
        submit = (Button) findViewById(R.id.btn_submit);
        next = (Button) findViewById(R.id.btn_next);
        back.setTextColor(Color.GRAY);
        submit.setTextColor(Color.GRAY);
        optionA = (RadioButton) findViewById(R.id.rb_option_A);
        optionB = (RadioButton) findViewById(R.id.rb_option_B);
        optionC = (RadioButton) findViewById(R.id.rb_option_C);
        optionD= (RadioButton) findViewById(R.id.rb_option_D);
        optionE= (RadioButton) findViewById(R.id.rb_option_E);
        noQuestions= (TextView) findViewById(R.id.txt_no_question);

        questionOuter= (LinearLayout) findViewById(R.id.question_outerLayout);
        radioGroup = (RadioGroup) findViewById(R.id.rgSurveyoption);
        Intent intent = getIntent();
        surveyId = intent.getStringExtra("SurveyId");
        surveyname = intent.getStringExtra("SurveyName");
        //db=new DatabaseQueries(this);
        surveyName.setText(surveyname);
        if (Config.isConnectingToInternet(SurveyQuestionActivity.this))
        {
            loading.setVisibility(View.VISIBLE);
            SurveyQuestionAsyncTask surveyQuestion=new SurveyQuestionAsyncTask(surveyId,SurveyQuestionActivity.this,SurveyQuestionActivity.this);
            surveyQuestion.execute();
        }
        else
        {
            Config.alertDialog(SurveyQuestionActivity.this,"Network Error","Internet Connection is not available.");
        }

        if (optionA.isChecked())
        {
            selectedAns=optionA.getText().toString();
        }
        else if (optionB.isChecked())
        {
            selectedAns=optionB.getText().toString();
        }
        else if (optionC.isChecked())
        {
            selectedAns=optionC.getText().toString();
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (questionlistnew.size()>0)
                {
                    next.setText("Save and Next");
                    next.setTextColor(Color.WHITE);
                    unchekRadio();
                    if (questionlistnew.size()>0)
                    {
                        next.setEnabled(true);
                        currentQuestionNo--;
                        if (currentQuestionNo==0)
                        {
                            back.setEnabled(false);
                            back.setTextColor(Color.GRAY);
                        }

                        queNo.setText("Q " + (currentQuestionNo + 1) + ":");
                        question.setText(questionlistnew.get(currentQuestionNo).getQuestionText());
                        if (questionlistnew.get(currentQuestionNo).getAnswerOptionNo().equals("")||questionlistnew.get(currentQuestionNo).getAnswerOptionNo().equals(null)||questionlistnew.get(currentQuestionNo).getAnswerOptionNo().equals("null"))
                        {

                        }
                        else
                        {
                            String optionId=questionlistnew.get(currentQuestionNo).getAnswerOptionNo();
                            setRadioUsingRadioID(optionId);
                        }

                    }
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //call SurveyQuestionSaveAsyncTask here
              /* if (Config.isConnectingToInternet(SurveyQuestionActivity.this))
               {
                   if (getOptionId()=="")
                   {
                       Config.alertDialog(SurveyQuestionActivity.this,"Error","Please select any one option..!");
                   }
                   else
                   {
                       loading.setVisibility(View.VISIBLE);
                       Calendar calendar = Calendar.getInstance();
                       SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");
                       String date = df.format(calendar.getTime());
                       String[] optid=getOptionId().split("@@");
                       SurveyQuestionSaveAsyncTask saveans=new SurveyQuestionSaveAsyncTask(surveyId,sharedpreferences.getString("userId","").toString(),questionlistnew.get(currentQuestionNo).getQuestionId(),optid[0],date.toString(),SurveyQuestionActivity.this,SurveyQuestionActivity.this);
                       saveans.execute();
                       questionlistnew.get(currentQuestionNo).setAnswerOptionNo(optid[0]);
                   }
               }
                else
               {*/
                //Config.alertDialog(SurveyQuestionActivity.this,"Finish","Go to Report..");
                currentQuestionNo=0;
                Intent i=new Intent(SurveyQuestionActivity.this, SurveyReportActivity.class);
                i.putExtra("SurveyId", surveyId);
                i.putExtra("SurveyName", surveyname);
                startActivity(i);
                finish();
               //}


            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Config.isConnectingToInternet(SurveyQuestionActivity.this))
                {
                    if (getOptionId()=="")
                    {
                        Config.alertDialog(SurveyQuestionActivity.this,"Error","Please select any one option..!");
                    }
                    else
                    {
                        String[] optid=getOptionId().split("@@");
                        if (optid[0].equals(questionlistnew.get(currentQuestionNo).getAnswerOptionNo()))
                        {
                            back.setEnabled(true);
                            back.setTextColor(Color.WHITE);
                            if (questionlistnew.size()>0)
                            {
                                if (questionlistnew.size()-1>currentQuestionNo)
                                {
                                    if (questionlistnew.size()-2==currentQuestionNo)
                                    {
                                        next.setText("Save & Complete");
                                        currentQuestionNo++;
                                        queNo.setText("Q " + (currentQuestionNo + 1) + ":");
                                        question.setText(questionlistnew.get(currentQuestionNo).getQuestionText());
                                        if (questionlistnew.get(currentQuestionNo).getAnswerOptionNo().equals("")||questionlistnew.get(currentQuestionNo).getAnswerOptionNo().equals(null))
                                        {

                                        }
                                        else
                                        {
                                            String optionId=questionlistnew.get(currentQuestionNo).getAnswerOptionNo();
                                            setRadioUsingRadioID(optionId);
                                        }
                                    }
                                    else if (questionlistnew.size()-1==currentQuestionNo)
                                    {
                                        currentQuestionNo++;
                                        SurveyStatus="true";
                                        queNo.setText("Q " + (currentQuestionNo + 1) + ":");
                                        question.setText(questionlistnew.get(currentQuestionNo).getQuestionText());
                                        if (questionlistnew.get(currentQuestionNo).getAnswerOptionNo().equals("")||questionlistnew.get(currentQuestionNo).getAnswerOptionNo().equals(null))
                                        {

                                        }
                                        else
                                        {
                                            String optionId=questionlistnew.get(currentQuestionNo).getAnswerOptionNo();
                                            setRadioUsingRadioID(optionId);
                                        }
                                    }
                                    else {
                                        currentQuestionNo++;
                                        queNo.setText("Q " + (currentQuestionNo + 1) + ":");
                                        question.setText(questionlistnew.get(currentQuestionNo).getQuestionText());
                                        if (questionlistnew.get(currentQuestionNo).getAnswerOptionNo().equals("") || questionlistnew.get(currentQuestionNo).getAnswerOptionNo().equals(null)) {

                                        } else {
                                            String optionId = questionlistnew.get(currentQuestionNo).getAnswerOptionNo();
                                            setRadioUsingRadioID(optionId);
                                        }
                                    }
                                }
                                else
                                {
                                    next.setEnabled(false);
                                    next.setTextColor(Color.GRAY);
                                    submit.setEnabled(true);
                                    submit.setTextColor(Color.WHITE);
                                }
                            }
                        }
                        else
                        {
                            if (questionlistnew.size()-1==currentQuestionNo)
                            {
                                SurveyStatus="true";
                                submit.setEnabled(true);
                                submit.setTextColor(Color.WHITE);
                                next.setEnabled(false);
                            }
                            loading.setVisibility(View.VISIBLE);
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");
                            String date = df.format(calendar.getTime());

                            SurveyQuestionSaveAsyncTask saveans = new SurveyQuestionSaveAsyncTask(surveyId, sharedpreferences.getString("userId", "").toString(), questionlistnew.get(currentQuestionNo).getQuestionId(), optid[0], date.toString(),SurveyStatus, SurveyQuestionActivity.this, SurveyQuestionActivity.this);
                            saveans.execute();
                            questionlistnew.get(currentQuestionNo).setAnswerOptionNo(optid[0]);
                            back.setEnabled(true);
                            back.setTextColor(Color.WHITE);
                        }
                    }
                }
                else
                {
                    Config.alertDialog(SurveyQuestionActivity.this, "Network Error", "No Internet Connection..!");
                }
                /*if (questionlistnew.size()>0)
                {
                    unchekRadio();
                    if (questionlistnew.size()-1>currentQuestionNo)
                    {
                        currentQuestionNo++;
                        queNo.setText("Q " + (currentQuestionNo + 1) + ":");
                        question.setText(questionlistnew.get(currentQuestionNo).getQuestionText());
                        if (questionlistnew.get(currentQuestionNo).getAnswerOptionNo().equals("")||questionlistnew.get(currentQuestionNo).getAnswerOptionNo().equals(null))
                        {

                        }
                        else
                        {
                            String optionId=questionlistnew.get(currentQuestionNo).getAnswerOptionNo();
                            setRadioUsingRadioID(optionId);
                        }
                    }
                    else
                    {
                        AlertDialogCustom("Questions Completed");
                        submit.setEnabled(true);
                    }
                }*/
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        home=menu.findItem(R.id.action_home);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_home:
                finish();
                Intent i=new Intent(this, SurveyListActivity.class);
                startActivity(i);
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onTaskCompleted(String s) {
        int optlength=0;
        loading.setVisibility(View.GONE);
        String result[]=s.split("@@@");
        if (result[1].equalsIgnoreCase("Question"))
        {
            JSONArray rootObj = null;
            try
            {
                rootObj = new JSONArray(s);
                questionlength=rootObj.length();
                for (int i=0;i<rootObj.length();i++)
                {
                    JSONObject obj1 = rootObj.getJSONObject(i);
                    SurveyQuestionsModel obj=new SurveyQuestionsModel();
                    obj.setSurveyId(obj1.getString("surveyId"));
                    obj.setAnswerOptionNo(obj1.getString("answerOptionNo"));
                    //obj.setOptionSerialNo(obj1.getString("optionSerialNo"));
                    obj.setQueSerialNo(obj1.getString("queSerialNo"));
                    obj.setQuestionId(obj1.getString("questionId"));
                    obj.setQuestionText(obj1.getString("questionText"));
                    obj.setInsertedAnswer("");

                    JSONArray jsonArray=obj1.getJSONArray("options");
                    obj.setOptionList(String.valueOf(jsonArray.length()));
                    optlength=Integer.valueOf(jsonArray.length());
                    questionlistnew.add(obj);
                    for (int j=0;j<jsonArray.length();j++)
                    {
                        JSONObject obj2 = jsonArray.getJSONObject(j);
                        SurveyOptionsModel optModel=new SurveyOptionsModel();
                        optModel.setOptSurveyId(obj2.getString("surveyId"));
                        optModel.setOptSerialNo(obj2.getString("serialNo"));
                        optModel.setOptionText(obj2.getString("optionText"));
                        optModel.setOptionId(obj2.getString("optionId"));
                        optModel.setOptSurvey(obj2.getString("survey"));
                        optModel.setOptQuesId(obj1.getString("questionId"));
                        optionsList.add(optModel);
                    }
                }

                questionOuter.setVisibility(View.VISIBLE);
                if (questionlistnew.size()>0) {
                    queNo.setText("Q 1:");
                    question.setText(questionlistnew.get(currentQuestionNo).getQuestionText());
                    if (optlength==1)
                    {
                        setRadioButtions(optlength, optionsList.get(0).getOptionText().toString(),"","","","");
                    }
                    else if (optlength==2)
                    {
                        setRadioButtions(optlength, optionsList.get(0).getOptionText().toString(),optionsList.get(1).getOptionText().toString(),"","","");
                    }
                    else if (optlength==3)
                    {
                        setRadioButtions(optlength, optionsList.get(0).getOptionText().toString(),optionsList.get(1).getOptionText().toString(),optionsList.get(2).getOptionText().toString(),"","");
                    }
                    else if (optlength==4)
                    {
                        setRadioButtions(optlength, optionsList.get(0).getOptionText().toString(),optionsList.get(1).getOptionText().toString(),optionsList.get(2).getOptionText().toString(),optionsList.get(3).getOptionText().toString(),"");
                    }
                    else if (optlength==5)
                    {
                        setRadioButtions(optlength, optionsList.get(0).getOptionText().toString(),optionsList.get(1).getOptionText().toString(),optionsList.get(2).getOptionText().toString(),optionsList.get(3).getOptionText().toString(),optionsList.get(4).getOptionText().toString());
                    }
                    else
                    {

                    }

                    if (questionlistnew.get(currentQuestionNo).getAnswerOptionNo().equals("")||questionlistnew.get(currentQuestionNo).getAnswerOptionNo().equals(null))
                    {

                    }
                    else
                    {
                        String optionId=questionlistnew.get(currentQuestionNo).getAnswerOptionNo();
                        setRadioUsingRadioID(optionId);
                    }
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        else if (result[1].equalsIgnoreCase("SaveAnswer"))
        {
           if (result[0].equals("true"))
           {
               next.setEnabled(true);
               //AlertDialogCustom("Answer Submited Successfully");
               unchekRadio();
               if (questionlistnew.size()>0)
               {
                   if (questionlistnew.size()-1>currentQuestionNo)
                   {
                       currentQuestionNo++;
                       queNo.setText("Q " + (currentQuestionNo + 1) + ":");
                       question.setText(questionlistnew.get(currentQuestionNo).getQuestionText());
                       if (questionlistnew.get(currentQuestionNo).getAnswerOptionNo().equals("")||questionlistnew.get(currentQuestionNo).getAnswerOptionNo().equals(null))
                       {

                       }
                       else
                       {
                           String optionId=questionlistnew.get(currentQuestionNo).getAnswerOptionNo();
                           setRadioUsingRadioID(optionId);
                       }
                   }
                   else
                   {
                       /*AlertDialogCustom("Questions Completed");*/
                       next.setEnabled(false);
                       next.setTextColor(Color.GRAY);
                       submit.setEnabled(true);
                   }
               }
           }
            else
           {
           }
        }
        else {
            noQuestions.setVisibility(View.VISIBLE);
            questionOuter.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        currentQuestionNo=0;
        finish();
        Intent i=new Intent(this, SurveyListActivity.class);
        startActivity(i);
    }
    public void setRadioUsingRadioID(String RadioID)
    {
        String opt="";
        if (RadioID.equals("null"))
        {

        }
        else {
            if (optionsList.get(0).getOptionId().equals(RadioID)) {
                opt = "1";
            } else if (optionsList.get(1).getOptionId().equals(RadioID)) {
                opt = "2";
            } else if (optionsList.get(2).getOptionId().equals(RadioID)) {
                opt = "3";
            } else if (optionsList.get(3).getOptionId().equals(RadioID)) {
                opt = "4";
            } else if (optionsList.get(4).getOptionId().equals(RadioID)) {
                opt = "5";
            } else {
            }
        }
        setRadioButtonCheck(opt);

    }

    public void setRadioButtonCheck(String opt)
    {
        if (opt.equals("")||opt.equals(null))
        {}
        else {
            if (opt.equals("1")) {
                optionA.setChecked(true);
            } else if (opt.equals("2")) {
                optionB.setChecked(true);
            } else if (opt.equals("3")) {
                optionC.setChecked(true);
            } else if (opt.equals("4")) {
                optionD.setChecked(true);
            } else if (opt.equals("5")) {
                optionE.setChecked(true);
            }
        }
    }
    public String getOptionId()
    {
        String optionId="";
        if (optionA.isChecked()==true)
        {
            optionId=optionsList.get(0).getOptionId().toString();
            optionId=optionId+"@@1";
        }
        else if (optionB.isChecked()==true)
        {
            optionId=optionsList.get(1).getOptionId().toString();
            optionId=optionId+"@@2";
        }
        else if (optionC.isChecked()==true)
        {
            optionId=optionsList.get(2).getOptionId().toString();
            optionId=optionId+"@@3";
        }
        else if (optionD.isChecked()==true)
        {
            optionId=optionsList.get(3).getOptionId().toString();
            optionId=optionId+"@@4";
        }
        else if (optionE.isChecked()==true)
        {
            optionId=optionsList.get(4).getOptionId().toString();
            optionId=optionId+"@@5";
        }
        return optionId;
    }

    public void setRadioButtions(int size,String rd1,String rd2,String rd3,String rd4,String rd5)
    {
        if (size==1)
        {
            optionA.setVisibility(View.VISIBLE);
            optionA.setText(rd1.toString());
            optionB.setVisibility(View.GONE);
            optionC.setVisibility(View.GONE);
            optionD.setVisibility(View.GONE);
            optionE.setVisibility(View.GONE);
        }
        else   if (size==2)
        {
            optionA.setVisibility(View.VISIBLE);
            optionA.setText(rd1.toString());
            optionB.setVisibility(View.VISIBLE);
            optionB.setText(rd2.toString());
            optionC.setVisibility(View.GONE);
            optionD.setVisibility(View.GONE);
            optionE.setVisibility(View.GONE);
        }
        else   if (size==3)
        {
            optionA.setVisibility(View.VISIBLE);
            optionA.setText(rd1.toString());
            optionB.setVisibility(View.VISIBLE);
            optionB.setText(rd2.toString());
            optionC.setVisibility(View.VISIBLE);
            optionC.setText(rd3.toString());
            optionD.setVisibility(View.GONE);
            optionE.setVisibility(View.GONE);
        }
        else   if (size==4)
        {
            optionA.setVisibility(View.VISIBLE);
            optionA.setText(rd1.toString());
            optionB.setVisibility(View.VISIBLE);
            optionB.setText(rd2.toString());
            optionC.setVisibility(View.VISIBLE);
            optionC.setText(rd3.toString());
            optionD.setVisibility(View.VISIBLE);
            optionD.setText(rd4.toString());
            optionE.setVisibility(View.GONE);
        }
        else   if (size==5) {
            optionA.setVisibility(View.VISIBLE);
            optionA.setText(rd1.toString());
            optionB.setVisibility(View.VISIBLE);
            optionB.setText(rd2.toString());
            optionC.setVisibility(View.VISIBLE);
            optionC.setText(rd3.toString());
            optionD.setVisibility(View.VISIBLE);
            optionD.setText(rd4.toString());
            optionE.setVisibility(View.VISIBLE);
            optionE.setText(rd5.toString());
        }

    }
    public void unchekRadio()
    {
        radioGroup.clearCheck();
        /*optionA.setChecked(false);
        optionB.setChecked(false);
        optionC.setChecked(false);
        optionD.setChecked(false);
        optionE.setChecked(false);*/
    }
    public void AlertDialogCustom(String Title)
    {
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.alert_dialog_layout, null);
        Button submit = (Button)dialoglayout.findViewById(R.id.btn_submit);
        TextView title= (TextView) dialoglayout.findViewById(R.id.dialogTitle);
        final AlertDialog.Builder builder = new AlertDialog.Builder(SurveyQuestionActivity.this);
        builder.setView(dialoglayout);
        title.setText(Title);

        final AlertDialog alertDialog = builder.create();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }
    public void setAnimation()
    {
        /*Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_slide_in_right);
        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_slide_in_left);
        optionA.setAnimation(animation1);
        optionB.setAnimation(animation2);
        optionC.setAnimation(animation1);
        optionD.setAnimation(animation2);
        optionE.setAnimation(animation1);*/
    }
}
