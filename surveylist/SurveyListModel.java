package realizer.com.mysurvey.surveylist;

/**
 * Created by shree on 12/7/2016.
 */
public class SurveyListModel {
    
    public String surveyName;
    public String surveyId;
    public String surveyDescription;
    public String surveyStatus;
    public String listGetFrom;

    public String getListGetFrom() {
        return listGetFrom;
    }

    public void setListGetFrom(String listGetFrom) {
        this.listGetFrom = listGetFrom;
    }

    public String getSurveyStatus() {
        return surveyStatus;
    }

    public void setSurveyStatus(String surveyStatus) {
        this.surveyStatus = surveyStatus;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public String getSurveyDescription() {
        return surveyDescription;
    }

    public void setSurveyDescription(String surveyDescription) {
        this.surveyDescription = surveyDescription;
    }
}
