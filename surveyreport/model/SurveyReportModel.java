package realizer.com.mysurvey.surveyreport.model;

/**
 * Created by Win on 27/12/2016.
 */
public class SurveyReportModel
{
    public String SurveyId="";
    public String UserveyName="";
    public String questionNo="";
    public String questionText="";
    public String optionNo="";
    public String optionText="";
    public String numberofAnswers="";
    public String CreatedTime="";



    public String getSurveyId() {
        return SurveyId;
    }

    public void setSurveyId(String surveyId) {
        SurveyId = surveyId;
    }

    public String getUserveyName() {
        return UserveyName;
    }

    public void setUserveyName(String userveyName) {
        UserveyName = userveyName;
    }

    public String getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(String questionNo) {
        this.questionNo = questionNo;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getOptionNo() {
        return optionNo;
    }

    public void setOptionNo(String optionNo) {
        this.optionNo = optionNo;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public String getNumberofAnswers() {
        return numberofAnswers;
    }

    public void setNumberofAnswers(String numberofAnswers) {
        this.numberofAnswers = numberofAnswers;
    }

    public String getCreatedTime() {
        return CreatedTime;
    }

    public void setCreatedTime(String createdTime) {
        CreatedTime = createdTime;
    }
}
