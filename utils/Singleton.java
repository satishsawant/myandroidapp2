package realizer.com.mysurvey.utils;

import android.content.Context;



import java.util.ArrayList;

import realizer.com.mysurvey.surveyquestion.model.SurveyQuestionModel;

/**
 * Created by shree on 12/7/2016.
 */
public class Singleton {

    public static ArrayList<SurveyQuestionModel> QuestioninfoList=new ArrayList<>();

    private Singleton()
    {
        // Constructor hidden because this is a singleton
    }

    public static ArrayList<SurveyQuestionModel> getUserinfoList() {
        return QuestioninfoList;
    }

    public static void setUserinfoList(ArrayList<SurveyQuestionModel> userinfoList) {
        Singleton.QuestioninfoList = userinfoList;
    }


    public static int dip2px(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
