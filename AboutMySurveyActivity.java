package realizer.com.mysurvey;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import realizer.com.mysurvey.utils.GradientBackgroundPainter;


/**
 * Created by Win on 13/12/2016.
 */
public class AboutMySurveyActivity extends Activity
{
    private GradientBackgroundPainter gradientBackgroundPainter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_my_survey_activity);
        final Typeface face= Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/girolight001.otf");

        TextView title= (TextView) findViewById(R.id.app_title);
        title.setTypeface(face);

        View backgroundImage = findViewById(R.id.about_rootview);

        final int[] drawables = new int[3];
        drawables[0] = R.drawable.gradient1;
        drawables[1] = R.drawable.gradient2;
        drawables[2] = R.drawable.gradient3;

        gradientBackgroundPainter = new GradientBackgroundPainter(backgroundImage, drawables);
        gradientBackgroundPainter.start();

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
