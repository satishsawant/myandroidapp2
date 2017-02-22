package realizer.com.mysurvey.surveyquestion.model;

/**
 * Created by shree on 12/8/2016.
 */
public class SurveyQuestionModel {

    public String surveyId;
    public String questionId;
    public String questionText;
    public String queSerialNo;
    public String userId;
    public String userSurveyId;
    public String answerOptionNo;
    public String optionText;
    public String optionSerialNo;

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getQueSerialNo() {
        return queSerialNo;
    }

    public void setQueSerialNo(String queSerialNo) {
        this.queSerialNo = queSerialNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserSurveyId() {
        return userSurveyId;
    }

    public void setUserSurveyId(String userSurveyId) {
        this.userSurveyId = userSurveyId;
    }

    public String getAnswerOptionNo() {
        return answerOptionNo;
    }

    public void setAnswerOptionNo(String answerOptionNo) {
        this.answerOptionNo = answerOptionNo;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public String getOptionSerialNo() {
        return optionSerialNo;
    }

    public void setOptionSerialNo(String optionSerialNo) {
        this.optionSerialNo = optionSerialNo;
    }
}
