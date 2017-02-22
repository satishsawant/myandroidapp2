package realizer.com.mysurvey;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import realizer.com.mysurvey.surveylist.SurveyListActivity;
import realizer.com.mysurvey.utils.FontManager;


/**
 * Created by Win on 22/12/2016.
 */
public class UserDashboardActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        getSupportActionBar().setTitle("Online Survey");
        Typeface face= Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/BRLNSB.TTF");
        TextView SurveyBtn= (TextView) findViewById(R.id.survey_btn);
        TextView SurveyReport= (TextView) findViewById(R.id.survey_report_btn);
        TextView userProf= (TextView) findViewById(R.id.survey_userprof_btn);
        TextView logoutbtn= (TextView) findViewById(R.id.logout_btn);

       /* SurveyBtn.setTypeface(face);
        SurveyReport.setTypeface(face);
        userProf.setTypeface(face);
        logoutbtn.setTypeface(face);*/
        SurveyBtn.setTypeface(FontManager.getTypeface(UserDashboardActivity.this, FontManager.FONTAWESOME));
        SurveyReport.setTypeface(FontManager.getTypeface(UserDashboardActivity.this,FontManager.FONTAWESOME));
        userProf.setTypeface(FontManager.getTypeface(UserDashboardActivity.this,FontManager.FONTAWESOME));
        logoutbtn.setTypeface(FontManager.getTypeface(UserDashboardActivity.this,FontManager.FONTAWESOME));
        SurveyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(UserDashboardActivity.this);
                SharedPreferences.Editor edit = sharedpreferences.edit();
                edit.putString("SurveyListFrom", "Survey");
                edit.commit();
                Intent i = new Intent(UserDashboardActivity.this, SurveyListActivity.class);
                startActivity(i);
            }
        });
        userProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserDashboardActivity.this, UserProfileActivity.class);
                startActivity(i);
            }
        });
        SurveyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(UserDashboardActivity.this);
                SharedPreferences.Editor edit = sharedpreferences.edit();
                edit.putString("SurveyListFrom", "Report");
                edit.commit();
                Intent i=new Intent(UserDashboardActivity.this, SurveyListActivity.class);
                startActivity(i);
            }
        });
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(UserDashboardActivity.this);
                SharedPreferences.Editor edit = sharedpreferences.edit();
                edit.putString("Login", "false");
                edit.commit();
                finish();
                Intent i=new Intent(UserDashboardActivity.this, LoginActivity.class);
                startActivity(i);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
